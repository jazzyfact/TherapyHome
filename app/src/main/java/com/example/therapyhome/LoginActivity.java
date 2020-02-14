package com.example.therapyhome;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnLoginPatient;
    Button btnLoginGuardian;
    Button btnLoginDoctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLoginPatient = findViewById(R.id.login_button_patient);
        btnLoginPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnLoginGuardian = findViewById(R.id.login_button_guardian);
        btnLoginGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuardianmainActivity.class);
                startActivity(intent);
            }
        });

        btnLoginDoctor = findViewById(R.id.login_button_doctor);
        btnLoginDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctermsgActivity.class);
                startActivity(intent);
            }
        });

    }
}
