package co.aurasphere.bluepair;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class TV_MainActivity extends AppCompatActivity {

    private String log_tag = "ir";
    private int status = 0;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Button b2;
    TextView info;
    long interval_in_min=15;
    Handler handler=new Handler();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_ir_activity_main);

        Log.e(log_tag, "TV Main activity launched");

        settings = getApplicationContext().getSharedPreferences("co.aurasphere.bluepair_preferences", 0);
        status = settings.getInt("status", 0);

        ConsumerIrManager irmanager = (ConsumerIrManager) this.getSystemService(Context.CONSUMER_IR_SERVICE);
        boolean has_ir = irmanager.hasIrEmitter();

        if (!has_ir) {
            status = 2;
        }

        b2 = (Button) findViewById(R.id.b2);
        if (status==0) {
            b2.setText("start");
        } else if (status == 1){
            b2.setText("stop");
        } else {
            b2.setText("NO IR SENSOR");
        }

        info = (TextView) findViewById(R.id.info);
    }

    public void onB2Clicked(View v) {

        if (status == 0) {
            Log.e(log_tag,"start button clicked");
            AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TV_RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval_in_min * 60 * 1000,
                    pendingIntent);
            Log.e(log_tag, "set repeating alarm");
            Log.e(log_tag, "interval= " + interval_in_min);


            intent = new Intent(this, TV_StartStopReceiver.class);
            intent.putExtra("type", false);
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            Calendar now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 30);
            long alarmMillis = calendar.getTimeInMillis();
            if (calendar.before(now)) alarmMillis+= 86400000L;
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmMillis, pendingIntent);

            status = 1;
            editor = settings.edit();
            editor.putInt("status", status);
            editor.apply();

            b2.setText("stop");

        } else if (status == 1) {
            Log.e(log_tag,"stop button clicked");
            AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TV_RemoteReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.cancel(pendingIntent);
            Log.e(log_tag, "alarm cancelled");

            status = 0;
            editor = settings.edit();
            editor.putInt("status", status);
            editor.apply();

            b2.setText("start");
        } else if (status == 2) {

        }

    }

}

