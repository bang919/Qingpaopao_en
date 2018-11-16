package com.wopin.qingpaopao.model;

import com.google.gson.Gson;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyCommentModel {

    public Observable<MyCommentsRsp> getMyComments() {
        return HttpClient.getApiInterface()
                .getMyComments(1, 99)
                .map(new Function<ResponseBody, MyCommentsRsp>() {
                    @Override
                    public MyCommentsRsp apply(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject result = jsonObject.optJSONObject("result");
                        if (result != null) {
                            MyCommentsRsp myCommentsRsp = new Gson().fromJson(string, MyCommentsRsp.class);
                            List<MyCommentsRsp.ResultBean.CommentsBean> comments = myCommentsRsp.getResult().getComments();
                            for (int i = comments.size() - 1; i >= 0; i--) {
                                MyCommentsRsp.ResultBean.CommentsBean commentsBean = comments.get(i);
                                if (commentsBean.getParent() > 0) {
                                    comments.remove(i);
                                }
                            }
                            myCommentsRsp.getResult().setComments(comments);
                            return myCommentsRsp;
                        }
                        return new MyCommentsRsp();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> deleteComment(int CommentId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"commentId\":\"" + CommentId + "\"}");
        return HttpClient.getApiInterface()
                .deleteComment(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
