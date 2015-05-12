package com.wyliodrin.wyliodrinsensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alexandru on 12/05/15.
 */
public class WyliodrinSender extends Thread
{
    private Context context;
    private boolean quit = false;

    public WyliodrinSender (Context context)
    {
        this.context = context;
    }

    public void run ()
    {
        while (quit == false)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String phone_id = prefs.getString("phone id", "phone_id");
            int speed = Integer.parseInt(prefs.getString("speed", "1000"));
            synchronized (SensorListener.mutex)
            {
                System.out.println("sending message");
                if (SensorListener.message != null && MainActivity.wylioBoard != null) MainActivity.wylioBoard.sendMessage("mobile:"+phone_id, SensorListener.message);
                SensorListener.message = null;
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void quit ()
    {
        quit = true;
    }
}
