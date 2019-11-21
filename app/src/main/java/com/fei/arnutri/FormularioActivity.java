package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fei.arnutri.Api.Consts;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormularioActivity extends AppCompatActivity {

    private Button btnProx;
    private EditText etIdade, etPeso, etAltura, etCircunBraco, etCircuCin, etDiamAbdom;


    private static final String BASE_URL = Consts.BASE_URL;
    private static Retrofit retrofit = null;

    private List<EditText> textFields = new ArrayList<>();

    private Validation validation = new Validation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        etIdade = findViewById(R.id.etIdade);
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etCircunBraco = findViewById(R.id.etCircunBraco);
        etCircuCin = findViewById(R.id.etCircuCin);
        etDiamAbdom = findViewById(R.id.etDiamAbdom);

        btnProx = findViewById(R.id.btnProx1);

        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormularioActivity.this, FormPessoalActivity.class);
                startActivity(intent);

            }
        });

    }


    private void sendData()
}
