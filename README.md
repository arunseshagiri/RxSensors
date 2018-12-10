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

    implementation 'com.github.arunseshagiri:RxSensors:1.0.2'

# Usage

Initialize Sensor Manager

    SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

Initialize RxSensor class
    
    RxSensors<Accelerometer> rxSensors = new RxSensors.Builder()
            .samplingPeriod(SENSOR_DELAY_NORMAL)
            .build(sensorManager, Accelerometer.instance());
    
Listen to sensor events

    Disposable disposable = rxSensors
        .listenForSensorEvents()
            .subscribe(
                sensorEvents ->  Use sensorEvents object to get events, accuracy, etc,
                error -> new Throwable(error)
            );
            
Unregister from Sensor events

    disposable.dispose();

