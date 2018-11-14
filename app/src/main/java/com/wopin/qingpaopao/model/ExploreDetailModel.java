package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.SendCommentReq;
import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.FollowListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.ApiInterface;
import com.wopin.qingpaopao.http.HttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ExploreDetailModel {

    public Observable<CommentRsp> getComments(String exploreid) {
        return HttpClient.getApiInterface().getComments(exploreid)
                .map(new Function<CommentRsp, CommentRsp>() {
                    @Override
                    public CommentRsp apply(CommentRsp commentRsp) throws Exception {
                        ArrayList<Integer> mapList = new ArrayList<>();
                        ArrayList<CommentRsp.CommentBean> newComments = new ArrayList<>();
                        ArrayList<CommentRsp.CommentBean> comments = commentRsp.getComments();
                        for (int i = comments.size() - 1; i >= 0; i--) {
                            CommentRsp.CommentBean commentBean = comments.get(i);
                            int parent = commentBean.getParent();
                            if (parent == 0) {
                                mapList.add(0, commentBean.getId());
                                newComments.add(0, commentBean);
                            } else {
                                int index = mapList.indexOf(parent);
                                if (index != -1) {
                                    ArrayList<CommentRsp.CommentBean> followComment = newComments.get(index).getFollowComment();
                                    if (followComment == null) {
                                        followComment = new ArrayList<>();
                                    }
                                    followComment.add(commentBean);
                                    newComments.get(index).setFollowComment(followComment);
                                }
                            }
                        }
                        Collections.sort(newComments, new Comparator<CommentRsp.CommentBean>() {
                            @Override
                            public int compare(CommentRsp.CommentBean o1, CommentRsp.CommentBean o2) {
                                return o2.getLikes() - o1.getLikes();
                            }
                        });
                        commentRsp.setComments(newComments);
                        return commentRsp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> sendComment(int postId, String content, int parentCommentId) {
        SendCommentReq sendCommentReq = new SendCommentReq();
        sendCommentReq.setPostId(postId);
        sendCommentReq.setContent(content);
        sendCommentReq.setParent(parentCommentId);
        return HttpClient.getApiInterface().sendComment(sendCommentReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FollowListRsp> getMyFollowList() {
        return HttpClient.getApiInterface().getMyFollowList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FollowListRsp> getMyFansList() {
        return HttpClient.getApiInterface().getMyFansList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> setFollowAuthor(String authorId, boolean isFollow) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"userId\":\"" + authorId + "\"}");
        ApiInterface apiInterface = HttpClient.getApiInterface();
        Observable<NormalRsp> observable;
        if (isFollow) {
            observable = apiInterface.followAuthor(requestBody);
        } else {
            observable = apiInterface.unFollowAuthor(requestBody);
        }
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> setLikeBlogComment(String blogId, boolean isLike) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"id\":\"" + blogId + "\"}");
        ApiInterface apiInterface = HttpClient.getApiInterface();
        Observable<NormalRsp> observable;
        if (isLike) {
            observable = apiInterface.likeBlogComment(requestBody);
        } else {
            observable = apiInterface.unlikeBlogComment(requestBody);
        }
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> setCollectBlogPost(String blogId, boolean isCollect) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"postId\":\"" + blogId + "\"}");
        ApiInterface apiInterface = HttpClient.getApiInterface();
        Observable<NormalRsp> observable;
        if (isCollect) {
            observable = apiInterface.collectBlogPost(requestBody);
        } else {
            observable = apiInterface.uncollectBlogPost(requestBody);
        }
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> setLikeBlogPost(String blogId, boolean isLike) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"postId\":\"" + blogId + "\"}");
        ApiInterface apiInterface = HttpClient.getApiInterface();
        Observable<NormalRsp> observable;
        if (isLike) {
            observable = apiInterface.likeBlogPost(requestBody);
        } else {
            observable = apiInterface.unlikeBlogPost(requestBody);
        }
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> deleteMyBlog(String blogId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"postId\":\"" + blogId + "\"}");
        return HttpClient.getApiInterface().deleteMyBlog(requestBody)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
