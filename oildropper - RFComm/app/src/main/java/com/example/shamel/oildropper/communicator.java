package com.example.shamel.oildropper;

/**
 * Created by Shamel on 2/9/2018.
 */

public interface communicator {
    public void homebutton();
    public void bluetoothsignal(char signal);

    void homedisappear();

    public void sendtime(int time);
    public void sessionend();
    public void heat(int heatertemp,int flagheater);
    public void new_session();
    public void old_readings(String x[],int time,int readcount);



}
