package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DoctermsgActivity extends AppCompatActivity {

    // 리사이클러뷰 변수선언
    private RecyclerView rv_docterview; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private List<Doctermsg> doctermsgList = new ArrayList<>(); // 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctermsg);

        // 더미데이터 넣기
        Doctermsg doctermsg = new Doctermsg();
        doctermsg.setName("김바부");
        doctermsg.setPhone(01012341234);
        int dummy = 0;
        while (dummy <= 30) {
            doctermsgList.add(doctermsg);
            dummy ++;
        }

        // 리아시클러뷰 객체화
        rv_docterview = findViewById(R.id.rv_docterview);
        rv_docterview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rv_docterview.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        adapter = new DoctermsgAdapter(doctermsgList); // 어댑터에 리스트 붙이고
        rv_docterview.setAdapter(adapter); // 리사이클러뷰에 어댑터 장착

    }
}
