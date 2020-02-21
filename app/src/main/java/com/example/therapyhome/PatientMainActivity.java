package com.example.therapyhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PatientMainActivity extends AppCompatActivity {

    Button btnSendMsg, btnSmartHome;
    public static String PatientMainSpSelectNum;
    public static String PatientMainSpSelectMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);


//
//        btnSendMsg = findViewById(R.id.bt_send_message);
//        btnSmartHome = findViewById(R.id.bt_smart_home);
//
//        btnSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent msgIntent =new Intent(getApplicationContext(), PatientMsgActivity.class);
//                msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(msgIntent);
//            }
//        });
//
//        btnSmartHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent smartHomeIntent =new Intent(getApplicationContext(), PatientSmartHomeActivity.class);
//                smartHomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(smartHomeIntent);
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}
