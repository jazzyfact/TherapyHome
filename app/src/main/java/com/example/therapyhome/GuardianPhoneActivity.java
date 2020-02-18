package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import static com.example.therapyhome.LoginActivity.pwdck;

public class GuardianPhoneActivity extends AppCompatActivity {
    /**
     * 환자의 연락처를 수정하는 액티비티
     */
    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg;
    ImageView btGuardianContactNumAdd;

    private RecyclerView rvGuardianEditPhone;
    private RecyclerView.Adapter editPhoneAdapter;
    // 환자의 연락처를 가져올 어레이 리스트
    private List<PhoneContactEdit> guardianPhoneList = new ArrayList<>();

    // 파이어 베이스
    private DatabaseReference databaseReference;
    PhoneContactEdit conteactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_edit_phone);
        // 파이어 베이스 데이터 주소
        databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");

        // 데이터 추가하는곳 시작 -----------------------------------------------------------------------
        /**
         * <데이터 추가 로직>
         *     1. 중복검사를 통해 (key 값은 ID 값) 이사람이 아이디가 저장되어있는지 아닌지 체크한다.
         */
        btGuardianContactNumAdd = findViewById(R.id.bt_guardian_contactNum_add);
        btGuardianContactNumAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentCk = "연락처추가";
                Intent patientEdit = new Intent(getApplicationContext(),CustomDialogActivity.class);
                patientEdit.putExtra("intentCk",intentCk);
//                patientEdit.putExtra("name",intentCk);
//                patientEdit.putExtra("num",pwdck.getName());
//                patientEdit.putExtra("emergency",pwdck.getNum());
                startActivityForResult(patientEdit,1);

            }
        });

        // 데이터 추가하는곳 끝 -------------------------------------------------------------------------

        // 리사이클러뷰 시작 ---------------------------------------------------------------------------
        /**
         *
         * 연락처 추가,수정,삭제 로직
         *  1. 데이터가 있는지 없는지 확인하기
         *  2. 데이터가 없으면 등록된 데이터가 없다는 텍스트 나오게
         *  3. 데이터가 있으면 리사이클러뷰에 뿌려주기
         *  4. 데이터를 수정하고 싶으면 클릭해서 커스텀 다이얼로그 띄우기
         *
         */

        // 파이어 베이스에서 데이터 저장된 데이터값 가져오기

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어 베이스 검색하기
                databaseReference.child(pwdck.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // 111이 가지고 있는 데이터 리스트 가져오기
                        // for문을 쓸때는 하단의 표현식 처럼 써야됨
                        // 여기서 if 문으로 데이터가 없는 경우/ 있는경우를 나눠서 써야한다.
                        // 데이터가 있는 경우
                        if(dataSnapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                conteactNum = dataSnapshot1.getValue(PhoneContactEdit.class);

                                /**
                                 * 1. 파이어 베이스에서 1개씩 데이터 가져오기 오브젝트로 가져와야함
                                 * 2. 1개씩 가져온 데이터를 어레이 리스트에 넣기
                                 * 3. 넣은 어레이 리스트를 리사이클러뷰에 넣기
                                 *
                                 *     String name;
                                 *     String num;
                                 *     String emergency;
                                 *
                                 */

                                guardianPhoneList.add(conteactNum);
                            }
                            // 리사이클러뷰 연결
                            rvGuardianEditPhone = findViewById(R.id.rv_guardian_edit_phone);
                            rvGuardianEditPhone.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                            rvGuardianEditPhone.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            editPhoneAdapter = new GuardianPhoneEditAdapter(guardianPhoneList);
                            rvGuardianEditPhone.setAdapter(editPhoneAdapter);
                        } else {
                            Toast.makeText(GuardianPhoneActivity.this, "등록된 연락처가 없습니다.", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                Log.i("커스텀다이얼로그 결과확인", "들어왔니? ");
                String NameResult = data.getStringExtra("editName");
                String NumResult = data.getStringExtra("editNum");
                // 파이어 베이스에 저장되도록 하기
                // 저장할 클래스 만들기

            }
        }
    }
}
