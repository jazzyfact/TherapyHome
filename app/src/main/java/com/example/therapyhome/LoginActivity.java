package com.example.therapyhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button login_button_patient;
    Button login_button_guardian;
    Button login_button_doctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_button_patient = findViewById(R.id.login_button_patient);
        login_button_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        login_button_guardian = findViewById(R.id.login_button_guardian);
        login_button_guardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuardianmainActivity.class);
                startActivity(intent);
            }
        });

        login_button_doctor = findViewById(R.id.login_button_doctor);
        login_button_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctermsgActivity.class);
                startActivity(intent);
            }
        });
    }
}
