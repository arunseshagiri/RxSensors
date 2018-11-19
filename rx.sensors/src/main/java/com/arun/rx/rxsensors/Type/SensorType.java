package com.arun.rx.rxsensors.Type;

import android.hardware.Sensor;

/**
 * Created by arunkumar on 19/11/18.
 */
public interface SensorType {
    int sensorType();

    <T> T sensorEvents(Sensor sensor, float[] data, int accuracy, long timestamp);

    float[] data();

    Sensor sensor();

    int accuracy();

    long timestamp();
}
