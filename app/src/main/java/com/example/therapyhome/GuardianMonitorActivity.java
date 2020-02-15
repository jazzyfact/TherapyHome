package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuardianMonitorActivity extends AppCompatActivity {

    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;

    private int bpm = 80;
    private int co2 = 95;
    private TextView tvBpmData;
    private TextView tvCo2mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_monitor);

//        하단 네비게이션바
//        Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;
        btnEditKeyword = findViewById(R.id.bt_edit_keyword); //키워드 편집 버튼
        btnEditPhone = findViewById(R.id.bt_edit_phone);//연락처 편집 버튼
        btnCheckHealth = findViewById(R.id.bt_check_health);//건강 상태 버튼
        btnReadMsg = findViewById(R.id.bt_read_msg);//문자 모아보기 버튼

        tvBpmData = findViewById(R.id.tv_bpmdata);
        tvCo2mData = findViewById(R.id.tv_co2mdata);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_read_msg: //문자모아보기
                        Intent intent = new Intent(getApplicationContext(), GuardianMsgActivity.class);
                       // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.bt_edit_keyword: //키워드 편집
                        Intent editKeywordIntent = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                       // editKeywordIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editKeywordIntent);
                        finish();
                        break;
                    case R.id.bt_edit_phone://연락처 편집
                        Intent editPhomeIntent = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                       // editPhomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editPhomeIntent);
                        finish();
                        break;
                    case R.id.bt_check_health://환자 건강 상태
                        Intent checkHealthIntent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
                       // checkHealthIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(checkHealthIntent);
                        finish();
                        break;
                }
            }
        };

        btnReadMsg.setOnClickListener(onClickListener);
        btnCheckHealth.setOnClickListener(onClickListener);
        btnEditKeyword.setOnClickListener(onClickListener);
        btnEditPhone.setOnClickListener(onClickListener);



    }



    //스레드
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
            updateCo2Thread();//호흡
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
            }
        });
        myThread.start();
    }

    private void updateThread() {
        bpm++;
        tvBpmData.setText(String.valueOf(bpm));



    }

    private void updateCo2Thread(){
        co2 ++;
        tvCo2mData.setText(String.valueOf(co2));
    }
}
