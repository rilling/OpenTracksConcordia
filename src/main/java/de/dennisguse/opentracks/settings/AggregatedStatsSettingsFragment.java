package de.dennisguse.opentracks.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.BuildConfig;
import de.dennisguse.opentracks.R;

public class AggregatedStatsSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_aggregated_stats);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_aggregated_stats);
        findPreference(getString(R.string.settings_aggregated_stats_key));
    }
}
