package de.dennisguse.opentracks.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.BuildConfig;
import de.dennisguse.opentracks.R;

public class UserProfileSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.user_profile_settings);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_api_title);
        findPreference(getString(R.string.publicapi_package_key))
                .setSummary(BuildConfig.APPLICATION_ID);
    }
}
