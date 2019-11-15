package com.fei.arnutri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import retrofit2.http.FormUrlEncoded;

public class HomeActivity extends AppCompatActivity {
    private Button btnDiag,btnReco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDiag = findViewById(R.id.btnDiagnostico);
        btnReco = findViewById(R.id.btnReconhecer);

        btnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        btnReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, YoloActivity.class);
                startActivity(intent);
            }
        });

    }
}
