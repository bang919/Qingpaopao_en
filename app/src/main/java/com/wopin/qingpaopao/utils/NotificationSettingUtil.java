package com.wopin.qingpaopao.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.receiver.AlarmNotificationReceiver;
import com.wopin.qingpaopao.server.AlarmNotificationService;

import java.util.Calendar;
import java.util.TimeZone;

public class NotificationSettingUtil {
    public static void startNotification(Context context) {

        Intent intent = new Intent(context, AlarmNotificationService.class);
        Calendar calendar = getCalendar();
        intent.putExtra(Constants.AlarmNotificationReceiver_0730, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_0900, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1100, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1300, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1500, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_1730, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_2000, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_2200, calendar.getTimeInMillis());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
//发送一条启动闹铃图标的广播
//        Intent intentIcon = new Intent("com.gaozhidong.android.Color");
//        intentIcon.putExtra("noteId", 0);
//        context.sendBroadcast(intentIcon);

        SPUtils.put(context, Constants.DRINKING_NOTIFICATION, true);
    }

    public static void stopNotification(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //取消警报
        for (String key : Constants.AlarmNotificationReceivers) {
            stopEachNotification(context, am, key);
        }

        SPUtils.put(context, Constants.DRINKING_NOTIFICATION, false);
    }

    private static void stopEachNotification(Context context, AlarmManager alarmManager, String key) {
        Intent intent = new Intent(context, AlarmNotificationReceiver.class);
        intent.setAction(key);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private static Calendar getCalendar() {
        //得到日历实例，主要是为了下面的获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        //设置在几分提醒  设置的为25分
        calendar.set(Calendar.MINUTE, 30);
        //下面这两个看字面意思也知道
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是13点25分的时间点

        //获取上面设置的13点25分的毫秒值
        long selectTime = calendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
//        if (systemTime > selectTime) {
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }

        return calendar;
    }
}
