package com.example.shamel.oildropper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;
import java.util.UUID;

public class time extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "20:15:11:23:93:85"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    Button time1,time2,time3;
    String command;
    //int time_time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        time1 = (Button) findViewById(R.id.time11);
        time2 = (Button) findViewById(R.id.time22);
        time3 = (Button) findViewById(R.id.time3);

        configureHomeButton();

        final int[] time_time = new int[1];
        time_time[0] = 0;

        /*Intent i = new Intent(getApplicationContext(), Mainmenu.class);

        i.putExtra("key",time_time);
        startActivity(i);*/

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "1";
                time_time[0] = 30;
                Intent i = new Intent(getApplicationContext(), Mainmenu.class);
                i.putExtra("key", time_time[0]);
                startActivity(i);
                /*try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }*/
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "1";
                time_time[0] = 45;
                Intent i = new Intent(getApplicationContext(), Mainmenu.class);
                i.putExtra("key", time_time[0]);
                startActivity(i);
                /*try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }*/
            }
        });

        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "1";
                time_time[0] = 60;
                Intent i = new Intent(getApplicationContext(), Mainmenu.class);
                i.putExtra("key", time_time[0]);
                startActivity(i);
                /*try
                {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }*/
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
