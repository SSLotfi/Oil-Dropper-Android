package com.example.shamel.oildropper;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shamel on 2/11/2018.
 */

public class heat_frag extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    String signal;
    Spinner templist;
    int flagheater = 0;
    int flagrunning = 0;
    Button Home ;
    Button offheat,onheat;
    communicator comm2;
    TextView heatertext;
    int heatertemp = 0;
    ArrayAdapter<CharSequence> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.heat_frag,container,false);


    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("heat")) {
            Bundle args2 = getArguments();
            heatertemp = args2.getInt("heat");
            flagheater = args2.getInt("heatflag");
            flagrunning = args2.getInt("systemon");
        }
        comm2 = (communicator) getActivity();
        templist = getActivity().findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.temp_values,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        templist.setAdapter(adapter);
        templist.setSelection(heatertemp);
        offheat = getActivity().findViewById(R.id.offheat);
        offheat.setOnClickListener(this);
        onheat = getActivity().findViewById(R.id.onheat);
        onheat.setOnClickListener(this);
        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);
        templist.setOnItemSelectedListener(this);

        if(flagheater == 1){
            heatertext = getActivity().findViewById(R.id.heatertext);
            heatertext.setText("Heater ON");
            heatertext.setTextColor(Color.parseColor("#00DD00"));
        }
        if(flagrunning == 0){
            heatertext = getActivity().findViewById(R.id.heatertext);
            heatertext.setText("Heater OFF");
            heatertext.setTextColor(Color.parseColor("#DD0000"));
        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        heatertext = getActivity().findViewById(R.id.heatertext);
        switch (view.getId()) {
            case R.id.home: comm2.homebutton(); break;
            case R.id.offheat:
                signal = "i";
                flagheater = 0;
                comm2.bluetoothsignal(signal);
                heatertext.setText("Heater OFF");
                heatertext.setTextColor(Color.parseColor("#DD0000"));
                Toast.makeText(getActivity(), "Heater OFF", Toast.LENGTH_SHORT).show();
                break;
            case R.id.onheat:
                signal = "p";
                flagheater = 1;
                comm2.bluetoothsignal(signal);
                heatertext.setText("Heater ON");
                heatertext.setTextColor(Color.parseColor("#00DD00"));
                Toast.makeText(getActivity(), "Heater ON", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        if(flagheater == 1) {
            Toast.makeText(this.getActivity(), adapterView.getItemAtPosition(position) + " is selected", Toast.LENGTH_SHORT).show();//dont send signal until heater is on
            if (position == 0) {
                heatertemp = position;
                comm2.heat(0,flagheater);
                comm2.bluetoothsignal("c");
            }
            if (position == 1) {
                heatertemp = position;
                comm2.heat(1,flagheater);
                comm2.bluetoothsignal("d");
            }
            if (position == 2) {
                heatertemp = position;
                comm2.heat(2,flagheater);
                comm2.bluetoothsignal("e");
            }
            if (position == 3) {
                heatertemp = position;
                comm2.heat(3,flagheater);
                comm2.bluetoothsignal("f");
            }
            if (position == 4) {
                heatertemp = position;
                comm2.heat(4,flagheater);
                comm2.bluetoothsignal("g");
            }
            if(position == 5) {
                heatertemp = position;
                comm2.heat(5,flagheater);
                comm2.bluetoothsignal("h");
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
