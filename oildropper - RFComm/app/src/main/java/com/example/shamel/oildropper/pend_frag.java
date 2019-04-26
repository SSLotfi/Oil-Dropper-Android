package com.example.shamel.oildropper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Shamel on 2/13/2018.
 */

public class pend_frag extends Fragment implements View.OnClickListener {
    Button Home ;
    Button pend11,pend22,pend33,offpend;
    String signal;
    communicator comm3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pend_frag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm3 = (communicator) getActivity();
        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);

        pend11 = getActivity().findViewById(R.id.pend11);
        pend11.setOnClickListener(this);
        pend22 = getActivity().findViewById(R.id.pend22);
        pend22.setOnClickListener(this);
        pend33 = getActivity().findViewById(R.id.pend33);
        pend33.setOnClickListener(this);
        offpend = getActivity().findViewById(R.id.offpend);
        offpend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home: comm3.homebutton(); break;
            case R.id.pend11 :
                comm3.bluetoothsignal('5');
                Toast.makeText(getActivity(), "Pendulum Mode set to Mode 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pend22 :
                comm3.bluetoothsignal('6');
                Toast.makeText(getActivity(), "Pendulum Mode set to Mode 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pend33 :
                comm3.bluetoothsignal('7');
                Toast.makeText(getActivity(), "Pendulum Mode set to Mode 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.offpend :
                comm3.bluetoothsignal('8');
                Toast.makeText(getActivity(), "Pendulum OFF", Toast.LENGTH_SHORT).show();
                break;
        }


    }
}
