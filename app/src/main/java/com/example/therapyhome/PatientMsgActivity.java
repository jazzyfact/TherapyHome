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
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
import com.example.therapyhome.item.docterPatient;
import com.example.therapyhome.item.patientGaurdian;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.therapyhome.LoginActivity.pwdck;

//수정

import static com.example.therapyhome.PatientMainActivity.PatientMainSpSelectMsg;
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
    Button BtEditIot, BtEditMsg, mbMsg1, mbMsg2, mbMsg3;//mb_msg_1, mb_msg_2, mb_msg_3 환자창 메세지

    String result;

    Button btnPatientCall;
    //메세지 테스트
    String msgCk;
    String msgNum;

    // 리사이클러뷰 리퀘스트 코드
    int REQUEST_CODE = 0;

    // 체크박스
    PatientEditKeyWord text01;
    PatientEditKeyWord text02;
    PatientEditKeyWord text03;



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
    private DatabaseReference databaseReferenceMSG2;
    private DatabaseReference databaseReferenceMSG3;

    // 페이스 트래킹 변수 시작 ------------------------------------------------------------------------
    private static final String TAG = "MainActivity";
    int touchView;
    Button cursor;
    CameraSource cameraSource;
    Handler handler = new Handler();
    float face_x, move_x;
    float face_y, move_y;
    float deviceWidth;
    float deviceHeight;
    boolean isTouch = false;
    long touchStart = 0;
    long touchNow = 0;
    long touchTime = 0;
    // 페이스 트래킹 변수 끝 ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        // 리사이클러뷰에서 넘어온 메세지 확인

       //환자 메세지 버튼 가져오기 mb_msg_1, mb_msg_2, mb_msg_3
        mbMsg1 = (Button) findViewById(R.id.mb_msg_1);
        mbMsg2 = (Button) findViewById(R.id.mb_msg_2);
        mbMsg3 = (Button) findViewById(R.id.mb_msg_3);

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
                                    Toast.makeText(getApplicationContext(),spMsgSelectArray.get(position)+"님이 선택되었습니다.",
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
//        Log.i("환자 메세지 보내기 ", "onDataChange: " + "2");
//        databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("patientMsg");
//        // 파이어베이스에서 리사이클러뷰에 출력할 데이터 불러오기
//        databaseReferenceMSG.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("환자 메세지 보내기 ", "onDataChange: " + "3");
                // 핑;압[ㅇ;ㅅ, 감섹힉;// PatientMainSpSelectMsg



            databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("/patientMsg/"+"33/"+"text1");
        Log.i("환자 메세지 보내기 ", "onDataChange: " + databaseReferenceMSG.toString());
            databaseReferenceMSG.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text01 = dataSnapshot.getValue(PatientEditKeyWord.class);
                mbMsg1.setText(text01.getText());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
            });

        databaseReferenceMSG2 = FirebaseDatabase.getInstance().getReference("/patientMsg/"+"33/"+"text2");
        databaseReferenceMSG2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text02 = dataSnapshot.getValue(PatientEditKeyWord.class);
                mbMsg2.setText(text02.getText());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseReferenceMSG3 = FirebaseDatabase.getInstance().getReference("/patientMsg/"+"33/"+"text3");
        databaseReferenceMSG3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text03 = dataSnapshot.getValue(PatientEditKeyWord.class);
                mbMsg3.setText(text03.getText());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        databaseReferenceGuardian.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // patientGuardian 에서 등록된 환자를 찾는다.
//                // 해당하는 환자가 있으면 정보를 받아온다.
//                patientGaurdianInfo = dataSnapshot.getValue(patientGaurdian.class);
//                patientId = patientGaurdianInfo.getName();
//                TvPatientName.setText(patientGaurdianInfo.getName());
//                TvPatientNum.setText(patientGaurdianInfo.getNum());
//                Log.i("환자 정보 찾기 ", "onDataChange: " + patientGaurdianInfo.getName());
//                Log.i("환자 정보 찾기 ", "onDataChange: " + patientId);
//                Log.i("의사 정보 찾기 ", "onDataChange: " + databaseReferenceGuardian.toString());
//                databaseReferenceGuardianDocter = FirebaseDatabase.getInstance().getReference("/docterPatient/"+patientId);
//                Log.i("의사 정보 찾기 ", "onDataChange: " + databaseReferenceGuardianDocter.toString());
//                databaseReferenceGuardianDocter.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        // patientGuardian 에서 등록된 환자를 찾는다.
//                        // 해당하는 환자가 있으면 정보를 받아온다.
//                        docterGaurdianInfo = dataSnapshot2.getValue(docterPatient.class);
//                        Log.i("의사 정보 찾기 ", "onDataChange: " + docterGaurdianInfo.getName());
//                        TvDocterName.setText(String.valueOf(docterGaurdianInfo.getName()));
//                        TvDocterNum.setText(docterGaurdianInfo.getNum());
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

                databaseReferenceMSG.child("33").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //버튼으로 메세지 부르기
                        //첫번째 메세지 버튼
                        mbMsg1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("/patientMsg/"+pwdck.getId());
                                Log.i("환자메세지", "내용 : "+mbMsg1.getText().toString());
                                PatientMainSpSelectMsg = mbMsg1.getText().toString();
                            }
                        });

                        //두번째 메세지 버튼
                        mbMsg2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("/patientMsg/"+pwdck.getId());
                                Log.i("환자메세지", "내용 : "+mbMsg2.getText().toString());
                                PatientMainSpSelectMsg = mbMsg2.getText().toString();

                            }
                        });

                        //세번째 메세지 버튼
                        mbMsg3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReferenceMSG = FirebaseDatabase.getInstance().getReference("/patientMsg/"+pwdck.getId());
                                Log.i("환자메세지", "내용 : "+mbMsg3.getText().toString());
                                PatientMainSpSelectMsg = mbMsg3.getText().toString();

                            }
                        });


//                        리사이클러뷰
/*                        if(dataSnapshot.hasChildren()){
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                rvPatientClass = dataSnapshot1.getValue(PatientEditKeyWord.class);
//                                patientEditMsg.set(rvPatientClass);
//                                patientEditMsg.setNum(spPhone);
//                                patientEditMsg.setText(rvPatientClass.getText());
                                rvPatientMsgList.add(rvPatientClass);
                                Log.i("환자키워드편집 리스트1", "onDataChange: " + "1");
                                Log.i("환자키워드편집 리스트2", "onDataChange: " + rvPatientClass.toString());
                                // 리사이클러뷰
//                                rvPatientMsg = findViewById(R.id.rv_patient_msg);
//                                rvPatientMsg.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
//                                rvPatientMsg.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                                rvPatientMsgAdapter = new PatientMsgAdapter(rvPatientMsgList);
//                                rvPatientMsg.setAdapter(rvPatientMsgAdapter);

                            }
                        }*/
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//
//        });



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
            }
        });

        // 하단 네비게이션바 끝--------------------------------------------------------------------------

        try {
            Intent intent = getIntent();
            msgCk = intent.getExtras().getString("sendText");
            result = intent.getExtras().getString("문자보내기");
            if(!msgCk.isEmpty()){
                Log.i("문자보내기확인", msgCk);

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
                result = "테스트";
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

        // 페이스 트래킹 온크리에이트 시작 ------------------------------------------------------------------------

        // 기기 화면 크기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;

        // 커서 버튼 맨 앞으로
        cursor = findViewById(R.id.cursor);
        cursor.setVisibility(View.VISIBLE);
        cursor.setX(deviceWidth/2);
        cursor.setY(deviceHeight/2);
        cursor.bringToFront();

        // 카메라 권한 있으면, 카메라에서 리소스 받아오기
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(this, "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        }
        else {
            createCameraSource();
        }
        // 페이스 트래킹 온크리에이트 끝 ------------------------------------------------------------------------

    }
    // 문자 발송 시작 ----------------------------------------------------------------------------------
    public void smsMessageSent() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        Log.i("sms 메세지보내기", "1"+PatientMainSpSelectNum);
        Log.i("문자보내기확인 번호", "2" + result);

        Log.i("메세지 보낼 번호","번호 : "+PatientMainSpSelectNum);
        Log.i("메세지 내용","내용 : "+PatientMainSpSelectMsg);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity
                            .RESULT_OK:
                        Toast.makeText(getApplicationContext(),"메세지 전송이 완료되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(PatientMainSpSelectNum, null, PatientMainSpSelectMsg, sentPI, deliveredPI);

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

    // 페이스 트래킹 메서드 시작 ------------------------------------------------------------------------
    //This class will use google vision api to detect eyes
    private class EyesTracker extends Tracker<Face> {

        // 문턱. 눈 뜰 확률.
        private final float THRESHOLD = 0.75f;

        private EyesTracker() {

        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {

            // 얼굴점 좌표 구하기
            PointF leftEyePosition = face.getLandmarks().get(4).getPosition();
            PointF rightEyePosition = face.getLandmarks().get(10).getPosition();
            face_x = (leftEyePosition.x + rightEyePosition.x) / 2;
            face_y = (leftEyePosition.y + rightEyePosition.y) / 2;

            System.out.println("face_x : " + face_x);
            System.out.println("face_y : " + face_y);

            move_x = 6 * Math.abs(deviceWidth/2 - face_x);
            move_y = 12 * Math.abs(deviceHeight/2 - face_y);

            // 커서 이동
            cursor.setX(deviceWidth - (deviceWidth - move_x));
            cursor.setY(deviceHeight - move_y); // 경계넘어갈 경우도 생각해보자.

            if (BtEditIot.getX() <= cursor.getX() && cursor.getX() <= BtEditIot.getX() + BtEditIot.getWidth()
                    && BtEditIot.getY() <= cursor.getY() && cursor.getY() <= BtEditIot.getY() + BtEditIot.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                BtEditIot.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (btnPatientCall.getX() <= cursor.getX() && cursor.getX() <= btnPatientCall.getX() + btnPatientCall.getWidth()
                    && btnPatientCall.getY() <= cursor.getY() && cursor.getY() <= btnPatientCall.getY() + btnPatientCall.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                btnPatientCall.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (mbMsg1.getX() <= cursor.getX() && cursor.getX() <= mbMsg1.getX() + mbMsg1.getWidth()
                    && mbMsg1.getY() <= cursor.getY() && cursor.getY() <= mbMsg1.getY() + mbMsg1.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                mbMsg1.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (mbMsg2.getX() <= cursor.getX() && cursor.getX() <= mbMsg2.getX() + mbMsg2.getWidth()
                    && mbMsg2.getY() <= cursor.getY() && cursor.getY() <= mbMsg2.getY() + mbMsg2.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                mbMsg2.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (mbMsg3.getX() <= cursor.getX() && cursor.getX() <= mbMsg3.getX() + mbMsg3.getWidth()
                    && mbMsg3.getY() <= cursor.getY() && cursor.getY() <= mbMsg3.getY() + mbMsg3.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                mbMsg3.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (bt_sendtext.getX() <= cursor.getX() && cursor.getX() <= bt_sendtext.getX() + bt_sendtext.getWidth()
                    && bt_sendtext.getY() <= cursor.getY() && cursor.getY() <= bt_sendtext.getY() + bt_sendtext.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setText("1");
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setText("2");
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setText("3");
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setText("4");
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cursor.setText("5");
                                bt_sendtext.performClick();
                                Toast.makeText(getApplicationContext(), "touch", Toast.LENGTH_SHORT).show();
                                cursor.setText("");
                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else {
                if (isTouch) {
                    isTouch = false;
                    touchStart = 0;
                    cursor.setText("");
                }
            }



            if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD) {
                Log.i(TAG, "onUpdate: Eyes Detected");
                showStatus("Eyes Detected and open, so video continues");
//                if (!videoView.isPlaying())
//                    videoView.start();

            }
            else {
//                if (videoView.isPlaying())
//                    videoView.pause();

                showStatus("Eyes Detected and closed, so video paused");
            }
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            showStatus("Face Not Detected yet!");
        }

        @Override
        public void onDone() {
            super.onDone();
        }
    }

    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {

        private FaceTrackerFactory() {

        }

        @Override
        public Tracker<Face> create(Face face) {
            return new EyesTracker();
        }
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(true) // 얼굴 일관된 아이디
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS) // 눈뜨감, 웃음 여부
                .setProminentFaceOnly(true) // 주요얼굴만
                .setMode(FaceDetector.FAST_MODE) // 정확도 속도 선택.
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textView.setText(message);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();


        if (cameraSource != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                cameraSource.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);//엑티비티 종료 시 애니메이션 없애기

        if (cameraSource!=null) {
            cameraSource.stop();
        }
//        if (videoView.isPlaying()) {
//            videoView.pause();
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }
    // 페이스 트래킹 메서드 끝 ------------------------------------------------------------------------
}

