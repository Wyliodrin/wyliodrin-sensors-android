package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea Stoican on 29.04.2015.
 */
public class SensorListAdapter extends BaseAdapter {

    private List<WyliodrinSensor> elements;
    private Activity contex;

    public SensorListAdapter(Activity context) {
        this.contex = context;
        elements = new ArrayList<WyliodrinSensor>();
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item;
        Sensor sensor;

        if (view != null) {
            item = view;
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) contex.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.sensor_list_item, viewGroup, false);
        }

        sensor = elements.get(i).getSensor();
        TextView textView = (TextView) item.findViewById(R.id.sensor_text);
        textView.setText(sensor.getName());

        CheckBox checkBox = (CheckBox) item.findViewById(R.id.enable_sensor);
        checkBox.setChecked(elements.get(i).isChecked());

        return item;
    }

    public void addItem (Sensor sensor) {
        elements.add(new WyliodrinSensor(sensor));
    }

    public void toggleItem (int i) {
        elements.get(i).toggle();
    }
}
