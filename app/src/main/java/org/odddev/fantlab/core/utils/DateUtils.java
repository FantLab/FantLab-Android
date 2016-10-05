package org.odddev.fantlab.core.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author kenrube
 * @date 05.10.16
 */

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    public static Calendar stringToCalendar(String date, String format) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            calendar.setTime(sdf.parse(date));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
