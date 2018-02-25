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
    Sensor sensorAccelerometer, sensorGyroscope, sensorGravity;
    TextView accx, accy, accz, gyrx, gyry, gyrz, gx, gy, gz;

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager != null){
            sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
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
    }

    private void SensorNotSupported(int sensor){
        /*
        * 0 = Gyroscope
        * 1 = Accelerometer
        * 2 = Gravity
        * */

        switch (sensor) {
            case 0:
                Toast.makeText(this, "Giroscopio no soportado", Toast.LENGTH_SHORT).show();
                gyrx.setText("No soportado");
                gyry.setText("No soportado");
                gyrz.setText("No soportado");
                break;

            case 1:
                Toast.makeText(this, "Acelerometro no soportado", Toast.LENGTH_SHORT).show();
                accx.setText("No soportado");
                accy.setText("No soportado");
                accz.setText("No soportado");
                break;

            case 2:
                Toast.makeText(this, "Sensor de gravedad no soportado", Toast.LENGTH_SHORT).show();
                gx.setText("No soportado");
                gy.setText("No soportado");
                gz.setText("No soportado");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accx.setText("x = " + Float.toString(sensorEvent.values[0]));
            accy.setText("y = " + Float.toString(sensorEvent.values[1]));
            accz.setText("z = " + Float.toString(sensorEvent.values[2]));
            Log.d("SensorData", String.valueOf(sensorEvent.accuracy));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyrx.setText("x = " + Float.toString(sensorEvent.values[0]));
            gyry.setText("y = " + Float.toString(sensorEvent.values[1]));
            gyrz.setText("z = " + Float.toString(sensorEvent.values[2]));
        }else if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY){
            gx.setText("x = " + Float.toString(sensorEvent.values[0]));
            gy.setText("y = " + Float.toString(sensorEvent.values[1]));
            gz.setText("z = " + Float.toString(sensorEvent.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
