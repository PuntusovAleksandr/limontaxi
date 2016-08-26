package taxi.lemon.utils;

import android.util.Log;

/**
 * Class for phone validation
 */
public class PhoneUtils {
    private static final String TAG = PhoneUtils.class.getSimpleName();
    private static final int PHONE_DIGITS_COUNT = 12;
    private static final int PASS_DIGITS_COUNT = 6;
    private static final String COUNTRY_CODE = "38";

    /**
     * Replace all non digit characters from the string with phone number
     *
     * @param phone phone number
     * @return string without non digit characters
     */
    public static String replaceNonDigitCharacters(String phone) {
        return phone.replaceAll("\\D", "");
    }

    public static boolean isPhoneValid(String phoneNumber) {
        Log.d(TAG, "isPhoneValid() called with: " + "phoneNumber = [" + phoneNumber + "]");
        if (phoneNumber.length() != PHONE_DIGITS_COUNT) {
            return false;
        } else if (!phoneNumber.matches("^" + COUNTRY_CODE + ".*")) {
            return false;
        }
        return true;
    }

    /**
     * validation password
     *
     * @param mPassword
     * @return
     */
    public static boolean isPassValid(String mPassword) {
        Log.d(TAG, "isPassValid() called with: " + "mPassword = [" + mPassword + "]");
        String REGULAR_PASS = "(?=.*[A-z])(?=(.*)).{7,30}";
//        String REGULAR_PASS = "(?=(.*[0-9]))(?=.*[a-z])(?=(.*)).{6,30}";
//        if (mPassword.length() < PASS_DIGITS_COUNT) {
//            return false;
//        } else if (!mPassword.matches(REGULAR_PASS)) {
//            return false;
//        }
        return mPassword.matches(REGULAR_PASS);
    }
}
