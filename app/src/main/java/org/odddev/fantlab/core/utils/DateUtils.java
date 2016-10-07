package org.odddev.fantlab.core.utils;

import android.text.TextUtils;

import java.util.Calendar;

/**
 * @author kenrube
 * @date 07.10.16
 */

public class DateUtils {

    private static final String DELIMITER = ".";
    private static final String TWO_DIGITS_FORMAT = "%2s";
    private static final String SPACE = " ";
    private static final String ZERO = "0";

    public static String valuesToString(int day, int month, int year) {
        String dayStr = String.format(TWO_DIGITS_FORMAT, day).replace(SPACE, ZERO);
        String monthStr = String.format(TWO_DIGITS_FORMAT, month).replace(SPACE, ZERO);
        String yearStr = String.valueOf(year);
        return TextUtils.join(DELIMITER, new String[] {dayStr, monthStr, yearStr});
    }

    public static Calendar stringToCalendar(String string) {
        return null;
    }
}
