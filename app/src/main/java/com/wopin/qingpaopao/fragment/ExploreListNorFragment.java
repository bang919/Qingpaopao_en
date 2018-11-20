package com.wopin.qingpaopao.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ExploreListNorAdapter;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.explore.ExploreDetailFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ExceptionUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class ExploreListNorFragment extends BaseBarDialogFragment {

    public static final String TAG = "ExploreListNorFragment";

    private RecyclerView mExploreRv;
    private ExploreListNorAdapter mExploreListAdapter;
    private Disposable mDisposable;
    private final static int PAGE_NUMBER = 10;
    private int page = 1;

    @Override
    public void onDetach() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDetach();
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
        mExploreRv = rootView.findViewById(R.id.recyclerview);
        mExploreRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mExploreListAdapter = new ExploreListNorAdapter(R.layout.item_explore_list, null);
        mExploreListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExploreDetailFragment.build((ExploreListRsp.PostsBean) adapter.getItem(position)).show(getChildFragmentManager(), ExploreDetailFragment.TAG);
            }
        });
        mExploreListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initEvent();
            }
        }, mExploreRv);
        mExploreRv.setAdapter(mExploreListAdapter);
    }

    @Override
    protected void initEvent() {
        setLoadingVisibility(true);
        getExploreListObservable(page, PAGE_NUMBER).subscribe(new Observer<ExploreListRsp>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ExploreListRsp exploreListRsp) {
                mExploreListAdapter.addData(exploreListRsp.getPosts());
                if (exploreListRsp.getPosts().size() < PAGE_NUMBER) {
                    mExploreListAdapter.loadMoreEnd();
                } else {
                    page++;
                    mExploreListAdapter.loadMoreComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                mExploreListAdapter.loadMoreFail();
                ToastUtils.showShort(ExceptionUtil.getHttpExceptionMessage(e));
                mDisposable = null;
                setLoadingVisibility(false);
            }

            @Override
            public void onComplete() {
                mDisposable = null;
                setLoadingVisibility(false);
            }
        });
    }

    public abstract Observable<ExploreListRsp> getExploreListObservable(int page, int number);
}
