package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.therapyhome.Adapter.GuardianPhoneEditAdapter;
import com.example.therapyhome.item.PhoneContactEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuardianPhoneActivity extends AppCompatActivity {
    /**
     * 환자의 연락처를 수정하는 액티비티
     */
    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;

    RecyclerView rvGuardianEditPhone;
    RecyclerView.Adapter editPhoneAdapter;
    // 환자의 연락처를 가져올 어레이 리스트
    List<PhoneContactEdit> guardianPhoneList = new ArrayList<>();

    // 파이어 베이스
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_edit_phone);

        // 리사이클러뷰 시작 ---------------------------------------------------------------------------

        // 파이어 베이스에서 데이터 저장된 데이터값 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // 파이어 베이스 검색하기
                databaseReference.child("111").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 111이 가지고 있는 데이터 리스트 가져오기
                        /**
                         * 1. 어레이스트를 만들어서 사이즈 찾기
                         *
                         */
                        Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
//                        dataSnapshot.child()



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



        // 리사이클러뷰 연결
        rvGuardianEditPhone = findViewById(R.id.rv_guardian_edit_phone);
        rvGuardianEditPhone.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        rvGuardianEditPhone.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editPhoneAdapter = new GuardianPhoneEditAdapter(guardianPhoneList);
        rvGuardianEditPhone.setAdapter(editPhoneAdapter);
        // 리사이클러뷰 끝 ---------------------------------------------------------------------------

        // 하단 네비게이션바 ---------------------------------------------------------------------------
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
                       // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.bt_edit_keyword: //키워드 편집
                        Intent editKeywordIntent = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                        //editKeywordIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editKeywordIntent);
                        finish();
                        break;
                    case R.id.bt_edit_phone://연락처 편집
                        Intent editPhomeIntent = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                        //editPhomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editPhomeIntent);
                        finish();
                        break;
                    case R.id.bt_check_health://환자 건강 상태
                        Intent checkHealthIntent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
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
}
