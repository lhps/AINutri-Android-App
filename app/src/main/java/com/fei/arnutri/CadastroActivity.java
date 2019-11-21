package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.fei.arnutri.Api.AuthController;
import com.fei.arnutri.Api.AuthRoute;
import com.fei.arnutri.Api.Consts;
import com.fei.arnutri.Api.FlaskApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private static final String TAG = "CadastroActivity";
    private TextView tvData, tvResult;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioGroup mRadioGroup;
    private EditText etNome, etEmail, etSenha, etSenhaConf, etCrm;
    private Spinner spinnerGenero;
    private String nome, email, senha, senhaConf, crm, dataNasc, genero, userType;
    private RadioButton rbMed;
    private RadioButton rbPac;
    private Button btnEscData, btnFinalizar;
    private Date dataEsclhida;
    String textoGen;
    Calendar cal;
    DatePickerDialog datePickerDialog;
    private FlaskApi flaskApi;

    private static final String BASE_URL = Consts.BASE_URL;
    private static Retrofit retrofit = null;

    private List<EditText> textFields = new ArrayList<>();

    private Validation validation = new Validation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        //Campos
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etSenhaConf = findViewById(R.id.etSenhaConf);

        // Radio buttons
        mRadioGroup = findViewById(R.id.rBtnGrupo);
        etCrm = findViewById(R.id.etCRM);
        rbMed = findViewById(R.id.rBtnMedico);
        rbPac = findViewById(R.id.rBtnPaciente);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnEscData = findViewById(R.id.btnEscolherData);
        btnFinalizar = findViewById(R.id.btnCadastrar);
        tvResult = findViewById(R.id.tvResultado);

        tvData = findViewById(R.id.tvDataNasc);

        // API

        //  Retrofit retrofit = new Retrofit.Builder()
        //        .baseUrl("127.0.0.1:5000")
        //            .addConverterFactory(GsonConverterFactory.create())
        //              .build();
//
        // flaskApi = retrofit.create(FlaskApi.class);

        //

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
                //Toast.makeText(CadastroActivity.this, itemValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Ao selecionar o RB "Medico"
        rbMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = mRadioGroup.getCheckedRadioButtonId();

                etCrm.setEnabled(true);
                etCrm.setVisibility(View.VISIBLE);
                userType = "Médico";
                //Toast.makeText(this, "Selecionou o Radio Button: " + mRadioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        // Ao selecionar o RB Paciente
        rbPac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCrm.setText("");
                etCrm.setEnabled(false);
                etCrm.setVisibility(View.INVISIBLE);
                userType = "Paciente";
            }
        });

        // Botão de cadastrar


        // Calendário
        btnEscData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                final int dia = cal.get(Calendar.DAY_OF_MONTH);
                final int mes = cal.get(Calendar.MONTH);
                final int ano = cal.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(CadastroActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        //String data =  dayOfMonth +"/"+month + "/" + year;
                        String data = year + "-" + month + "-" + dayOfMonth;
                        dataNasc = data;
                        tvData.setText(data);
                    }
                }, dia, mes, ano);

                datePickerDialog.show();
            }
        });


        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textFields.add(etNome);
                textFields.add(etEmail);
                textFields.add(etSenha);
                textFields.add(etSenhaConf);

                if (validation.validateField(textFields) == false) {
                    System.out.println("ERRO");
                } else {
                    User user = new User(etNome.getText().toString(),
                            genero, etEmail.getText().toString(),
                            dataNasc, etSenha.getText().toString(),
                            etCrm.getText().toString(), userType);

                    createUser(user);
                }
            }
        });


    }


    public void createUser (User user){

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
        }

        AuthRoute api = retrofit.create(AuthRoute.class);

        Call<ResponseBody> call = api.createUser(user.getName(), user.getGender(), user.getEmail(),
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
                            Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
