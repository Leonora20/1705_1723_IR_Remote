package co.aurasphere.bluepair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        this.setTitle("");

        ImageButton ac = (ImageButton) findViewById(R.id.ac);
        ImageButton projector = (ImageButton) findViewById(R.id.projector);
        ImageButton speaker = (ImageButton) findViewById(R.id.speaker);
        ImageButton tv = (ImageButton) findViewById(R.id.tv);

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), Opt_AC.class);
                view.getContext().startActivity(myintent);
            }
        });

        projector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), Opt_Projector.class);
                view.getContext().startActivity(myintent);
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), Opt_Speaker.class);
                view.getContext().startActivity(myintent);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(view.getContext(), Opt_TV.class);
                view.getContext().startActivity(myintent);
            }
        });


    }
}
