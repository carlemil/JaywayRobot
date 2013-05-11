package se.kjellstrand.robot.settings;

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
     * Reference to the SharedPreferences used by this application.
     */
    private static SharedPreferences mPrefs;

    /**
     * Return the width of the last configured room.
     * 
     * @param context used for locking up resources.
     * @return the width of the last configured room.
     */
    public static int getRoomWidth(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_room_width_key),
                context.getString(R.string.default_room_width)));
    }

    /**
     * Return the length of the last configured room.
     * 
     * @param context used for locking up resources.
     * @return the length of the last configured room.
     */
    public static int getRoomLength(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_room_length_key),
                context.getString(R.string.default_room_length)));
    }

    /**
     * Return the x coordinate of the start position of the last configured
     * room.
     * 
     * @param context used for locking up resources.
     * @return the x coordinate of the start position of the last configured
     *         room.
     */
    public static int getRobotStartX(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_start_X_key),
                context.getString(R.string.default_start_X)));
    }

    /**
     * Return the y coordinate of the start position of the last configured
     * room.
     * 
     * @param context used for locking up resources.
     * @return the y coordinate of the start position of the last configured
     *         room.
     */
    public static int getRobotStartY(Context context) {
        return Integer.parseInt(getPrefs(context).getString(context.getString(R.string.pref_start_Y_key),
                context.getString(R.string.default_start_Y)));
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
        } else if (lang != null && lang.equals(context.getString(R.string.arrows))) {
            return Language.ARROWS;
        } else {
            return Language.ENGLISH;
        }
    }

    /**
     * Fetch a stored room shape.
     * 
     * @param context of the activity
     * 
     * @return the shape of the room.
     */
    public static int getRoomShape(Context context) {
        String roomShapePrefKey = context.getString(R.string.pref_room_shape_key);
        String shape = getPrefs(context).getString(roomShapePrefKey, null);
        if (shape != null && shape.equals(context.getString(R.string.circular))) {
            return R.string.circular;
        } else {
            return R.string.rectangular;
        }
    }

    /**
     * Fetch a stored program.
     * 
     * @param context of the activity
     * 
     * @return the program.
     */
    public static StringBuilder getProgram(Context context) {
        String program = getPrefs(context).getString(PROGRAM_KEY, null);
        StringBuilder sb = new StringBuilder();
        if (program != null) {
            sb.append(program);
        }
        return sb;
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

    private static SharedPreferences getPrefs(Context context) {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mPrefs;
    }

}
