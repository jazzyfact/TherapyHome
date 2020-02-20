package com.example.therapyhome;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class PatientSmartHomeActivity extends AppCompatActivity {
    Button btEmergencyCall; // 긴급호출 bt_patient_call
    //IoT제어
    //서버주소 (대회당일 변경해야하는 부분)
    String LightWifiAddress = "http://192.168.0.183"; //조명
    String PanWifiAddress = "http://192.168.0.187"; //선풍기(공기청정기)
    //스위치
    Switch StLightOnOff, StPanOnOff;
    //하단네비게이션
    //버튼
    Button BtEditMsg, BtEditIot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        StLightOnOff = findViewById(R.id.st_light_onoff);
        StLightOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sendRequest(LightWifiAddress + "/ledon");
                    Log.d("IoT", "ledon");
                } else {
                    sendRequest(LightWifiAddress + "/ledoff");
                    Log.d("IoT", "ledoff");
                }
            }
        });
        //Pan제어 (공기청정기)
        StPanOnOff = findViewById(R.id.st_pan_onoff);
        StPanOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sendRequest(PanWifiAddress + "/panon");
                    Log.d("IoT", "panon");
                } else {
                    sendRequest(PanWifiAddress + "/panoff");
                    Log.d("IoT", "panoff");
                }
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);//엑티비티 종료 시 애니메이션 없애기
    }

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
}
//긴급알림 푸시 끝 -----------------------------------------------------------------------------------------
