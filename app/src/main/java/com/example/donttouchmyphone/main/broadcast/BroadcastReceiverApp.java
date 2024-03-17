package com.example.donttouchmyphone.main.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.donttouchmyphone.main.MainActivity;
import com.example.donttouchmyphone.sev.DataLocalManager;

public class BroadcastReceiverApp extends BroadcastReceiver {
    static final String ACTION_KEY = "MAIN_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)){
                sendMainActivity(context,true);
            }else {
                sendMainActivity(context,false);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private void sendMainActivity(Context context, boolean value) {
        Intent intent = new Intent(ACTION_KEY);
        intent.putExtra("broadcastTrue",value);
        context.sendBroadcast(intent);
    }

    public boolean isOnline(Context context){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo!=null) && networkInfo.isConnected();
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
}
