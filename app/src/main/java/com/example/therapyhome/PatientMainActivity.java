package com.example.therapyhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PatientMainActivity extends AppCompatActivity {

    Button btnSendMsg, btnSmartHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        btnSendMsg = findViewById(R.id.bt_send_message);
        btnSmartHome = findViewById(R.id.bt_smart_home);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent =new Intent(getApplicationContext(), PatientMsgActivity.class);
                startActivity(msgIntent);
            }
        });

        btnSmartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smartHomeIntent =new Intent(getApplicationContext(), PatientSmartHomeActivity.class);
                startActivity(smartHomeIntent);
            }
        });

    }
}
