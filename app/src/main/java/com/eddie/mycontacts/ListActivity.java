package com.eddie.mycontacts;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class ListActivity extends AppCompatActivity implements MyAdapter.MyClickListener  {

    private MyAdapter adapter;
    private boolean isProgress = false;

    private ArrayList<Contact> listContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listContacts = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Response<ContactsResponse> response = RetrofitProvider.api.getAllContacts(LoginActivity.token).execute();

                    if (response.isSuccessful()) {

                        ContactsResponse contacts = response.body();

                        for (Contact c : contacts.contacts) {

                            listContacts.add(c);
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




        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(layoutManager);


        RecyclerView.ItemDecoration divider =
                new DividerItemDecoration(this,((LinearLayoutManager) layoutManager).getOrientation());
        ((DividerItemDecoration) divider).setDrawable(getResources().getDrawable(R.drawable.divider_bg));

        recyclerView.addItemDecoration(divider);

        recyclerView.setAdapter(adapter);

        adapter = new MyAdapter(listContacts);

        adapter.setListener(this);


    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list,menu);

        return super.onCreateOptionsMenu(menu);
    }

}
