package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.ExploreDetailModel;
import com.wopin.qingpaopao.view.ExploreDetailView;

public class ExploreDetailPresenter extends BasePresenter<ExploreDetailView> {

    private ExploreDetailModel mExploreDetailModel;

    public ExploreDetailPresenter(Context context, ExploreDetailView view) {
        super(context, view);
        mExploreDetailModel = new ExploreDetailModel();
    }

    public void getComments(String exploreid) {
        subscribeNetworkTask(getClass().getSimpleName().concat("getComments"), mExploreDetailModel.getComments(exploreid), new MyObserver<CommentRsp>() {
            @Override
            public void onMyNext(CommentRsp commentRsp) {
                mView.onComments(commentRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void sendComment(final int postId, String content, int parentCommentId) {
        subscribeNetworkTask(getClass().getSimpleName().concat("sendComment"), mExploreDetailModel.sendComment(postId, content, parentCommentId), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                getComments(String.valueOf(postId));
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
