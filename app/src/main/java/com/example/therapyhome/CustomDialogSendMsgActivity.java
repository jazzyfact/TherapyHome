package com.example.therapyhome;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.therapyhome.LoginActivity.pwdck;

public class CustomDialogSendMsgActivity extends Activity {

    /**
     *
     * 삭제하는 커스텀 다이얼로그
     *
     * @param savedInstanceState
     */

    Button okButtonDelete,cancelButtonDelete;
    DatabaseReference databaseReference;

    String sendMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue_msgsend);

        // 저장하기 버튼 누르면
        okButtonDelete = findViewById(R.id.okButton_delete);
        okButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 연락처 삭제하기  --------------------------------------------------
//                // 리사이클러뷰에서 받아온 정보
//                patientEdit.putExtra("intentCk",intentCk);
//                patientEdit.putExtra("sendText",patientEditKeyWordList.get(position).getText());
                Intent intent = getIntent();
                sendMsg =  intent.getExtras().getString("sendText");
                // 받은 내용가지고 메인으로 인텐트 보내기
                Intent sendIntent = new Intent(getApplicationContext(), PatientMsgActivity.class);
                sendIntent.putExtra("sendText",sendMsg);
                sendIntent.putExtra("intentCk",sendMsg);
                startActivity(sendIntent);
                finish();
            }
        });
        // 취소하기 버튼 누르기
        cancelButtonDelete = findViewById(R.id.cancelButton_delete);
        cancelButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
