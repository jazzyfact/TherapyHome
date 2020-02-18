package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.therapyhome.item.SignUpclass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Iterator;

public class SignUpActivity extends AppCompatActivity {

//    private static final String TAG = "SignupActivity";

    TextInputEditText etSignUpName; //이름
    TextInputEditText etSignUpId; // 아이디
    TextInputEditText etSignUpPwd;// 비번
    TextInputEditText etSignUpNum;//휴대폰
    TextInputEditText etSignUpComId;//의료진아이디
    CheckBox cbAgree;
    Button btnSignUp;
    Spinner spSignupSelect;

    //파이어베이스에 보낼 값들
    public String strSignUpName;
    public String strSignUpId;
    public String strSignUpPwd;
    public String strSignUpNum;
    public String strSignUpComId;
    public String strSignUpCom;

    FrameLayout FLSignupComid;

    //파이어베이스에서 데이터 읽기,쓰기 할 때 필요함
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference DatabasePath = database.getReference("user");
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
        FLSignupComid = findViewById(R.id.FL_signup_comid);

        // 스피너 부분
        spSignupSelect = findViewById(R.id.sp_signup_select);

        spSignupSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSignUpCom =  spSignupSelect.getSelectedItem().toString();
                Log.i("스피너 셀렉", "onItemSelected: "+ strSignUpCom);
                // 스피너로 값 가져오는거 구현하기
                if(strSignUpCom.equals("환자")){
                    FLSignupComid.setVisibility(View.VISIBLE);
                    etSignUpComId.setHint("의사 ID (선택)");
                } else if(strSignUpCom.equals("보호자")){
                    FLSignupComid.setVisibility(View.VISIBLE);
                    etSignUpComId.setHint("환자 ID (선택)");
                } else if (strSignUpCom.equals("의료진")){
                    FLSignupComid.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Log.d(TAG, "onClick: 회원가입버튼 누름" );
                //회원가입 정보창에 입력한 내용들 가져오기

                strSignUpName = etSignUpName.getText().toString(); // 이름
                strSignUpId = etSignUpId.getText().toString(); // 아이디
                strSignUpPwd = etSignUpPwd.getText().toString(); // 비밀번호
                strSignUpNum = etSignUpNum.getText().toString(); // 휴대폰

                strSignUpComId = etSignUpComId.getText().toString(); // 의료진 아이디


                // 파이어 베이스로 회원가입 구현하는 부분 --------------------------------------------------
                DatabasePath.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // user 의 모든 자식들의 key 값과 value 값들을 iterator에 참조한다.
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                        // 중복 유무 확인
                        while (child.hasNext()){
                            if(child.next().getKey().equals(strSignUpId)) {
                                // 중복이 될경우
                                Toast.makeText(getApplicationContext(), "존재하는 아이디 입니다.", Toast.LENGTH_LONG).show();
                                DatabasePath.removeEventListener(this);
                                return;
                            }
                        } // 중복 확인 끝나는곳료
                        mekeNewId();
                        Toast.makeText(getApplicationContext(), "가입완료", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // ---------------------------------------------------------------------------------


            }
            // 아이디 만들기
            void mekeNewId(){
                // 데이터 저장하는곳
                // child("띠용") : 띠용이라는 키
                /**
                 * strSignUpName  // 이름 (name)
                 * strSignUpId  // 아이디 (id)
                 * strSignUpPwd // 비밀번호 (pwd)
                 * strSignUpNum // 휴대폰 (num)
                 * strSignUpComId // 의료진 아이디 (comId)
                 */
                SignUpclass makeId = new SignUpclass(strSignUpName,strSignUpId,strSignUpPwd,strSignUpNum,strSignUpCom,strSignUpComId);
                DatabasePath.child(strSignUpId).setValue(makeId);

            }
        });




    }
}
