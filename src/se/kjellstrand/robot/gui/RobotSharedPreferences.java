package se.kjellstrand.robot.gui;

import android.content.Context;
import android.content.SharedPreferences;

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

    private static final String PREFS_NAME = "RobotSharedPreferencesFile";

    private static SharedPreferences mPrefs;
    
    /**
     * Store a program.
     * 
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
     * @param context the 
     * 
     * @return the program.
     */
    public static String getProgram(Context context) {
        return getPrefs(context).getString(PROGRAM_KEY, null);
    }

    private static SharedPreferences getPrefs(Context context) {
        if (mPrefs == null) {
            mPrefs = context.getSharedPreferences(PREFS_NAME, 0);
        }
        return mPrefs;
    }

}
