package com.example.therapyhome;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.therapyhome.item.SignUpclass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    /**
     *     현재 아이디가 저장되어있는 object
     *     로그아웃 할때는 pwdck를 업데이트 해줘야한다.
     *     정보를 업데이트 하거나 삭제할때 pwdck 를 업데이트 하는것 잊지말기
     *     연락처를 저장하는 데이터 구조를 다시생각하기
     *
     */

    public static SignUpclass pwdck;


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

    EditText etEnterId;
    EditText etEnterPwd;
//
//    private String findpwd;
//    private String findId;

    String selectCK ; // 클릭했는지 안했는지 체크하기
    // 파이어 베이스 관련
    private DatabaseReference databaseReference;
    //
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 로그인 회원가입 입력값
        etEnterId = findViewById(R.id.et_enter_id);
        etEnterPwd = findViewById(R.id.et_enter_pwd);

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
                        Log.i("파이어베이스 데이터 흐름", "1");
                        // 파이어 베이스 관련
                        databaseReference = FirebaseDatabase.getInstance().getReference("user");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.i("파이어베이스 데이터 흐름", "2");
                                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                                while(child.hasNext())
                                {
                                    final String findId = etEnterId.getText().toString();
                                    final String findpwd = etEnterPwd.getText().toString();
                                    //찾고자 하는 ID값은 key로 존재하는 값
                                    Log.i("파이어베이스 데이터 흐름", "3");
                                    Log.i("파이어베이스 데이터 변수", findId);
                                    final String ck = child.next().getKey();
                                    Log.i("파이어베이스 데이터 변수2", ck );
                                    if(ck.equals(findId)) {
                                    Object ck2 = dataSnapshot.child(findId).getValue();
                                        Log.i("파이어베이스 데이터 흐름", "4");
                                        Log.i("파이어베이스 데이터 구조2", "onDataChange: "+dataSnapshot.child(findId).getValue());
                                    databaseReference.child(findId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.getValue();
                                            Log.i("파이어베이스 데이터 흐름", "4");
                                            Log.i("파이어베이스 데이터 구조2", "onDataChange: "+ dataSnapshot.getValue());
                                            pwdck = dataSnapshot.getValue(SignUpclass.class);
                                            String pwdckeck = pwdck.getPwd();

                                            /**
                                             * 현재까지 제이슨으로 받아오는것은 성공
                                             * 그다음에는 제이슨을 받아와서 파싱해야한다.
                                             * 아니 시발 알고보니 제이슨이 아니였음
                                             * 시발..
                                             */
                                            if(findpwd.equals(pwdckeck)){
                                                Toast.makeText(getApplicationContext(),"로그인!",Toast.LENGTH_LONG).show();
                                                Log.i("파이어베이스 데이터 흐름", "5");
                                                // 로그인 완료
                                                //3가지 기준으로 인텐트를 보내한다.
                                                String com = pwdck.getCom();
                                                Log.i("파이어베이스 데이터 흐름", "6");
                                                if (com.equals("환자")){
                                                    Log.i("파이어베이스 데이터 흐름", "7");
                                                    Intent intent = new Intent(getApplicationContext(), PatientMsgActivity.class);
                                                    startActivity(intent);
                                                    return;
                                                } else if (com.equals("보호자")){
                                                    Log.i("파이어베이스 데이터 흐름", "7");
                                                    Intent intent = new Intent(getApplicationContext(), GuardianMainActivity.class);
                                                    startActivity(intent);
                                                    return;
                                                }else if (com.equals("의료진")) {
                                                    Log.i("파이어베이스 데이터 흐름", "7");
                                                    Intent intent = new Intent(getApplicationContext(), DoctorPatientListActivity.class);
                                                    startActivity(intent);
                                                    return;
                                                }
                                                Log.i("파이어베이스 데이터 흐름", "8");


                                            } else {
                                                Toast.makeText(getApplicationContext(),"아이디 및 비밀번호를 다시 확인해주세요.",Toast.LENGTH_LONG).show();
                                                Log.i("파이어베이스 데이터 흐름", "6");
                                                return;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    Log.i("파이어베이스 데이터 흐름", "7");

                                }Log.i("파이어베이스 데이터 흐름", "8");

                            }Log.i("파이어베이스 데이터 흐름", "9");
//                                Log.i("파이어베이스 데이터 흐름", "10");
//                                Toast.makeText(getApplicationContext(),"존재하지 않는 아이디입니다.",Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

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



