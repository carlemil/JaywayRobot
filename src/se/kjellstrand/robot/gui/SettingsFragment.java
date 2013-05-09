package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Inflates and shows the settings layout. Also handles updating of the settings
 * view and callback to the ClockService to notify it about changes in the
 * settings.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        prefs.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
    }

    /**
     * Listens for changes in SharedPreference, handles updates of the ui and
     * callback to the ControlPanel.
     */
    OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Activity activity = getActivity();
            if (activity != null) {
                // Handle updates of the Charset setting.
                String prefCharsetForDigitsKey = activity.getResources().getString(R.string.pref_languages_key);
                if (key.equals(prefCharsetForDigitsKey)) {
                    Preference languagesPref = findPreference(key);
                    String charset = sharedPreferences.getString(key,
                            getResources().getString(R.string.pref_languages_key));
                    String format = getResources().getString(R.string.pref_languages_summary);
                    // Set summary to be the user-description for the selected
                    // value
                    languagesPref.setSummary(String.format(format, charset));
                }
            }
        }
    };
}
