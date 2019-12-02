package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import retrofit2.http.Field;

public class FormPessoalActivity extends AppCompatActivity {
    private Button btnProx;

    private EditText etGrauEscolar, etQtdPessoas, etRendFamiliar, etIdade;

    private Spinner spinnerGenero;

    private String genero;

    private static final String BASE_URL = Consts.BASE_URL;
    private static Retrofit retrofit = null;

    private List<EditText> textFields = new ArrayList<>();

    private Validation validation = new Validation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pessoal);


        etGrauEscolar = findViewById(R.id.etGrauEscolar);
        etQtdPessoas = findViewById(R.id.etQtdPessoas);
        etRendFamiliar = findViewById(R.id.etRendFamiliar);
        etIdade = findViewById(R.id.etIdade);

        textFields.add(etGrauEscolar);
        textFields.add(etQtdPessoas);
        textFields.add(etRendFamiliar);
        textFields.add(etIdade);
        spinnerGenero = findViewById(R.id.spinnerGenderPessoal);

        btnProx = findViewById(R.id.btnProx2);




        List<String> listaGeneros = new ArrayList<>();
        listaGeneros.add("Feminino");
        listaGeneros.add("Masculino");
        listaGeneros.add("Outro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaGeneros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGenero.setAdapter(adapter);
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                genero = itemValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validation.validateField(textFields));
                else{
                    sendData(
                            etIdade.getText().toString(),
                            genero,
                            etGrauEscolar.getText().toString(),
                            etRendFamiliar.getText().toString(),
                            etQtdPessoas.getText().toString()

                    );
                }
            }
        });
    }

    private void sendData(
            String age,
            String genero,
            String educationalLevel,
            String householdIncome,
            String totalPeopleResidence){

        Retrofit retrofit = RetrofitClient.getInstance();

        FormRoute api = retrofit.create(FormRoute.class);

        Call<ResponseBody> call = api.sendPersonal(
                age,
                genero,
                educationalLevel,
                householdIncome,
                totalPeopleResidence
        );


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    Intent intent = new Intent(FormPessoalActivity.this, FormNutrientesActivity.class);
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
