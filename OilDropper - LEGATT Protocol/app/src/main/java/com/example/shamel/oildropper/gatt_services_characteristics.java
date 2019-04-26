/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shamel.oildropper;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class gatt_services_characteristics extends Activity implements communicator {
    private final static String TAG = gatt_services_characteristics.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private int[] RGBFrame = {0, 0, 0};
    volatile int flagreadtemp = 0, flagreadGRS = 0;
    private TextView isSerial;
    private TextView mConnectionState;
    private TextView mDataField;
    private SeekBar mRed, mGreen, mBlue;
    private String mDeviceAddress;
    private Handler handler4;
    private Handler handler5;
    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    ///////////////////////////////oildropper//////////variables///////////////////////////////
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
    ArrayList<String> arrayreading = new ArrayList<String>(100);
    String[] arrayreading2 = new String[1000];
    int devicechosen = 0;
    int flagrunning = 0;
    int flagheateron = 0;
    int current_time = 0;
    int newsession = 0;
    int mytimer = 0;
    TextView textStatus, textStatus2;
    int setREADING = 0;
    int startsent = 0;
    int pausesent = 0;
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public final static UUID HM_RX_TX =
            UUID.fromString(SampleGattAttributes.HM_RX_TX);

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void clearUI() {
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_services_characteristics);

        final Intent intent = getIntent();
        String mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        if (((TextView) findViewById(R.id.text3)) != null) {
            ((TextView) findViewById(R.id.text3)).setText(mDeviceAddress);
        }
        mConnectionState = (TextView) findViewById(R.id.bluetoothtext);
        // is serial present?
          //isSerial = (TextView) findViewById(R.id.text1);

           mDataField = (TextView) findViewById(R.id.text2);
        //   mRed = (SeekBar) findViewById(R.id.seekRed);
        // mGreen = (SeekBar) findViewById(R.id.seekGreen);
        //  mBlue = (SeekBar) findViewById(R.id.seekBlue);

        //readSeek(mRed, 0);
        //readSeek(mGreen, 1);
        //readSeek(mBlue, 2);

        //getActionBar().setTitle(mDeviceName);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        handler4 = new Handler();

        final Runnable[] runnable = {new Runnable() {

            @Override
            public void run() {

                if (flagreadtemp >= 4) {
                    makeChange("u");
                    setREADING = 1;
                    flagreadtemp = -1;
                } else if (flagreadGRS >= 11) {
                    makeChange("x");
                    setREADING = 2;
                    flagreadGRS = -1;
                }
                flagreadGRS = flagreadGRS + 1;
                flagreadtemp = flagreadtemp + 1;


                handler4.postDelayed(this, 1000);
            }
        }};
        handler4.postDelayed(runnable[0],3000);

        ////////////////////////////////////////oildropper/////////////////////////////////////////////
        configureNextButton();

        Arrays.fill(arrayreading2, "1");

        //textStatus = (TextView) findViewById(R.id.text2);
        //textStatus2 = (TextView) findViewById(R.id.text3);

        cont = (Button) findViewById(R.id.cont);

        rest = (Button) findViewById(R.id.rest);

        Bundle extras = getIntent().getExtras();

        if (extras != null)

        {
            time = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        PRG = (ProgressBar) findViewById(R.id.progressBar);

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


                            //if (extras != null) {
                                //time = extras.getInt("key");
                                //The key argument here must match that used in the other activity
                            //}
                            if (flagconnect == 0) {
                                Toast.makeText(getBaseContext(), "Please Connect to Bluetooth", Toast.LENGTH_LONG).show();
                                compoundButton.setChecked(false);
                            }

                            if (time == 0 && flag == 0) {
                                Toast.makeText(getApplicationContext(), "Please Choose Session Time !", Toast.LENGTH_LONG).show();
                                compoundButton.setChecked(false);
                            } else if (time != 0 && b && flag == 0 && flagconnect == 1) { //started session
                                flagrunning = 1;
                                flagsessionend = 0;
                                current_time = time;
                                PRG.setMax(time * 1000 * 60 );    // time change
                                progtime += delay;
                                PRG.setProgress(progtime);
                                if(startsent == 0 ){
                                    makeChange("z");
                                    makeChange("y");
                                    startsent = 1;
                                    pausesent = 0;
                                }

                               //try {
                                   // outputStream.write('z');
                                   // outputStream.write('y');
                                //} catch (IOException e) {
                                  //  e.printStackTrace();
                                //}
                                //progtext.setText(String.valueOf(progtime));
                                if (PRG.getProgress() == time * 1000 * 60 ) { // time change
                                    compoundButton.setChecked(false);
                                    h.removeCallbacks(runnable[0]);
                                }

                                runnable[0] = this;

                                h.postDelayed(runnable[0], delay);
                            }
                        }
                    }, delay2);
                } else {
                    h.removeCallbacks(runnable[0]); //stop handler when activity not visible

                    if (PRG.getProgress() != time * 1000 * 60  && PRG.getProgress() != 0) {  // time change
                        Toast.makeText(getApplicationContext(), "Session Suddenly Stopped !", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < 20; i++) {

                            if(pausesent == 1)
                            {
                                makeChange("s");
                                pausesent = 0;
                                startsent = 0;
                            }
                            //try {
                              //  outputStream.write('s');
                            //} catch (IOException e) {                ///bluetooth send character
                              //  e.printStackTrace();
                            //}
                        }
                        flagrunning = 0;
                        //cont.setVisibility(View.VISIBLE);
                        rest.setVisibility(View.VISIBLE);
                        cont.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                compoundButton.setChecked(true);
                                for (int i = 0; i < 20; i++) {
                                    if(startsent == 0)
                                    {
                                        makeChange("z");
                                        startsent = 1;
                                    }

                                    //try {
                                      //  outputStream.write('z');      //bluetooth send character
                                    //} catch (IOException e) {
                                      //  e.printStackTrace();
                                    //}
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
                    } else if ((PRG.getProgress() == time * 1000 * 60 ) && progtime != 0) {   // time change
                        time = 0;
                        PRG.setMax(0);
                        progtime = 0;
                        PRG.setProgress(0);
                        h.removeCallbacks(runnable[0]);
                        //progtext.setText("0");
                        compoundButton.setChecked(false);
                        flag = 1;
                        Toast.makeText(getBaseContext(), "Session Done", Toast.LENGTH_SHORT).show();
                        flagrunning = 0;
                        for (int i = 0; i < 20; i++) {
                            if(pausesent == 0) {
                                makeChange("t");
                                pausesent = 1;
                                startsent = 0;
                            }
                            //try {
                              //  outputStream.write('t');
                            //} catch (IOException e) {         //send bluetooth character
                              //  e.printStackTrace();
                            //}
                        }
                        flagsessionend = 1;
                        cont.setVisibility(View.GONE);
                        rest.setVisibility(View.GONE);
                    }
                }
            }

        });

        ///////////////////////////////////////////////////////////////////////////////////////////////


    }

    /////////////////////////////////////////////oil dropper functions /////////////////////////////////////

    /////////////////////////////////oildropper configurenextbutton //////////////////////////////////////////
    private void configureNextButton() {
        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        if (pumpmenu != null) {
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
        }

        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        if (lightmenu != null) {
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
        }

        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        if (heatmenu != null) {
            heatmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    heat_frag frag2 = new heat_frag();
                    Bundle bundle234 = new Bundle();
                    bundle234.putInt("heat", currentheat);
                    bundle234.putInt("heatflag", flagheateron);
                    bundle234.putInt("systemon", flagrunning);
                    frag2.setArguments(bundle234);
                    Manager = getFragmentManager();
                    FragmentTransaction transaction = Manager.beginTransaction();
                    transaction.add(R.id.main_layout, frag2, "heat fragment");
                    transaction.commit();
                    homedisappear();
                    //startActivity(new Intent(Mainmenu.this,heat.class));
                }
            });
        }

        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        if (pendmenu != null) {
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
        }

        Button timemenu = (Button) findViewById(R.id.timemenu);
        if (timemenu != null) {
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
        }

        Button graphmenu = (Button) findViewById(R.id.graph);
        if (graphmenu != null) {
            graphmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if(counterreading == 1){


                    if (flagsessionend == 1) {

                        graph_frag frag6 = new graph_frag();

                        Bundle bundle23 = new Bundle();

                        bundle23.putStringArray("array2", arrayreading2);
                        bundle23.putInt("readingcount", counterreading - 1);
                        bundle23.putInt("time2", current_time);

                        frag6.setArguments(bundle23);
                        counterreading = 0;
                        Manager = getFragmentManager();
                        FragmentTransaction transaction = Manager.beginTransaction();
                        transaction.add(R.id.main_layout, frag6, "graph fragment");
                        transaction.commit();
                        homedisappear();

                    } else {
                        if (time == 0 && flag == 0) {
                            Toast.makeText(getBaseContext(), "No session exists", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Session must end first", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////oildropper homebutton///////////////////////////////////////

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

    //////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////home appear <-> dissappear /////////////////////////////

    public void homedisappear() {
        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        if (pumpmenu != null) {
            pumpmenu.setVisibility(View.GONE);
        }
        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        if (lightmenu != null) {
            lightmenu.setVisibility(View.GONE);
        }
        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        if (heatmenu != null) {
            heatmenu.setVisibility(View.GONE);
        }
        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        if (pendmenu != null) {
            pendmenu.setVisibility(View.GONE);
        }
        Button musicmenu = (Button) findViewById(R.id.graph);
        if (musicmenu != null) {
            musicmenu.setVisibility(View.GONE);
        }
        Button timemenu = (Button) findViewById(R.id.timemenu);
        if (timemenu != null) {
            timemenu.setVisibility(View.GONE);
        }
        ON_OFF.setVisibility(View.GONE);
        TextView text1 = (TextView) findViewById(R.id.text1);
        if (text1 != null) {
            text1.setVisibility(View.GONE);
        }
        TextView text2 = (TextView) findViewById(R.id.text2);
        if (text2 != null) {
            text2.setVisibility(View.GONE);
        }
        //TextView text3 = (TextView) findViewById(R.id.text3);
        //text3.setVisibility(View.GONE);
        PRG.setVisibility(View.GONE);
        //Button bluetooth = (Button) findViewById(R.id.bluetooth);
        //bluetooth.setVisibility(View.GONE);
        TextView bluetext = (TextView) findViewById(R.id.bluetoothtext);
        if (bluetext != null) {
            bluetext.setVisibility(View.GONE);
        }
        Button cont2 = (Button) findViewById(R.id.cont);
        if (cont2 != null) {
            cont2.setVisibility(View.GONE);
        }
        Button rest2 = (Button) findViewById(R.id.rest);
        if (rest2 != null) {
            rest2.setVisibility(View.GONE);
        }

    }

    public void homeappear() {

        Button pumpmenu = (Button) findViewById(R.id.pumpmenu);
        if (pumpmenu != null) {
            pumpmenu.setVisibility(View.VISIBLE);
        }
        Button lightmenu = (Button) findViewById(R.id.lightmenu);
        if (lightmenu != null) {
            lightmenu.setVisibility(View.VISIBLE);
        }
        Button heatmenu = (Button) findViewById(R.id.heatmenu);
        if (heatmenu != null) {
            heatmenu.setVisibility(View.VISIBLE);
        }
        Button pendmenu = (Button) findViewById(R.id.pendmenu);
        if (pendmenu != null) {
            pendmenu.setVisibility(View.VISIBLE);
        }
        Button musicmenu = (Button) findViewById(R.id.graph);
        if (musicmenu != null) {
            musicmenu.setVisibility(View.VISIBLE);
        }
        Button timemenu = (Button) findViewById(R.id.timemenu);
        if (timemenu != null) {
            timemenu.setVisibility(View.VISIBLE);
        }
        ON_OFF.setVisibility(View.VISIBLE);
        TextView text1 = (TextView) findViewById(R.id.text1);
        if (text1 != null) {
            text1.setVisibility(View.VISIBLE);
        }
        TextView text2 = (TextView) findViewById(R.id.text2);
        if (text2 != null) {
            text2.setVisibility(View.VISIBLE);
        }
        //TextView text3 = (TextView) findViewById(R.id.text3);
        //text3.setVisibility(View.VISIBLE);
        PRG.setVisibility(View.VISIBLE);
        //Button bluetooth = (Button) findViewById(R.id.bluetooth);
        //bluetooth.setVisibility(View.VISIBLE);
        TextView bluetext = (TextView) findViewById(R.id.bluetoothtext);
        if (bluetext != null) {
            bluetext.setVisibility(View.VISIBLE);
        }
        Button cont2 = (Button) findViewById(R.id.cont);
        if (cont2 != null) {
            cont2.setVisibility(View.GONE);
        }
        Button rest2 = (Button) findViewById(R.id.rest);
        if (rest2 != null) {
            rest2.setVisibility(View.GONE);
        }

    }


    public void sendtime(int time1) {
        time = time1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////communicator functions ///////////////////////////////////////////////////
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
                if(resourceId == R.string.connected)
                {
                    mConnectionState.setTextColor(Color.parseColor("#00FF00"));
                    flagconnect = 1;
                }
                else if(resourceId == R.string.disconnected)
                {
                    mConnectionState.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
    }

    private void displayData(final String data) {
        if(!data.equals("z")&&!data.equals("u")&&!data.equals("t")&&!data.equals("1")&&!data.equals("2")&&!data.equals("3")&&!data.equals("4")&&!data.equals("5")&&!data.equals("6")&&!data.equals("7")&&!data.equals("8")&&!data.equals("9")&&!data.equals("a")&&!data.equals("b")&&!data.equals("c")&&!data.equals("d")&&!data.equals("e")&&!data.equals("f")&&!data.equals("g")&&!data.equals("h")&&!data.equals("i")&&!data.equals("s")&&!data.equals("t")&&!data.equals("y")&&!data.equals("x")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data != null) {
                        if (setREADING == 2) {
                            String text88 = "Temperature : " + data + "°C";
                            mDataField.setText(text88);
                        } else if (setREADING == 1) {
                            arrayreading2[counterreading] = data;
                            counterreading++;
                        }
                    }
                }
            });
        }


    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            String LIST_NAME = "NAME";
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            // If the service exists for HM 10 Serial, say so.
            if(SampleGattAttributes.lookup(uuid, unknownServiceString) == "HM 10 Serial") { /*isSerial.setText("Yes, serial :-)");*/ } else {  /*isSerial.setText("No, serial :-(");*/ }
            String LIST_UUID = "UUID";
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /*private void readSeek(SeekBar seekBar,final int pos) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                RGBFrame[pos]=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                //makeChange();
            }
        });
    }*/
    // on change of bars write char 
    private void makeChange(String x) {
        //String str = RGBFrame[0] + "," + RGBFrame[1] + "," + RGBFrame[2] + "\n";
        //Log.d(TAG, "Sending result=" + str);
        final byte[] tx = x.getBytes();   //x was str
        if(mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
        }
    }

    ////////////////////////////oildropper send data////////////////////////////////////////
    public void bluetoothsignal(String signal) {
        makeChange(signal);
    }

}