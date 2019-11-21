package com.fei.arnutri.Api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fei.arnutri.HomeActivity;
import com.fei.arnutri.LoginActivity;
import com.fei.arnutri.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class AuthController extends AppCompatActivity  {

    public static class AuthHelper{

    }

    private Activity currActivity;

    private static Retrofit retrofit = null;

    private static final String BASE_URL = Consts.BASE_URL;

    public void createUser(User user){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if(retrofit == null){
            Log.d("INFO","CREATING RETROFIT");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        AuthRoute api = retrofit.create(AuthRoute.class);

        Call<ResponseBody> call = api.createUser(user.getName(),user.getGender(), user.getEmail(),
                                            user.getBirth_date(), user.getPassword(),
                                            user.getMedicalRegister(), user.getUserType());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    String body = response.body().string();

                    try {
                        JSONObject json = new JSONObject(body);

                        String success = json.getString("success");

                        if (success == "true") {
                            //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            //Log.d("INTENT", intent.toString());

                            //startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                catch (IOException e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void loginUser(String email, String password){


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if(retrofit == null){
            Log.d("INFO","CREATING RETROFIT");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        AuthRoute api = retrofit.create(AuthRoute.class);

        Call<ResponseBody> call = api.loginUser(email, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    String body = response.body().string();

                    try {
                        JSONObject json = new JSONObject(body);

                        String success = json.getString("success");

                        if(success == "true"){
                            Intent intent = new Intent(currActivity, HomeActivity.class);
                            Log.d("INTENT", intent.toString());

                            startActivity(intent);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("FAIL", "SOMETHING WRONG ", t);
            }
        });

    }

}
