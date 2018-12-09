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

    public static Accelerometer instance() {
        return new Accelerometer();
    }

    @Override
    public Accelerometer sensorEvents(Sensor sensor, float[] data, int accuracy, long timestamp) {
        this.sensor = sensor;
        this.data = data;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public int sensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    public float[] data() {
        return data;
    }

    public Sensor sensor() {
        return sensor;
    }

    public int accuracy() {
        return accuracy;
    }

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
