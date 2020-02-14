package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GuardianMainActivity extends AppCompatActivity {

    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;
    ImageView ivGuardianEditDoctor, ivGuardianEditFamily, ivGuardianEditMy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_main);



        ivGuardianEditMy = findViewById(R.id.iv_guardian_editmy); //내정보
        ivGuardianEditFamily = findViewById(R.id.iv_guardian_editFamily);//등록된 환자
        ivGuardianEditDoctor = findViewById(R.id.bt_guardian_editdocter); //담당 의사선생님


        btnEditKeyword = findViewById(R.id.bt_edit_keyword); //키워드 편집 버튼
        btnEditPhone = findViewById(R.id.bt_edit_phone);//연락처 편집 버튼
        btnCheckHealth = findViewById(R.id.bt_check_health);//건강 상태 버튼
        btnReadMsg = findViewById(R.id.bt_read_msg);//문자 모아보기 버튼



        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_read_msg: //문자모아보기
                        Intent intent = new Intent(getApplicationContext(), GuardianMsgActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.bt_edit_keyword: //키워드 편집
                        Intent editKeywordIntent = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                        startActivity(editKeywordIntent);
                        break;
                    case R.id.bt_edit_phone://연락처 편집
                        Intent editPhomeIntent = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                        startActivity(editPhomeIntent);
                        break;
                    case R.id.bt_check_health://환자 건강 상태
                        Intent checkHealthIntent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
                        startActivity(checkHealthIntent);
                        break;

                    case R.id.iv_guardian_editFamily://등록된 환자
                        Intent editFamilyIntent = new Intent(getApplicationContext(), CustomDialogActivity.class);
                        startActivity(editFamilyIntent);
                        break;
                    case R.id.iv_guardian_editmy://내정보
                        Intent editmyIntent = new Intent(getApplicationContext(), CustomDialogActivity.class);
                        startActivity(editmyIntent);
                        break;
                    case R.id.bt_guardian_editdocter: //등록된 의사
                        Intent editDoctorIntent = new Intent(getApplicationContext(), CustomDialogActivity.class);
                        startActivity(editDoctorIntent);
                        break;
                }
            }
        };





        ivGuardianEditFamily.setOnClickListener(onClickListener);
        ivGuardianEditMy.setOnClickListener(onClickListener);
        ivGuardianEditDoctor.setOnClickListener(onClickListener);


        btnReadMsg.setOnClickListener(onClickListener);
        btnCheckHealth.setOnClickListener(onClickListener);
        btnEditKeyword.setOnClickListener(onClickListener);
        btnEditPhone.setOnClickListener(onClickListener);


    }


}
