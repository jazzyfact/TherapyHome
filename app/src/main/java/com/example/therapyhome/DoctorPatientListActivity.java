package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.therapyhome.Adapter.DoctorPatientListAdapter;
import com.example.therapyhome.Adapter.PatientMsgAdapter;
import com.example.therapyhome.item.DoctorMsg;
import com.example.therapyhome.item.PatientEditKeyWord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.therapyhome.LoginActivity.pwdck;

public class DoctorPatientListActivity extends AppCompatActivity {

    // 리사이클러뷰 변수선언
    private RecyclerView rvDocterview; // 리사이클러뷰
    private RecyclerView.Adapter adapter; // 어댑터
    private List<DoctorMsg> docterMsgList = new ArrayList<>(); // 리스트
    //파이어베이스 관련
    DoctorMsg doctorMsgClass;
    private DatabaseReference databaseReferenceDocMSG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);

        // 리사이클러뷰 시작 ---------------------------------------------------------------------------
        Log.i("환자 리스트 보기 ", "onDataChange: " + "1");
        // 파이어베이스 시작
        Log.i("환자 리스트 보기 ", "onDataChange: " + "2");
        databaseReferenceDocMSG = FirebaseDatabase.getInstance().getReference("docter");
        // 파이어베이스에서 리사이클러뷰에 출력할 데이터 불러오기
        databaseReferenceDocMSG.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("환자 리스트 보기", "onDataChange: " + "3");
                databaseReferenceDocMSG.child(pwdck.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                doctorMsgClass = dataSnapshot1.getValue(DoctorMsg.class);
                                // 여기서 어레이 리스트에 넣을 클래스를 생성한다.
//                                patientEditMsg.set(rvPatientClass);
//                                patientEditMsg.setNum(spPhone);
//                                patientEditMsg.setText(rvPatientClass.getText());
                                docterMsgList.add(doctorMsgClass);
                                Log.i("환자키워드편집 리스트1", "onDataChange: " + "1");
                                Log.i("환자키워드편집 리스트2", "onDataChange: " + doctorMsgClass.toString());
                                // 리아시클러뷰 객체화
                                rvDocterview = findViewById(R.id.rv_docterview);
                                rvDocterview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                                rvDocterview.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
                                adapter = new DoctorPatientListAdapter(docterMsgList, getApplicationContext()); // 어댑터에 리스트 붙이고
                                rvDocterview.setAdapter(adapter); // 리사이클러뷰에 어댑터 장착
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

    }
}
