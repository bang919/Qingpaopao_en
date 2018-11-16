package com.wopin.qingpaopao.model;

import com.google.gson.Gson;
import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.http.HttpClient;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MessageMainModel {

    public Observable<SystemMessageRsp> getSystemMessage() {
        return HttpClient.getApiInterface().getSystemMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewCommentRsp> getNewCommentNotify() {
        return HttpClient.getApiInterface()
                .getNewBlogMessage()
                .map(new Function<ResponseBody, NewCommentRsp>() {
                    @Override
                    public NewCommentRsp apply(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject result = jsonObject.optJSONObject("result");
                        Gson gson = new Gson();
                        if (result != null) {
                            return gson.fromJson(string, NewCommentRsp.class);
                        }
                        return new NewCommentRsp();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
