package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static com.example.therapyhome.LoginActivity.pwdck;

public class GuardianMainActivity extends AppCompatActivity {

    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;
    ImageView ivGuardianEditDoctor, ivGuardianEditFamily, ivGuardianEditMy;
    // 메인 페이지 연락처 표시
    TextView TvGuardianName;
    TextView TvGuardianNum;
    TextView TvGuardianEmergency;
    TextView TvPatientName;
    TextView TvPatientNum;
    TextView TvPatientEmergency;
    TextView TvDocterName;
    TextView TvDocterNum;
    TextView TvDocterEmergency;

    /**
     * 2020 02 17 진행해야할사항
     * 1. 연락처 편집을 하면 연락처 편집이 되게
     * 2. 파이어베이스에서 정보를 받아와서 보여주기
     * 3. 파이어베이스에서 정보 수정하기
     *
     * @param savedInstanceState
     */

    // 파이어베이스
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_main);

        ivGuardianEditMy = findViewById(R.id.iv_guardian_editmy); //내정보
        ivGuardianEditFamily = findViewById(R.id.iv_guardian_editFamily);//등록된 환자
        ivGuardianEditDoctor = findViewById(R.id.bt_guardian_editdocter); //담당 의사선생님

//        하단 네비게이션바
//        Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;
        btnEditKeyword = findViewById(R.id.bt_edit_keyword); //키워드 편집 버튼
        btnEditPhone = findViewById(R.id.bt_edit_phone);//연락처 편집 버튼
        btnCheckHealth = findViewById(R.id.bt_check_health);//건강 상태 버튼
        btnReadMsg = findViewById(R.id.bt_read_msg);//문자 모아보기 버튼

        // 연락처 표시하기
        TvGuardianName = findViewById(R.id.tv_guardian_name);
        TvGuardianNum = findViewById(R.id.tv_guardian_num);
        TvGuardianEmergency = findViewById(R.id.tv_guardian_emergency);

        TvPatientName = findViewById(R.id.tv_patient_name);
        TvPatientNum = findViewById(R.id.tv_patient_num);
        TvPatientEmergency = findViewById(R.id.tv_patient_emergency);

        TvDocterName = findViewById(R.id.tv_docter_name);
        TvDocterNum = findViewById(R.id.tv_docter_num);
        TvDocterEmergency = findViewById(R.id.tv_docter_emergency);

        // 내정보 표시하기
        TvGuardianName.setText(pwdck.getName());
        TvGuardianNum.setText(pwdck.getNum());

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_read_msg: //문자모아보기
                        Intent intent = new Intent(getApplicationContext(), GuardianMsgActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.bt_edit_keyword: //키워드 편집
                        Intent editKeywordIntent = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                        editKeywordIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editKeywordIntent);
                        break;
                    case R.id.bt_edit_phone://연락처 편집
                        Intent editPhomeIntent = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                        editPhomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editPhomeIntent);
                        break;
                    case R.id.bt_check_health://환자 건강 상태
                        Intent checkHealthIntent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
                        checkHealthIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(checkHealthIntent);
                        break;

                    case R.id.iv_guardian_editFamily://등록된 환자 수정하기
                        Intent patientEdit = new Intent(getApplicationContext(),CustomDialogActivity.class);
                        patientEdit.putExtra("name",pwdck.getName());
                        patientEdit.putExtra("num",pwdck.getNum());
                        startActivityForResult(patientEdit,1);
                        break;
                    case R.id.iv_guardian_editmy://내정보 수정하기
                        Intent editmyIntent = new Intent(getApplicationContext(), CustomDialogActivity.class);
                        editmyIntent.putExtra("name",pwdck.getName());
                        editmyIntent.putExtra("num",pwdck.getNum());
                        startActivityForResult(editmyIntent,2);

                        Log.i("커스텀다이얼로그 결과확인", "onActivityResult: " + editmyIntent);
                        break;
                    case R.id.bt_guardian_editdocter: //등록된 의사 수정하기
                        Intent editDoctorIntent = new Intent(getApplicationContext(), CustomDialogActivity.class);
                        editDoctorIntent.putExtra(pwdck.getName(),"my_name");
                        editDoctorIntent.putExtra(pwdck.getNum(),"my_num");
//                        editDoctorIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                Log.i("커스텀다이얼로그 결과확인", "들어왔니? ");
                String NameResult = data.getStringExtra("editName");
                String NumResult = data.getStringExtra("editNum");
                TvGuardianName.setText(NameResult);
                TvGuardianNum.setText(NumResult);
                // 파이어 베이스에 저장되도록 하기


            }
        }
    }


    }


