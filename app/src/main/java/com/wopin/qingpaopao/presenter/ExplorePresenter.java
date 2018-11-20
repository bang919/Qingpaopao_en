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

    public void listHotExplores(int page, int number) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("listHotExplores"), mExploreModel.listHotExplores(page, number), new MyObserver<ExploreListRsp>() {
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

    public void listNewlyExplores(int page, int number) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("listNewlyExplores"), mExploreModel.listNewlyExplores(page, number), new MyObserver<ExploreListRsp>() {
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

    public void listMyExplores(int page, int number) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("listMyExplores"), mExploreModel.listMyExplores(page, number), new MyObserver<ExploreListRsp>() {
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

    public void searchExplores(String searchString) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("searchExplores"), mExploreModel.searchExplores(searchString), new MyObserver<ExploreListRsp>() {
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
