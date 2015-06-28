package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wyliodrin.wyliodrinsensors.api.WylioBoard;
import com.wyliodrin.wyliodrinsensors.api.WylioMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandru on 12/05/15.
 */
public class WyliodrinSender extends Thread
{
    private Activity context;
    private boolean quit = false;
    private SensorSenderListener listener;

    public WyliodrinSender (Activity context)
    {
        this.context = context;
    }

    public void setSensorSenderListener (SensorSenderListener listener)
    {
        this.listener = listener;
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
                // System.out.println("sending message");
                WylioBoard.WYLIO_ADDRESS = prefs.getString("application", WylioBoard.WYLIO_ADDRESS);
                if (SensorListener.message != null && MainActivity.wylioBoard != null) MainActivity.wylioBoard.sendMessage("mobile:"+phone_id, SensorListener.message);
                SensorListener.message = null;
                context.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listener.valuesReset();
                    }
                });
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
