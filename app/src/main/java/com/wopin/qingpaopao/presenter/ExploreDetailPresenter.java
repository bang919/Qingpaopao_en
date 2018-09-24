package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.FollowListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.ExploreDetailModel;
import com.wopin.qingpaopao.view.ExploreDetailView;

import java.util.List;

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

    public void checkFollow(final String authorId) {
        subscribeNetworkTask(getClass().getSimpleName().concat("checkFollow"), mExploreDetailModel.getMyFollowList(), new MyObserver<FollowListRsp>() {
            @Override
            public void onMyNext(FollowListRsp followListRsp) {
                boolean isFollowed = false;
                List<FollowListRsp.MyFollowBean> result = followListRsp.getFollowBeans();
                for (FollowListRsp.MyFollowBean myFollowBean : result) {
                    if (myFollowBean.get_id().equals(authorId)) {
                        isFollowed = true;
                        break;
                    }
                }
                mView.isAuthorFollowed(isFollowed);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void setFollowAuthor(final String authorId, boolean isFollow) {
        subscribeNetworkTask(getClass().getSimpleName().concat("setFollowAuthor"),
                mExploreDetailModel.setFollowAuthor(authorId, isFollow),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        checkFollow(authorId);
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void setLikeBlogComment(String blogId, boolean isLike) {
        subscribeNetworkTask(mExploreDetailModel.setLikeBlogComment(blogId, isLike));
    }

    public void setCollectBlogPost(String blogId, boolean isCollect) {
        subscribeNetworkTask(mExploreDetailModel.setCollectBlogPost(blogId, isCollect));
    }

    public void setLikeBlogPost(String blogId, boolean isLike) {
        subscribeNetworkTask(mExploreDetailModel.setLikeBlogPost(blogId, isLike));
    }
}
