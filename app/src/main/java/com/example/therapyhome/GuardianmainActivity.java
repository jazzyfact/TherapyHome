package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuardianmainActivity extends AppCompatActivity {

    TextView btnGuardianMain;
    TextView btnGuardianTxt;
    TextView btnGuardianNum;
    TextView btnGuardianState;
    TextView btGuardianMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardianmain);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_guardian_main:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bt_guardian_txt:
                        Intent txt_intent = new Intent(getApplicationContext(), GuardiantexteditActivity.class);
                        startActivity(txt_intent);
                        break;
                    case R.id.bt_guardian_num:
                        Intent num_intent = new Intent(getApplicationContext(), GuardiannumActivity.class);
                        startActivity(num_intent);
                        break;
                    case R.id.bt_guardian_state:
                        Intent state_intent = new Intent(getApplicationContext(), GuardianstateActivity.class);
                        startActivity(state_intent);
                        break;
                    case R.id.bt_guardian_msg:
                        Intent msg_intent = new Intent(getApplicationContext(), GuardianmsgActivity.class);
                        startActivity(msg_intent);
                        break;
                }
            }
        };
        btnGuardianMain = findViewById(R.id.bt_guardian_main);
        btnGuardianMain.setOnClickListener(onClickListener);
        btnGuardianTxt = findViewById(R.id.bt_guardian_txt);
        btnGuardianTxt.setOnClickListener(onClickListener);
        btnGuardianNum = findViewById(R.id.bt_guardian_num);
        btnGuardianNum.setOnClickListener(onClickListener);
        btnGuardianState = findViewById(R.id.bt_guardian_state);
        btnGuardianState.setOnClickListener(onClickListener);
        btGuardianMsg = findViewById(R.id.bt_guardian_msg);
        btGuardianMsg.setOnClickListener(onClickListener);
    }
}
