package com.wopin.qingpaopao.fragment.explore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ExploreListAdapter;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.presenter.ExplorePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ExploreView;

public class ExploreFragment extends BaseMainFragment<ExplorePresenter> implements ExploreView, ExploreListAdapter.ExploreListItemClick {

    private RecyclerView mExploreRv;
    private View mLoadingView;
    private ExploreListAdapter mExploreListAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_explore;
    }

    @Override
    protected ExplorePresenter initPresenter() {
        return new ExplorePresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mExploreRv = rootView.findViewById(R.id.rv_explore_list);
        mLoadingView = rootView.findViewById(R.id.progress_bar_layout);
    }

    private void setLoadingVisibility(boolean isVisibility) {
        mLoadingView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        mExploreRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mExploreListAdapter = new ExploreListAdapter(this);
        mExploreRv.setAdapter(mExploreListAdapter);
    }

    @Override
    public void refreshData() {
        mPresenter.listExplores();
    }

    @Override
    public void onLoading() {
        setLoadingVisibility(true);
    }

    @Override
    public void onExploreList(ExploreListRsp exploreListRsp) {
        setLoadingVisibility(false);
        mExploreListAdapter.setDatas(exploreListRsp.getPosts());
        onDataRefreshFinish(true);
    }

    @Override
    public void onError(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onExploreItemClick(ExploreListRsp.PostsBean postsBean, int position) {
        String webViewUrl = "http://url.cn/5WQEb6T";
        switch (postsBean.getID()) {
            case 1:
                webViewUrl = "http://url.cn/5WQEb6T";
                break;
            case 43:
                webViewUrl = "http://url.cn/5I49MBy";
                break;
            case 56:
                webViewUrl = "http://url.cn/5bMMFrg";
                break;
            case 78:
                webViewUrl = "http://url.cn/5EzNsV3";
                break;
            case 82:
                webViewUrl = "http://url.cn/5f1JsDp";
                break;
        }
        ExploreDetailFragment.build(webViewUrl).show(getChildFragmentManager(), ExploreDetailFragment.TAG);
    }
}
