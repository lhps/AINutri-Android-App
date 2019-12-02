package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fei.arnutri.Api.AuthRoute;
import com.fei.arnutri.Api.Consts;
import com.fei.arnutri.Api.FormRoute;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class FormularioActivity extends AppCompatActivity {

    private Button btnProx;
    private EditText etPeso, etAltura, etBatimentosCardiacos, etPressaoSistolica, etPressaoDiastolica;
    private EditText etCircunBraco, etCircuCin, etDiamAbdom, etForcaPunho, etImc;

    private static final String BASE_URL = Consts.BASE_URL;
    //rivate static Retrofit retrofit = null;

    private List<EditText> textFields = new ArrayList<>();

    private Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etCircunBraco = findViewById(R.id.etCircunBraco);
        etCircuCin = findViewById(R.id.etCircuCin);
        etDiamAbdom = findViewById(R.id.etDiamAbdom);
        etBatimentosCardiacos = findViewById(R.id.etBatimentosCardiacos);
        etForcaPunho = findViewById(R.id.etForcaPunho);
        etImc = findViewById(R.id.etImc);
        etPressaoSistolica = findViewById(R.id.etPressaoSistolica);
        etPressaoDiastolica = findViewById(R.id.etPressaoDiastolica);

        textFields.add(etPeso);
        textFields.add(etAltura);
        textFields.add(etCircunBraco);
        textFields.add(etCircuCin);
        textFields.add(etDiamAbdom);
        textFields.add(etBatimentosCardiacos);
        textFields.add(etForcaPunho);
        textFields.add(etImc);
        textFields.add(etPressaoSistolica);
        textFields.add(etPressaoDiastolica);


        btnProx = findViewById(R.id.btnProx1);

        //DEBUG

        Intent intent = new Intent(FormularioActivity.this, FormNutrientesActivity.class);
        startActivity(intent);

        //DEBUG

        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validation.validateField(textFields));
                else {
                    sendData(
                            etPeso.getText().toString(),
                            etAltura.getText().toString(),
                            etCircunBraco.getText().toString(),
                            etCircuCin.getText().toString(),
                            etDiamAbdom.getText().toString(),
                            etForcaPunho.getText().toString(),
                            etBatimentosCardiacos.getText().toString(),
                            etImc.getText().toString(),
                            etPressaoSistolica.getText().toString(),
                            etPressaoDiastolica.getText().toString()
                    );
                }
            }
        });

    }


private void sendData(
                       String weight,
                       String height,
                       String armCircunference,
                       String waistCircunference,
                       String sagittalAbdominalDiameter,
                       String fistStrength,
                       String heartBeats,
                       String bmi,
                       String systolicPressure,
                       String diastolicPressure){
/*
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();

    if (retrofit == null) {
        Log.d("INFO", "CREATING RETROFIT");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }*/

    Retrofit retrofit = RetrofitClient.getInstance();

    FormRoute api = retrofit.create(FormRoute.class);

    Call<ResponseBody> call = api.sendAntropometric(
            weight,
            height,
            armCircunference,
            waistCircunference,
            sagittalAbdominalDiameter,
            fistStrength,
            heartBeats,
            bmi,
            systolicPressure,
            diastolicPressure
    );

    final Call<ResponseBody> diagnosticCall = api.makeDiagnostic();


    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            try {

                String body = response.body().string();

                String header =  response.headers().get("Set-Cookie");

                try {
                    JSONObject json = new JSONObject(body);

                    String success = json.getString("success");

                    if (success.equals("true")) {
                        // Calls diagnostic method
                        diagnosticCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Intent intent = new Intent(FormularioActivity.this, FormPessoalActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
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

        }
    });

}

}
