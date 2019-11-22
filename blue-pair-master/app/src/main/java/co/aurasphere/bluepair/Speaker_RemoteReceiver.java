package co.aurasphere.bluepair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class Speaker_RemoteReceiver extends BroadcastReceiver {

    private String log_tag = "ir";
    private int status = 0;
    private int start = 1;
    ConsumerIrManager irmanager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("co.aurasphere.bluepair_preferences", Context.MODE_PRIVATE);
        status = prefs.getInt("status", 0);
        start = prefs.getInt("start", 1);
        Log.e(log_tag, "broadcast received!" +Integer.toString(start));


        if (start == 0) {
            start = 1;
            startSpeaker(context);
        } else if (start == 1) {
            start = 0;
            stopSpeaker(context);
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
    public static void startSpeaker(Context c) {
        int start_frame[] = {346,173,29,60,28,17,28,16,28,16,29,16,29,16,28,60,28,16,28,16,29,16,28,16,28,60,28,16,28,17,28,16,28,16,28,17,28,16,28,16,29,16,29,16,28,16,29,16,28,16,28,17,28,16,28,16,28,17,28,60,29,16,28,60,29,16,28,16,28,60,28,16,29,5000};
        transmitIR(start_frame, c);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void stopSpeaker(Context c) {
        int stop_frame[] = {346,173,28,60,28,16,28,16,29,60,28,60,28,16,29,60,28,16,28,17,28,16,28,17,28,60,29,16,28,16,28,16,29,16,28,16,28,16,29,16,28,16,28,17,28,16,28,16,29,16,28,16,28,17,28,16,28,16,28,60,28,16,28,60,28,17,28,16,28,60,28,16,28,5000};
        transmitIR(stop_frame, c);
    }

}
