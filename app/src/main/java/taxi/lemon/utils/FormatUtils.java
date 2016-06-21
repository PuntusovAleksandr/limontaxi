package taxi.lemon.utils;

import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import taxi.lemon.App;

/**
 * Hold methods to convert screen dimensions values
 */
public class FormatUtils {
    private static final SimpleDateFormat date = new SimpleDateFormat("yyyy:MM:dd");
    private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

    /**
     * Convert dp to pixel
     * @param dp value to convert
     * @return pixel value of dp
     */
    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = App.getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     * Convert pixel to dp
     * @param px value to convert
     * @return dp value of px
     */
    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = App.getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * Get string with formatted date
     * @param year year
     * @param month month
     * @param day day
     * @return formatted string
     */
    public static String getDateString(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return date.format(c.getTime());
    }

    /**
     * Get string with formatted time
     * @param hour hour
     * @param minute minute
     * @param sec second
     * @return formatted time
     */
    public static String getTimeString(int hour, int minute, int sec) {
        Calendar c = Calendar.getInstance();
        c.set(0, 0, 0, hour, minute, sec);
        return time.format(c.getTime());
    }

    public static String convertStringForPreorder(String toConvert) {
        return toConvert.replace(":", "-");
    }
}
