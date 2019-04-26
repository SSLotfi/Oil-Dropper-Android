package com.example.shamel.oildropper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;


public class Mainmenu extends Activity implements communicator {

    ProgressBar PRG;
    int currentheat = 0;
    int time = 0;
    int progtime = 0;
    TextView progtext, bluetoothText;
    private Handler nHandler = new Handler();
    Button cont, rest, bluetoothConnect;
    ToggleButton ON_OFF;
    Handler h = new Handler();
    Runnable runnable;
    int delay = 100;
    int delay2 = 100;
    int flag = 0;
    FragmentManager Manager;
    int flagconnect = 0;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    int flagsessionend = 0;
    boolean connected = false;
    int counterreading = 0;
    ArrayList<String>  arrayreading = new ArrayList<String>(100);
    String [] arrayreading2 = new String[1000];
    int devicechosen = 0;
    int flagrunning = 0;
    int flagheateron = 0;
    int current_time = 0;
    int newsession = 0;
    int mytimer = 0;





    ///////////////////////////////Bluetooth/////////////////////////////////////
    private String DEVICE_ADDRESS = "20:17:02:15:51:14"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    char command;
    final int handlerState = 0;
    Handler bluetoothIn;
    private StringBuilder recDataString = new StringBuilder();

    TextView textStatus, textStatus2, txtStringLength, sensorView0, sensorView1;
    BluetoothAdapter bluetoothAdapter;
    ThreadConnected myThreadConnected;


    /* //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;


    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;*/

    /////////////////////////////////////end Bluetooth/////////////////////////////////////
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        configureNextButton();

        Arrays.fill(arrayreading2,"1");

        textStatus = (TextView)findViewById(R.id.text2);
        textStatus2 = (TextView)findViewById(R.id.text3);

        cont = (Button) findViewById(R.id.cont);

        rest = (Button) findViewById(R.id.rest);

        Bundle extras = getIntent().getExtras();

        if (extras != null)

        {
            time = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        PRG =

                findViewById(R.id.progressBar);

        ON_OFF = (ToggleButton)

                findViewById(R.id.toggleButton);
        //progtext = findViewById(R.id.progtext);


        ON_OFF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {

                if (b) {
                    flag = 0;
                    counterreading = 0;

                    h.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            Bundle extras = getIntent().getExtras();
                            cont.setVisibility(View.GONE);
                            rest.setVisibility(View.GONE);


                            /*if (extras != null) {
                                time = extras.getInt("key");
                                //The key argument here must match that used in the other activity
                            }*/
                            if(flagconnect == 0){
                                Toast.makeText(getBaseContext(),"Please Connect to Bluetooth",Toast.LENGTH_LONG).show();
                                compoundButton.setChecked(false);
                            }

                            if (time == 0 && flag == 0) {
                                Toast.makeText(getApplicationContext(), "Please Choose Session Time !", Toast.LENGTH_LONG).show();
                                compoundButton.setChecked(false);
                            } else if (time != 0 && b && flag == 0 && flagconnect == 1 ) { //started session
                                flagrunning = 1;
                                flagsessionend = 0;
                                current_time = time;
                                PRG.setMax(time * 1000 * 60);
                                progtime += delay;
                                PRG.setProgress(progtime);
                                try {
                                    outputStream.write('z');
                                    outputStream.write('y');
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //progtext.setText(String.valueOf(progtime));
                                if (PRG.getProgress() == time * 1000 * 60) {
                                    compoundButton.setChecked(false);
                                    h.removeCallbacks(runnable);
                                }

                                runnable = this;

                                h.postDelayed(runnable, delay);
                            }
                        }
                    }, delay2);
                } else {
                    h.removeCallbacks(runnable); //stop handler when activity not visible

                    if (PRG.getProgress() != time * 1000 * 60 && PRG.getProgress() != 0) {
                        Toast.makeText(getApplicationContext(), "Session Suddenly Stopped !", Toast.LENGTH_SHORT).show();
                        for(int i = 0;i<20;i++) {
                            try {
                                outputStream.write('s');
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        flagrunning = 0;
                        //cont.setVisibility(View.VISIBLE);
                        rest.setVisibility(View.VISIBLE);
                        cont.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                compoundButton.setChecked(true);
                                for(int i = 0; i < 20 ; i++)
                                {
                                    try {
                                        outputStream.write('z');
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        rest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //time = 0;
                                PRG.setProgress(0);
                                progtime = 0;
                                //progtext.setText("0");
                            }
                        });
                    } else if ((PRG.getProgress() == time * 1000 * 60) && progtime != 0) {
                        time = 0;
                        PRG.setMax(0);
                        progtime = 0;
                        PRG.setProgress(0);
                        h.removeCallbacks(runnable);
                        //progtext.setText("0");
                        compoundButton.setChecked(false);
                        flag = 1;
                        Toast.makeText(Mainmenu.this, "Session Done", Toast.LENGTH_SHORT).show();
                        flagrunning = 0;
                        for(int i = 0;i<20;i++) {
                            try {
                                outputStream.write('t');
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        flagsessionend = 1;
                        cont.setVisibility(View.GONE);
                        rest.setVisibility(View.GONE);
                    }
                }
            }

        });
    }

    ;

    private void configureNextButton() {
        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        pumpmenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pump_frag frag1 = new pump_frag();
                Manager = getFragmentManager();
                FragmentTransaction transaction = Manager.beginTransaction();
                transaction.add(R.id.main_layout, frag1, "pump fragment");
                transaction.commit();
                homedisappear();

                //startActivity(new Intent(Mainmenu.this, testy.class));
            }

        });

        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        lightmenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                light_frag frag5 = new light_frag();
                Manager = getFragmentManager();
                FragmentTransaction transaction = Manager.beginTransaction();
                transaction.add(R.id.main_layout, frag5, "light fragment");
                transaction.commit();
                homedisappear();
            }
        });

        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        heatmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heat_frag frag2 = new heat_frag();
                Bundle bundle234 = new Bundle();
                bundle234.putInt("heat",currentheat);
                bundle234.putInt("heatflag",flagheateron);
                bundle234.putInt("systemon",flagrunning);
                frag2.setArguments(bundle234);
                Manager = getFragmentManager();
                FragmentTransaction transaction = Manager.beginTransaction();
                transaction.add(R.id.main_layout, frag2, "heat fragment");
                transaction.commit();
                homedisappear();
                //startActivity(new Intent(Mainmenu.this,heat.class));
            }
        });

        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        pendmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pend_frag frag3 = new pend_frag();
                Manager = getFragmentManager();
                FragmentTransaction transaction = Manager.beginTransaction();
                transaction.add(R.id.main_layout, frag3, "pend fragment");
                transaction.commit();
                homedisappear();
            }
        });

        Button timemenu = (Button) findViewById(R.id.timemenu);
        timemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_frag frag5 = new time_frag();
                Manager = getFragmentManager();
                FragmentTransaction transaction = Manager.beginTransaction();
                transaction.add(R.id.main_layout, frag5, "time fragment");
                transaction.commit();
                homedisappear();
            }
        });

        Button graphmenu = (Button) findViewById(R.id.graph);
        graphmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if(counterreading == 1){


                    if(flagsessionend == 1) {

                        graph_frag frag6 = new graph_frag();

                        Bundle bundle23 = new Bundle();

                        bundle23.putStringArray("array2", arrayreading2);
                        bundle23.putInt("readingcount",counterreading-1);
                        bundle23.putInt("time2",current_time);

                        frag6.setArguments(bundle23);
                        counterreading = 0;
                        Manager = getFragmentManager();
                        FragmentTransaction transaction = Manager.beginTransaction();
                        transaction.add(R.id.main_layout, frag6, "graph fragment");
                        transaction.commit();
                        homedisappear();

                    }
                    else{
                        if (time == 0 && flag == 0){
                            Toast.makeText(getBaseContext(), "No session exists", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Session must end first", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });




        bluetoothConnect = findViewById(R.id.bluetooth);
        bluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(),"pop",Toast.LENGTH_SHORT).show();
                if(flagconnect == 0) {
                    if(devicechosen == 0) {
                        startActivity(new Intent(Mainmenu.this, DeviceListActivity.class));
                    }
                    else{
                        //Toast.makeText(getBaseContext(),DEVICE_ADDRESS,Toast.LENGTH_SHORT).show();
                        if (BTinit()) {
                            //Toast.makeText(getBaseContext(),"connecting please wait",Toast.LENGTH_LONG).show();
                            BTconnect();

                        }
                        final TextView bluetext = findViewById(R.id.bluetoothtext);
                        if (connected || flagconnect == 1) {
                            if (bluetext != null) {
                                bluetext.setText("Connected");
                                bluetext.setTextColor(Color.parseColor("#00DD00"));
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"Device Already Connected to Bluetooth",Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void homebutton() {
        pump_frag f1 = (pump_frag) Manager.findFragmentByTag("pump fragment");
        FragmentTransaction transaction = Manager.beginTransaction();
        if (f1 != null) {
            transaction.remove(f1);
            transaction.commit();
        }
        heat_frag f2 = (heat_frag) Manager.findFragmentByTag("heat fragment");
        FragmentTransaction transaction1 = Manager.beginTransaction();
        if (f2 != null) {
            transaction1.remove(f2);
            transaction1.commit();
        }
        pend_frag f3 = (pend_frag) Manager.findFragmentByTag("pend fragment");
        FragmentTransaction transaction2 = Manager.beginTransaction();
        if (f3 != null) {
            transaction2.remove(f3);
            transaction2.commit();
        }
        light_frag f5 = (light_frag) Manager.findFragmentByTag("light fragment");
        FragmentTransaction transaction4 = Manager.beginTransaction();
        if (f5 != null) {
            transaction4.remove(f5);
            transaction4.commit();
        }
        time_frag f6 = (time_frag) Manager.findFragmentByTag("time fragment");
        FragmentTransaction transaction5 = Manager.beginTransaction();
        if (f6 != null) {
            transaction5.remove(f6);
            transaction5.commit();
        }
        graph_frag f888 = (graph_frag) Manager.findFragmentByTag("graph fragment");
        FragmentTransaction transaction666 = Manager.beginTransaction();
        if (f888 != null) {
            transaction666.remove(f888);
            transaction666.commit();
        }
        homeappear();


    }


    @Override
    public void bluetoothsignal(char signal) {
        command = signal;

        if(flagconnect == 1) {
            for(int i = 0 ;i < 20 ; i++) {
                try {
                    outputStream.write(command);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Please Connect to Bluetooth", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(getBaseContext(),"Please connect to Bluetooth",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void homedisappear() {
        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        pumpmenu.setVisibility(View.GONE);
        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        lightmenu.setVisibility(View.GONE);
        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        heatmenu.setVisibility(View.GONE);
        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        pendmenu.setVisibility(View.GONE);
        Button musicmenu = (Button) findViewById(R.id.graph);
        musicmenu.setVisibility(View.GONE);
        Button timemenu = (Button) findViewById(R.id.timemenu);
        timemenu.setVisibility(View.GONE);
        ON_OFF.setVisibility(View.GONE);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setVisibility(View.GONE);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setVisibility(View.GONE);
        TextView text3 = (TextView) findViewById(R.id.text3);
        text3.setVisibility(View.GONE);
        PRG.setVisibility(View.GONE);
        Button bluetooth = (Button) findViewById(R.id.bluetooth);
        bluetooth.setVisibility(View.GONE);
        TextView bluetext = (TextView) findViewById(R.id.bluetoothtext);
        bluetext.setVisibility(View.GONE);
        Button cont2 = (Button) findViewById(R.id.cont);
        cont2.setVisibility(View.GONE);
        Button rest2 = (Button) findViewById(R.id.rest);
        rest2.setVisibility(View.GONE);

    }

    public void homeappear() {

        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        pumpmenu.setVisibility(View.VISIBLE);
        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        lightmenu.setVisibility(View.VISIBLE);
        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        heatmenu.setVisibility(View.VISIBLE);
        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        pendmenu.setVisibility(View.VISIBLE);
        Button musicmenu = (Button) findViewById(R.id.graph);
        musicmenu.setVisibility(View.VISIBLE);
        Button timemenu = (Button) findViewById(R.id.timemenu);
        timemenu.setVisibility(View.VISIBLE);
        ON_OFF.setVisibility(View.VISIBLE);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setVisibility(View.VISIBLE);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setVisibility(View.VISIBLE);
        TextView text3 = (TextView) findViewById(R.id.text3);
        text3.setVisibility(View.VISIBLE);
        PRG.setVisibility(View.VISIBLE);
        Button bluetooth = (Button) findViewById(R.id.bluetooth);
        bluetooth.setVisibility(View.VISIBLE);
        TextView bluetext = (TextView) findViewById(R.id.bluetoothtext);
        bluetext.setVisibility(View.VISIBLE);
        Button cont2 = (Button) findViewById(R.id.cont);
        cont2.setVisibility(View.GONE);
        Button rest2 = (Button) findViewById(R.id.rest);
        rest2.setVisibility(View.GONE);

    }

    @Override
    public void sendtime(int time1) {
        time = time1;
    }

    /*///////////////////////////////////bluetooth////////////////////////////////////*/


    //create new class for connect thread
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;


        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;

            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;

        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            Arrays.fill(arrayreading2,"0");

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    final String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = strReceived.substring(0, 1);
                    final String newReceived = strReceived.substring(1);


                    runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if(msgReceived.equals("#") && newReceived.length() >= 2) {
                                            int index2 = newReceived.indexOf("~");
                                            String texty2="";
                                            if(index2 >= 0) {
                                                texty2 = newReceived.substring(0, index2);
                                            }
                                            String texty = "Tempearture : " + texty2 + " °C";
                                            textStatus.setText(texty);

                                        }
                                        if(msgReceived.equals("@") && newReceived.length() >= 2) {
                                            int index3 = newReceived.indexOf("~");
                                            String texty3 = "";
                                            if(index3 >= 0 ) {
                                                texty3 = newReceived.substring(0, index3);
                                            }
                                            String texty = "FlowRate : " + texty3 + " ml/min";
                                            textStatus2.setText(texty);
                                        }
                                        if(msgReceived.equals("*") && newReceived.length() >= 2) {
                                            mytimer++;
                                            int index4 = newReceived.indexOf("~");
                                            String texty4 ="";
                                            if(index4 >= 0){
                                                texty4 = newReceived.substring(0,index4);
                                            }
                                            if(counterreading < 1000 && flagrunning == 1 && texty4!=null) {
                                                arrayreading2[counterreading] = texty4;

                                                if(arrayreading2[counterreading]!="0" && mytimer == 20)
                                                {
                                                    counterreading++;
                                                    mytimer=0;

                                                    Toast.makeText(getBaseContext(),Integer.toString(counterreading)+arrayreading2[counterreading-1],Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                    }

                                });


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    /*final String msgConnectionLost = "Connection lost\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            textStatus.setText(msgConnectionLost);
                        }});*/
                }
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] writeBuf = (byte[]) msg.obj;
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;

            switch(msg.what) {
                case 1:
                    String writeMessage = new String(writeBuf);
                    writeMessage = writeMessage.substring(begin, end);
                    break;
            }
        }
    };

    public boolean BTinit()
    {
        boolean found = false;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    //Toast.makeText(getBaseContext(),"found",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
            flagconnect = 1;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
            Toast.makeText(getBaseContext(),"Not connected Please Try again",Toast.LENGTH_LONG).show();
        }

        if(connected)
        {
            flagconnect = 1;
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            startThreadConnected(socket);
        }
        return connected;
    }
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }
    public void onPause() {
        super.onPause();
        if (socket != null) {
            try {
                //Don't leave Bluetooth sockets open when leaving activity
                socket.close();
            } catch (IOException e2) {
                //Toast.makeText(getBaseContext(), "error 3", Toast.LENGTH_LONG).show();
                //insert code to deal with thi11s
            }
        }
        h.removeCallbacks(runnable); //stop handler when activity not visible
    }
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        //Get the MAC address from the DeviceListActivty via EXTRA
        DEVICE_ADDRESS = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        devicechosen = intent.getIntExtra("key2",0);
        if(devicechosen == 1){
            bluetoothConnect.setText("Click to Connect to Bluetooth");
        }
        if (PRG.getProgress() != time * 1000 * 60
                && PRG.getProgress() != 0) {
            Toast.makeText(getApplicationContext(), "Session Suddenly Stopped !", Toast.LENGTH_LONG).show();
            //cont.setVisibility(View.VISIBLE);
            rest.setVisibility(View.VISIBLE);
            cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ON_OFF.setChecked(true);
                }
            });
            rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //time = 0;
                    PRG.setProgress(0);
                    progtime = 0;
                    //progtext.setText("0");
                }
            });
        } else if ((PRG.getProgress() == time * 1000 * 60) && progtime != 0) {
            time = 0;
            PRG.setMax(0);
            progtime = 0;
            PRG.setProgress(0);
            h.removeCallbacks(runnable);
            //progtext.setText("0");
            ON_OFF.setChecked(false);
            flag = 1;
            Toast.makeText(Mainmenu.this, "Session Done", Toast.LENGTH_LONG).show();
            cont.setVisibility(View.GONE);
            rest.setVisibility(View.GONE);

        }
    }
    public void sessionend(){
        flagsessionend = 0;
    }
    public void heat(int heatertemp,int flagheater){
        currentheat = heatertemp;
        flagheateron = flagheater;
    }

    public void new_session() {
        newsession = 1;
    }

    public void old_readings(String x[],int timy , int readcount){
        arrayreading2 = x;
        time = timy;
        counterreading = readcount;
    }

}





