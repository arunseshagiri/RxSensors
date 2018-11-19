package com.arun.rx.rxsensors.Type;

import android.hardware.Sensor;

/**
 * Created by arunkumar on 19/11/18.
 */
public class AccelerometerUnCalibrated implements SensorType {

    private Sensor  sensor;
    private float[] data;
    private int     accuracy;
    private long    timestamp;

    private AccelerometerUnCalibrated() {
    }

    public static AccelerometerUnCalibrated instance() {
        return new AccelerometerUnCalibrated();
    }

    @Override
    public int sensorType() {
        return Sensor.TYPE_ACCELEROMETER_UNCALIBRATED;
    }

    @Override
    public AccelerometerUnCalibrated sensorEvents(Sensor sensor, float[] data, int accuracy, long timestamp) {
        this.data = data;
        this.sensor = sensor;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        return this;
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
               data[3] +
               " - " +
               data[4] +
               " - " +
               data[5] +
               " - " +
               sensor +
               " - " +
               accuracy +
               " - " +
               timestamp;
    }
}
