package se.kjellstrand.robot.gui;

import se.kjellstrand.robot.engine.Language;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * 
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

    private static final String PREFS_NAME = "RobotSharedPreferencesFile";

    private static SharedPreferences mPrefs;

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
        Log.d("TAG", "Language " + language.toString());

        editor.commit();
    }

    /**
     * Fetch a stored language.
     * 
     * @param context of the activity
     * 
     * @return the language.
     */
    public static Language getLanguage(Context context) {
        Language language = Language.values()[getPrefs(context).getInt(LANGUAGE_KEY, 0)];
        Log.d("TAG", "Language " + language.toString());
        return language;
    }

    private static SharedPreferences getPrefs(Context context) {
        if (mPrefs == null) {
            mPrefs = context.getSharedPreferences(PREFS_NAME, 0);
        }
        return mPrefs;
    }

}
