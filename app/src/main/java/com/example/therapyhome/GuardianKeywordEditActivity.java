package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.therapyhome.Adapter.GuardianEditKeyWordAdapter;
import com.example.therapyhome.Adapter.GuardianPhoneEditAdapter;
import com.example.therapyhome.item.PatientEditKeyWord;
import com.example.therapyhome.item.PhoneContactEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.therapyhome.LoginActivity.pwdck;

public class GuardianKeywordEditActivity extends AppCompatActivity {
    /**
     * 문자의 텍스트를 편집하는 액티비티
     */
    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;

    RecyclerView rvGuardianEditKey;
    RecyclerView.Adapter rvGuardianEditKeyAdapter;
    // 환자의 연락처를 가져올 어레이 리스트
    private List<PatientEditKeyWord> guardianEditKeyList = new ArrayList<>();
    PatientEditKeyWord patientEditKey;
    DatabaseReference dtabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_text_edit);

        /**
         * 환자의 문자를 수정하는로직
         * 1. 일단 리사이클러뷰
         */


        // 파이어베이스 주소
        dtabaseReference = FirebaseDatabase.getInstance().getReference("patientMsg");
        Log.i("환자키워드편집 리스트1", "onDataChange: " + "되고있나?");
        // 파이어베이스에서 리사이클러뷰에 출력할 데이터 불러오기
        dtabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 핑;압[ㅇ;ㅅ, 감섹힉;
                dtabaseReference.child("33").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                patientEditKey = dataSnapshot1.getValue(PatientEditKeyWord.class);
                                guardianEditKeyList.add(patientEditKey);
                                Log.i("환자키워드편집 리스트1", "onDataChange: " + "1");
                                Log.i("환자키워드편집 리스트2", "onDataChange: " + patientEditKey.toString());
                                // 리사이클러뷰
                                rvGuardianEditKey= (RecyclerView) findViewById(R.id.rv_guardian_Editkey);
                                rvGuardianEditKey.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                                rvGuardianEditKey.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rvGuardianEditKeyAdapter = new GuardianEditKeyWordAdapter(guardianEditKeyList);
                                rvGuardianEditKey.setAdapter(rvGuardianEditKeyAdapter);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


//        하단 네비게이션바
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

                }
            }
        };
        btnReadMsg.setOnClickListener(onClickListener);
        btnCheckHealth.setOnClickListener(onClickListener);
        btnEditKeyword.setOnClickListener(onClickListener);
        btnEditPhone.setOnClickListener(onClickListener);


    }


}

