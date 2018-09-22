package com.wopin.qingpaopao.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class StringUtils {
    public static String getRandomString2(int length) {
        //产生随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //循环length次
        for (int i = 0; i < length; i++) {
            //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                //如果number产生的是数字0；
                case 0:
                    //产生A-Z的ASCII码
                    result = Math.round(Math.random() * 25 + 65);
                    //将ASCII码转换成字符
                    sb.append(String.valueOf((char) result));
                    break;
                case 1:
                    //产生a-z的ASCII码
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char) result));
                    break;
                case 2:
                    //产生0-9的数字
                    sb.append(String.valueOf
                            (new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }

    public static String getAssetsTex(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
