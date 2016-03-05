package com.njit.buddy.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author toyknight 11/20/2015.
 */
public class DateParser {

    public static String toString(long timestamp) {
        Date date = new Date(timestamp);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
