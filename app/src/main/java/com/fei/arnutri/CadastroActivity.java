package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private static final String TAG = "CadastroActivity";
    private TextView tvData,tvResult;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RadioGroup mRadioGroup;
    private EditText etNome,etEmail,etSenha,etSenhaConf, etCrm;
    private Spinner spinnerGenero;
    private String nome,email,senha,senhaConf,crm,dataNasc,genero, userType;
    private RadioButton rbMed;
    private RadioButton rbPac;
    private Button btnEscData,btnFinalizar;
    private Date dataEsclhida;
    String textoGen;
    Calendar cal;
    DatePickerDialog datePickerDialog;
    private FlaskApi flaskApi;
    String baseUrl = "https://29c6e2db-2a54-4766-bd1d-a8c0e9c8982d.mock.pstmn.io";




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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,listaGeneros);
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
                        String data = year+"-"+month+"-"+dayOfMonth;
                        dataNasc = data;
                        tvData.setText(data);
                    }
                }, dia, mes , ano);

                datePickerDialog.show();
            }
        });


        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                content += "name: "+ etNome.getText() + "\n";
                content += "gender: " + genero + "\n";
                content += "email: " + etEmail.getText()+"\n";
                content += "birth_date: " + dataNasc+"\n";
                content += "password: " +etSenha.getText() +"\n";
                content += "medicalRegister:" + etCrm.getText() + "\n";
                content += "userType:" + userType+ "\n\n";


                tvResult.setText(content);

                Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });




        /*
        //
         tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CadastroActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano,mes,dia);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String data =  dayOfMonth +"/"+month + "/" + year;
                tvData.setText(data);
            }
        };
        //
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CadastroActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                Log.d(TAG, "onDateSet: date:" + dayOfMonth + "/" + month + "/" + year);
                String data =  dayOfMonth + "/" + month + "/" + year;
                tvData.setText(data);
            }

        };
        */

    }

    public void cadastrarConta(){
        inicializa();
        if( !validaCampos()){
            Toast.makeText(this,"Falha no cadastro!", Toast.LENGTH_SHORT).show();
        }
        else {
            cadastroComSucesso();
        }
    }

    public void cadastroComSucesso(){

    }
    public boolean validaCampos(){
        boolean valido = true;
        if(nome.isEmpty() || nome.length() > 60){
            etNome.setError("Por favor, entre com um nome válido");
            valido = false;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Por favor, entre com um e-mail válido");
            valido = false;
        }

        if(senha.isEmpty()){
            etSenha.setError("Por favor, entre com uma senha");
            valido = false;
        }

        if(senhaConf.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etSenhaConf.setError("Por favor, entre com uma senha");
            valido = false;
        }


        return valido;
    }

    public void inicializa() {
        nome = etNome.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        senha = etSenha.getText().toString().trim();
        senhaConf = etSenhaConf.getText().toString().trim();
        genero = textoGen;
        crm = etCrm.getText().toString().trim();
        dataNasc = tvData.getText().toString().trim();
    }

    private void createUser(){
        final User user = new User("Lucas","Male","lucas@hotmail.com","1998-08-12","senha","crm123","Medico");

        //Call<User> call = flaskApi.createUser("Lucas","Male","lucas@hotmail.com","1998-08-12","senha","crm123","Medico");
        Call<User> call = flaskApi.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if( !response.isSuccessful()){
                    tvResult.setText("Code: "+ response.code());
                    return;
                }

                User userResponse = response.body() ;

                String content = "";
                content += "Code: "+ response.code() + "\n";
                content += "name: "+ userResponse.getName() + "\n";
                content += "gender: " + userResponse.getGender() + "\n";
                content += "email: " + userResponse.getEmail()+"\n";
                content += "birth_date: " + userResponse.getBirth_date()+"\n";
                content += "password: " +userResponse.getPassword() +"\n";
                content += "medicalRegister:" + userResponse.getMedicalRegister() + "\n";
                content += "userType:" + userResponse.getUserType()+ "\n\n";


                tvResult.setText(content);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvResult.setText(t.getMessage());
            }
        });
    }

}
