package com.arun.rx.sampleapp;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arun.rx.rxsensors.RxSensors;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

public class SensorSampleAppActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private RxSensors     rxSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_sample_app);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rxSensors = new RxSensors.Builder()
            .samplingPeriod(SENSOR_DELAY_NORMAL)
            .build(sensorManager, TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rxSensors
            .listenForSensorEvents()
            .subscribe(
                result -> Log.v("", result.dataX() + ""),
                error -> new Throwable(error)
            );
    }

    @Override
    protected void onPause() {
        super.onPause();
        rxSensors
            .disconnect()
            .subscribe();
    }
}
