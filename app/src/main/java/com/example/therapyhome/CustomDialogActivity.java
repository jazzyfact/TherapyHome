package com.example.therapyhome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CustomDialogActivity extends Activity {

    Button okButton;
    Button cancelButton;

//    private Context context;
//    final AlertDialog dlg = (AlertDialog) new Dialog(context);



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialogue);

        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
