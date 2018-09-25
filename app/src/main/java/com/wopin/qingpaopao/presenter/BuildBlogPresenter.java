package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.BuildBlogModel;
import com.wopin.qingpaopao.view.BuildBlogView;

public class BuildBlogPresenter extends BasePresenter<BuildBlogView> {

    private BuildBlogModel mBuildBlogModel;

    public BuildBlogPresenter(Context context, BuildBlogView view) {
        super(context, view);
        mBuildBlogModel = new BuildBlogModel();
    }

    public void newBlog(String title, String content) {
        subscribeNetworkTask(getClass().getSimpleName().concat("newBlog"), mBuildBlogModel.newBlog(title, content), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onBlogBuildSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
