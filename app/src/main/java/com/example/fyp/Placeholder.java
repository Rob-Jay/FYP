package com.example.fyp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//This is the placeholder dor the HTTP request
public interface Placeholder {
    //Send a post of type request to the server
    // "/action" is the endpoint
    @POST("action")
    Call<Requests> createPost(@Body Requests post);

}
