package se.kjellstrand.robot.activitys;

import se.kjellstrand.robot.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Inflates and shows the settings layout. Also handles updating of the settings
 * view and callback to the ClockService to notify it about changes in the
 * settings.
 */
public class SettingsFragment extends PreferenceFragment {

    /**
     * Listens for changes in SharedPreference, handles updates of the ui.
     */
    OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Activity activity = getActivity();
            if (activity != null) {
                updatePrefMenuAlt(sharedPreferences, key, activity,
                        R.string.pref_languages_key, R.string.pref_languages_summary);
                updatePrefMenuAlt(sharedPreferences, key, activity,
                        R.string.pref_room_width_key, R.string.pref_room_width_summary);
                updatePrefMenuAlt(sharedPreferences, key, activity,
                        R.string.pref_room_length_key, R.string.pref_room_length_summary);
                updatePrefMenuAlt(sharedPreferences, key, activity,
                        R.string.pref_start_X_key, R.string.pref_start_X_summary);
                updatePrefMenuAlt(sharedPreferences, key, activity,
                        R.string.pref_start_Y_key, R.string.pref_start_Y_summary);
            }
        }
    };

    private void updatePrefMenuAlt(SharedPreferences sharedPreferences, String key, Activity activity,
            int updateKey, int summaryId) {
        if (key.equals(activity.getResources().getString(updateKey))) {
            Preference pref = findPreference(key);
            String value = sharedPreferences.getString(key,
                    getResources().getString(updateKey));
            String format = getResources().getString(summaryId);
            // Set summary to be the user-description for the selected value
            pref.setSummary(String.format(format, value));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        String key = getActivity().getResources().getString(R.string.pref_room_width_key);
        updatePrefMenuAlt(sharedPreferences, key, getActivity(),
                R.string.pref_room_width_key, R.string.pref_room_width_summary);

        key = getActivity().getResources().getString(R.string.pref_room_length_key);
        updatePrefMenuAlt(sharedPreferences, key, getActivity(),
                R.string.pref_room_length_key, R.string.pref_room_length_summary);

        key = getActivity().getResources().getString(R.string.pref_start_X_key);
        updatePrefMenuAlt(sharedPreferences, key, getActivity(),
                R.string.pref_start_X_key, R.string.pref_start_X_summary);

        key = getActivity().getResources().getString(R.string.pref_start_Y_key);
        updatePrefMenuAlt(sharedPreferences, key, getActivity(),
                R.string.pref_start_Y_key, R.string.pref_start_Y_summary);

    }
}
