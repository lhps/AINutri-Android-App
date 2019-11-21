package com.fei.arnutri.Api;

import com.fei.arnutri.Post;
import com.fei.arnutri.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FlaskApi {

    @GET("posts")
    Call<List<Post>> getPosts();

   // @POST("auth/register")
   // Call<User> createUser(@Body User user);

    @FormUrlEncoded
    @POST("/auth/register")
    Call<User> createUser(
            @Field("name") String name,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("password") String password,
            @Field("medicalRegister") String medicalRegister,
            @Field("userType") String userType
    );



}
