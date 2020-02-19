package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TutorialJoyStickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_joy_stick);
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}
