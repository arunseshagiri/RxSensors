package com.arun.rx.rxsensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.arun.rx.rxsensors.Type.SensorType;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;

import static io.reactivex.Observable.fromPublisher;

/**
 * Created by arunkumar on 30/10/18.
 */
public class RxSensors {
    private PublishProcessor<SensorType> mqttEventSubject;
    private SensorManager                sensorManager;
    private SensorEventListener          listener;
    private int                          samplingPeriod;
    private SensorType                   sensorType;

    private RxSensors() {
    }

    protected Observable<List<Sensor>> listOfAllSensors(int type) {
        List<Sensor> sensors = sensorManager.getSensorList(type);
        return Observable
            .just(sensors);
    }

    protected Observable<List<Sensor>> listOfAvailableSensors(int type) {
        List<Sensor> sensors = sensorManager.getSensorList(type);
        return Observable
            .just(sensors)
            .flatMapIterable(sensorsList -> sensorsList)
            .filter(
                sensor -> sensorManager.getDefaultSensor(sensor.getType()) != null
            )
            .toList()
            .toObservable();
    }

    public boolean isConnected() {
        if (mqttEventSubject != null) {
            return true;
        }
        return false;
    }

    public Observable<SensorType> listenForSensorEvents() {
        if (mqttEventSubject != null) {
            return fromPublisher(mqttEventSubject);
        }

        mqttEventSubject = PublishProcessor.create();
        listener = registerForSensorEvents();
        sensorManager
            .registerListener(
                listener,
                sensorManager.getDefaultSensor(sensorType.sensorType()),
                samplingPeriod
            );
        return fromPublisher(mqttEventSubject)
            .doOnDispose(
                () -> sensorManager.unregisterListener(listener)
            );
    }

    public SensorEventListener registerForSensorEvents() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                mqttEventSubject
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

        public RxSensors build(SensorManager sensorManager, SensorType sensorType) {
            RxSensors rxSensor = new RxSensors();
            rxSensor.sensorManager = sensorManager;
            rxSensor.samplingPeriod = samplingPeriod;
            rxSensor.sensorType = sensorType;
            return rxSensor;
        }
    }

}
