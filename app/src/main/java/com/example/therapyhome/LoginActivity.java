package com.example.therapyhome;


import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    Button btnLoginPatient;
    Button btnLoginGuardian;
    Button btnLoginDoctor;
    Button btn_login;
    Button btn_signup;
    // 라디오버튼 관련
    RadioGroup cb_login_group;
    RadioButton cbloginPatientBox; //환자 체크
    RadioButton cbloginGuaridanBox; // 보호자 체크
    RadioButton cbLoginDocterBox;//의사체크

    String selectCK ; // 클릭했는지 안했는지 체크하기
    //
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 라디오버튼
        cbloginPatientBox = findViewById(R.id.cb_login_docter);
        cbloginGuaridanBox = findViewById(R.id.cb_login_guaridan);
        cbLoginDocterBox = findViewById(R.id.cb_login_patient);

        //라디오 버튼 클릭 리스너
        RadioButton.OnClickListener radioButtonClickListener = new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "의사클릭 : "+cbloginPatientBox.isChecked() + "보호자클 : " +cbloginGuaridanBox.isChecked() + "보호자클 : " +cbLoginDocterBox.isChecked() , Toast.LENGTH_SHORT).show();
            }
        };

        // 라디오 버튼 하나하나에도 클릭 리스너를 붙여줘야한다.
        cbloginPatientBox.setOnClickListener(radioButtonClickListener);
        cbloginGuaridanBox.setOnClickListener(radioButtonClickListener);
        cbLoginDocterBox.setOnClickListener(radioButtonClickListener);

        // 라디오 그룹에 클릭리스너를 붙여줘야함
        RadioGroup.OnCheckedChangeListener adioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.cb_login_docter){
                    //환자 눌렀을때
                    selectCK = "의사";
                } else if(checkedId == R.id.cb_login_guaridan){
                    // 보호자 눌렀을때
                    selectCK = "보호자";
                } else if (checkedId == R.id.cb_login_patient){
                    //환자 눌렀을때
                    selectCK = "환자";
                }
            }
        };

        // 라디오그룹 (라디오버튼으로 누를려면 있어야함)
        cb_login_group= (RadioGroup) findViewById(R.id.cb_login_group);
        cb_login_group.setOnCheckedChangeListener(adioGroupButtonChangeListener);



        // 로그인 하기 버튼
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectCK == "환자") {
                    Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (selectCK == "보호자"){
                    Intent intent = new Intent(getApplicationContext(), GuardianMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if(selectCK == "의사"){
                    Intent intent = new Intent(getApplicationContext(), DoctorPatientListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        // 회원가입하기 버튼
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}



