package com.wyliodrin.wyliodrinsensors;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.preference.PreferenceManager;

import org.json.JSONObject;

/**
 * Created by Andreea Stoican on 29.04.2015.
 */
public class WyliodrinSensor {

    private Sensor sensor;
    private boolean checked;
    private String type;
    private String name;

    public WyliodrinSensor (Sensor sensor) {
        this.sensor = sensor;
        checked = false;
        this.name = sensor.getName();
        this.type = getId();
    }

    public WyliodrinSensor (String name)
    {
        if (name == "gps")
        {
            this.sensor = null;
            checked = false;
            this.name = "GPS";
            this.type = "gps";
        }
        else
        {
            throw new RuntimeException("sensor unimplmeneted");
        }
    }

    private String getId() {
        switch (sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                return "accelerometer";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "ambient_temperature";
            case Sensor.TYPE_GRAVITY:
                return "gravity";
            case Sensor.TYPE_GYROSCOPE:
                return "gyroscope";
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                return "gyroscope_uncalibrated";
            case Sensor.TYPE_LIGHT:
                return "light";
            case Sensor.TYPE_HEART_RATE:
                return "hear_rate";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "linear_acceleration";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "magnetic_field";
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                return "magnetic_field_uncalibrated";
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
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return "game_rotation_vector";
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return "geomagnetic_rotation_vector";
            case Sensor.TYPE_TEMPERATURE:
                return "temperature";
            case Sensor.TYPE_STEP_COUNTER:
                return "step_counter";
            case Sensor.TYPE_STEP_DETECTOR:
                return "step_detector";
        }
        return "other";
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked (boolean checked)
    {
        this.checked = checked;
    }

    public void toggle() {
        checked = !checked;
    }

    public String getName ()
    {
        return this.name;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public String getType ()
    {
        return type;
    }

    public JSONObject getData (Location location)
    {
        JSONObject json = new JSONObject();
        try {
            json.put ("latitude", location.getLatitude());
            json.put ("longitude", location.getLongitude());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            json = null;

        }
        return json;
    }

    public JSONObject getData (SensorEvent event)
    {
        JSONObject json = new JSONObject();
        try {
            switch (sensor.getType()) {
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                    json.put("x", event.values[0]);
                    json.put("y", event.values[1]);
                    json.put("z", event.values[2]);
                    json.put("drift_x", event.values[3]);
                    json.put("drift_y", event.values[4]);
                    json.put("drift_z", event.values[5]);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                    json.put("x", event.values[0]);
                    json.put("y", event.values[1]);
                    json.put("z", event.values[2]);
                    json.put("bias_x", event.values[3]);
                    json.put("bias_y", event.values[4]);
                    json.put("bias_z", event.values[5]);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    json.put("azimuth", event.values[0]);
                    json.put("pitch", event.values[1]);
                    json.put("roll", event.values[2]);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                case Sensor.TYPE_GRAVITY:
                case Sensor.TYPE_GYROSCOPE:
                case Sensor.TYPE_LINEAR_ACCELERATION:
                case Sensor.TYPE_MAGNETIC_FIELD:
                case Sensor.TYPE_ROTATION_VECTOR:
                case Sensor.TYPE_GAME_ROTATION_VECTOR:
                    json.put("x", event.values[0]);
                    json.put("y", event.values[1]);
                    json.put("z", event.values[2]);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    json.put ("value", event.values[0]);
                    break;
                case Sensor.TYPE_LIGHT:
//                    if (event.values.length == 3)
//                    {
//                        json.put ("red", event.values[0]);
//                        json.put ("green", event.values[1]);
//                        json.put ("blue", event.values[2]);
//                    }
//                    else
//                    {
//                        json.put ("value", event.values[0]);
//                    }
//                    break;
                case Sensor.TYPE_HEART_RATE:
                case Sensor.TYPE_PRESSURE:
                case Sensor.TYPE_PROXIMITY:
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                case Sensor.TYPE_TEMPERATURE:
                case Sensor.TYPE_STEP_COUNTER:
                case Sensor.TYPE_STEP_DETECTOR:
                case Sensor.TYPE_SIGNIFICANT_MOTION:
                    json.put ("value", event.values[0]);
                    break;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            json = null;

        }
        return json;
    }
}
