package com.example.acremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class RemoteReceiver extends BroadcastReceiver {

    private String log_tag = "ir";
    private int status = 0;
    private int start = 1;
    ConsumerIrManager irmanager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("com.example.acremote_preferences", Context.MODE_PRIVATE);
        status = prefs.getInt("status", 0);
        start = prefs.getInt("start", 1);
        Log.e(log_tag, "broadcast received!" +Integer.toString(start));


        if (start == 0) {
            start = 1;
            startAC(context);
        } else if (start == 1) {
            start = 0;
            stopAC(context);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("start", start);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void transmitIR (int frame[], Context c) {
        ConsumerIrManager irmanager = (ConsumerIrManager) c.getSystemService(Context.CONSUMER_IR_SERVICE);

        int f = 38000;
        int i = frame.length;
        for (int k = 0; k < i; k++) {
            frame[k] = (int) ((1000000.0f * ((float) frame[k])) / ((float) f));
        }
        irmanager.transmit(f,frame);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void startAC(Context c) {
        int start_frame[] = {346,173,29,60,28,17,28,16,28,16,29,16,29,16,28,60,28,16,28,16,29,16,28,16,28,60,28,16,28,17,28,16,28,16,28,17,28,16,28,16,29,16,29,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,29,5000};
        transmitIR(start_frame, c);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void stopAC(Context c) {
        int stop_frame[] = {346,173,28,60,28,16,28,16,29,60,28,60,28,16,29,60,28,16,28,17,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,60,28,16,28,60,28,17,28,16,28,60,28,16,28,5000};
        transmitIR(stop_frame, c);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void onCoolClicked(Context c){
            int cool_frame[] = {38000,346,173,28,60,29,16,28,16,28,60,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,60,29,16,28,16,29,16,29,16,28,16,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,29,16,29,16,28,16,29,60,28,16,29,60,28,16,29,16,28,60,29,16,28,5000};
            transmitIR(cool_frame, c);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void onFanClicked(Context c){
        int fan_frame[] = {38000,346,173,30,60,30,60,29,16,30,60,29,60,30,16,29,60,29,16,29,16,29,16,29,16,29,60,29,16,30,16,29,16,29,16,30,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,29,16,30,16,29,16,30,16,29,60,30,16,29,60,30,16,29,16,29,60,29,16,30,5000};
        transmitIR(fan_frame, c);
    }
}