package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fei.arnutri.Api.AuthController;
import com.fei.arnutri.Api.AuthRoute;
import com.fei.arnutri.Api.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = Consts.BASE_URL;
    private static Retrofit retrofit = null;

    private Button login;
    private EditText etUsername, etPassword;
    private List<EditText> textFields = new ArrayList<>();

    private Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //CookieHandler.setDefault(new CookieManager(new SessionCookieStore(this), CookiePolicy.ACCEPT_ALL));

        login = findViewById(R.id.btnEntrar);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textFields.add(etUsername);
                textFields.add(etPassword);

                if (!validation.validateField(textFields)) {

                } else {
                    loginUser(etUsername.getText().toString(), etPassword.getText().toString());
                }
            }
        });

    }


    public void loginUser(String email, String password) {


        Retrofit retrofit = RetrofitClient.getInstance();

        AuthRoute api = retrofit.create(AuthRoute.class);

        Call<ResponseBody> call = api.loginUser(email, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    String body = response.body().string();

                    List<String> cookies = response.headers().values("Set-Cookie");

                    //System.out.println(cookies.get(0));



                    if(body == null){
                        return;
                    }

                    try {
                        JSONObject json = new JSONObject(body);

                        String success = json.getString("success");

                        if (success == "true") {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Log.d("INTENT", intent.toString());

                            startActivity(intent);
                        }
                    } catch (JSONException e) {
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