package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FormPessoalActivity extends AppCompatActivity {
    private Button btnProx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pessoal);

        btnProx = findViewById(R.id.btnProx2);

        btnProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormPessoalActivity.this, FormNutrientesActivity.class);
                startActivity(intent);
            }
        });
    }
}
