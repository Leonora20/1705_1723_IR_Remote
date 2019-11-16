package com.example.projectnewjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Opt_Projector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_projector);

        Button bluetooth = (Button) findViewById(R.id.bluetooth_btn_projector);
        Button ir = (Button) findViewById(R.id.ir_btn_projector);

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), MainActivityBluetooth.class);
                view.getContext().startActivity(myintent);
            }
        });

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), IR_Projector.class);
                view.getContext().startActivity(myintent);
            }
        });
    }
}
