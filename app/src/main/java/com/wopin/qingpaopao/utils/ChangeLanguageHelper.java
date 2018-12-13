package com.wopin.qingpaopao.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;

import java.util.Locale;

public class ChangeLanguageHelper {

    public static final int CHANGE_LANGUAGE_CHINA = 1;
    public static final int CHANGE_LANGUAGE_ENGLISH = 2;

    public static void changeLanguage(Resources resources, int language) {

        Configuration config = resources.getConfiguration();     // 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case CHANGE_LANGUAGE_CHINA:
                config.locale = Locale.SIMPLIFIED_CHINESE;     // 中文
                config.setLayoutDirection(Locale.SIMPLIFIED_CHINESE);
                setAppLanguage(CHANGE_LANGUAGE_CHINA);
                break;
            case CHANGE_LANGUAGE_ENGLISH:
                config.locale = Locale.ENGLISH;   // 英文
                config.setLayoutDirection(Locale.ENGLISH);
                setAppLanguage(CHANGE_LANGUAGE_ENGLISH);
                break;
        }
        resources.updateConfiguration(config, dm);
    }

    private static void setAppLanguage(int language) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.LANGUAGE, language);
    }

    public static int getAppLanguage() {
        int language = (int) SPUtils.get(MyApplication.getMyApplicationContext(), Constants.LANGUAGE, 0);
        if (language == 0) {
            language = Locale.getDefault().getLanguage().equals("zh") ? ChangeLanguageHelper.CHANGE_LANGUAGE_CHINA : ChangeLanguageHelper.CHANGE_LANGUAGE_ENGLISH;
        }
        return language;
    }
}
