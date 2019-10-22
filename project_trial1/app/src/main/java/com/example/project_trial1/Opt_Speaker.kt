package com.example.project_trial1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Opt_Speaker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opt_speaker)

        var bluetooth: ImageButton

        bluetooth = findViewById(R.id.bluetooth_btn_speaker)

        bluetooth.setOnClickListener{
            val myintent = Intent(this, MainActivityBluetooth::class.java)
            startActivity(myintent)
        }

    }
}