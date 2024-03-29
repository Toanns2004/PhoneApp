package com.example.donttouchmyphone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.example.donttouchmyphone.views.activities.main.MainActivity;

public class CheckInternet extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            if (isNetworkAvailable(context)){
//                sendMainActivity(context,true);
//                Toast.makeText(context, "có wiffi", Toast.LENGTH_SHORT).show();
                DataLocalManager.setInternet(true);
            }else {
                sendMainActivity(context,false);
//                Toast.makeText(context, "Không có wiffi", Toast.LENGTH_SHORT).show();
                DataLocalManager.setInternet(false);
            }
        }
    }
    private void sendMainActivity(Context context, boolean value) {
        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.putExtra("WIFI", value);
        context.sendBroadcast(newIntent);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            // For devices below Android M
            return isNetworkAvailableLegacy(connectivityManager);
        }
    }

    private boolean isNetworkAvailableLegacy(ConnectivityManager connectivityManager) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
