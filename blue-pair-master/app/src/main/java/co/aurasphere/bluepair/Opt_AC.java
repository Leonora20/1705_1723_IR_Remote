package co.aurasphere.bluepair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Opt_AC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_ac);

        this.setTitle("IR/Bluetooth");

        Button bluetooth = (Button) findViewById(R.id.bluetooth_btn_ac);

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(myintent);
            }
        });
    }
}
