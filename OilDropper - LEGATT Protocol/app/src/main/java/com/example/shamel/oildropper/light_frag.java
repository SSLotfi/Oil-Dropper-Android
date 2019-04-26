package com.example.shamel.oildropper;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Shamel on 2/9/2018.
 */

public class light_frag extends Fragment implements View.OnClickListener{
    Button Home ;
    Button light11,light22,light33,offlight;
    String signal;
    communicator comm5;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.light_frag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm5 = (communicator) getActivity();
        Home = getActivity().findViewById(R.id.home);
        if (Home != null) {
            Home.setOnClickListener(this);
        }
        light11 = getActivity().findViewById(R.id.light11);
        if (light11 != null) {
            light11.setOnClickListener(this);
        }
        light22 = getActivity().findViewById(R.id.light22);
        if (light22 != null) {
            light22.setOnClickListener(this);
        }
        light33 = getActivity().findViewById(R.id.light33);
        if (light33 != null) {
            light33.setOnClickListener(this);
        }
        offlight = getActivity().findViewById(R.id.offlight);
        if (offlight != null) {
            offlight.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home: comm5.homebutton(); break;
            case R.id.light11:
                comm5.bluetoothsignal("1");
                Toast.makeText(getActivity(), "Low Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.light22 :
                comm5.bluetoothsignal("2");
                Toast.makeText(getActivity(), "Medium Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.light33 :
                comm5.bluetoothsignal("3");
                Toast.makeText(getActivity(), "High Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.offlight :
                comm5.bluetoothsignal("4");
                Toast.makeText(getActivity(), "Lights OFF", Toast.LENGTH_SHORT).show();
                break;
        }


    }
}


