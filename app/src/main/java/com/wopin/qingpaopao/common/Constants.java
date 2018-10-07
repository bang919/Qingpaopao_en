package com.wopin.qingpaopao.common;

import com.wopin.qingpaopao.R;

/**
 * Created by Administrator on 2017/11/9.
 */

public class Constants {

    public final static int NULL_COLOR = -1;//状态栏不需要颜色的常量

    public final static String WECHAT_APPID = "wxf42ec50449767feb";


    public static final String WIFI = "WIFI";
    public static final String BLE = "BLE";

    //SP
    public final static String LOGIN_REQUEST = "login request";
    public final static String THIRD_REQUEST = "third request";
    public final static String LOGIN_BEAN = "login bean";
    public final static String USERNAME = "username";
    public final static String SIGN_IN_DATA = "sign in data";
    public final static String DRINKING_NOTIFICATION = "drinking notification";


    //定时提醒喝水
    public final static String AlarmNotificationReceiver_0730 = "AlarmNotificationReceiver_0730";
    public final static String AlarmNotificationReceiver_0900 = "AlarmNotificationReceiver_0900";
    public final static String AlarmNotificationReceiver_1100 = "AlarmNotificationReceiver_1100";
    public final static String AlarmNotificationReceiver_1300 = "AlarmNotificationReceiver_1300";
    public final static String AlarmNotificationReceiver_1500 = "AlarmNotificationReceiver_1500";
    public final static String AlarmNotificationReceiver_1730 = "AlarmNotificationReceiver_1730";
    public final static String AlarmNotificationReceiver_2000 = "AlarmNotificationReceiver_2000";
    public final static String AlarmNotificationReceiver_2200 = "AlarmNotificationReceiver_2200";

    public final static String[] AlarmNotificationReceivers = {
            AlarmNotificationReceiver_0730,
            AlarmNotificationReceiver_0900,
            AlarmNotificationReceiver_1100,
            AlarmNotificationReceiver_1300,
            AlarmNotificationReceiver_1500,
            AlarmNotificationReceiver_1730,
            AlarmNotificationReceiver_2000,
            AlarmNotificationReceiver_2200
    };

    //杯子颜色
    public final static int[] CUP_COLOR_INT = {0, 1, 2, 3, 4, 5, 6};
    public final static String[] CUP_COLOR_NAME = {"--", "土豪金", "玫瑰金", "宝石蓝", "中国红", "草木绿", "曜石黑"};
    public final static int[] CUP_COLOR_PIC_SRC = {R.mipmap.pc_cup, R.mipmap.cup_color_1, R.mipmap.cup_color_2, R.mipmap.cup_color_3, R.mipmap.cup_color_4, R.mipmap.cup_color_5, R.mipmap.cup_color_6};
}
