package com.wopin.qingpaopao.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.wopin.qingpaopao.R;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "qingpaopao_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mainIntent = new Intent(context, NotificationClickReceiver.class);
        mainIntent.setAction("NotificationClickReceiver");
        PendingIntent mainPendingIntent = PendingIntent.getBroadcast(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrates = {0, 1000, 1000, 1000};
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(context.getColor(R.color.colorGreen));
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        String action = intent.getAction();
        int bodyRes = R.string.AlarmNotificationReceiver_0730;
        switch (action) {
            case "AlarmNotificationReceiver_0730":
                bodyRes = R.string.AlarmNotificationReceiver_0730;
                break;
            case "AlarmNotificationReceiver_0900":
                bodyRes = R.string.AlarmNotificationReceiver_0900;
                break;
            case "AlarmNotificationReceiver_1100":
                bodyRes = R.string.AlarmNotificationReceiver_1100;
                break;
            case "AlarmNotificationReceiver_1300":
                bodyRes = R.string.AlarmNotificationReceiver_1300;
                break;
            case "AlarmNotificationReceiver_1500":
                bodyRes = R.string.AlarmNotificationReceiver_1500;
                break;
            case "AlarmNotificationReceiver_1730":
                bodyRes = R.string.AlarmNotificationReceiver_1730;
                break;
            case "AlarmNotificationReceiver_2000":
                bodyRes = R.string.AlarmNotificationReceiver_2000;
                break;
            case "AlarmNotificationReceiver_2200":
                bodyRes = R.string.AlarmNotificationReceiver_2200;
                break;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                //设置通知标题
                .setContentTitle("氢泡泡饮水提醒")
                //设置通知内容
                .setContentText(context.getString(bodyRes))
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .setSound(defaultSoundUri)
                .setVibrate(vibrates);
        //设置小图标
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.i_tran_icon);
            builder.setColor(Color.BLUE);
        } else {
            builder.setSmallIcon(R.mipmap.launch_icon);
        }
        Notification notification = builder.build();
        notificationManager.notify(123, notification);
    }
}
