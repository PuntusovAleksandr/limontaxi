package digitalpromo.cabsdemo.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Class for phone validation
 */
public class PhoneUtils {
    private static final String TAG = PhoneUtils.class.getSimpleName();
    private static final int PHONE_DIGITS_COUNT = 12;
    private static final String COUNTRY_CODE = "38";

    /**
     * Replace all non digit characters from the string with phone number
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
}
