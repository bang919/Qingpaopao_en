package com.wopin.qingpaopao.fragment.welfare.scoremarket;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentDetailAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.ScoreMarketContentDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ScoreMarketContentDetailView;
import com.wopin.qingpaopao.widget.RecyclerViewAdDotLayout;

import java.util.ArrayList;

public class ScoreMarketContentDetailFragment extends BaseBarDialogFragment<ScoreMarketContentDetailPresenter> implements View.OnClickListener, EnchangeScoreMarketProduceDialog.BuyInformationDialogCallback, ScoreMarketContentDetailView {

    public static final String TAG = "ScoreMarketContentDetailFragment";
    private ProductContent mProductContent;
    private RecyclerView mGoodsDetailRv;
    private RecyclerViewAdDotLayout mRecyclerViewAdDotLayout;

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

        ArrayList<String> descriptionImage = mProductContent.getDescriptionImage();
        LinearLayout detailLinearLayout = rootView.findViewById(R.id.rv_goods_detail_linearlayout);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        for (final String image : descriptionImage) {
            final ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
            imageView.setLayoutParams(layoutParams);
            detailLinearLayout.addView(imageView);
            GlideUtils.loadImage(imageView, -1, image, new CenterInside());
        }

        ((TextView) rootView.findViewById(R.id.tv_title)).setText(mProductContent.getName());
        ((TextView) rootView.findViewById(R.id.tv_score)).setText(String.format(getString(R.string.score_number), mProductContent.getPrice()));
        ((TextView) rootView.findViewById(R.id.tv_reference_price)).setText(String.format(getString(R.string.market_reference_price_value), mProductContent.getRegular_price()));
        ((TextView) rootView.findViewById(R.id.tv_count)).setText(String.format(getString(R.string.residue_count), Integer.valueOf(mProductContent.getSku())));

        mGoodsDetailRv = rootView.findViewById(R.id.rv_goods_top_detail);
        mRecyclerViewAdDotLayout = rootView.findViewById(R.id.rv_advertising_decorate);
        rootView.findViewById(R.id.btn_i_want_to_change).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        mGoodsDetailRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ScoreMarketContentDetailAdapter scoreMarketContentDetailAdapter = new ScoreMarketContentDetailAdapter();
        scoreMarketContentDetailAdapter.setScoreMarketContentDetails(mProductContent.getImages());
        mGoodsDetailRv.setAdapter(scoreMarketContentDetailAdapter);
        mRecyclerViewAdDotLayout.attendToRecyclerView(mGoodsDetailRv);
        mRecyclerViewAdDotLayout.setDotCount(mProductContent.getImages().size());
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mGoodsDetailRv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_i_want_to_change:
                EnchangeScoreMarketProduceDialog enchangeScoreMarketProduceDialog = EnchangeScoreMarketProduceDialog.build(mProductContent);
                enchangeScoreMarketProduceDialog.setBuyInformationDialogCallback(this);
                enchangeScoreMarketProduceDialog.show(getChildFragmentManager(), EnchangeScoreMarketProduceDialog.TAG);
                break;
        }
    }

    @Override
    public void OnBuyInformation(int number, String addressId) {
        mPresenter.payMentScores(addressId, mProductContent.getName(), mProductContent.getDescriptionImage().size() == 0 ? null : mProductContent.getDescriptionImage().get(0), mProductContent.getId(), number, Integer.valueOf(mProductContent.getPrice()));
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
