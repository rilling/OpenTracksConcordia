package de.dennisguse.opentracks.settings;

import de.dennisguse.opentracks.R;

public enum WeightUnit {
    POUND(R.string.unit_pound),
    KILOGRAM(R.string.unit_kilogram);

    private final int preferenceIdentifier;

    WeightUnit(int preferenceIdentifier) {
        this.preferenceIdentifier = preferenceIdentifier;
    }

    public int getPreferenceId() {
        return preferenceIdentifier;
    }

    // If you need a default value, you can define it here
    public static WeightUnit defaultWeightUnit() {
        return KILOGRAM;
    }
}
