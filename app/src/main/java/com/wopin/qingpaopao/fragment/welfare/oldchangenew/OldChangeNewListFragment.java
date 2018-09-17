package com.wopin.qingpaopao.fragment.welfare.oldchangenew;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.OldChangeNewListAdapter;
import com.wopin.qingpaopao.adapter.WelfareBannerAdapter;
import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.fragment.welfare.scoremarket.ScoreMarketContentDetailFragment;
import com.wopin.qingpaopao.presenter.WelfarePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.widget.WelfareView;

import java.util.ArrayList;

public class OldChangeNewListFragment extends BaseBarDialogFragment<WelfarePresenter> implements WelfareView, OldChangeNewListAdapter.OldChangeNewClickListener {

    public static final String TAG = "OldChangeNewListFragment";
    private RecyclerView mBannerRv;
    private RecyclerView mListRv;
    private WelfareBannerAdapter mWelfareBannerAdapter;
    private OldChangeNewListAdapter mOldChangeNewListAdapter;

    @Override
    protected String setBarTitle() {
        return getString(R.string.old_change_new);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_oldchangenew_list;
    }

    @Override
    protected WelfarePresenter initPresenter() {
        return new WelfarePresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mBannerRv = rootView.findViewById(R.id.rv_banner);
        mListRv = rootView.findViewById(R.id.list_recyclerview);
    }

    @Override
    protected void initEvent() {
        mBannerRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mWelfareBannerAdapter = new WelfareBannerAdapter();
        mBannerRv.setAdapter(mWelfareBannerAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mBannerRv);
        mListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mOldChangeNewListAdapter = new OldChangeNewListAdapter();
        mListRv.setAdapter(mOldChangeNewListAdapter);
        mOldChangeNewListAdapter.setOldChangeNewClickListener(this);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = 1;
                outRect.left = 1;
                outRect.top = 1;
                outRect.bottom = 1;
            }
        };
        mListRv.addItemDecoration(itemDecoration);
        mPresenter.getOldChangeNewProduct();
    }

    @Override
    public void onProductBanner(ProductBanner productBanner) {
        mWelfareBannerAdapter.setProductBanner(productBanner);
    }

    @Override
    public void onProductContentList(ArrayList<ProductContent> productContents) {
        mOldChangeNewListAdapter.setOldChangeNewDatas(productContents);
    }

    @Override
    public void onLoading() {
        setLoadingVisibility(true);
    }

    @Override
    public void onRequestSuccess() {
        setLoadingVisibility(false);
    }

    @Override
    public void onError(String error) {
        setLoadingVisibility(false);
        ToastUtils.showShort(error);
    }

    @Override
    public void onItemClick(ProductContent productContent) {
        OldChangeNewContentDetailFragment.build(productContent).show(getChildFragmentManager(), ScoreMarketContentDetailFragment.TAG);
    }
}
