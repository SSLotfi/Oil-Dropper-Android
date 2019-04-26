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

public class time_frag extends Fragment implements View.OnClickListener{
    Button Home ;
    Button time11,time22,time33;
    String signal;
    communicator comm6;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_frag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm6 = (communicator) getActivity();
        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);

        time11 = getActivity().findViewById(R.id.time11);
        time11.setOnClickListener(this);
        time22 = getActivity().findViewById(R.id.time22);
        time22.setOnClickListener(this);
        time33 = getActivity().findViewById(R.id.time33);
        time33.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home: comm6.homebutton(); break;
            case R.id.time11:
                comm6.bluetoothsignal("j");
                comm6.sendtime(30);
                comm6.new_session();
                Toast.makeText(getActivity(), "Time set to 30 mins", Toast.LENGTH_SHORT).show();
                break;
            case R.id.time22 :
                comm6.bluetoothsignal("k");
                comm6.sendtime(45);
                comm6.new_session();
                Toast.makeText(getActivity(), "Time set to 45 mins", Toast.LENGTH_SHORT).show();
                break;
            case R.id.time33 :
                comm6.bluetoothsignal("l");
                comm6.sendtime(60);
                comm6.new_session();
                Toast.makeText(getActivity(), "Time set to 60 mins", Toast.LENGTH_SHORT).show();
                break;
        }


    }
}


