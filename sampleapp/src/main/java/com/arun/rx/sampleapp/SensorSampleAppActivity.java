package com.arun.rx.sampleapp;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.arun.rx.rxsensors.RxSensors;
import com.arun.rx.rxsensors.Type.AccelerometerUnCalibrated;

import io.reactivex.disposables.Disposable;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

public class SensorSampleAppActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private RxSensors     rxSensors;
    private Disposable    disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_sample_app);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rxSensors = new RxSensors.Builder()
            .samplingPeriod(SENSOR_DELAY_NORMAL)
            .build(sensorManager, AccelerometerUnCalibrated.instance());
        disposable = rxSensors
            .listenForSensorEvents()
            .subscribe(
                result -> Log.v(
                    "",
                    result.toString()
                ),
                error -> new Throwable(error)
            );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
