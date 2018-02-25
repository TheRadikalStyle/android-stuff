package com.theradikalsoftware.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensorAccelerometer, sensorGyroscope, sensorGravity, sensorStep, sensorProximity, sensorPressure, sensorLight, sensorTemp;
    TextView accx, accy, accz, gyrx, gyry, gyrz, gx, gy, gz, stepCounter, proximity, pressure, light, temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accx = findViewById(R.id.cardview_accelerometer_textview_x);
        accy = findViewById(R.id.cardview_accelerometer_textview_y);
        accz = findViewById(R.id.cardview_accelerometer_textview_z);

        gyrx = findViewById(R.id.cardview_gyroscope_textview_x);
        gyry = findViewById(R.id.cardview_gyroscope_textview_y);
        gyrz = findViewById(R.id.cardview_gyroscope_textview_z);

        gx = findViewById(R.id.cardview_gravity_textview_x);
        gy = findViewById(R.id.cardview_gravity_textview_y);
        gz = findViewById(R.id.cardview_gravity_textview_z);

        stepCounter = findViewById(R.id.cardview_steps_textview_steps);

        proximity = findViewById(R.id.cardview_proximity_textview_prox);

        pressure = findViewById(R.id.cardview_pressure_textview_pressure);

        light = findViewById(R.id.cardview_light_textview_light);

        temperature = findViewById(R.id.cardview_temperature_textview_temp);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager != null){
            sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            sensorStep = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            sensorTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }else{
            Toast.makeText(this, "Error al obtener sensores", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(sensorGyroscope == null)
            SensorNotSupported(0);

        if(sensorAccelerometer == null)
            SensorNotSupported(1);

        if(sensorGravity == null)
            SensorNotSupported(2);
        if(sensorStep == null)
            SensorNotSupported(3);

        if(sensorProximity == null)
            SensorNotSupported(4);

        if(sensorPressure == null)
            SensorNotSupported(5);

        if(sensorLight == null)
            SensorNotSupported(6);

        if(sensorTemp == null)
            SensorNotSupported(7);
    }

    private void SensorNotSupported(int sensor){
        /*
        * 0 = Gyroscope
        * 1 = Accelerometer
        * 2 = Gravity
        * 3 = Step
        * 4 = Proximity
        * 5  = Pressure
        * 6 = Light
        * 7 = Ambient temperature
        * */

        switch (sensor) {
            case 0:
                gyrx.setText("No soportado");
                gyry.setText("No soportado");
                gyrz.setText("No soportado");
                break;

            case 1:
                accx.setText("No soportado");
                accy.setText("No soportado");
                accz.setText("No soportado");
                break;

            case 2:
                gx.setText("No soportado");
                gy.setText("No soportado");
                gz.setText("No soportado");
                break;

            case 3:
                stepCounter.setText("No soportado");
                break;

            case 4:
                proximity.setText("No soportado");
                break;

            case 5:
                pressure.setText("No soportado");
                break;

            case 6:
                light.setText("No soportado");
                break;

            case 7:
                temperature.setText("No soportado");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorStep, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorTemp, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accx.setText("x = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[0]));
            accy.setText("y = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[1]));
            accz.setText("z = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[2]));
            Log.d("SensorData", String.valueOf(sensorEvent.accuracy));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyrx.setText("x = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[0]));
            gyry.setText("y = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[1]));
            gyrz.setText("z = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[2]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gx.setText("x = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[0]));
            gy.setText("y = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[1]));
            gz.setText("z = " + String.format(java.util.Locale.US, "%.2f", sensorEvent.values[2]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            stepCounter.setText("Pasos = " + String.valueOf(sensorEvent.values[0]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY){
            proximity.setText((String.valueOf(sensorEvent.values[0])));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText("Presión = " + Float.toString(sensorEvent.values[0]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText("" + Float.toString(sensorEvent.values[0]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temperature.setText(Float.toString(sensorEvent.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
