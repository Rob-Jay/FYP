package com.example.fyp;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.Call;
import retrofit2.http.POST;

public interface Placeholder {
    @POST("/action")
    Call<Requests> createPost(@Body Requests post);

    @POST("/test")
    Call<Requests> createtest();


}
