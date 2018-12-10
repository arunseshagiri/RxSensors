package com.arun.rx.rxsensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.arun.rx.rxsensors.Type.SensorType;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static io.reactivex.Observable.fromPublisher;

/**
 * Created by arunkumar on 30/10/18.
 */
public class RxSensors<T extends SensorType> {
    private PublishProcessor<T> sensorEventSubject;
    private SensorManager       sensorManager;
    private SensorEventListener listener;
    private int                 samplingPeriod = SENSOR_DELAY_NORMAL;
    private T                   sensorType;

    private RxSensors() {
    }

    protected Flowable<List<Sensor>> listOfAllSensors(int type) {
        List<Sensor> sensors = sensorManager.getSensorList(type);
        return Flowable
            .just(sensors);
    }

    protected Flowable<List<Sensor>> listOfAvailableSensors(int type) {
        List<Sensor> sensors = sensorManager.getSensorList(type);
        return Flowable
            .just(sensors)
            .flatMapIterable(sensorsList -> sensorsList)
            .filter(
                sensor -> sensorManager.getDefaultSensor(sensor.getType()) != null
            )
            .toList()
            .toFlowable();
    }

    public boolean isConnected() {
        if (sensorEventSubject != null) {
            return true;
        }
        return false;
    }

    public Flowable<T> listenForSensorEvents() {
        if (sensorEventSubject != null) {
            return fromPublisher(sensorEventSubject)
                .toFlowable(BackpressureStrategy.LATEST);
        }

        sensorEventSubject = PublishProcessor.create();
        listener = registerForSensorEvents();
        sensorManager
            .registerListener(
                listener,
                sensorManager.getDefaultSensor(sensorType.sensorType()),
                samplingPeriod
            );
        return fromPublisher(sensorEventSubject)
            .toFlowable(BackpressureStrategy.LATEST)
            .filter(__ -> sensorManager != null)
            .doOnCancel(
                () -> sensorManager.unregisterListener(listener)
            )
            .doOnCancel(
                () -> sensorEventSubject = null
            );
    }

    private SensorEventListener registerForSensorEvents() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                sensorEventSubject
                    .onNext(
                        sensorType
                            .sensorEvents(
                                sensorEvent.sensor,
                                sensorEvent.values,
                                sensorEvent.accuracy,
                                sensorEvent.timestamp
                            )
                    );
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //Will implement in next release
            }
        };
    }

    public static class Builder {
        private int samplingPeriod;

        public Builder samplingPeriod(int samplingPeriod) {
            this.samplingPeriod = samplingPeriod;
            return this;
        }

        public <T extends SensorType> RxSensors<T> build(SensorManager sensorManager, T sensorType) {
            RxSensors<T> rxSensor = new RxSensors();
            rxSensor.sensorManager = sensorManager;
            rxSensor.samplingPeriod = samplingPeriod;
            rxSensor.sensorType = sensorType;
            return rxSensor;
        }
    }

}
