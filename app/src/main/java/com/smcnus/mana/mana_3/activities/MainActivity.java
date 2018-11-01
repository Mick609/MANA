package com.smcnus.mana.mana_3.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smcnus.mana.mana_3.R;
import com.smcnus.mana.mana_3.TCPClient.TcpClient;
import com.smcnus.mana.mana_3.domains.Ping;
import com.smcnus.mana.mana_3.utilities.ServerNTP;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 001;
    private final String TAG = "mana_3";
    private final long ping_interval = 5000;

    private Context mContext;
    private TextView console;
    private TextView connection_text;

    private ServerNTP mServerNTP;
    private TcpClient mTcpClient;
    private Ping last_ping = new Ping(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String[] permissionList = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WAKE_LOCK};

        requestPermissions(permissionList, PERMISSIONS_WRITE_EXTERNAL_STORAGE);

        mServerNTP = new ServerNTP(this);
        Log.d(TAG, "The true time: " + mServerNTP.getTrueTime().toString() + " from: " + mServerNTP.getServerNTP());

        console = (TextView) findViewById(R.id.console);
        connection_text = (TextView) findViewById(R.id.connection);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
//            case R.id.button_toCamera:
//                intent = new Intent(this, CameraActivity.class);
//                startActivity(intent);
//                break;
            case R.id.button_connect:
                new ConnectTask().execute("");
                break;
            case R.id.button_start:
                if (mTcpClient != null) {
                    String participant_id = ((EditText) findViewById(R.id.text_participant_id)).getText().toString();
                    String session = ((EditText) findViewById(R.id.text_session)).getText().toString();
                    String bpm = ((EditText) findViewById(R.id.text_bpm)).getText().toString();
                    mTcpClient.sendMessage("start," + participant_id + "," + session + "," + bpm);
                } else {
                    Log.d(TAG, "send message fail");
                }
                break;
            case R.id.button_stop:
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("stop");
                } else {
                    Log.d(TAG, "send message fail");
                }
                break;
            default:
                break;
        }
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, mContext);

            String ip = ((EditText) findViewById(R.id.text_address)).getText().toString();
            int port = Integer.parseInt(((EditText) findViewById(R.id.text_port)).getText().toString());
            mTcpClient.setServerIp(ip);
            mTcpClient.setServerPort(port);

            startRepeatingTask();

            mTcpClient.run();


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d(TAG, "response " + values[0]);

            //process server response here....
            String[] msg = values[0].split(",");
            if (msg[0].equalsIgnoreCase("ping")) {
                if (msg[1].equalsIgnoreCase(last_ping.getTimestamp() + "")) {
                    last_ping.setSentBack(true);
                    connection_text.setText("ONLINE");
                    connection_text.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                }
            } else {
                String console_text = console.getText().toString();
                console.setText(mServerNTP.getTrueTime().toString() + ": (Received)" + values[0] + "\n" + console_text);
            }

//            Intent intent;
//            intent = new Intent(MainActivity.this
//                    , CameraActivity.class);
//            startActivity(intent);

        }

        Handler mHandler = new Handler();

        Runnable mHandlerTask = new Runnable() {
            @Override
            public void run() {
                long current_time = mServerNTP.getTrueTime().getTime();
                if (current_time - last_ping.getTimestamp() > ping_interval) {
                    if (!last_ping.isSentBack()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                connection_text.setText("OFFLINE");
                                connection_text.setTextColor(ContextCompat.getColor(mContext, R.color.alert_red));
                            }
                        });
                    }
                    String message = "ping," + current_time;
                    mTcpClient.sendMessage(message);
                    last_ping = new Ping(current_time);
                }
                mHandler.postDelayed(mHandlerTask, ping_interval);
            }
        };

        void startRepeatingTask() {
            mHandlerTask.run();
        }

        void stopRepeatingTask() {
            mHandler.removeCallbacks(mHandlerTask);
        }
    }
}