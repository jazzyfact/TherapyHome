package com.example.therapyhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.therapyhome.item.PhoneContactEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

import static com.example.therapyhome.LoginActivity.pwdck;

public class CustomDialogDeleteActivity extends Activity {

    /**
     *
     * 삭제하는 커스텀 다이얼로그
     *
     * @param savedInstanceState
     */

    Button okButtonDelete,cancelButtonDelete;
    DatabaseReference databaseReference;

    String deleteName;
    String deleteNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue_delete);

        // 저장하기 버튼 누르면
        okButtonDelete = findViewById(R.id.okButton_delete);
        okButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 파이어 베이스 데이터 주소
                databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");

                // 연락처 삭제하기  --------------------------------------------------
                // 리사이클러뷰에서 받아온 정보
                Intent intent = getIntent();
                deleteName =  intent.getExtras().getString("name");
                deleteNum =  intent.getExtras().getString("num");
                databaseReference.child(pwdck.getId()).child(deleteName).removeValue();
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
