package co.aurasphere.bluepair;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class Projector_StartStopReceiver  extends BroadcastReceiver {

    private String log_tag = "ir";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean type = intent.getBooleanExtra("type",true);

        SharedPreferences.Editor editor;
        SharedPreferences prefs = context.getSharedPreferences("co.aurasphere.bluepair_preferences", Context.MODE_PRIVATE);
        int status = prefs.getInt("status", 0);
        int start = prefs.getInt("start", 1);

        if (type) {
            Log.e(log_tag, "start alarm!");
            AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent_rr = new Intent(context, Projector_RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent_rr, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000,
                    pendingIntent);
            Log.e(log_tag, "set repeating alarm");

        } else {
            Log.e(log_tag,"stop alarm!");
            AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent_rr = new Intent(context, Projector_RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent_rr, 0);
            alarmManager.cancel(pendingIntent);
            Log.e(log_tag, "alarm cancelled");
            if (start==1) {
                Projector_RemoteReceiver.stopProjector(context);
                status = 0;
                editor = prefs.edit();
                editor.putInt("status", status);
                editor.apply();
            }
        }
    }
}
