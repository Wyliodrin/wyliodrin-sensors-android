package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.wyliodrin.wyliodrinsensors.api.WylioBoard;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Andreea Stoican on 29.04.2015.
 */
public class SensorListener implements SensorEventListener {

    private static final long UPDATE_TIMEOUT = 1000;

    private Activity context;
    private WyliodrinSensor sensor;
    private long lastUpdate;

    public SensorListener (Activity context, WyliodrinSensor sensor) {
        this.context = context;
        this.sensor = sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long time = System.currentTimeMillis();

        if (time - lastUpdate < UPDATE_TIMEOUT) //10 pe sec (sa fac o mediere)
            return;

        lastUpdate = time;
        Log.d("sensor changed ", sensor.getSensor().getName().toString());

        JSONArray values = new JSONArray();

        if (MainActivity.wylioBoard != null) {
            for (int i = 0; i < sensorEvent.values.length; i++) {
                try {
                    values.put(sensorEvent.values[i]);
                } catch (JSONException e) {
                }
            }

            //mobile:sensor_name:string_id
            MainActivity.wylioBoard.sendMessage("mobile:" + getId()+":id", values.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private String getId() {
        switch (sensor.getSensor().getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                return "accelerometer";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "ambient_temp";
            case Sensor.TYPE_GRAVITY:
                return "gravity";
            case Sensor.TYPE_GYROSCOPE:
                return "gyroscope";
            case Sensor.TYPE_LIGHT:
                return "light";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "linear_acceleration";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "magnetic_field";
            case Sensor.TYPE_ORIENTATION:
                return "orientation";
            case Sensor.TYPE_PRESSURE:
                return "pressure";
            case Sensor.TYPE_PROXIMITY:
                return "proximity";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "relative_humidity";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "rotation_vector";
            case Sensor.TYPE_TEMPERATURE:
                return "temperature";
        }
        return "general";
    }

    public WyliodrinSensor getWylioSensor() {
        return sensor;
    }
}
