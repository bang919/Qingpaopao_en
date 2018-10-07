package com.wopin.qingpaopao.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.receiver.AlarmNotificationReceiver;

public class AlarmNotificationService extends Service {

    private static final String TAG = "test";
    private AlarmManager mAlarmManager;

    public AlarmNotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //设置广播
        if (intent != null) {
            for (String key : Constants.AlarmNotificationReceivers) {
                setAlarmManager(intent, key);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void setAlarmManager(Intent intent, String key) {
        Intent intent2 = new Intent(this, AlarmNotificationReceiver.class);
        intent2.setAction(key);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
        long longExtra = intent.getLongExtra(key, 0);
        if (longExtra != 0) {
            //根据不同的版本使用不同的设置方法
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
//        } else {
//            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
//        }
            //每天重复
            long l = System.currentTimeMillis();
            if (l > longExtra) {//今天时间已经过了，明天开始算
                longExtra += 1000 * 60 * 60 * 24;
            }
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, longExtra, 1000 * 60 * 60 * 24, pendingIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: 服务被杀死");
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
