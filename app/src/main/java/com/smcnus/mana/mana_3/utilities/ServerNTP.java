package com.smcnus.mana.mana_3.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.instacart.library.truetime.TrueTime;

import java.util.Date;

public class ServerNTP {
    private static InitTrueTimeAsyncTask trueTime;
    private Context mContext;
    private static String serverNTP = "0.sg.pool.ntp.org";

    public ServerNTP(Context mContext) {
        this.mContext = mContext;
        initTrueTime(this.mContext);
    }

    public static Date getTrueTime() {
        Date date = TrueTime.isInitialized() ? TrueTime.now() : new Date();
        return date;
    }

    public static void initTrueTime(Context ctx) {
        if (isNetworkConnected(ctx)) {
            if (!TrueTime.isInitialized()) {
                trueTime = new InitTrueTimeAsyncTask(ctx, serverNTP);
                trueTime.execute();
            }
        }
    }

    public String getServerNTP() {
        return serverNTP;
    }

    public void setServerNTP(String serverNTP) {
        this.serverNTP = serverNTP;
    }

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }
}
