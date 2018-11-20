package com.wopin.qingpaopao.model;

import com.google.gson.Gson;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyCommentModel {

    public Observable<MyCommentsRsp> getMyComments(int page, int number) {
        return HttpClient.getApiInterface()
                .getMyComments(page, number)
                .map(new Function<ResponseBody, MyCommentsRsp>() {
                    @Override
                    public MyCommentsRsp apply(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject result = jsonObject.optJSONObject("result");
                        if (result != null) {
                            MyCommentsRsp myCommentsRsp = new Gson().fromJson(string, MyCommentsRsp.class);

                            //整理replyMe和relatedPost
                            List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> commentsReplyMe = myCommentsRsp.getResult().getCommentsReplyMe();
                            TreeMap<Integer, List<MyCommentsRsp.ResultBean.CommentsReplyMeBean>> replyMeCommentMap = new TreeMap<>();
                            for (MyCommentsRsp.ResultBean.CommentsReplyMeBean commentsReplyMeBean : commentsReplyMe) {
                                List<MyCommentsRsp.ResultBean.CommentsReplyMeBean> commentsReplyMeBeans = replyMeCommentMap.get(commentsReplyMeBean.getParent());
                                if (commentsReplyMeBeans == null) {
                                    commentsReplyMeBeans = new ArrayList<>();
                                }
                                commentsReplyMeBeans.add(commentsReplyMeBean);
                                replyMeCommentMap.put(commentsReplyMeBean.getParent(), commentsReplyMeBeans);
                            }

                            List<MyCommentsRsp.ResultBean.RelatedPostsBean> relatedPosts = myCommentsRsp.getResult().getRelatedPosts();
                            TreeMap<Integer, MyCommentsRsp.ResultBean.RelatedPostsBean> postMap = new TreeMap<>();
                            for (MyCommentsRsp.ResultBean.RelatedPostsBean relatedPostsBean : relatedPosts) {
                                postMap.put(relatedPostsBean.getId(), relatedPostsBean);
                            }

                            //把replyMe和relatedPost扔到comments里面，并剔除getParent > 0的comments
                            List<MyCommentsRsp.ResultBean.CommentsBean> comments = myCommentsRsp.getResult().getComments();
                            for (int i = comments.size() - 1; i >= 0; i--) {
                                MyCommentsRsp.ResultBean.CommentsBean commentsBean = comments.get(i);
                                if (commentsBean.getParent() > 0) {
                                    comments.remove(i);
                                } else {
                                    commentsBean.setCommentReplys(replyMeCommentMap.get(commentsBean.getId()));
                                    commentsBean.setRelatedPostsBean(postMap.get(commentsBean.getPost()));
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
