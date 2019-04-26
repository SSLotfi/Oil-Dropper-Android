package com.example.shamel.oildropper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Shamel on 2/9/2018.
 */

public class pump_frag extends Fragment implements View.OnClickListener{
    Button Home ;
    Button pump11,pump22,pump33,offpump;
    String signal;
    communicator comm;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pump_frag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (communicator) getActivity();
        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);

        pump11 = getActivity().findViewById(R.id.pump11);
        pump11.setOnClickListener(this);
        pump22 = getActivity().findViewById(R.id.pump22);
        pump22.setOnClickListener(this);
        pump33 = getActivity().findViewById(R.id.pump33);
        pump33.setOnClickListener(this);
        offpump = getActivity().findViewById(R.id.offpump);
        offpump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                comm.homebutton();
                break;
            case R.id.pump11:
                comm.bluetoothsignal('9');
                Toast.makeText(getActivity(),"Low Speed Selected",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pump22 :
                comm.bluetoothsignal('0');
                Toast.makeText(getActivity(),"Medium Speed Selected",Toast.LENGTH_SHORT).show();
                break;
            case R.id.pump33 :
                comm.bluetoothsignal('a');
                Toast.makeText(getActivity(),"High Speed Selected",Toast.LENGTH_SHORT).show();
                break;
            case R.id.offpump :
                comm.bluetoothsignal('b');
                Toast.makeText(getActivity(),"Pump OFF",Toast.LENGTH_SHORT).show();
                break;
        }


    }
}


