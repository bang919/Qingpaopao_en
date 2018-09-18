package com.wopin.qingpaopao.fragment.welfare.oldchangenew;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentDetailAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.OldChangeNewContentDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.OldChangeNewContentDetailView;
import com.wopin.qingpaopao.widget.RecyclerViewAdDotLayout;

import java.util.ArrayList;

public class OldChangeNewContentDetailFragment extends BaseBarDialogFragment<OldChangeNewContentDetailPresenter> implements OldChangeNewContentDetailView, View.OnClickListener, BuyOldChangeNewProduceDialog.BuyOldChangeNewCallback {

    public static final String TAG = "OldChangeNewContentDetailFragment";
    private ProductContent mProductContent;
    private ProductContent mOldProduct;
    private RecyclerView mGoodsDetailRv;
    private Spinner mChooseOldSpinner;
    private RecyclerViewAdDotLayout mRecyclerViewAdDotLayout;

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
        ((TextView) rootView.findViewById(R.id.tv_subtitle)).setText(Html.fromHtml(mProductContent.getShort_description()));
        ((TextView) rootView.findViewById(R.id.tv_price)).setText(String.format(getString(R.string.score_number), mProductContent.getPrice()));
        ((TextView) rootView.findViewById(R.id.tv_count)).setText(String.format(getString(R.string.residue_count),
                Integer.valueOf(mProductContent.getSku())));

        mGoodsDetailRv = rootView.findViewById(R.id.rv_goods_top_detail);
        mChooseOldSpinner = rootView.findViewById(R.id.choose_old_to_new_produce);
        mRecyclerViewAdDotLayout = rootView.findViewById(R.id.rv_advertising_decorate);
        rootView.findViewById(R.id.btn_i_want_to_buy).setOnClickListener(this);
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

        setLoadingVisibility(true);
        mPresenter.getOldGoodList();
    }

    @Override
    public void onOldGoodsList(final ArrayList<ProductContent> oldGoodsList) {
        setLoadingVisibility(false);
        ArrayList<String> datas = new ArrayList<>();
        datas.add(getString(R.string.choose_old_to_new_produce));
        for (ProductContent productContent : oldGoodsList) {
            datas.add(productContent.getName() + " " + productContent.getPrice() + "元");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, datas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseOldSpinner.setAdapter(arrayAdapter);
        mChooseOldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mOldProduct = oldGoodsList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onPayMentExchangeSubmit() {
        ToastUtils.showShort(R.string.buy_success);
        dismiss();
    }

    @Override
    public void onError(String errorSting) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorSting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_i_want_to_buy:
                BuyOldChangeNewProduceDialog build = BuyOldChangeNewProduceDialog.build(mProductContent, mOldProduct);
                build.setBuyOldChangeNewCallback(this);
                build.show(getChildFragmentManager(), BuyOldChangeNewProduceDialog.TAG);
                break;
        }
    }

    @Override
    public void OnBuyInformation(int number, String addressId) {
        mPresenter.payMentExchange(addressId, mProductContent.getName(), mProductContent.getDescriptionImage().size() == 0 ? null : mProductContent.getDescriptionImage().get(0), mProductContent.getId(), number, Integer.valueOf(mProductContent.getPrice()), mOldProduct == null ? 0 : Integer.valueOf(mOldProduct.getPrice()));
    }
}
