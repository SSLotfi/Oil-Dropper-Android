package com.example.shamel.oildropper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Shamel on 2/15/2018.
 */

public class music_frag extends Fragment implements View.OnClickListener{

    Button Home ;
    communicator comm4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.music_frag,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);
        comm4 = (communicator) getActivity();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.home:
                comm4.homebutton();
                break;
        }
    }
}
