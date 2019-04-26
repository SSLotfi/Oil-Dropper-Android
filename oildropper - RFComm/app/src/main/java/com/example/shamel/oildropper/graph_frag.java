package com.example.shamel.oildropper;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


/**
 * Created by Shamel on 2/26/2018.
 */

public class graph_frag extends Fragment implements View.OnClickListener {

    int counter = 0;
    int flagsessionend = 0;
    communicator comm66;
    Button Home;
    double y,x = 0;
    ArrayList<String> values;
    //String[] Receivedreading = new String[100];
    String[] Receivedreading;
    int readingcounter = 0;
    int time = 0;

    ArrayList<String> arraylist = new ArrayList<String>(10);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        //return inflater.inflate(R.layout.music_frag,container,false);
        View view = inflater.inflate(R.layout.graph_frag, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graphy);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        //Intent in=getActivity().getIntent();
        //Receivedreading=in.getStringExtra("array");
        if (getArguments() != null && getArguments().containsKey("array2")) {
            Bundle args = getArguments();
            Receivedreading = args.getStringArray("array2");
            readingcounter = args.getInt("readingcount");
            time = args.getInt("time2");
        }
        //Toast.makeText(getActivity(),time,Toast.LENGTH_LONG).show();
        double step ;

        step = (float) time / (float) readingcounter ;

        int counter = 0;

        for(int i = 0 ; i <= time ; i += step)
        {
            //Toast.makeText(getActivity(),Integer.toString(readingcounter), Toast.LENGTH_LONG).show();
            double x = i;
            float y = 0;
            if (Receivedreading != null && counter <= readingcounter) {
                y = Float.parseFloat(Receivedreading[counter]);
                counter++;
                //Toast.makeText(getActivity(), Float.toString(y), Toast.LENGTH_SHORT).show();
            }
            //if(counter != 0 && Receivedreading[counter - 1] != "1") {
                series.appendData(new DataPoint(x,y),true,10000
                );
            //}
        }
        counter = 0;
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (min)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Stress level");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        //graph.getViewport().setMaxX(time+10);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1023);
        graph.getViewport().setScrollable(true);
        series.setColor(Color.GREEN);
        graph.addSeries(series);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        comm66 = (communicator) getActivity();
        Home = getActivity().findViewById(R.id.home);
        Home.setOnClickListener(this);
        comm66.homedisappear();
        comm66.old_readings(Receivedreading,time,readingcounter);
    }


    @Override
    public void onClick(View view2) {

        switch (view2.getId()) {
            case R.id.home:
                //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                comm66.homebutton();
                break;
        }
    }
}
