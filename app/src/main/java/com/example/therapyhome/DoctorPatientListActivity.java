package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.therapyhome.Adapter.DoctorPatientListAdapter;
import com.example.therapyhome.item.DoctorMsg;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientListActivity extends AppCompatActivity {

    // 리사이클러뷰 변수선언
    private RecyclerView rvDocterview; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private List<DoctorMsg> docterMsgList = new ArrayList<>(); // 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);

        // 더미데이터 넣기

        DoctorMsg doctorMsg = new DoctorMsg();
        doctorMsg.setName("곽철용");
        doctorMsg.setPhone("01012347777");
        int dummy = 0;
        while (dummy <= 30) {
            docterMsgList.add(doctorMsg);
            dummy ++;
        }

        // 리아시클러뷰 객체화
        rvDocterview = findViewById(R.id.rv_docterview);
        rvDocterview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rvDocterview.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        adapter = new DoctorPatientListAdapter(docterMsgList, getApplicationContext()); // 어댑터에 리스트 붙이고
        rvDocterview.setAdapter(adapter); // 리사이클러뷰에 어댑터 장착

    }
}
