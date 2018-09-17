package com.wopin.qingpaopao.fragment.welfare.scoremarket;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentDetailAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.ScoreMarketContentDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ScoreMarketContentDetailView;

public class ScoreMarketContentDetailFragment extends BaseBarDialogFragment<ScoreMarketContentDetailPresenter> implements View.OnClickListener, BuyInformationDialog.BuyInformationDialogCallback, ScoreMarketContentDetailView {

    public static final String TAG = "ScoreMarketContentDetailFragment";
    private ProductContent mProductContent;
    private RecyclerView mGoodsDetailRv;

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
    protected ScoreMarketContentDetailPresenter initPresenter() {
        return new ScoreMarketContentDetailPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mProductContent = getArguments().getParcelable(TAG);

        String description = mProductContent.getDescription();
        GlideUtils.loadImage((ImageView) rootView.findViewById(R.id.iv_top_img), -1, mProductContent.getDescriptionImage(), new CenterCrop());
        ((TextView) rootView.findViewById(R.id.tv_title)).setText(mProductContent.getName());
        ((TextView) rootView.findViewById(R.id.tv_score)).setText(String.format(getString(R.string.score_number), mProductContent.getPrice()));
        ((TextView) rootView.findViewById(R.id.tv_reference_price)).setText(String.format(getString(R.string.market_reference_price_value), mProductContent.getRegular_price()));
        ((TextView) rootView.findViewById(R.id.tv_count)).setText(String.format(getString(R.string.residue_count), 666));

        mGoodsDetailRv = rootView.findViewById(R.id.rv_goods_detail);
        rootView.findViewById(R.id.btn_i_want_to_change).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        mGoodsDetailRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ScoreMarketContentDetailAdapter scoreMarketContentDetailAdapter = new ScoreMarketContentDetailAdapter();
        scoreMarketContentDetailAdapter.setScoreMarketContentDetails(mProductContent.getImages());
        mGoodsDetailRv.setAdapter(scoreMarketContentDetailAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mGoodsDetailRv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_i_want_to_change:
                BuyInformationDialog buyInformationDialog = BuyInformationDialog.build(mProductContent);
                buyInformationDialog.setBuyInformationDialogCallback(this);
                buyInformationDialog.show(getChildFragmentManager(), BuyInformationDialog.TAG);
                break;
        }
    }

    @Override
    public void OnBuyInformation(int number, String addressId) {
        mPresenter.payMentScores(addressId, mProductContent.getName(), mProductContent.getDescriptionImage(), mProductContent.getId(),
                number, Integer.valueOf(mProductContent.getPrice()));
    }

    @Override
    public void onExchangeSuccess() {
        mPresenter.refreshPersonMessage();
        ToastUtils.showShort(R.string.exchange_success);
        dismiss();
    }

    @Override
    public void onError(String error) {
        ToastUtils.showShort(error);
    }
}
