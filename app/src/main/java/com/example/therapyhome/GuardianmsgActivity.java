package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class GuardianmsgActivity extends AppCompatActivity {

    // 리사이클러뷰 변수선언
    private RecyclerView rvGuardianview; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private List<Guardianmsg> guardianMsgList = new ArrayList<>(); // 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardianmsg);


        // 더미데이터 넣기
        Guardianmsg guardianmsg = new Guardianmsg();
        guardianmsg.setMessage("바부바부바부야");
        int dummy = 0;
        while (dummy <= 30) {
            guardianMsgList.add(guardianmsg);
            dummy ++;
        }

        // 리아시클러뷰 객체화
        rvGuardianview = findViewById(R.id.rv_guardianview);
        rvGuardianview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvGuardianview.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        adapter = new GuardianmsgAdapter(guardianMsgList); // 어댑터에 리스트 붙이고
        rvGuardianview.setAdapter(adapter); // 리사이클러뷰에 어댑터 장착
    }
}
