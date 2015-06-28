package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wyliodrin.wyliodrinsensors.api.WylioBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Andreea Stoican on 29.04.2015.
 */
public class SensorListener implements SensorEventListener, LocationListener, SensorSenderListener {

    private static long UPDATE_TIMEOUT = 1000;

    private Activity context;
    private WyliodrinSensor sensor;
    private long lastUpdate;

    public static JSONObject message;
    public static Object mutex = new Object();

    public SensorListener (Activity context, WyliodrinSensor sensor) {
        this.context = context;
        this.sensor = sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long time = System.currentTimeMillis();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        UPDATE_TIMEOUT = Integer.parseInt (prefs.getString("speed", "1000"));

        String phone_id = prefs.getString("phone id", "phone_id");

//        if (time - lastUpdate < UPDATE_TIMEOUT/2) //10 pe sec (sa fac o mediere)
//            return;

        lastUpdate = time;
        Log.d("sensor changed ", sensor.getName().toString());

        synchronized (mutex)
        {
            if (message == null) message = new JSONObject();
            JSONObject json = sensor.getData(sensorEvent);
            //mobile:sensor_name:string_id
            try {
                if (json != null) {
                    if (!json.has("value")) {
                        message.put(sensor.getType(), json);
                    } else {
                        message.put (sensor.getType(), json.getJSONArray("value"));
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


//        if (MainActivity.wylioBoard != null) {
//            JSONObject json = sensor.getData(sensorEvent);
//            //mobile:sensor_name:string_id
//            if (json!=null) {
//                if (!json.has("value")) {
//                    MainActivity.wylioBoard.sendMessage("mobile:" + sensor.getType() + ":", json);
//                } else {
//                    try {
//                        MainActivity.wylioBoard.sendMessage("mobile:" + sensor.getType() + ":"+phone_id, json.getDouble("value"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }



    public void unregister ()
    {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public WyliodrinSensor getWylioSensor() {
        return sensor;
    }

    @Override
    public void onLocationChanged(Location location) {
        long time = System.currentTimeMillis();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        UPDATE_TIMEOUT = Integer.parseInt (prefs.getString("speed", "1000"));
        String phone_id = prefs.getString("phone id", "phone_id");

        if (time - lastUpdate < UPDATE_TIMEOUT/2) //10 pe sec (sa fac o mediere)
            return;

        lastUpdate = time;
        Log.d("sensor changed ", sensor.getName());


        synchronized (mutex)
        {
            if (message == null) message = new JSONObject();
            JSONObject json = sensor.getData(location);
            //mobile:sensor_name:string_id
            try {
                if (json != null) {
                    if (!json.has("value")) {
                        message.put(sensor.getType(), json);
                    } else {
                        message.put (sensor.getType(), json.getDouble("value"));
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

//        if (MainActivity.wylioBoard != null) {
//            JSONObject json = sensor.getData(location);
//            //mobile:sensor_name:string_id
//            if (json!=null) {
//                if (!json.has("value")) {
//                    MainActivity.wylioBoard.sendMessage("mobile:" + sensor.getType() + ":", json);
//                } else {
//                    try {
//                        MainActivity.wylioBoard.sendMessage("mobile:" + sensor.getType() + ":"+phone_id, json.getDouble("value"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void valuesReset() {
        this.sensor.reset ();
    }
}
