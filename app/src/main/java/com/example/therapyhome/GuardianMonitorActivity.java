package com.example.therapyhome;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
public class GuardianMonitorActivity extends AppCompatActivity {
    /**
     * 가은이가 수정한 페이지(블루투스로 센서 데이터 받아오기)
     */
    Button btnEditKeyword, btnEditPhone, btnCheckHealth, btnReadMsg, btnHomeCam;
    private int bpm = 80;
    private int co2 = 95;
    private TextView tvBpmData, tvCo2mData, tvTempertureData, tvWaterData;
    //블루투스 관련
    private final int REQUEST_BLUETOOTH_ENABLE = 100;
    ConnectedTask mConnectedTask = null; //블루투스 연결을 유지시켜주는 AsyncTask
    static BluetoothAdapter mBluetoothAdapter; //블루투스를 연결해주는 Adapter
    private String mConnectedDeviceName = null; //연결된 블루투스 기기 이름
    static boolean isConnectionError = false; //블루투스 연결에러
    private static final String TAG = "BluetoothClient"; //로그를 위한 TAG 변수
    private String BluetoothData; //블루투스장치에서 받아온 데이터
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_monitor);

        Log.d("메인" ," 모니터 크리에이티드" );
        //환자데이터
        tvBpmData = findViewById(R.id.tv_bpmdata); //심박수
        tvCo2mData = findViewById(R.id.tv_co2mdata); //산소포화도
        tvTempertureData = findViewById(R.id.tv_temperturedata); //체온
        tvWaterData = findViewById(R.id.tv_waterdata); //습도
        //홈캠
        btnHomeCam = findViewById(R.id.bt_home_cam);
        btnHomeCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        //블루투스 연결
        Log.d( TAG, "Initalizing Bluetooth adapter...");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("이 기기는 블루투스를 지원하지 않습니다.");
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //블루투스 연결
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        }
        else {
            Log.d(TAG, "Initialisation successful.");
            showPairedDevicesListDialog();
        }
        //하단 네비게이션바
        btnEditKeyword = findViewById(R.id.bt_edit_keyword); //키워드 편집 버튼
        btnEditPhone = findViewById(R.id.bt_edit_phone);//연락처 편집 버튼
        btnCheckHealth = findViewById(R.id.bt_check_health);//건강 상태 버튼
        btnReadMsg = findViewById(R.id.bt_read_msg);//문자 모아보기 버튼
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_read_msg: //문자모아보기
                        Intent intent = new Intent(getApplicationContext(), GuardianMainActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        break;
                    case R.id.bt_edit_keyword: //키워드 편집
                        Intent editKeywordIntent = new Intent(getApplicationContext(), GuardianKeywordEditActivity.class);
                        startActivity(editKeywordIntent);
                        editKeywordIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                    case R.id.bt_edit_phone://연락처 편집
                        Intent editPhomeIntent = new Intent(getApplicationContext(), GuardianPhoneActivity.class);
                        startActivity(editPhomeIntent);
                        editPhomeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                    case R.id.bt_check_health://환자 건강 상태
                        Intent checkHealthIntent = new Intent(getApplicationContext(), GuardianMonitorActivity.class);
                        startActivity(checkHealthIntent);
                        checkHealthIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        break;
                }
            }
        };
        btnReadMsg.setOnClickListener(onClickListener);
        btnCheckHealth.setOnClickListener(onClickListener);
        btnEditKeyword.setOnClickListener(onClickListener);
        btnEditPhone.setOnClickListener(onClickListener);
    } //end "OnCreate()"
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mConnectedTask != null ) { //블루투스 AsyncTask 종료
            mConnectedTask.cancel(true);
        }
    } //end "OnDestroy()"
    /** ========================== start "블루투스 연결 AsyncTask" ==========================*/
    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {
        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;
        ConnectTask(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();
            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d( TAG, "create socket for "+mConnectedDeviceName);
            } catch (IOException e) {
                Log.e( TAG, "socket create failed " + e.getMessage());
            }
            //mConnectionStatus.setText("connecting...");
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean isSucess) {
            if ( isSucess ) {
                connected(mBluetoothSocket);
            }
            else{
                isConnectionError = true;
                Log.d( TAG,  "Unable to connect device");
                showErrorDialog("블루투스 장치를 연결할 수없습니다.");
            }
        }
    } //end "ConnectTask()"
    public void connected( BluetoothSocket socket ) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }
    /** ========================== end "블루투스 연결 AsyncTask" ==========================*/
    /** ======================== start "블루투스 연결 유지 AsyncTask" ========================*/
    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {
        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;
        ConnectedTask(BluetoothSocket socket){
            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                Log.d(TAG, "ConnectedTask: inputstream" + mInputStream);
                mOutputStream = mBluetoothSocket.getOutputStream();
                Log.d(TAG, "ConnectedTask: output" + mOutputStream);
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e );
            }
            Log.d( TAG, "connected to "+mConnectedDeviceName);
            //mConnectionStatus.setText( "connected to "+mConnectedDeviceName);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            byte [] readBuffer = new byte[1024];
            int readBufferPosition = 0;
            while (true) {
                if ( isCancelled() ) return false;
                try {
                    int bytesAvailable = mInputStream.available();
                    if(bytesAvailable > 0) {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mInputStream.read(packetBytes);
                        for(int i=0;i<bytesAvailable;i++) {
                            byte b = packetBytes[i];
                            if(b == '\n')
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");
                                readBufferPosition = 0;
                                Log.d(TAG, "recv message: " + recvMessage);
                                BluetoothData = recvMessage; //블루투스에서 받아온 데이터 전역변수에 넣어줌
                                publishProgress(recvMessage);
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }
        }
        @Override
        protected void onProgressUpdate(String... recvMessage) {
            //mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
            //블루투스 받아온 데이터 화면에 표시하기
            if(BluetoothData .equals("0\r")) {
            }else{
                String[] array = BluetoothData.split(",");
                String humidity = array[0];
                String temperature = array[1];
                String heart = array[2];
                tvWaterData.setText(humidity+" %");
                tvTempertureData.setText(temperature+" ℃");
                tvBpmData.setText(heart+" Bpm");
            }
        }
        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);
            Log.d(TAG, "recv message(isSucess): " + isSucess);
            if (!isSucess) {
                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("블루투스 장치와의 연결이 끊어졌습니다.");
            }
        }
        @Override
        protected void onCancelled(Boolean aBoolean) { //블루투스연결 AsyncTask 종료
            super.onCancelled(aBoolean);
            closeSocket();
        }
        void closeSocket(){ //AsyncTask가 종료되면, 블루투스 소켓 닫기
            try {
                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }
    }
/** ======================== end "블루투스 연결 유지 AsyncTask" ========================*/
    /** ===================== start "블루투스 연결과 관련된 Dialog" =====================*/
    //start "showPairedDevicesListDialog()" : 휴대폰에 연결된 블루투스 디바이스 목록 불러오기
    public void showPairedDevicesListDialog()
    {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);
        if ( pairedDevices.length == 0 ){ //휴대폰에 연결된 블루투스 디바이스가 없다면
            showQuitDialog( "연결되어있는 블루투스장치가 없습니다.\n"
                    +"먼저 블루투스장치를 연결해주세요.");
            return;
        }
        //휴대폰에 연결된 블루투스 디바이스 목록
        String[] items;
        items = new String[pairedDevices.length];
        for (int i=0;i<pairedDevices.length;i++) {
            items[i] = pairedDevices[i].getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("환자의 상태를 확인하시겠습니까?"); // Dialog : 디바이스 선택시 띄울 제목
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //선택한 디바이스 연결해서 AsyncTask 시키기
                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    } //end "showPairedDevicesListDialog()";
    //블루투스 연결 중 에러가 발생하였을 때 나타나는 Dialog
    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 연결중 문제가 발생하였습니다.");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("닫기",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( isConnectionError  ) {
                    isConnectionError = false;
                    finish();
                }
            }
        });
        builder.create().show();
    } //end "showErrorDialog()"
    //블루투스 연결이 안됐을 때 나타나는 Dialog
    public void showQuitDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 연결이 끊어졌습니다.");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("확인",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    } //end "showQuitDialog()"
    /** ===================== end "블루투스 연결과 관련된 Dialog" =====================*/
    //start "onActivityResult()"
    //휴대폰에 연결되어있는 블루투스 목록을 인텐트로 가져옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if (resultCode == RESULT_CANCELED) {
                showQuitDialog("휴대폰의 블루투스를 켜주세요.");
            }
        }
    } //end "onActivityResult()"


    public GuardianMonitorActivity() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("메인" ," 모니터 온스타트" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("메인" ," 모니터 온스탑" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("메인" ," 모니터 퍼스" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("메인" ," 모니터 리줌" );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("메인" ," 모니터 리스타트" );
    }
}