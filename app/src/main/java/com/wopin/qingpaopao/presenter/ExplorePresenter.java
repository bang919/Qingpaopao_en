package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.model.ExploreModel;
import com.wopin.qingpaopao.view.ExploreView;

public class ExplorePresenter extends BasePresenter<ExploreView> {

    private ExploreModel mExploreModel;

    public ExplorePresenter(Context context, ExploreView view) {
        super(context, view);
        mExploreModel = new ExploreModel();
    }

    public void listExplores() {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("ExplorePresenter"), mExploreModel.listExplores(), new MyObserver<ExploreListRsp>() {
            @Override
            public void onMyNext(ExploreListRsp exploreListRsp) {
                mView.onExploreList(exploreListRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
