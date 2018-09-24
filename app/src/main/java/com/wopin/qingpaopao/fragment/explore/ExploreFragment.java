package com.wopin.qingpaopao.fragment.explore;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ExploreListAdapter;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.presenter.ExplorePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ExploreView;

public class ExploreFragment extends BaseMainFragment<ExplorePresenter> implements ExploreView, ExploreListAdapter.ExploreListItemClick {

    private TabLayout mTabLayout;
    private EditText mSearchEt;
    private RecyclerView mExploreRv;
    private View mLoadingView;
    private ExploreListAdapter mExploreListAdapter;

    @Override
    public void onViewPagerFragmentPause() {
        super.onViewPagerFragmentPause();
        setLoadingVisibility(false);
    }

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
        mSearchEt = rootView.findViewById(R.id.et_search);
        mTabLayout = rootView.findViewById(R.id.explore_tablayout);
        mExploreRv = rootView.findViewById(R.id.rv_explore_list);
        mLoadingView = rootView.findViewById(R.id.progress_bar_layout);
    }

    private void setLoadingVisibility(boolean isVisibility) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        mExploreRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mExploreListAdapter = new ExploreListAdapter(this);
        mExploreRv.setAdapter(mExploreListAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String s = tab.getText().toString();
                if (s.equals(getString(R.string.hot_topic))) {//热门话题
                    mPresenter.listHotExplores();
                } else if (s.equals(getString(R.string.newest_topic))) {//最新话题
                    mPresenter.listNewlyExplores();
                } else {//我的话题
                    mPresenter.listMyExplores();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mSearchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String s = mSearchEt.getText().toString();
                    if (TextUtils.isEmpty(s)) {
                        ToastUtils.showShort(R.string.search_your_favourite);
                        return true;
                    }
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mPresenter.searchExplores(s);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void refreshData() {
        mPresenter.listHotExplores();
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
    public void onExploreItemClick(final ExploreListRsp.PostsBean postsBean, int position) {
        ExploreDetailFragment.build(postsBean).show(getChildFragmentManager(), ExploreDetailFragment.TAG);
    }
}
