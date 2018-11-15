package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.ExploreDetailModel;
import com.wopin.qingpaopao.model.MyCommentModel;
import com.wopin.qingpaopao.view.MyCommentView;

public class MyCommentPresenter extends BasePresenter<MyCommentView> {

    private MyCommentModel mMyCommentModel;

    public MyCommentPresenter(Context context, MyCommentView view) {
        super(context, view);
        mMyCommentModel = new MyCommentModel();
    }

    public void getMyComments() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getMyComments"), mMyCommentModel.getMyComments(), new MyObserver<MyCommentsRsp>() {
            @Override
            public void onMyNext(MyCommentsRsp myCommentsRsp) {
                mView.onMyCommentsRsp(myCommentsRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void sendComment(final int postId, String content, int parentCommentId) {
        ExploreDetailModel exploreDetailModel = new ExploreDetailModel();
        subscribeNetworkTask(getClass().getSimpleName().concat("sendComment"), exploreDetailModel.sendComment(postId, content, parentCommentId), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                getMyComments();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void deleteComment(int commentId) {
        subscribeNetworkTask(getClass().getSimpleName().concat("deleteComment"), mMyCommentModel.deleteComment(commentId), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                getMyComments();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
