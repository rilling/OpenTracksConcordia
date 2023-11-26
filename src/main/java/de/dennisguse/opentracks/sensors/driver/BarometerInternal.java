package de.dennisguse.opentracks.sensors.driver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import de.dennisguse.opentracks.data.models.AtmosphericPressure;
import de.dennisguse.opentracks.sensors.AltitudeSumManager;

import java.util.LinkedList;
import java.util.Queue;



public class BarometerInternal {

    private static final String TAG = BarometerInternal.class.getSimpleName();

    private static final int SAMPLING_PERIOD = (int) TimeUnit.SECONDS.toMicros(5);

    private AltitudeSumManager observer;

    private static final int FILTER_WINDOW_SIZE = 5; // Window size for the moving average filter
    private Queue<Float> pressureReadings = new LinkedList<>();
    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!isConnected()) {
                Log.w(TAG, "Not connected to sensor, cannot process data.");
                return;
            }

            // Add new reading to the queue
            if (pressureReadings.size() >= FILTER_WINDOW_SIZE) {
                pressureReadings.poll(); // Remove the oldest reading
            }
            pressureReadings.offer(event.values[0]);

            // Calculate the average of the readings in the queue
            float sum = 0;
            for (Float reading : pressureReadings) {
                sum += reading;
            }
            float average = sum / pressureReadings.size();

            // Use the averaged value instead of the raw reading
            observer.onSensorValueChanged(AtmosphericPressure.ofHPA(average));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.w(TAG, "Sensor accuracy changes are (currently) ignored.");
        }
    };

    public void connect(Context context, Handler handler, AltitudeSumManager observer) {
        this.observer = observer;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressureSensor == null) {
            Log.w(TAG, "No pressure sensor available.");
            this.observer = null;
            return;
        }

        if (sensorManager.registerListener(listener, pressureSensor, SAMPLING_PERIOD, handler)) {
            this.observer = observer;
            return;
        }

        disconnect(context);
    }

    public void disconnect(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(listener);
    }

    public boolean isConnected() {
        return observer != null;
    }
}
