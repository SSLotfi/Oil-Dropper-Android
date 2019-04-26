package com.example.shamel.oildropper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class light extends AppCompatActivity {

    Button light1,light2,light3,offlight;
    String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        configureHomeButton();

        light1 = (Button) findViewById(R.id.light11);
        light2 = (Button) findViewById(R.id.light22);
        light3 = (Button) findViewById(R.id.light33);
        offlight = (Button) findViewById(R.id.offlight);

        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        offlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void configureHomeButton(){
        Button Home=(Button) findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
