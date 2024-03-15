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

public class BroadcastReceiverApp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)){
                Toast.makeText(context, "Có wifi", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Không Có wifi", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private void sendMainActivity(Context context) {
        Intent intent = new Intent("MAIN_ACTION");
        intent.putExtra("broadcastTrue",true);
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
