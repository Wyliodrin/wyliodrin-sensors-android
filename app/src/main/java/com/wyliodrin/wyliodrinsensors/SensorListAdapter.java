package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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

    private SensorsListListener listener;

    private SharedPreferences prefs;

    class Items
    {
        public TextView textView;
        public ImageView sensorImage;
        public CheckBox checkBox;
    }

    public SensorListAdapter(Activity context) {
        this.contex = context;
        if (context instanceof SensorsListListener) this.listener = (SensorsListListener)context;
        prefs = PreferenceManager.getDefaultSharedPreferences(contex);
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
        WyliodrinSensor sensor;

        if (view != null) {
            item = view;
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) contex.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.sensor_list_item, viewGroup, false);
        }

        Items items = (Items)item.getTag ();
        if (items == null)
        {
            items = new Items ();
            items.textView = (TextView) item.findViewById(R.id.sensor_text);

            items.checkBox = (CheckBox) item.findViewById(R.id.enable_sensor);

            items.sensorImage = (ImageView) item.findViewById(R.id.sensor_image);

            item.setTag(items);

        }

        sensor = elements.get(i);

        items.textView.setText(sensor.getName());
        items.checkBox.setChecked(elements.get(i).isChecked());
        items.sensorImage.setImageResource(contex.getResources().getIdentifier(sensor.getType(), "drawable", contex.getPackageName()));

        return item;
    }

    public void addItem (Sensor sensor) {
        WyliodrinSensor wyliodrinSensor = new WyliodrinSensor(sensor);
        wyliodrinSensor.setChecked(prefs.getBoolean (wyliodrinSensor.getType(), false));
        elements.add(wyliodrinSensor);
    }
    public void addItem (String sensorType) {
        WyliodrinSensor wyliodrinSensor = new WyliodrinSensor(sensorType);
        wyliodrinSensor.setChecked(prefs.getBoolean (wyliodrinSensor.getType(), false));
        elements.add(wyliodrinSensor);
    }

    public void toggleItem (int i) {
        elements.get(i).toggle();
        if (listener != null) listener.sensorsListToggle();
    }
}
