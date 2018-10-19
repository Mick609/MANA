package com.smcnus.mana.mana_3.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.instacart.library.truetime.TrueTime;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class InitTrueTimeAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private String serverNTP;

    public InitTrueTimeAsyncTask(Context context,String serverNTP) {
        mContext = context;
        this.serverNTP = serverNTP;
    }

    protected Void doInBackground(Void... params) {
        try {
            TrueTime.build()
                    .withSharedPreferences(mContext)
                    .withNtpHost(serverNTP)
                    .withLoggingEnabled(false)
                    .withConnectionTimeout(31_428)
                    .initialize();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception when trying to get TrueTime", e);
        }
        return null;
    }

}