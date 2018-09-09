package com.wopin.qingpaopao.http;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by bigbang on 2018/3/7.
 */

public class NotJsonResponseConverterFactory extends Converter.Factory {

    public static NotJsonResponseConverterFactory create() {
        return new NotJsonResponseConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type.toString().equals(String.class.toString())) {
            return new NotJsonResponseBodyConverter<>();
        } else {
            return null;
        }
    }


    class NotJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        @Override
        public T convert(ResponseBody value) throws IOException {
            String body = value.string();
            return (T) body;
        }
    }
}
