package de.dennisguse.opentracks.settings;

import de.dennisguse.opentracks.R;

public enum TemperatureUnit {
    CELSIUS(R.string.unit_celsius),
    FAHRENHEIT(R.string.unit_fahrenheit);

    private final int preferenceIdentifier;

    TemperatureUnit(int preferenceIdentifier) {
        this.preferenceIdentifier = preferenceIdentifier;
    }

    public int getPreferenceId() {
        return preferenceIdentifier;
    }

    // If you need a default value, you can define it here
    public static TemperatureUnit defaultTemperatureUnit() {
        return CELSIUS;
    }
}
