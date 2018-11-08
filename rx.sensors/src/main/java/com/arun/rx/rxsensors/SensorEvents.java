package com.arun.rx.rxsensors;

import android.hardware.Sensor;

/**
 * Created by arunkumar on 08/11/18.
 */
public class SensorEvents {
    private final Sensor sensor;
    private final float  dataX;
    private final float  dataY;
    private final float  dataZ;
    private final int    accuracy;
    private final long   timestamp;

    public SensorEvents(Sensor sensor, float[] data, int accuracy, long timestamp) {
        this.sensor = sensor;
        this.dataX = data[0];
        this.dataY = data[1];
        this.dataZ = data[2];
        this.accuracy = accuracy;
        this.timestamp = timestamp;
    }

    public float dataX() {
        return dataX;
    }

    public float dataY() {
        return dataY;
    }

    public float dataZ() {
        return dataZ;
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
}
