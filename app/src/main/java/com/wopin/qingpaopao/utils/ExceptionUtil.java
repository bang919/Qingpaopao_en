package com.wopin.qingpaopao.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wopin.qingpaopao.bean.response.ErrorResponseBean;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/11/8.
 */

public class ExceptionUtil {

    private static Gson gson = new Gson();

    public static String getHttpExceptionMessage(Throwable t) {
        if (t instanceof HttpException) {
            ResponseBody body = ((HttpException) t).response().errorBody();
            try {
                ErrorResponseBean errorResBean = gson.fromJson(body.string(), ErrorResponseBean.class);
                return errorResBean.getMsg();
            } catch (Exception e) {
                e.printStackTrace();
                try {//直接返回body
                    if (!TextUtils.isEmpty(body.string())) {
                        return body.string();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return t.getLocalizedMessage();
    }
}
