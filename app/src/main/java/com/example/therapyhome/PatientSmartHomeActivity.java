package com.example.therapyhome;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.therapyhome.item.FaceIOT;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PatientSmartHomeActivity extends AppCompatActivity {
    Button btEmergencyCall; // 긴급호출 bt_patient_call
    //IoT제어
    //서버주소 (대회당일 변경해야하는 부분)
    String LightWifiAddress = "http://192.168.0.183"; //조명
    String PanWifiAddress = "http://192.168.0.187"; //선풍기(공기청정기)
    //스위치
    List<FaceIOT> faceIOTList = new ArrayList<>();
    Button BtLightOnOff; Boolean isLishgt = false;
    Button BtPanOnOff; Boolean isPan = false;
    //하단네비게이션
    //버튼
    Button BtEditMsg, BtEditIot;

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
        // 항상켜짐 플래그
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_patient_smart_home);
        // 하단 네비게이션바
        // 메세지보내기 누를때
        BtEditMsg = findViewById(R.id.bt_edit_msg);
        BtEditMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent = new Intent(getApplicationContext(), PatientMsgActivity.class);
                startActivity(msgIntent);
                finish();
            }
        });
        // 긴급호출 버튼
        btEmergencyCall = findViewById(R.id.bt_patient_call);
        btEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FcmPushTest();
            }
        });
        if (AppHelper.requestQueue == null) {
            //리퀘스트큐 생성 (MainActivit가 메모리에서 만들어질 때 같이 생성이 될것이다.
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        //IoT스위치
        //Light제어 (조명)
        BtLightOnOff = findViewById(R.id.bt_light_onoff);
        BtLightOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLishgt == false) {
                    sendRequest(LightWifiAddress + "/ledon");
                    Log.d("IoT", "ledon");
                    isLishgt = true;
                } else {
                    sendRequest(LightWifiAddress + "/ledoff");
                    Log.d("IoT", "ledoff");
                    isLishgt = false;
                }
            }
        });
        //Pan제어 (공기청정기)
        BtPanOnOff = findViewById(R.id.bt_pan_onoff);
        BtPanOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPan == false) {
                    sendRequest(PanWifiAddress + "/panon");
                    Log.d("IoT", "panon");
                    isPan = true;
                } else {
                    sendRequest(PanWifiAddress + "/panoff");
                    Log.d("IoT", "panoff");
                    isPan = false;
                }
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


    // start "sendRequest()" : Volley의 요청함수
    public void sendRequest(String url) {
        //StringRequest는 요청객체중 하나
        //요청객체는 다음고 같이 보내는방식(GET,POST), URL, 응답성공리스너, 응답실패리스너 이렇게 4개의 파라미터를 전달
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답시 호출 (응답을 성공적으로 받았을 때 자동 호출)
                    @Override
                    public void onResponse(String response) {
                        // response = 응답결과
                    }
                },
                new Response.ErrorListener() { //에러발생시 호출
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error = 에러 이유
                        Log.d("IoT", "error : " + error);
                    }
                }
        );
        /* 아래 add코드처럼 넣어줄때 Volley라고하는게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
         * 그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도 있다.
         * 따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
         * 결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌 */
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    } // end "sendRequest()"

    //긴급알림 푸시 시작 -----------------------------------------------------------------------------------------
    public void FcmPushTest() {
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
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


            if (BtEditMsg.getX() <= cursor.getX() && cursor.getX() <= BtEditMsg.getX() + BtEditMsg.getWidth()
                    && BtEditMsg.getY() <= cursor.getY() && cursor.getY() <= BtEditMsg.getY() + BtEditMsg.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BtEditMsg.performClick();

                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (btEmergencyCall.getX() <= cursor.getX() && cursor.getX() <= btEmergencyCall.getX() + btEmergencyCall.getWidth()
                    && btEmergencyCall.getY() <= cursor.getY() && cursor.getY() <= btEmergencyCall.getY() + btEmergencyCall.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btEmergencyCall.performClick();

                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (BtLightOnOff.getX() <= cursor.getX() && cursor.getX() <= BtLightOnOff.getX() + BtLightOnOff.getWidth()
                    && BtLightOnOff.getY() <= cursor.getY() && cursor.getY() <= BtLightOnOff.getY() + BtLightOnOff.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BtLightOnOff.performClick();

                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else if (BtPanOnOff.getX() <= cursor.getX() && cursor.getX() <= BtPanOnOff.getX() + BtPanOnOff.getWidth()
                    && BtPanOnOff.getY() <= cursor.getY() && cursor.getY() <= BtPanOnOff.getY() + BtPanOnOff.getHeight()) {
                if (!isTouch) {
                    touchStart = System.currentTimeMillis();
                    isTouch = true;
                } else {
                    touchNow = System.currentTimeMillis();
                    touchTime = touchNow - touchStart;
                    System.out.println(touchTime);
                    if (1000 < touchTime && touchTime < 2000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (2000 < touchTime && touchTime < 3000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (3000 < touchTime && touchTime < 4000) {
                        cursor.setBackgroundColor(Color.RED);
                    } else if (4000 < touchTime && touchTime < 5000) {
                        cursor.setBackgroundColor(Color.BLUE);
                    } else if (touchTime > 5000) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BtPanOnOff.performClick();

                            }
                        }, 1);
                        isTouch = false;
                    }
                }
            } else {
                if (isTouch) {
                    isTouch = false;
                    touchStart = 0;
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
