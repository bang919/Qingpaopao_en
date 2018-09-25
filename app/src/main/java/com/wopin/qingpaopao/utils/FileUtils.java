package com.wopin.qingpaopao.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by bigbang on 2018/3/29.
 */

public class FileUtils {

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static void saveResponseBody(ResponseBody responseBody, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        byte[] fileReader = new byte[4096];

        InputStream inputStream = responseBody.byteStream();
        OutputStream outputStream = new FileOutputStream(file);

        while (true) {
            int read = inputStream.read(fileReader);

            if (read == -1) {
                break;
            }
            outputStream.write(fileReader, 0, read);
        }
        outputStream.flush();
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
