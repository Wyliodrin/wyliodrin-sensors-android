package com.wyliodrin.wyliodrinsensors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wyliodrin.wyliodrinsensors.api.WylioBoard;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.AlertDialog.*;


public class MainActivity extends Activity implements SensorsListListener {

    public static WylioBoard wylioBoard = null;

    private SensorManager mSensorManager;
    private List<SensorListener> listeners;

    private LocationManager mLocationManager;

    private SensorListAdapter adapter;

    private static WyliodrinSender wyliodrinSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (wyliodrinSender == null) {
            wyliodrinSender = new WyliodrinSender(this);
            wyliodrinSender.start();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String token = prefs.getString("wylio board token", null);

        if (token != null) {
            wylioBoard = new WylioBoard(token);
        }
        listeners = new ArrayList<SensorListener>();

        adapter = new SensorListAdapter(this);
        ListView sensorsList = (ListView) findViewById(R.id.sensors_list);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        adapter.addItem("gps");

        for (int i = 0; i < deviceSensors.size(); i++) {
            adapter.addItem(deviceSensors.get(i));
        }

        sensorsList.setAdapter(adapter);

        sensorsListToggle();

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
                IntentIntegrator.initiateScan(MainActivity.this);
            }
        });

        ImageView menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Builder alert = new Builder(MainActivity.this);
//
//                alert.setTitle("Phone ID");
//                alert.setMessage("Type your phone id");
//
//                // Set an EditText view to get user input
//                final EditText input = new EditText(MainActivity.this);
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                alert.setView(input);
//                input.setText(prefs.getString("phone id", "phone_id"));
//
//                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        String value = input.getText().toString();
//                        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
//                        prefs.putString("phone id", value);
//                        prefs.commit();
//                        // Do something with value!
//                    }
//                });
//
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // Canceled.
//                    }
//                });
//
//                alert.show();
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && resultCode == Activity.RESULT_OK) {
            String token = scanResult.getContents();
            if (token.contains("wyliodrin:gadget:")) {
                token = token.replace("wyliodrin:gadget://", "");

                wylioBoard = new WylioBoard(token);

                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();
                prefs.putString("wylio board token", token);
                prefs.commit();

                // Button sendBtn = (Button) findViewById(R.id.send_button);
                // sendBtn.setText("Send");
            } else {
                Toast.makeText(this, token.length(), Toast.LENGTH_LONG).show();
            }
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (SensorListener s : listeners) {
            mSensorManager.unregisterListener(s);
            mLocationManager.removeUpdates(s);
        }

        wyliodrinSender.quit();
        wyliodrinSender = null;
    }

//    @Override
//    protected void onRestart() {
//        super.onResume();
//
//        for (SensorListener s : listeners) {
//            mSensorManager.registerListener(s, s.getWylioSensor().getSensor(), s.getWylioSensor().getSensor().getType());
//        }
//        sensorsListToggle();
//    }

    @Override
    public void sensorsListToggle() {
        System.out.println("toggle sensors");
        for (SensorListener s : listeners) {
            mSensorManager.unregisterListener(s);
            mLocationManager.removeUpdates(s);
        }
        listeners.clear();

        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();

        for (int i = 0; i < adapter.getCount(); i++) {
            WyliodrinSensor sensor = (WyliodrinSensor) adapter.getItem(i);
            prefs.putBoolean(sensor.getType(), sensor.isChecked());
            if (sensor.isChecked()) {
                SensorListener sensorListener = new SensorListener(MainActivity.this, sensor);
                listeners.add(sensorListener);
                if (sensor.getSensor()!=null) {
                    mSensorManager.registerListener(sensorListener, sensor.getSensor(), sensor.getSensor().getType());
                }
                else
                {
                    if (sensor.getType().equals("gps"))
                    {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, sensorListener);
                    }
                }
            }
            prefs.commit();
        }
    }
}
