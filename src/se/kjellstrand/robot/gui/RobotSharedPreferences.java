package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.R;
import se.kjellstrand.robot.engine.Language;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Convenience class for handling the persistence of some settings.
 * 
 */
public class RobotSharedPreferences {

    /**
     * Key used by SharedPreferences as well as by the savedInstanceState of
     * fragments/activities to store the program of the current robot.
     */
    public static final String PROGRAM_KEY = "program";

    /**
     * Key used by SharedPreferences as well as by the savedInstanceState of
     * fragments/activities to store the language of the current robot.
     */
    public static final String LANGUAGE_KEY = "language";

    private static SharedPreferences mPrefs;

    public static int getRoomWidth(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_room_width_key), "5"));
    }

    public static int getRoomLength(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_room_length_key), "5"));
    }

    public static int getRobotStartX(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_start_X_key), "1"));
    }

    public static int getRobotStartY(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_start_Y_key), "1"));
    }

    /**
     * Fetch a stored language.
     * 
     * @param context of the activity
     * 
     * @return the language.
     */
    public static Language getLanguage(Context context) {
        String languagePrefKey = context.getString(R.string.pref_languages_key);
        String lang = getPrefs(context).getString(languagePrefKey, null);
        if (lang != null && lang.equals(context.getString(R.string.swedish))) {
            return Language.SWEDISH;
        } else {
            return Language.ENGLISH;
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mPrefs;
    }

    /**
     * Fetch a stored program.
     * 
     * @param context of the activity
     * 
     * @return the program.
     */
    public static String getProgram(Context context) {
        return getPrefs(context).getString(PROGRAM_KEY, null);
    }

    /**
     * 
     * @param context of the activity
     * @param language the language.
     */
    public static void putLanguage(Context context, Language language) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(LANGUAGE_KEY, language.ordinal());
        editor.commit();
    }

    /**
     * Store a program.
     * 
     * @param context of the activity
     * @param program to be stored.
     */
    public static void putProgram(Context context, String program) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(PROGRAM_KEY, program);
        editor.commit();
    }

}
