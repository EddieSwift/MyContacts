package com.eddie.mycontacts;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @POST("api/registration")
    Call<AuthResponse> registration(@Body Auth auth);

    @POST("api/login")
    Call<AuthResponse> login(@Body Auth auth);

    @GET("api/contact")
    Call<ContactsResponse> getAllContacts(@Header("Authorization") String token);

    @POST("api/contact")
    Call<Contact> addContact(@Header("Authorization") String token, @Body Contact contact);

    @DELETE("api/contact/{id}")
    Call<Contact> deleteContact(@Header("Authorization") String token, @Path("id") long id);

}
