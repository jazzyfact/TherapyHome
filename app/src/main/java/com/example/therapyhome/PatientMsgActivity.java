package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientMsgActivity extends AppCompatActivity {


    CheckBox cb_sendagree_01;
    CheckBox cb_sendagree_02;
    CheckBox cb_sendagree_03;
    CheckBox cb_sendagree_04;
    CheckBox cb_sendagree_05;
    CheckBox cb_sendagree_06;
    CheckBox cb_sendagree_07;
    CheckBox cb_sendagree_08;

    Button bt_sendtext, btSmartHome;
    Button BtEditIot, BtEditMsg;

    String result;

    Button btnPatientCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
//        cb_sendagree_01 = findViewById(R.id.cb_sendagree_01);
//        cb_sendagree_02 = findViewById(R.id.cb_sendagree_02);
//        cb_sendagree_03 = findViewById(R.id.cb_sendagree_03);
//        cb_sendagree_04 = findViewById(R.id.cb_sendagree_04);
//        cb_sendagree_05 = findViewById(R.id.cb_sendagree_05);
//        cb_sendagree_06 = findViewById(R.id.cb_sendagree_06);
//        cb_sendagree_07 = findViewById(R.id.cb_sendagree_07);
//        cb_sendagree_08 = findViewById(R.id.cb_sendagree_08);

        // 하단 네비게이션바 시작 ------------------------------------------------------------------------

        BtEditIot = findViewById(R.id.bt_edit_iot);
        BtEditIot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smartHomeIntent =new Intent(getApplicationContext(), PatientSmartHomeActivity.class);
                smartHomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(smartHomeIntent);
                finish();
            }
        });

        BtEditMsg = findViewById(R.id.bt_edit_msg);
        BtEditMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent =new Intent(getApplicationContext(), PatientMsgActivity.class);
                msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(msgIntent);
                finish();
            }
        });

        // 하단 네비게이션바 끝--------------------------------------------------------------------------


        // ????????? 시작-----------------------------------------------------------------------------
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
        // ????????? 끝-----------------------------------------------------------------------------


        btnPatientCall = findViewById(R.id.bt_patient_call);
        btnPatientCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 긴급호출
                FcmPushTest();

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
//                // SMS 발송
//                Uri uri = Uri.parse("smsto:01075582371");
//                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                it.putExtra("sms_body", result);
//                startActivity(it);
                smsMessageSent();
                restart();


            }
        });

    }
    // 문자 발송 시작 ----------------------------------------------------------------------------------
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
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("01086658747", null, result, sentPI, deliveredPI);
    }

    // 문자 발송한뒤 액티비티 재시작
    public void restart() {
        Intent intent = new Intent(getApplicationContext(), PatientMsgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    // 문자 발송 끝 ----------------------------------------------------------------------------------

    //긴급알림 푸시 시작 -----------------------------------------------------------------------------------------
    public void FcmPushTest(){

        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + "emergency");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "emergency");
            notificationObj.put("body", "긴급상황입니다!");
            //replace notification with data when went send data
            json.put("notification", notificationObj);
            String URL = "https://fcm.googleapis.com/fcm/send";
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            URL, json,
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    Log.d("FCM", "onResponse: 전송 성공");
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Failure Callback
                                    Log.d("MUR", "onError: " + error.networkResponse);
                                }
                            }) {
                        /**
                         * URL 헤더에 제이슨 정보를 담아서 구글의 서버에 보내준다.s*
                         */
                        @Override
                        public Map<String,String> getHeaders() throws AuthFailureError {
                            Map<String,String> header = new HashMap<>();
                            header.put("content-type", "application/json");
                            // 우리 앱 서비스키
                            header.put("authorization", "key=AAAAerwEOd4:APA91bEjWgznvATDZozpLYijoHPyqGannB3XANIXDgtad5XgEX-rD6Iiw5-5rbwEuuhW62LaO3Z2UB8uMBRDHlsFV5VCEZ2NK7cPU42wLwS0551L-OvPHtK9fNbDXR1Mf0Wj0Jb0S1cZ"
                            );
                            return header;
                        }
                    };
                    mRequestQue.add(jsonObjReq);
                } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //긴급알림 푸시 끝 -----------------------------------------------------------------------------------------
}
