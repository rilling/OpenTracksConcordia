package de.dennisguse.opentracks.sensors;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import de.dennisguse.opentracks.data.models.AtmosphericPressure;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.sensors.driver.BarometerInternal;

/**
 * Estimates the altitude gain and altitude loss using the device's pressure sensor (i.e., barometer).
 */
public class AltitudeSumManager implements SensorConnector {

    private static final String TAG = AltitudeSumManager.class.getSimpleName();

    private final BarometerInternal driver;

    private AtmosphericPressure lastAcceptedSensorValue;

    private AtmosphericPressure lastSeenSensorValue;

    private Float altitudeGainMeters;
    private Float altitudeLossMeters;

    public AltitudeSumManager() {
        driver = new BarometerInternal();
    }

    @VisibleForTesting
    public AltitudeSumManager(BarometerInternal mock) {
        this.driver = mock;
    }

    public void start(Context context, Handler handler) {
        driver.connect(context, handler, this);

        lastAcceptedSensorValue = null;
        reset();
    }

    public void stop(Context context) {
        Log.d(TAG, "Stop");

        driver.disconnect(context);
        reset();
    }

    public void fill(@NonNull TrackPoint trackPoint) {
        trackPoint.setAltitudeGain(altitudeGainMeters);
        trackPoint.setAltitudeLoss(altitudeLossMeters);
    }

    @Nullable
    public Float getAltitudeGainMeters() {
        return driver.isConnected() ? altitudeGainMeters : null;
    }

    @VisibleForTesting
    public void setAltitudeGainMeters(float altitudeGainMeters) {
        this.altitudeGainMeters = altitudeGainMeters;
    }

    @Nullable
    public Float getAltitudeLossMeters() {
        return driver.isConnected() ? altitudeLossMeters : null;
    }

    @VisibleForTesting
    public void setAltitudeLossMeters(float altitudeLossMeters) {
        this.altitudeLossMeters = altitudeLossMeters;
    }

    public void reset() {
        Log.d(TAG, "Reset");
        altitudeGainMeters = null;
        altitudeLossMeters = null;
    }

    public void onSensorValueChanged(AtmosphericPressure currentSensorValue) {
        if (lastAcceptedSensorValue == null) {
            lastAcceptedSensorValue = currentSensorValue;
            lastSeenSensorValue = currentSensorValue;
            return;
        }

        altitudeGainMeters = altitudeGainMeters != null ? altitudeGainMeters : 0;
        altitudeLossMeters = altitudeLossMeters != null ? altitudeLossMeters : 0;

        PressureSensorUtils.AltitudeChange altitudeChange = PressureSensorUtils.computeChangesWithSmoothingMeters(lastAcceptedSensorValue, lastSeenSensorValue, currentSensorValue);
        if (altitudeChange != null) {
            altitudeGainMeters += altitudeChange.getAltitudeGainMeters();

            altitudeLossMeters += altitudeChange.getAltitudeLossMeters();

            lastAcceptedSensorValue = altitudeChange.currentSensorValue();
        }

        lastSeenSensorValue = currentSensorValue;

        Log.v(TAG, "altitude gain: " + altitudeGainMeters + ", altitude loss: " + altitudeLossMeters);
    }
}
