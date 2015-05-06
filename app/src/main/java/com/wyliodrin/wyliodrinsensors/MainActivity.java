package com.wyliodrin.wyliodrinsensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wyliodrin.wyliodrinsensors.api.WylioBoard;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static WylioBoard wylioBoard = null;

    private SensorManager mSensorManager;
    private List<SensorListener> listeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wylioBoard = null;
        listeners = new ArrayList<SensorListener>();

        final SensorListAdapter adapter = new SensorListAdapter(this);
        ListView sensorsList = (ListView) findViewById(R.id.sensors_list);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < deviceSensors.size(); i++) {
            adapter.addItem(deviceSensors.get(i));
        }

        sensorsList.setAdapter(adapter);

        sensorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.toggleItem(i);
                adapter.notifyDataSetChanged();
            }
        });

        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wylioBoard == null)
                    IntentIntegrator.initiateScan(MainActivity.this);
                else {
                    for (SensorListener s : listeners) {
                        mSensorManager.unregisterListener(s);
                    }
                    listeners.clear();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        WyliodrinSensor sensor = (WyliodrinSensor) adapter.getItem(i);
                        if (sensor.isChecked()) {
                            SensorListener sensorListener = new SensorListener(MainActivity.this, sensor);
                            listeners.add(sensorListener);
                            mSensorManager.registerListener(sensorListener, sensor.getSensor(), sensor.getSensor().getType());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String token = scanResult.getContents();
            if (token.contains("wyliodrin:gadget:")) {
                token = token.replace("wyliodrin:gadget://", "");

                wylioBoard = new WylioBoard(token);

                Button sendBtn = (Button) findViewById(R.id.send_button);
                sendBtn.setText("Send");
            } else {
                Toast.makeText(this, token.length(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (SensorListener s : listeners) {
            mSensorManager.unregisterListener(s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (SensorListener s : listeners) {
            mSensorManager.registerListener(s, s.getWylioSensor().getSensor(), s.getWylioSensor().getSensor().getType());
        }
    }
}
