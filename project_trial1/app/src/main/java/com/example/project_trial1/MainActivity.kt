package com.example.project_trial1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTitle("Wireless Remote App")

        var ac: ImageButton
        var projector: ImageButton
        var speaker: ImageButton
        var tv: ImageButton

        ac = findViewById(R.id.ac)
        projector = findViewById(R.id.projector)
        speaker = findViewById(R.id.speaker)
        tv = findViewById(R.id.tv)

        ac.setOnClickListener{
            val myintent = Intent(this, Opt_AC::class.java)
            startActivity(myintent)
        }

        projector.setOnClickListener{
            val myintent = Intent(this, Opt_Projector::class.java)
            startActivity(myintent)
        }

        speaker.setOnClickListener{
            val myintent = Intent(this, Opt_Speaker::class.java)
            startActivity(myintent)
        }

        tv.setOnClickListener{
            val myintent = Intent(this, Opt_TV::class.java)
            startActivity(myintent)
        }
    }

}
