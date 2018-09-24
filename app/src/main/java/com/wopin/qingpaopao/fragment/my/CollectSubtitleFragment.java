package com.wopin.qingpaopao.fragment.my;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ExploreListAdapter;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.fragment.explore.ExploreDetailFragment;
import com.wopin.qingpaopao.model.ExploreModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ExceptionUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CollectSubtitleFragment extends BaseBarDialogFragment implements ExploreListAdapter.ExploreListItemClick {

    public static final String TAG = "CollectSubtitleFragment";

    private RecyclerView mExploreRv;
    private ExploreListAdapter mExploreListAdapter;
    private Disposable mDisposable;

    @Override
    public void onDetach() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDetach();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.my_collect);
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
        mExploreListAdapter = new ExploreListAdapter(this);
        mExploreRv.setAdapter(mExploreListAdapter);
    }

    @Override
    protected void initEvent() {
        setLoadingVisibility(true);
        ExploreModel exploreModel = new ExploreModel();
        exploreModel.listColletionExplores().subscribe(new Observer<ExploreListRsp>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ExploreListRsp exploreListRsp) {
                mExploreListAdapter.setDatas(exploreListRsp.getPosts());
            }

            @Override
            public void onError(Throwable e) {
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

    @Override
    public void onExploreItemClick(ExploreListRsp.PostsBean postsBean, int position) {
        ExploreDetailFragment.build(postsBean).show(getChildFragmentManager(), ExploreDetailFragment.TAG);
    }
}
