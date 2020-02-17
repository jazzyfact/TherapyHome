package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PatientSmartHomeActivity extends AppCompatActivity {


    String ledurl;
    Button btMsg;
    Button bt_edit_msg, bt_edit_iot;
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
                Intent msgIntent =new Intent(getApplicationContext(), PatientMsgActivity.class);
                startActivity(msgIntent);
                finish();
            }
        });
        // iot 버튼 누를때
        BtEditIot = findViewById(R.id.bt_edit_iot);
        BtEditIot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smartHomeIntent =new Intent(getApplicationContext(), PatientSmartHomeActivity.class);
                startActivity(smartHomeIntent);
                finish();
            }
        });


        Switch st_light_onoff = findViewById(R.id.st_light_onoff);

        st_light_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {



            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub

                if(isChecked ==true){
                    ledurl = "http://192.168.0.11/on";
                    sendRequest(ledurl);
//                    Toast.makeText(PatientSmartHomeActivity.this, "체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();

                } else{
                    ledurl = "http://192.168.0.11/off";
                    sendRequest(ledurl);
//                    Toast.makeText(PatientSmartHomeActivity.this, "체크상태 = " + isChecked, Toast.LENGTH_SHORT).show();

                }

            }

        });

        if(AppHelper.requestQueue == null){
            //리퀘스트큐 생성 (MainActivit가 메모리에서 만들어질 때 같이 생성이 될것이다.
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }


    }

    public void sendRequest(String url){


        //StringRequest를 만듬 (파라미터구분을 쉽게하기위해 엔터를 쳐서 구분하면 좋다)
        //StringRequest는 요청객체중 하나이며 가장 많이 쓰인다고한다.
        //요청객체는 다음고 같이 보내는방식(GET,POST), URL, 응답성공리스너, 응답실패리스너 이렇게 4개의 파라미터를 전달할 수 있다.(리퀘스트큐에 ㅇㅇ)
        //화면에 결과를 표시할때 핸들러를 사용하지 않아도되는 장점이있다.
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {  //응답을 문자열로 받아서 여기다 넣어달란말임(응답을 성공적으로 받았을 떄 이메소드가 자동으로 호출됨
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener(){ //에러발생시 호출될 리스너 객체
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            //만약 POST 방식에서 전달할 요청 파라미터가 있다면 getParams 메소드에서 반환하는 HashMap 객체에 넣어줍니다.
            //이렇게 만든 요청 객체는 요청 큐에 넣어주는 것만 해주면 됩니다.
            //POST방식으로 안할거면 없어도 되는거같다.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        //아래 add코드처럼 넣어줄때 Volley라고하는게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }

}
