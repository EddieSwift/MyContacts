package com.eddie.mycontacts;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static Api api;
    private OkHttpClient client;

    private EditText inputEmail, inputPassword;
    private Button regBtn, loginBtn, getAllContacts, addBtn, deleteBtn;
    private ProgressBar myProgress;

    public static String token;

    private static final int LIST_ACTIVITY = 0x01;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);

        regBtn = findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(this);

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        myProgress = findViewById(R.id.progressBar);

//        getAllContacts = findViewById(R.id.getContactList);
//        getAllContacts.setOnClickListener(this);
//
//        addBtn = findViewById(R.id.addBtn);
//        addBtn.setOnClickListener(this);
//
//        deleteBtn = findViewById(R.id.deleteBtn);
//        deleteBtn.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://contacts-telran.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        client = new OkHttpClient();
    }

    // Retrofit registration
    private void registration(String email, String password) throws IOException {

        Auth auth = new Auth(email, password);
        Call<AuthResponse> call = api.registration(auth);

        Response<AuthResponse> response = call.execute();

        if (response.isSuccessful()) {

            AuthResponse token = response.body();

            this.token = token.getToken();


            Log.d("MY_TAG", "Registration" + token.getToken());

        } else if (response.code() == 409) {

            String json = response.errorBody().string();
            Log.d("MY_TAG", "Error: " + response.code());
        }
    }

    private void login(String email, String password) throws IOException {

        Auth auth = new Auth(email, password);
        Call<AuthResponse> call = api.login(auth);
        //Call<AuthResponse> callLog = api.login(auth);
        Response<AuthResponse> response = call.execute();


        if (response.isSuccessful()) {

            AuthResponse token = response.body();
            this.token = token.getToken();
            Log.d("MY_TAG", "login: " + token.getToken());
            showNextView();

        } else if (response.code() == 401) {

            String json = response.errorBody().string();
            Log.d("MY_TAG", "login: " + json);
        }


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login_btn) {

            final String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();

            try {
                checkEmail(email);
            } catch (EmailValidException e) {
                inputEmail.setError(e.getMessage());
            }

            try {
                checkPassword(password);
            } catch (PasswordValidException e) {
                inputPassword.setError(e.getMessage());
            }

            showProgress();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        login(email,password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } else if (v.getId() == R.id.reg_btn) {

            final String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        registration(email, password);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//            asyncReg(email,password);

        }
    }

    // Additional Methods

    private void showProgress() {

        myProgress.setVisibility(View.VISIBLE);
        inputEmail.setEnabled(false);
        inputPassword.setEnabled(false);
        loginBtn.setEnabled(false);
        regBtn.setEnabled(false);
    }

    private void hideProgress(){
        myProgress.setVisibility(View.GONE);
        inputEmail.setEnabled(true);
        inputPassword.setEnabled(true);
        loginBtn.setEnabled(true);
        regBtn.setEnabled(true);
    }

    private void showEmailError(String error) {

        inputEmail.setError(error);
    }

    private void showPasswordError(String error){

        inputPassword.setError(error);
    }

    private void showError(String error){

        new AlertDialog.Builder(this)
                .setMessage(error)
                .setTitle("Error!")
                .setPositiveButton("Ok",null)
                .setCancelable(false)
                .create()
                .show();
    }

    private void showNextView(){

        //Toast.makeText(this, "Next Activity", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ListActivity.class);
        startActivityForResult(intent,LIST_ACTIVITY);
    }


    private void checkEmail(String email) throws EmailValidException {
        if(email.isEmpty()){
            throw new EmailValidException("Email can't be empty!");
        }

        int at = email.indexOf("@");
        if(at < 0) {
            throw new EmailValidException("Wrong email format! Example: name@mail.com");
        }

        if(email.lastIndexOf("@") != at) {
            throw new EmailValidException("Wrong email format! Example: name@mail.com");
        }

        int dot = email.lastIndexOf(".");
        if(dot < 0 || dot < at) {
            throw new EmailValidException("Wrong email format! Example: name@mail.com");
        }

        if(email.length() - 1 - dot <= 1) {
            throw new EmailValidException("Wrong email format! Example: name@mail.com");
        }
    }

    private void checkPassword(String password) throws PasswordValidException {
        if(password.length() < 8) {
            throw new PasswordValidException("Password length need be 8 or more symbols");
        }
        boolean[] tests = new boolean[4];
        char[] arr = password.toCharArray();
        for (char anArr : arr) {
            if (Character.isUpperCase(anArr)) {
                tests[0] = true;
            }

            if (Character.isLowerCase(anArr)) {
                tests[1] = true;
            }

            if (Character.isDigit(anArr)) {
                tests[2] = true;
            }

            if (isSpecSymbol(anArr)) {
                tests[3] = true;
            }
        }

        if(!tests[0]){
            throw new PasswordValidException("Password must contain at least one uppercase letter!");
        }
        if(!tests[1]){
            throw new PasswordValidException("Password must contain at least one lowercase letter!");
        }
        if(!tests[2]){
            throw new PasswordValidException("Password must contain at least one digit!");
        }
        if(!tests[3]){
            throw new PasswordValidException("Password must contain at least one special symbol from ['$','~','-','_']!");
        }
    }

    private boolean isSpecSymbol(char c) {
        char[] arr = {'$','~','-','_'};
        for (char anArr : arr) {
            if (anArr == c) {
                return true;
            }
        }
        return false;
    }


}
