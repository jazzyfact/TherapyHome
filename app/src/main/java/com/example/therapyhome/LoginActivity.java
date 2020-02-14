package com.example.therapyhome;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import android.widget.CheckBox;

public class LoginActivity extends AppCompatActivity {


    Button btnLoginPatient;
    Button btnLoginGuardian;
    Button btnLoginDoctor;

    CheckBox cbloginPatientBox; //환자 체크
    CheckBox cbloginGuaridanBox; // 보호자 체크
    CheckBox cbLoginDocterBox;//의사체크






    //
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//        cbloginPatientBox = findViewById(R.id.cb_login_patient_box);
//        cbloginGuaridanBox = findViewById(R.id.cb_login_guaridan_box);
//        cbLoginDocterBox = findViewById(R.id.cb_login_docter_box);



        btnLoginPatient =findViewById(R.id.login_button_patient);
        btnLoginGuardian =findViewById(R.id.login_button_guardian);
        btnLoginDoctor =findViewById(R.id.login_button_doctor);




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
                Intent intent = new Intent(getApplicationContext(), GuardianMainActivity.class);
                startActivity(intent);
            }
        });

        btnLoginDoctor = findViewById(R.id.login_button_doctor);
        btnLoginDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctorPatientListActivity.class);
                startActivity(intent);
            }
        });
    }
}



