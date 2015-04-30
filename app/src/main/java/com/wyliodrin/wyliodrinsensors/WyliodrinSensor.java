package com.wyliodrin.wyliodrinsensors;

import android.hardware.Sensor;

/**
 * Created by Andreea Stoican on 29.04.2015.
 */
public class WyliodrinSensor {

    private Sensor sensor;
    private boolean checked;

    public WyliodrinSensor (Sensor sensor) {
        this.sensor = sensor;
        checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void toggle() {
        checked = !checked;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
