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

    public static int getDaysDifference(String targetDay) throws ParseException {
        //2018-12-28T00:00:00
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long targetTime = simpleDateFormat.parse(targetDay).getTime();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        long todayTime = simpleDateFormat.parse(today).getTime();
        int days = (int) ((targetTime - todayTime) / 1000 / 60 / 60 / 24);
        return Math.max(0, days);
    }
}
