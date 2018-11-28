package com.wopin.qingpaopao.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wopin.qingpaopao.activity.SplashActivity;

import java.util.List;

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断app进程是否存活
        if (getAppSatus(context, "com.mywopin.com.cup.point")) {
            //目前已经打开，不动声色
//            intent.setClass(context, MainActivity.class);
//            context.startActivity(intent);
        } else {
            intent.setClass(context, SplashActivity.class);
            context.startActivity(intent);
        }
    }

    public boolean getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return true;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return true;
                }
            }
            return false;//栈里找不到，返回3
        }
    }
}
