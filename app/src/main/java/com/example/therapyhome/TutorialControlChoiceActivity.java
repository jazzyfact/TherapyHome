package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TutorialControlChoiceActivity extends AppCompatActivity {
    private static final String TAG = "TutorialControlChoiceActivity";
    Button btConSelectEye;
    Button bConSelectJoy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlselect);

        btConSelectEye = findViewById(R.id.bt_conSelect_eye);
        btConSelectEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 확인");
                Intent intent = new Intent(getApplicationContext(), PatientMsgActivity.class);
                startActivity(intent);
            }
        });
        bConSelectJoy = findViewById(R.id.bt_conSelect_joy);
        bConSelectJoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 확인2");
                Intent intent = new Intent(getApplicationContext(), PatientMsgActivity.class);
                startActivity(intent);
            }
        });





    }
}
