package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fei.arnutri.Api.FormRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormNutrientesActivity extends AppCompatActivity {

    private Button btnProx;

    private EditText etCalorias, etProtein, etCarbo, etAcucar, etFibras;
    private EditText etGorduras, etGorduraSat, etGorduraMono, etGorduraPoli, etColesterol, etAlcool;

    private Validation validation = new Validation();

    private List<EditText> textFields = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nutrientes);

        etCalorias = findViewById(R.id.etCalorias);
        etProtein = findViewById(R.id.etProtein);
        etCarbo = findViewById(R.id.etCarbo);
        etAcucar = findViewById(R.id.etAcucar);
        etFibras = findViewById(R.id.etFibras);
        etGorduras = findViewById(R.id.etGorduras);
        etGorduraSat = findViewById(R.id.etGorduraSat);
        etGorduraMono = findViewById(R.id.etGorduraMono);
        etGorduraPoli = findViewById(R.id.etGorduraPoli);
        etColesterol = findViewById(R.id.etColesterol);
        etAlcool = findViewById(R.id.etAlcool);

        textFields.add(etCalorias);
        textFields.add(etProtein);
        textFields.add(etCarbo);
        textFields.add(etAcucar);
        textFields.add(etFibras);
        textFields.add(etGorduras);
        textFields.add(etGorduraSat);
        textFields.add(etGorduraMono);
        textFields.add(etGorduraPoli);
        textFields.add(etColesterol);
        textFields.add(etAlcool);


        btnProx = findViewById(R.id.btnDiagnosticar);

        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validation.validateField(textFields));
                else{
                    sendData(
                            etCalorias.getText().toString(),
                            etProtein.getText().toString(),
                            etCarbo.getText().toString(),
                            etAcucar.getText().toString(),
                            etFibras.getText().toString(),
                            etGorduras.getText().toString(),
                            etGorduraSat.getText().toString(),
                            etGorduraMono.getText().toString(),
                            etGorduraPoli.getText().toString(),
                            etColesterol.getText().toString(),
                            etAlcool.getText().toString()
                    );
                }
            }
        });
    }




    private void sendData(
            String calories,
            String proteins,
            String carb,
            String sugar,
            String fiber,
            String gorduras,
            String saturatedFat,
            String monoFat,
            String poliFat,
            String colestherol,
            String alcohol
    ){

        Retrofit retrofit = RetrofitClient.getInstance();

        FormRoute api = retrofit.create(FormRoute.class);

        Call<ResponseBody> call = api.sendNutrients(
                calories,
                proteins,
                carb,
                sugar,
                fiber,
                gorduras,
                saturatedFat,
                monoFat,
                poliFat,
                colestherol,
                alcohol
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    Intent intent = new Intent(FormNutrientesActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {

                    try {
                        String errorBody = response.errorBody().string();
                        Log.d("ERROR LOG", errorBody);
                        Toast.makeText(getApplicationContext(), "Error while submting form", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
