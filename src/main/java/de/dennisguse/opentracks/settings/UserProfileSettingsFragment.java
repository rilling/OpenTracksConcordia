package de.dennisguse.opentracks.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.BuildConfig;
import de.dennisguse.opentracks.R;

public class UserProfileSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_user_profile);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
