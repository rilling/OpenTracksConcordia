package de.dennisguse.opentracks.sensors;

import android.hardware.SensorManager;

import androidx.annotation.VisibleForTesting;

import de.dennisguse.opentracks.data.models.AtmosphericPressure;

public class PressureSensorUtils {

    //Everything above is considered a meaningful change in altitude.
    private static final float ALTITUDE_CHANGE_DIFF_M = 3.0f;

    private static final float EXPONENTIAL_SMOOTHING = 0.3f;

    private static final float p0 = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

    private PressureSensorUtils() {
    }

    public record AltitudeChange(AtmosphericPressure currentSensorValue, float altitudeChangeMeters) {

        public float getAltitudeGainMeters() {
            return altitudeChangeMeters > 0 ? altitudeChangeMeters : 0;
        }

        public float getAltitudeLossMeters() {
            return altitudeChangeMeters < 0 ? -1 * altitudeChangeMeters : 0;
        }
    }

    /**
     * Applies exponential smoothing to sensor value before computation.
     */
    public static AltitudeChange computeChangesWithSmoothingMeters(AtmosphericPressure lastAcceptedSensorValue, AtmosphericPressure lastSeenSensorValue, AtmosphericPressure currentSensorValue) {
        AtmosphericPressure nextSensorValue = AtmosphericPressure.ofHPA(EXPONENTIAL_SMOOTHING * currentSensorValue.getHPA() + (1 - EXPONENTIAL_SMOOTHING) * lastSeenSensorValue.getHPA());

        return computeChanges(lastAcceptedSensorValue, nextSensorValue);
    }

    @VisibleForTesting
    public static AltitudeChange computeChanges(AtmosphericPressure lastAcceptedSensorValue, AtmosphericPressure currentSensorValue) {
        float lastSensorValueMeters = SensorManager.getAltitude(p0, lastAcceptedSensorValue.getHPA());
        float currentSensorValueMeters = SensorManager.getAltitude(p0, currentSensorValue.getHPA());

        float altitudeChangeMeters = currentSensorValueMeters - lastSensorValueMeters;
        if (Math.abs(altitudeChangeMeters) < ALTITUDE_CHANGE_DIFF_M) {
            return null;
        }

        // Limit altitudeC change by ALTITUDE_CHANGE_DIFF and computes pressure value accordingly.
        AltitudeChange altitudeChange = new AltitudeChange(currentSensorValue, altitudeChangeMeters);
        if (altitudeChange.altitudeChangeMeters() > 0) {
            return new AltitudeChange(getBarometricPressure(lastSensorValueMeters + ALTITUDE_CHANGE_DIFF_M), ALTITUDE_CHANGE_DIFF_M);
        } else {
            return new AltitudeChange(getBarometricPressure(lastSensorValueMeters - ALTITUDE_CHANGE_DIFF_M), -1 * ALTITUDE_CHANGE_DIFF_M);
        }
    }

    /*
     * Barometeric pressure to altitude estimation; inverts of SensorManager.getAltitude(float, float)
     * https://de.wikipedia.org/wiki/Barometrische_H%C3%B6henformel#Internationale_H%C3%B6henformel
     * {\color{White} p(h)} = p_0 \cdot \left( 1 - \frac{0{,}0065 \frac{\mathrm K}{\mathrm m} \cdot h}{T_0\ } \right)^{5{,}255}
     */
    @VisibleForTesting
    public static AtmosphericPressure getBarometricPressure(float altitudeMeters) {
        return AtmosphericPressure.ofHPA((float) (p0 * Math.pow(1.0 - 0.0065 * altitudeMeters / 288.15, 5.255f)));
    }

    @VisibleForTesting
    public static AtmosphericPressure calibrateBarometricPressure(float altitudeMeters) {
        return AtmosphericPressure.ofHPA((float) (p0 * Math.pow(1.0 - 0.0065 * altitudeMeters / 288.15, 5.255f)));
    }
}
