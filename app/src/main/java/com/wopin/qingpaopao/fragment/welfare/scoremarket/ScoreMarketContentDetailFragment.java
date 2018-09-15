package com.wopin.qingpaopao.fragment.welfare.scoremarket;

import android.os.Bundle;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class ScoreMarketContentDetailFragment extends BaseBarDialogFragment {

    public static final String TAG = "ScoreMarketContentDetailFragment";
    private ProductContent mProductContent;

    public static ScoreMarketContentDetailFragment build(ProductContent productContent) {
        ScoreMarketContentDetailFragment scoreMarketContentDetailFragment = new ScoreMarketContentDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, productContent);
        scoreMarketContentDetailFragment.setArguments(args);
        return scoreMarketContentDetailFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.commodity_details);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_score_market_content_detail;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {
        mProductContent = getArguments().getParcelable(TAG);
    }
}
