package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuardianmainActivity extends AppCompatActivity {

    TextView bt_guardian_main;
    TextView bt_guardian_txt;
    TextView bt_guardian_num;
    TextView bt_guardian_state;
    TextView bt_guardian_msg;

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
        bt_guardian_main = findViewById(R.id.bt_guardian_main);
        bt_guardian_main.setOnClickListener(onClickListener);
        bt_guardian_txt = findViewById(R.id.bt_guardian_txt);
        bt_guardian_txt.setOnClickListener(onClickListener);
        bt_guardian_num = findViewById(R.id.bt_guardian_num);
        bt_guardian_num.setOnClickListener(onClickListener);
        bt_guardian_state = findViewById(R.id.bt_guardian_state);
        bt_guardian_state.setOnClickListener(onClickListener);
        bt_guardian_msg = findViewById(R.id.bt_guardian_msg);
        bt_guardian_msg.setOnClickListener(onClickListener);
    }
}
