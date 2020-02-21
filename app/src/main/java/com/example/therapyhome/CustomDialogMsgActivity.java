package com.example.therapyhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.therapyhome.item.GuardianMsg;
import com.example.therapyhome.item.PatientEditKeyWord;
import com.example.therapyhome.item.PhoneContactEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

import static com.example.therapyhome.LoginActivity.pwdck;

public class CustomDialogMsgActivity extends Activity {

    /**
     * 메세지 추가 수정 삭제
     */

    EditText tvSubtitleMsg;

    Button okButton;
    Button cancelButton;

    EditText TvSubtitleName;
    EditText TvSubtitleNum;

    CheckBox cbAgreeEmergecy;
    String emergencyCk;

    String addName;
    String addNum;

    String editName;
    String editNum;

    Map<String, Object> userValue = null;


    DatabaseReference databaseReference;
    PhoneContactEdit phoneContactAddObject;
    GuardianMsg makeId;

//    private Context context;
//    final AlertDialog dlg = (AlertDialog) new Dialog(context);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue_msg);

        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);

        tvSubtitleMsg =(EditText) findViewById(R.id.tv_subtitle_msg);

        // 보호자화면에서 받아온 정보
        final Intent intent = getIntent(); /*데이터 수신*/
        String intentCk = intent.getExtras().getString("intentCk");
        Log.i("인텐트 확인", "onCreate: " +intentCk);

        if(intentCk.equals("데이터추가")){
            // 파이어 베이스 데이터 주소
            databaseReference = FirebaseDatabase.getInstance().getReference("/patientMsg/"+pwdck.getId());

            // TODO: 2020-02-22 키워드추가 추가하기 --------------------------------------------------------

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 파이어베이스 저장하기
                    // 파이어베이스에 저장할 클래스 만들기
                    PatientEditKeyWord makeId = new PatientEditKeyWord(tvSubtitleMsg.getText().toString());
                    databaseReference.child(tvSubtitleMsg.getText().toString()).setValue(makeId);
                    Intent edit = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                    startActivity(edit);
                    finish();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            // TODO: 2020-02-18 키워드 추가하기 끝 --------------------------------------------------------
        } else if (intentCk.equals("데이터수정")){

            /**
             * <키워드 편집하는 로직>
             *
             */

            // 파이어 베이스 데이터 주소
            databaseReference = FirebaseDatabase.getInstance().getReference("/patientMsg/"+pwdck.getId());

            // 연락처 편집하기  --------------------------------------------------
            // 리사이클러뷰에서 받아온 정보
            editName =  intent.getExtras().getString("name");
            editNum =  intent.getExtras().getString("num");
            emergencyCk = intent.getExtras().getString("ckbox");

            // 받아온 정보 에디트 텍스트에 저장시키기
            TvSubtitleName.setText(editName);
            TvSubtitleNum.setText(editNum);
            // 긴급 연락처 체크되었는지 확인하기
            if (emergencyCk.equals("Y")){
                cbAgreeEmergecy.setChecked(true);
            }else {
                cbAgreeEmergecy.setChecked(false);
            }
            cbAgreeEmergecy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(cbAgreeEmergecy.isChecked()){
                        emergencyCk = "Y";
                    }else {
                        emergencyCk = "N";
                    }
                }
            });

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // user 의 모든 자식들의 key 값과 value 값들을 iterator에 참조한다.
                    Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                    // 중복 유무 확인
                    while (child.hasNext()){
                        if(child.next().getKey().equals(editName)) {
                            // object -> Map
                            PhoneContactEdit editPhone = new PhoneContactEdit(TvSubtitleName.getText().toString(),TvSubtitleNum.getText().toString(),emergencyCk);
                            userValue = editPhone.toMap();
                            databaseReference.child(pwdck.getId()).child(TvSubtitleName.getText().toString()).updateChildren(userValue);
                            return;
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // 수정하기 버튼 눌렀을때
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("수정하기체크", "클릭함");
                    // object -> Map
                    PhoneContactEdit editPhone = new PhoneContactEdit(TvSubtitleName.getText().toString(),TvSubtitleNum.getText().toString(),emergencyCk);
                    userValue = editPhone.toMap();
                    Log.i("수정하기체크", "onDataChange: ");
                    databaseReference.child(pwdck.getId()).child(editName).removeValue();
                    databaseReference.child(pwdck.getId()).child(TvSubtitleName.getText().toString()).updateChildren(userValue);
                    Log.i("수정하기체크", "onDataChange: ");
                    Intent edit = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                    startActivity(edit);
                    finish();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}
