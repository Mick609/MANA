package com.smcnus.mana.mana_3.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.smcnus.mana.mana_3.R;
import com.smcnus.mana.mana_3.utilities.ServerNTP;

public class MainActivity extends AppCompatActivity {

    final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 001;
    private String TAG = "mana_3";
    private ServerNTP mServerNTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        View view = getSupportActionBar().getCustomView();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String[] permissionList = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WAKE_LOCK};

        requestPermissions(permissionList, PERMISSIONS_WRITE_EXTERNAL_STORAGE);

        mServerNTP = new ServerNTP(this);
        Log.d(TAG, "The true time: " + mServerNTP.getTrueTime().toString() + " from: " + mServerNTP.getServerNTP());
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_toCamera:
                intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}