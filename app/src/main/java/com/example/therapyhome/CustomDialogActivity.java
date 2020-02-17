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
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CustomDialogActivity extends Activity {

    Button okButton;
    Button cancelButton;
    EditText TvSubtitleName;
    EditText TvSubtitleNum;

//    private Context context;
//    final AlertDialog dlg = (AlertDialog) new Dialog(context);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue);

        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);
        // 보호자화면에서 받아온 정보
        final Intent intent = getIntent(); /*데이터 수신*/
        String name = intent.getExtras().getString("name");
        String num = intent.getExtras().getString("num");/*String형*/
        Log.i("인텐트 확인", "onCreate: " +name);

        // 확인 버튼과 취소버튼
        TvSubtitleName = findViewById(R.id.tv_subtitle_name_an);
        TvSubtitleNum = findViewById(R.id.tv_subtitle_num_an);

        TvSubtitleName.setText(name);
        TvSubtitleNum.setText(num);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.putExtra("editName",TvSubtitleName.getText().toString());
                intent1.putExtra("editNum",TvSubtitleNum.getText().toString());
                setResult(RESULT_OK,intent1);
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
