# RxSensors
Reactive Sensor APIs Library using RxAndroid and RxJava 2

# Download
To get a Git project into your build

Add it in your root build.gradle at the end of repositories

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

Add the dependency

    implementation 'com.github.arungiri10:RxSensors:1.0.0'

# Usage

Initialize RxSensor class
    
    RxSensors rxSensors = RxSensors.init(sensorManager, Sensor.TYPE_ACCELEROMETER);
    
Listen to sensor events

    rxSensors
        .listenForSensorEvents()
            .subscribe(
                sensorEvents ->  Use sensorEvents object to get events, accuracy, etc,
                error -> new Throwable(error)
            );
            
Unregister from Sensor events

    rxSensors
        .disconnect()
        .subscribe();
