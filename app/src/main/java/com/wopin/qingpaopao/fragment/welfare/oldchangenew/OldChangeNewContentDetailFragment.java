package com.wopin.qingpaopao.fragment.welfare.oldchangenew;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentDetailAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.OldChangeNewContentDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.OldChangeNewContentDetailView;

public class OldChangeNewContentDetailFragment extends BaseBarDialogFragment<OldChangeNewContentDetailPresenter> implements OldChangeNewContentDetailView, View.OnClickListener {

    public static final String TAG = "OldChangeNewContentDetailFragment";
    private ProductContent mProductContent;
    private RecyclerView mGoodsDetailRv;

    public static OldChangeNewContentDetailFragment build(ProductContent productContent) {
        OldChangeNewContentDetailFragment oldChangeNewContentDetailFragment = new OldChangeNewContentDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, productContent);
        oldChangeNewContentDetailFragment.setArguments(args);
        return oldChangeNewContentDetailFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.old_change_new);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_old_change_new_content_detail;
    }

    @Override
    protected OldChangeNewContentDetailPresenter initPresenter() {
        return new OldChangeNewContentDetailPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mProductContent = getArguments().getParcelable(TAG);

        GlideUtils.loadImage((ImageView) rootView.findViewById(R.id.iv_top_img), -1, mProductContent.getDescriptionImage(), new CenterCrop());
        ((TextView) rootView.findViewById(R.id.tv_title)).setText(mProductContent.getName());
        ((TextView) rootView.findViewById(R.id.tv_subtitle)).setText(Html.fromHtml(mProductContent.getShort_description()));
        ((TextView) rootView.findViewById(R.id.tv_price)).setText(String.format(getString(R.string.score_number), mProductContent.getPrice()));
        ((TextView) rootView.findViewById(R.id.tv_count)).setText(String.format(getString(R.string.residue_count),
                Integer.valueOf(mProductContent.getSku())));

        mGoodsDetailRv = rootView.findViewById(R.id.rv_goods_detail);
        rootView.findViewById(R.id.choose_old_to_new_produce).setOnClickListener(this);
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
    public void onError(String errorSting) {
        ToastUtils.showShort(errorSting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_old_to_new_produce:
                break;
            case R.id.btn_i_want_to_change:
                break;
        }
    }
}
