package digitalpromo.cabsdemo.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import digitalpromo.cabsdemo.App;

/**
 * Manager for shared preferences
 */
public class SharedPreferencesManager {
    private static final String TAG = SharedPreferencesManager.class.getSimpleName();
    private static final String PKG_KEY_PREFIX = SharedPreferencesManager.class.getPackage().getName();

    private static SharedPreferencesManager ourInstance = new SharedPreferencesManager();

    private static final String KEY_USER_LOGIN = PKG_KEY_PREFIX + ".user_login";
    private static final String KEY_USER_PASSWORD = PKG_KEY_PREFIX + ".user_password";
    private static final String KEY_AUTO_LOGIN = PKG_KEY_PREFIX + ".auto_login";
    private static final String KEY_IS_USER_LOGGED_IN = PKG_KEY_PREFIX + ".is_user_logged_in";

    public static final String DEF_AUTH_VALUE = "";

    /**
     * Instance of SharedPreferences object
     */
    private SharedPreferences sPref;

    /**
     * Editor of SharedPreferences object
     */
    private SharedPreferences.Editor editor;

    /**
     * Construct the instance of the object
     */
    public SharedPreferencesManager() {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        editor = sPref.edit();
    }

    /**
     * Get instance of SharedPreferencesManager object
     * @return instance of SharedPreferencesManager object
     */
    public static SharedPreferencesManager getInstance() {
        return ourInstance;
    }

    /**
     * Save user's login in shared preferences
     * @param userLogin user's login (same as user's phone)
     */
    public void saveUserLogin(@NonNull String userLogin) {
        Log.d(TAG, "saveUserLogin() called with: " + "userLogin = [" + userLogin + "]");
        editor.putString(KEY_USER_LOGIN, userLogin).commit();
    }

    /**
     * Load user's login from shared preferences
     * @return user's login
     */
    public String loadUserLogin() {
        return sPref.getString(KEY_USER_LOGIN, DEF_AUTH_VALUE);
    }

    /**
     * Save user's password in shared preferences
     * @param userPassword user's password
     */
    public void saveUserPassword(@NonNull String userPassword) {
        Log.d(TAG, "saveUserPassword() called with: " + "userPassword = [" + userPassword + "]");
        editor.putString(KEY_USER_PASSWORD, userPassword).commit();
    }

    /**
     * Load user's password from shared preferences
     * @return user's password
     */
    public String loadUserPassword() {
        return sPref.getString(KEY_USER_PASSWORD, DEF_AUTH_VALUE);
    }

    /**
     * Save auto login flag
     * @param isAutoLoginEnabled true - if need to perform auto login, false - otherwise
     */
    public void saveAutoLoginFlag(boolean isAutoLoginEnabled) {
        editor.putBoolean(KEY_AUTO_LOGIN, isAutoLoginEnabled).commit();
    }

    /**
     * Load auto login flag
     * @return auto login flag
     */
    public boolean loadAutoLoginFlag() {
        return sPref.getBoolean(KEY_AUTO_LOGIN, false);
    }

    /**
     * Save user's logged in state
     * @param isUserLoggedIn true - user logged in, false - otherwise
     */
    public void saveUserLoggedInFlag(boolean isUserLoggedIn) {
        editor.putBoolean(KEY_IS_USER_LOGGED_IN, isUserLoggedIn).commit();
    }

    /**
     * Check whether user logged in or not
     * @return true - user logged in, false - otherwise
     */
    public boolean isUserLoggedIn() {
        return sPref.getBoolean(KEY_IS_USER_LOGGED_IN, false);
    }
}
