package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

//    private static final String TAG = "SignupActivity";

    TextInputEditText etSignUpName; //이름
    TextInputEditText etSignUpId; // 아이디
    TextInputEditText etSignUpPwd;// 비번
    TextInputEditText etSignUpNum;//휴대폰
    TextInputEditText etSignUpComId;//의료진아이디
    CheckBox cbAgree;
    Button btnSignUp;

    //파이어베이스에 보낼 값들
    public String strSignUpName;
    public String strSignUpId;
    public String strSignUpPwd;
    public String strSignUpNum;
    public String strSignUpComId;

    //파이어베이스에서 데이터 읽기,쓰기 할 때 필요함
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpName = findViewById(R.id.et_signup_name);
        etSignUpId = findViewById(R.id.et_signup_id);
        etSignUpPwd = findViewById(R.id.et_signup_pwd);
        etSignUpNum = findViewById(R.id.et_signup_num);
        etSignUpComId = findViewById(R.id.et_signup_comid);
        btnSignUp = findViewById(R.id.btn_sign);


        mDatabase = FirebaseDatabase.getInstance().getReference();




        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Log.d(TAG, "onClick: 회원가입버튼 누름" );
                //회원가입 정보창에 입력한 내용들 가져오기

                strSignUpName = etSignUpName.getText().toString();
                strSignUpId = etSignUpId.getText().toString();
                strSignUpPwd = etSignUpPwd.getText().toString();
                strSignUpNum = etSignUpNum.getText().toString();
                strSignUpComId = etSignUpComId.getText().toString();


//sFFJKSDhfSDJHFJKD
            }
        });

    }




}
