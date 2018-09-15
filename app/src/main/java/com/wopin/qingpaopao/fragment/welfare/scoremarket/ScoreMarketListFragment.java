package com.wopin.qingpaopao.fragment.welfare.scoremarket;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ScoreMarketContentAdapter;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.WelfareModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ExceptionUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ScoreMarketListFragment extends BaseBarDialogFragment implements ScoreMarketContentAdapter.ScoreMarketContentClick {

    public static final String TAG = "ScoreMarketListFragment";
    private RecyclerView mScoreMarketListRv;
    private ScoreMarketContentAdapter mScoreMarketContentAdapter;
    private Disposable mScoreMarketContentDisposable;

    @Override
    public void onDestroy() {
        if (mScoreMarketContentDisposable != null) {
            mScoreMarketContentDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.score_market);
    }

    @Override
    protected int getLayout() {
        return R.layout.recyclerview_list;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mScoreMarketListRv = rootView.findViewById(R.id.recyclerview);
    }

    @Override
    protected void initEvent() {
        mScoreMarketListRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mScoreMarketContentAdapter = new ScoreMarketContentAdapter();
        mScoreMarketListRv.setAdapter(mScoreMarketContentAdapter);
        mScoreMarketContentAdapter.setScoreMarketContentClick(this);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = 1;
                outRect.left = 1;
                outRect.top = 1;
                outRect.bottom = 1;
            }
        };
        mScoreMarketListRv.addItemDecoration(itemDecoration);
        setLoadingVisibility(true);
        WelfareModel welfareModel = new WelfareModel();
        welfareModel.getProductContent(20)
                .subscribe(new Observer<ArrayList<ProductContent>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mScoreMarketContentDisposable = d;
                    }

                    @Override
                    public void onNext(ArrayList<ProductContent> productContents) {
                        mScoreMarketContentAdapter.setProductContents(productContents);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoadingVisibility(false);
                        ToastUtils.showShort(ExceptionUtil.getHttpExceptionMessage(e));
                    }

                    @Override
                    public void onComplete() {
                        setLoadingVisibility(false);
                    }
                });

    }

    @Override
    public void onScoreMarketContentClick(ProductContent productContent) {
        ScoreMarketContentDetailFragment.build(productContent).show(getChildFragmentManager(), ScoreMarketContentDetailFragment.TAG);
    }
}
