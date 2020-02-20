package com.example.therapyhome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.therapyhome.Adapter.GuardianEditKeyWordAdapter;
import com.example.therapyhome.Adapter.GuardianPhoneEditAdapter;
import com.example.therapyhome.Adapter.PatientMsgAdapter;
import com.example.therapyhome.item.PatientEditKeyWord;
import com.example.therapyhome.item.PhoneContactEdit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.therapyhome.LoginActivity.pwdck;
import static com.example.therapyhome.PatientMainActivity.PatientMainSpSelectNum;

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
    //메세지 테스트
    String msgCk;
    String msgNum;

    // 리사이클러뷰 리퀘스트 코드
    int REQUEST_CODE = 0;


    // 스피너
    Spinner spMsgSelect;
    ArrayList<String> spMsgSelectArray;
    ArrayAdapter<String> spMsgSelectAdapter;

    // 스피너에서 선택한 번호 담을 string
    ArrayList<String> spMsgPhoneSelectArray;
    String spSelectNum;
    String spPhone;

    // 파이어 베이스
    private DatabaseReference databaseReference;
    PhoneContactEdit conteactNum;

    // 리사이클러뷰
    RecyclerView rvPatientMsg;
    RecyclerView.Adapter rvPatientMsgAdapter;
    // 환자텍스트를 가져올 어레이 리스트
    PatientEditKeyWord patientEditMsg;
    private List<PatientEditKeyWord> rvPatientMsgList = new ArrayList<>();
    //파이어베이스 관련
    PatientEditKeyWord rvPatientClass;
    private DatabaseReference databaseReferenceMSG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        // 리사이클러뷰에서 넘어온 메세지 확인



        // 스피너에 파이어베이스 구현 시작 ----------------------------------------------------------------
        spMsgSelect = findViewById(R.id.sp_msg_select);

        //포문으로 파이어 베이스에서 가져온 데이터값 넣기
        spMsgSelectArray = new ArrayList<>();
        spMsgPhoneSelectArray = new ArrayList<>();

        // 파이어 베이스 데이터 주소
        databaseReference = FirebaseDatabase.getInstance().getReference("contactNumber");
        // 파이어 베이스에서 데이터 저장된 데이터값 가져오기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어 베이스 검색하기
                databaseReference.child("33").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                conteactNum = dataSnapshot1.getValue(PhoneContactEdit.class);
                                // 스피너 어댑터 구현
                                spMsgSelectArray.add(conteactNum.getName());
                                spMsgPhoneSelectArray.add(conteactNum.getNum());
                                Log.i("데이터확인", "onDataChange: " + conteactNum.getNum());
                            }
                            spMsgSelectAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_spinner_item,spMsgSelectArray);
                            spMsgSelect.setAdapter(spMsgSelectAdapter);
                            spMsgSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getApplicationContext(),spMsgPhoneSelectArray.get(position)+"가 선택되었습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    PatientMainSpSelectNum = spMsgPhoneSelectArray.get(position);
                                    Log.i("스피너 셀렉", "onDataChange: " + spPhone);
                                    /**
                                     * 여기 보기
                                     */

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            Toast.makeText(PatientMsgActivity.this, "등록된 연락처가 없습니다.", Toast.LENGTH_SHORT).show();
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
        // 스피너에 파이어베이스 구현 끝 ----------------------------------------------------------------


        // 리사이클러뷰 시작 ---------------------------------------------------------------------------
        Log.i("환자 메세지 보내기 ", "onDataChange: " + "1");
        // 파이어베이스 시작
        Log.i("환자 메세지 보내기 ", "onDataChange: " + "2");
        databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("patientMsg");
        // 파이어베이스에서 리사이클러뷰에 출력할 데이터 불러오기
        databaseReferenceMSG.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("환자 메세지 보내기 ", "onDataChange: " + "3");
                // 핑;압[ㅇ;ㅅ, 감섹힉;
                databaseReferenceMSG.child("33").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                rvPatientClass = dataSnapshot1.getValue(PatientEditKeyWord.class);
//                                patientEditMsg.set(rvPatientClass);
//                                patientEditMsg.setNum(spPhone);
//                                patientEditMsg.setText(rvPatientClass.getText());
                                rvPatientMsgList.add(rvPatientClass);
                                Log.i("환자키워드편집 리스트1", "onDataChange: " + "1");
                                Log.i("환자키워드편집 리스트2", "onDataChange: " + rvPatientClass.toString());
                                // 리사이클러뷰
                                rvPatientMsg = findViewById(R.id.rv_patient_msg);
                                rvPatientMsg.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                                rvPatientMsg.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rvPatientMsgAdapter = new PatientMsgAdapter(rvPatientMsgList);
                                rvPatientMsg.setAdapter(rvPatientMsgAdapter);

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



        // 라사이클러뷰 끝 ----------------------------------------------------------------------------



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

        try {
            Intent intent = getIntent();
            msgCk = intent.getExtras().getString("sendText");
            result = intent.getExtras().getString("문자보내기");
            if(!msgCk.isEmpty()){
                Log.i("문자보내기확인", msgCk);
                smsMessageSent();
                restart();
            } else if(msgCk.isEmpty()) {
                Log.i("문자보내기확인", "빈값임");
            }
        }catch (Exception e){

        }

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
//                result = "";
//                if (cb_sendagree_01.isChecked() == true)
//                    result += cb_sendagree_01.getText().toString();
//                if (cb_sendagree_02.isChecked() == true)
//                    result += cb_sendagree_02.getText().toString();
//                if (cb_sendagree_03.isChecked() == true)
//                    result += cb_sendagree_03.getText().toString();
//                if (cb_sendagree_04.isChecked() == true)
//                    result += cb_sendagree_04.getText().toString();
//                if (cb_sendagree_05.isChecked() == true)
//                    result += cb_sendagree_05.getText().toString();
//                if (cb_sendagree_06.isChecked() == true)
//                    result += cb_sendagree_06.getText().toString();
//                if (cb_sendagree_07.isChecked() == true)
//                    result += cb_sendagree_07.getText().toString();
//                if (cb_sendagree_08.isChecked() == true)
//                    result += cb_sendagree_08.getText().toString();
//                // SMS 발송
//                Uri uri = Uri.parse("smsto:01075582371");
//                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                it.putExtra("sms_body", result);
//                startActivity(it);
                smsMessageSent();
//                restart();


            }
        });

    }
    // 문자 발송 시작 ----------------------------------------------------------------------------------
    public void smsMessageSent() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        Log.i("sms 메세지보내기",PatientMainSpSelectNum);
        Log.i("문자보내기확인 번호", result);

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
        sms.sendTextMessage(PatientMainSpSelectNum, null, result, sentPI, deliveredPI);

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try {
                    Intent intent = getIntent();
                    msgCk = intent.getExtras().getString("sendText");
                    msgNum = intent.getExtras().getString("sendNum");
                    Log.i("문자보내기확인", msgCk);
                    Log.i("문자보내기확인 번호", msgNum);
//                    result = intent.getExtras().getString("문자보내기");
                    if(!msgCk.isEmpty()){
                        Log.i("문자보내기확인", msgCk);
                        Log.i("문자보내기확인 번호", msgNum);
                        smsMessageSent();
                    } else if(msgCk.isEmpty()) {
                        Log.i("문자보내기확인", "빈값임");
                    }
                }catch (Exception e){

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
            }
        }
    }

    }

   @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}

