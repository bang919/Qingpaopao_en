package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.SendCommentReq;
import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
}
