package com.example.projectnewjava;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectnewjava.infrared.IRController;
import com.example.projectnewjava.infrared.IRMessageRequest;
import com.example.projectnewjava.infrared.IRMessages;

public class IR_Speaker extends AppCompatActivity implements View.OnTouchListener {

    private IRController _irController;

    private Vibrator _vibrator = null;

    private long _lastBurstTime = 0;     // Microsecconds
    private long _waitTime = 315000;     // Microsecconds
    private long _waitTimeMax = 1000000; // Microsecconds
    private long _waitTimeMin = 300000;  // Microsecconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IRMessages.initialize();

        setContentView(R.layout.activity_ir_speaker);

        _vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        _irController = new IRController(getApplicationContext());
        _irController.startWork();

        Button on_btn = (Button) findViewById(R.id.on_btn);
        Button off_btn = (Button) findViewById(R.id.off_btn);
        Button vup_btn = (Button) findViewById(R.id.vup_btn);
        Button vdown_btn = (Button) findViewById(R.id.vdown_btn);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("InterruptedException","onPause");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.d("InterruptedException","onRestart");
    }

    @Override
    protected void onDestroy()
    {
        Log.d("InterruptedException","onDestroy");
        _irController.stopWork();
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        _waitTime = Math.max(_waitTime,_waitTimeMin);
        _waitTime = Math.min(_waitTime,_waitTimeMax);

        if((System.nanoTime() - _lastBurstTime) > (_waitTime * 1000))
        {
            int ev = event.getActionMasked();

            if(ev == MotionEvent.ACTION_MOVE)
            {
                _lastBurstTime = System.nanoTime();

                    _waitTime = sendIRMessage(((IRMessageRequest) v.getTag()));
                return true;
            }
        }

        return false;
    }

    public long sendIRMessage(IRMessageRequest request)
    {
        _vibrator.vibrate(60);
        return _irController.sendMessage(request);
    }
}
