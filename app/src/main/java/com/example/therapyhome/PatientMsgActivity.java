package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.IntentCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class PatientMsgActivity extends AppCompatActivity {


    CheckBox cb_sendagree_01;
    CheckBox cb_sendagree_02;
    CheckBox cb_sendagree_03;
    CheckBox cb_sendagree_04;
    CheckBox cb_sendagree_05;
    CheckBox cb_sendagree_06;
    CheckBox cb_sendagree_07;
    CheckBox cb_sendagree_08;

    Button bt_sendtext;

    String result;

    Button btnPatientCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        cb_sendagree_01 = findViewById(R.id.cb_sendagree_01);
        cb_sendagree_02 = findViewById(R.id.cb_sendagree_02);
        cb_sendagree_03 = findViewById(R.id.cb_sendagree_03);
        cb_sendagree_04 = findViewById(R.id.cb_sendagree_04);
        cb_sendagree_05 = findViewById(R.id.cb_sendagree_05);
        cb_sendagree_06 = findViewById(R.id.cb_sendagree_06);
        cb_sendagree_07 = findViewById(R.id.cb_sendagree_07);
        cb_sendagree_08 = findViewById(R.id.cb_sendagree_08);


        //채널
        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // 채널 ID
        String id = "my_channel_01";


        // 채널 이름
        CharSequence name = "test";


        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id, name, importance);


        // 알림 채널에 사용할 설정을 구성한다.

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true); //소리 및 진동
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


        // 뱃지 사용 여부를 설정한다.(8.0부터는 기본이 true인듯하다.)

        mChannel.setShowBadge(true);
        mNotificationManager.createNotificationChannel(mChannel);


        btnPatientCall = findViewById(R.id.bt_patient_call);
        btnPatientCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("PatientaMsgActivity", "긴급호출클릭");
                //알림


                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                // 알림 ID
                int notifyID = 1;


                // 1번에서 생성한 채널 ID 이름

                String CHANNEL_ID = "my_channel_01";


                // 알림을 만들고 알림 채널을 설정합니다.

                Notification notification = new Notification.Builder(PatientMsgActivity.this)

                        .setContentTitle("긴급호출!!!")

                        .setContentText("빨리 와주세요!!!!!!!")

                        .setSmallIcon(R.drawable.medical240)

                        .setChannelId(CHANNEL_ID)

                        .build();


// Issue the notification.

                mNotificationManager.notify(notifyID, notification);


            }
        });


        bt_sendtext = findViewById(R.id.bt_sendtext);
        bt_sendtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = "";
                if (cb_sendagree_01.isChecked() == true)
                    result += cb_sendagree_01.getText().toString();
                if (cb_sendagree_02.isChecked() == true)
                    result += cb_sendagree_02.getText().toString();
                if (cb_sendagree_03.isChecked() == true)
                    result += cb_sendagree_03.getText().toString();
                if (cb_sendagree_04.isChecked() == true)
                    result += cb_sendagree_04.getText().toString();
                if (cb_sendagree_05.isChecked() == true)
                    result += cb_sendagree_05.getText().toString();
                if (cb_sendagree_06.isChecked() == true)
                    result += cb_sendagree_06.getText().toString();
                if (cb_sendagree_07.isChecked() == true)
                    result += cb_sendagree_07.getText().toString();
                if (cb_sendagree_08.isChecked() == true)
                    result += cb_sendagree_08.getText().toString();
                // SMS 발송
                Uri uri = Uri.parse("smsto:01075582371");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", result);
                startActivity(it);
//
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage("01075582371", null, result, null, null);
                smsMessageSent();
                restart();


            }
        });

    }

    public void smsMessageSent() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity
                            .RESULT_OK:
                        Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("01075582371", null, result, sentPI, deliveredPI);
    }

    public void restart() {
//        Intent i = getBaseContext().getPackageManager().
//                getLaunchIntentForPackage(getBaseContext().getPackageName());
        Intent intent = new Intent(getApplicationContext(), PatientMsgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
