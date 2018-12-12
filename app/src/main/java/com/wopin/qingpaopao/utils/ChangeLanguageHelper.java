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
    public static final int CHANGE_LANGUAGE_DEFAULT = 0;

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
            case CHANGE_LANGUAGE_DEFAULT:
                int defaultLanguage = (int) SPUtils.get(MyApplication.getMyApplicationContext(), Constants.DEFAULT_LANGUAGE, 0);
                if (defaultLanguage == CHANGE_LANGUAGE_DEFAULT) {
                    defaultLanguage = Locale.getDefault().getLanguage().equals("zh") ? CHANGE_LANGUAGE_CHINA : CHANGE_LANGUAGE_ENGLISH;
                    SPUtils.put(MyApplication.getMyApplicationContext(), Constants.DEFAULT_LANGUAGE, defaultLanguage);
                }

                Locale defaultLocale;

                if (defaultLanguage == CHANGE_LANGUAGE_CHINA) {
                    defaultLocale = Locale.SIMPLIFIED_CHINESE;
                } else {
                    defaultLocale = Locale.ENGLISH;
                }

                config.locale = defaultLocale;         // 系统默认语言
                config.setLayoutDirection(defaultLocale);
                setAppLanguage(CHANGE_LANGUAGE_DEFAULT);
                break;
        }
        resources.updateConfiguration(config, dm);
    }

    private static void setAppLanguage(int language) {
        SPUtils.put(MyApplication.getMyApplicationContext(), Constants.LANGUAGE, language);
    }

    public static int getAppLanguage() {
        return (int) SPUtils.get(MyApplication.getMyApplicationContext(), Constants.LANGUAGE, CHANGE_LANGUAGE_DEFAULT);
    }
}
