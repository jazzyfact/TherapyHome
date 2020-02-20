package com.example.therapyhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.therapyhome.item.PhoneContactEdit;
import com.example.therapyhome.item.SignUpclass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.therapyhome.LoginActivity.pwdck;

public class CustomDialogActivity extends Activity {

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

//    private Context context;
//    final AlertDialog dlg = (AlertDialog) new Dialog(context);

    /**
     *
     * 커스텀 다이얼로그 받는 로직 (각자 다른 액티비티에서 커스텀다이얼로그를 띄우게 하려면 )
     *  1. 각 액티비티에서 클릭리스너에 인텐트를 담아서 커스텀 다이얼로그에 보낸다
     *  2. 커스텀 다이얼로그 자바 코드 (이곳) 에서 받은 인텐트에 for문을 이용해서 구분한다.
     *
     * @param savedInstanceState
     */



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue);

        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);
        cbAgreeEmergecy =findViewById(R.id.cb_agree_emergecy);

        TvSubtitleName = (EditText) findViewById(R.id.tv_subtitle_name_an);
        TvSubtitleNum = (EditText) findViewById(R.id.tv_subtitle_num_an);

        // 보호자화면에서 받아온 정보
        final Intent intent = getIntent(); /*데이터 수신*/
        String intentCk = intent.getExtras().getString("intentCk");
        Log.i("인텐트 확인", "onCreate: " +intentCk);

        if(intentCk.equals("연락처추가")){

            // 파이어 베이스 데이터 주소
            databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");

            // TODO: 2020-02-18 데이터 추가하기 -------------------------------------------------------- 

            addName = TvSubtitleName.getText().toString();
            addNum = TvSubtitleNum.getText().toString();

            Log.i("연락처 추가 입력확인", "onCreate: " +addName);
            Log.i("연락처 추가 입력확인", "onCreate: " +addNum);


            // 체크박스가 체크 되었는지 안되었는지 확인하기
            if(cbAgreeEmergecy.isChecked()){
                emergencyCk = "Y";
            }else {
                emergencyCk = "N";
            }
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("editName",addName);
                    intent1.putExtra("editNum",addNum);
                    setResult(RESULT_OK,intent1);
                    Log.i("연락처 추가 입력확인",  TvSubtitleName.getText().toString());
                    Log.i("연락처 추가 입력확인",  TvSubtitleNum.getText().toString());
                    Log.i("연락처 추가 입력확인",  emergencyCk);
                    // 파이어베이스 저장하기
                    // 파이어베이스에 저장할 클래스 만들기
                    PhoneContactEdit makeId = new PhoneContactEdit(TvSubtitleName.getText().toString(),TvSubtitleNum.getText().toString(),emergencyCk);
                    databaseReference.child(pwdck.getId()).child(TvSubtitleName.getText().toString()).setValue(makeId);
                    finish();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            // TODO: 2020-02-18 데이터 추가하기 끝 --------------------------------------------------------


        } else if (intentCk.equals("연락처편집")){

            /**
             * <연락처 편집하는 로직>
             *     1. 리사이클러뷰에서 데이터를 받아온다.
             *     2. 받아온 데이터를 에디트 텍스트에 set 시킨다.
             *     3. 파이어베이스 데이터 베이스에서 해당 하는 이름(name)의 key를 찾는다.
             *     4. 찾은키를 가지고 데이터를 업데이트 한다.
             *     5. 업데이트 할때는 저장한거랑 똑같이 class 를 이용해서 json 형태로 저장해줘야함
             */

            // 파이어 베이스 데이터 주소
            databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");

            // 연락처 편집하기  --------------------------------------------------
            // 리사이클러뷰에서 받아온 정보
            editName =  intent.getExtras().getString("name");
            editNum =  intent.getExtras().getString("num");

            // 받아온 정보 에디트 텍스트에 저장시키기
            TvSubtitleName.setText(editName);
            TvSubtitleNum.setText(editNum);

            if(cbAgreeEmergecy.isChecked()){
                emergencyCk = "Y";
            }else {
                emergencyCk = "N";
            }

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
                    databaseReference.child(pwdck.getId()).child(TvSubtitleName.getText().toString()).updateChildren(userValue);
                    databaseReference.child(pwdck.getId()).child(editName).removeValue();
                    Log.i("수정하기체크", "onDataChange: ");
                    finish();
                }
            });
            // ---------------------------------------------------------------------------------




        } else if(intentCk.equals("연락처삭제")){

            /**
             * 리사이클러뷰에서 연락처 삭제인지 아닌지 인텐트로 값 받아오기
             *
             */



        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}
