package com.fei.arnutri.Api;

import com.fei.arnutri.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthRoute {

    @FormUrlEncoded
    @POST("/auth/register")
    Call<ResponseBody> createUser(
            @Field("name") String name,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("password") String password,
            @Field("medicalRegister") String medicalRegister,
            @Field("userType") String userType
    );


    @FormUrlEncoded
    @POST("/auth/login")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

}
