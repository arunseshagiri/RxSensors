package com.arun.rx.rxsensors.Type;

import android.hardware.Sensor;

/**
 * Created by arunkumar on 19/11/18.
 */
public class Accelerometer implements SensorType {

    private Sensor  sensor;
    private float[] data;
    private int     accuracy;
    private long    timestamp;

    private Accelerometer() {
    }

    @Override
    public Accelerometer sensorEvents(Sensor sensor, float[] data, int accuracy, long timestamp) {
        this.sensor = sensor;
        this.data = data;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        return this;
    }

    public static Accelerometer instance() {
        return new Accelerometer();
    }

    @Override
    public int sensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    public float[] data() {
        return data;
    }

    @Override
    public Sensor sensor() {
        return sensor;
    }

    @Override
    public int accuracy() {
        return accuracy;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return data[0] +
               " - " +
               data[1] +
               " - " +
               data[2] +
               " - " +
               sensor +
               " - " +
               accuracy +
               " - " +
               timestamp;
    }
}
