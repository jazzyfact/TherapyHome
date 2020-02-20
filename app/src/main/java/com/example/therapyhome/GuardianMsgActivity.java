package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.therapyhome.Adapter.GuardianMsgAdapter;
import com.example.therapyhome.item.GuardianMsg;

import java.util.ArrayList;
import java.util.List;

public class GuardianMsgActivity extends AppCompatActivity {
    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;

    /**
     * 사용안함
     */

    // 리사이클러뷰 변수선언
    private RecyclerView rvGuardianview; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private List<GuardianMsg> guardianMsgList = new ArrayList<>(); // 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_msg);


        // 더미데이터 넣기
        GuardianMsg guardianmsg = new GuardianMsg();
        guardianmsg.setMessage("가래가 꼈어요");
        int dummy = 0;
        while (dummy <= 30) {
            guardianMsgList.add(guardianmsg);
            dummy ++;
        }

        // 리아시클러뷰 객체화
        rvGuardianview = findViewById(R.id.rv_guardianview);
        rvGuardianview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvGuardianview.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        adapter = new GuardianMsgAdapter(guardianMsgList); // 어댑터에 리스트 붙이고
        rvGuardianview.setAdapter(adapter); // 리사이클러뷰에 어댑터 장착


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
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}
