package com.eddie.mycontacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Contact> listContacts;

    private MyClickListener listener;

    public MyAdapter(ArrayList<Contact> listContacts) {

        listContacts = new ArrayList<>();

        Log.d("MY_TAG", "I am in Adapter Constructor with ArrayList<Contact>");

        this.listContacts = listContacts;
    }

    public void setListener(MyClickListener listener) {

        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d("MY_TAG", "onCreateViewHolder: ");

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_row, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Log.d("MY_TAG", "onBindViewHolder: " + i);

        Contact contact = listContacts.get(i);

        myViewHolder.emailTxt.setText(contact.getEmail());
        myViewHolder.nameTxt.setText(contact.getName());
    }

    @Override
    public int getItemCount() {

        return listContacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTxt, emailTxt;

        public MyViewHolder(View itemView) {

            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
        }
    }

    public interface MyClickListener {

        void onClick(int pos);
    }
}


