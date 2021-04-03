package com.example.fyp;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Placeholder {
    //Send a post of type request to the server
    // "/action" is the endpoint
    @POST("action")
    Call<Requests> createPost(@Body Requests post);

}
