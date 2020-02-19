package com.example.therapyhome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorMonitorActivity extends AppCompatActivity {


    private int bpm = 80;
    private int co2 = 95;
    private TextView tvBpmData;
    private TextView tvCo2mData;
    private TextView tvTempertureData;
    private TextView tvWaterData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_monitor);

        tvBpmData = findViewById(R.id.tv_bpmdata);
        tvCo2mData = findViewById(R.id.tv_co2mdata);
        tvTempertureData = findViewById(R.id.tv_temperturedata);
        tvWaterData = findViewById(R.id.tv_waterdata);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
            updateCo2Thread();//호흡
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
            }
        });
        myThread.start();
    }

    private void updateThread() {
        bpm++;
        tvBpmData.setText(String.valueOf(bpm));



    }

    private void updateCo2Thread(){
        co2 ++;
        tvCo2mData.setText(String.valueOf(co2));
    }
}
