package SETTINGS;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AleksandrP on 17.08.2016.
 */
public class SettingsApp {

    /**
     * The constant FILE_NAME.
     */
// Settings xml file name
    public static final String FILE_NAME = "settings";

    // Keys for opening settings from xml file
    private static final String KEY_EXCEPTION_HISTORY = "exception_history";


    /**
     * get all id historyItem then exception in list
     * @param preferences
     * @return
     */
    public static Set<String> getListException(SharedPreferences preferences) {
        return preferences.getStringSet(KEY_EXCEPTION_HISTORY, new HashSet<String>());
    }

    /**
     * set all id historyItem then exception in list
     * @param preferences
     * @return
     */
    public static void setListException(Set<String> mException, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_EXCEPTION_HISTORY, mException);
        editor.commit();
    }
}
