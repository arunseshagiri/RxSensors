package com.arun.rx.rxsensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.processors.PublishProcessor;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static io.reactivex.Observable.fromPublisher;

/**
 * Created by arunkumar on 30/10/18.
 */
public class RxSensors {
    private PublishProcessor<SensorEvents> mqttEventSubject;
    private SensorManager                  sensorManager;
    private SensorEventListener            listener;
    private int                            samplingPeriod;
    private int                            sensorType;

    private RxSensors() {
    }

    public static RxSensors init(SensorManager sensorManager, int sensorType) {
        return new Builder()
            .sensorManager(sensorManager, sensorType)
            .samplingPeriod(SENSOR_DELAY_NORMAL)
            .build();
    }

    public static RxSensors init(SensorManager sensorManager, int sensorType, int samplingPeriod) {
        return new Builder()
            .sensorManager(sensorManager, sensorType)
            .samplingPeriod(samplingPeriod)
            .build();
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

    public Observable<SensorEvents> listenForSensorEvents() {
        if (mqttEventSubject != null) {
            return fromPublisher(mqttEventSubject);
        }

        mqttEventSubject = PublishProcessor.create();
        listener = registerForSensorEvents();
        sensorManager
            .registerListener(
                listener,
                sensorManager.getDefaultSensor(sensorType),
                samplingPeriod
            );
        return fromPublisher(mqttEventSubject);
    }

    public Completable disconnect() {
        if (sensorManager == null) {
            mqttEventSubject = null;
            return Completable.error(new Throwable("Sensor manager is null"));
        }

        return Completable.create(
            e -> {
                sensorManager.unregisterListener(listener);
                mqttEventSubject = null;
                e.onComplete();
            }
        );
    }

    public SensorEventListener registerForSensorEvents() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                mqttEventSubject
                    .onNext(
                        new SensorEvents(
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
        private SensorManager sensorManager;
        private int           samplingPeriod;
        private int           sensorType;

        public Builder sensorManager(SensorManager sensorManager, int sensorType) {
            this.sensorManager = sensorManager;
            this.sensorType = sensorType;
            return this;
        }

        public Builder samplingPeriod(int samplingPeriod) {
            this.samplingPeriod = samplingPeriod;
            return this;
        }

        public RxSensors build() {
            RxSensors rxSensor = new RxSensors();
            rxSensor.sensorManager = sensorManager;
            rxSensor.samplingPeriod = samplingPeriod;
            rxSensor.sensorType = sensorType;
            return rxSensor;
        }
    }

}
