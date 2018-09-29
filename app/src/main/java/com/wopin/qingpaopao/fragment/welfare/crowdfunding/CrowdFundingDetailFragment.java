package com.wopin.qingpaopao.fragment.welfare.crowdfunding;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentDetailAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.CrowdFundingContentDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.CrowdFundingDetailView;
import com.wopin.qingpaopao.widget.RecyclerViewAdDotLayout;

import java.text.ParseException;
import java.util.ArrayList;

public class CrowdFundingDetailFragment extends BaseBarDialogFragment<CrowdFundingContentDetailPresenter> implements View.OnClickListener, CrowdFundingDetailView, CrowdFundingProduceDialog.SupportInformationDialogCallback {

    public static final String TAG = "CrowdFundingDetailFragment";
    private ProductContent mProductContent;
    private View mRootView;
    private RecyclerView mGoodsDetailRv;
    private RecyclerViewAdDotLayout mRecyclerViewAdDotLayout;

    public static CrowdFundingDetailFragment build(ProductContent productContent) {
        CrowdFundingDetailFragment scoreMarketContentDetailFragment = new CrowdFundingDetailFragment();
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
        return R.layout.fragment_crowd_funding_content_detail;
    }

    @Override
    protected CrowdFundingContentDetailPresenter initPresenter() {
        return new CrowdFundingContentDetailPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
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
        ((TextView) rootView.findViewById(R.id.tv_subtitle)).setText(mProductContent.getShort_description());
        ((TextView) rootView.findViewById(R.id.target_price_value)).setText(String.format(getString(R.string.price_number), mProductContent.getPrice()));

        int daysDifference = 1;
        try {
            daysDifference = TimeFormatUtils.getDaysDifference(mProductContent.getDate_on_sale_to());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((TextView) rootView.findViewById(R.id.tv_leave_time_value)).setText(String.format(getString(R.string.day_number),
                String.valueOf(daysDifference)));

        mGoodsDetailRv = rootView.findViewById(R.id.rv_goods_top_detail);
        mRecyclerViewAdDotLayout = rootView.findViewById(R.id.rv_advertising_decorate);
        rootView.findViewById(R.id.btn_i_want_to_support).setOnClickListener(this);
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

        mPresenter.crowdfundingOrderTotalMoneyAndPeople(mProductContent.getId(), mProductContent.getPrice());
        setLoadingVisibility(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_i_want_to_support:
                CrowdFundingProduceDialog build = CrowdFundingProduceDialog.build(mProductContent);
                build.setSupportInformationDialogCallback(this);
                build.show(getChildFragmentManager(), CrowdFundingProduceDialog.TAG);
                break;
        }
    }

    @Override
    public void onCrowdFundingPrice(String targetPrice, String currentPrice, float percent) {
        ((TextView) mRootView.findViewById(R.id.tv_already_price_value)).setText(String.format(getString(R.string.price_number), currentPrice));
        ((ProgressBar) mRootView.findViewById(R.id.progress_bar_percent)).setProgress((int) (percent * 100));
        TextView barText = mRootView.findViewById(R.id.bar_text);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) barText.getLayoutParams();
        layoutParams.horizontalBias = percent;
        barText.setLayoutParams(layoutParams);
        barText.setText(String.format(getString(R.string.percent_float), percent * 100));
    }

    @Override
    public void onCrowdFundingSupportCount(int totalPeople) {
        ((TextView) mRootView.findViewById(R.id.tv_support_count_value)).setText(String.valueOf(totalPeople));
    }

    @Override
    public void onDataSuccess() {
        setLoadingVisibility(false);
    }

    @Override
    public void onCrowdFundingPayMentSuccess() {
        setLoadingVisibility(false);
        ToastUtils.showShort(R.string.buy_success);
    }

    @Override
    public void onError(String error) {
        setLoadingVisibility(false);
        ToastUtils.showShort(error);
    }

    @Override
    public void onSupportInformation(ProductContent.AttributeBean attributeBean, int number, String addressId) {
        mPresenter.payMentCrowdfunding(addressId, attributeBean.getOptions().get(0), mProductContent.getDescriptionImage().size() == 0 ? null : mProductContent.getDescriptionImage().get(0), mProductContent.getId(), number, Integer.valueOf(attributeBean.getName()));
        setLoadingVisibility(true);
    }
}
