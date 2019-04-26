package com.example.shamel.oildropper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class pend extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "20:15:11:23:93:85"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    Button pend1,pend2,pend3,offpend;
    String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pend);

        pend1 = (Button) findViewById(R.id.pend11);
        pend2 = (Button) findViewById(R.id.pend22);
        pend3 = (Button) findViewById(R.id.pend33);
        offpend = (Button) findViewById(R.id.offpend);

        configureHomeButton();

        pend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "11";
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

        pend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "12";
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

        pend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "13";
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

        offpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "19";
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
