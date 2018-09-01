package com.wopin.qingpaopao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormatUtils {
    public static String formatToTime(String date, SimpleDateFormat format) {
        try {
            SimpleDateFormat ymdHmsSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00", Locale.getDefault());
            Date parse = ymdHmsSDF.parse(date);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "error time format";
    }
}
