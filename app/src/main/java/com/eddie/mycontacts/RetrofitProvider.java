package com.eddie.mycontacts;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    public static Api api;
    private OkHttpClient client;
    private static final RetrofitProvider ourInstance = new RetrofitProvider();
    public static String token;

    public static RetrofitProvider getInstance() {

        return ourInstance;
    }

    public RetrofitProvider() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://contacts-telran.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        client = new OkHttpClient();
    }

    // Retrofit registration
    public void registration(String email, String password) throws IOException {

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

    public void login(String email, String password) throws IOException {

        Auth auth = new Auth(email, password);
        Call<AuthResponse> call = api.login(auth);
        //Call<AuthResponse> callLog = api.login(auth);
        Response<AuthResponse> response = call.execute();


        if (response.isSuccessful()) {

            AuthResponse token = response.body();
            this.token = token.getToken();
            Log.d("MY_TAG", "login: " + token.getToken());


        } else if (response.code() == 401) {

            String json = response.errorBody().string();
            Log.d("MY_TAG", "login: " + json);
        }


    }

            /*
        else if (v.getId() == R.id.getContactList) {


            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        Response<ContactsResponse> response = api.getAllContacts(token).execute();

                        if (response.isSuccessful()) {

                            ContactsResponse contacts = response.body();

                            for (Contact c : contacts.contacts) {

                                Log.d("MY_TAG", "getContactList: " + c);
                            }
                        } else {

                            Log.d("MY_TAG", "getContactList: " + response.errorBody().string());
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }).start();

        } else if (v.getId() == R.id.addBtn) {

            //Contact newContact = new Contact("New", "Man", 3333, "88888888","newman@ocm.ua", "Haifa", "Friend");
            //progressBar.setVisibility(View.VISIBLE);

            //api.addContact(currentAuth, newContact);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        Response<Contact> response = api.addContact(token, new Contact("New", "Man", 0, "88888888","newman@ocm.ua", "Haifa", "Friend")).execute();

                        if (response.isSuccessful()) {


                            Contact contact = response.body();

                            Log.d("MY_TAG", "Contact added: " + contact);

                        } else if (response.code() == 400) {

                            Log.d("MY_TAG", "addContact: " + "Wrong contact format");

                        } else if (response.code() == 401) {

                            Log.d("MY_TAG", "addContact: " + "Wrong authorization");
                        } else if (response.code() == 409) {

                            Log.d("MY_TAG", "addContact: " + "Duplicate contact fields! Email and phone need be unique to each contact");
                        } else {

                            Log.d("MY_TAG", "addContact: " + response.errorBody().string());
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            }).start();

        } else if (v.getId() == R.id.deleteBtn) {

            try {
                Response<Contact> response = api.deleteContact(token, 1317).execute();

                if (response.isSuccessful()) {

                    Log.d("MY_TAG", "Contact deleted: ");
                } else if (response.code() == 400) {

                    Log.d("MY_TAG", "addContact: " + "Wrong format ID of contact");

                } else if (response.code() == 401) {

                    Log.d("MY_TAG", "addContact: " + "Wrong authorization");
                } else if (response.code() == 404) {

                    Log.d("MY_TAG", "addContact: " + "Contact ID not found");
                } else {

                    Log.d("MY_TAG", "addContact: " + response.errorBody().string());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
}
