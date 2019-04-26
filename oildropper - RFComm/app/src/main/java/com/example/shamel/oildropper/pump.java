package com.example.shamel.oildropper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.Button;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import java.io.OutputStream;
import java.util.UUID;

public class pump extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump);
        configureHomeButton();

        /*pump1 = (Button) findViewById(R.id.pump1);
        pump2 = (Button) findViewById(R.id.pump2);
        pump3 = (Button) findViewById(R.id.pump3);
        offpump = (Button) findViewById(R.id.offpump);



        pump1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "31";
                try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        pump2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "32";
                try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        pump3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "33";
                try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        offpump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "39";
                try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }*/
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

