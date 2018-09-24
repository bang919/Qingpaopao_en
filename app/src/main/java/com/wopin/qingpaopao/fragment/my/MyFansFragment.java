package com.wopin.qingpaopao.fragment.my;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.FollowListAdapter;
import com.wopin.qingpaopao.bean.response.FollowListRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.ExploreDetailModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ExceptionUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyFansFragment extends BaseBarDialogFragment {

    public static final String TAG = "MyFansFragment";
    private RecyclerView mRecyclerView;
    private FollowListAdapter mFollowListAdapter;
    private Disposable mDisposable;

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.my_focus);
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
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFollowListAdapter = new FollowListAdapter();
        mRecyclerView.setAdapter(mFollowListAdapter);
    }

    @Override
    protected void initEvent() {
        setLoadingVisibility(true);
        ExploreDetailModel exploreDetailModel = new ExploreDetailModel();
        exploreDetailModel.getMyFansList().subscribe(new Observer<FollowListRsp>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(FollowListRsp followListRsp) {
                mFollowListAdapter.setMyFollows(followListRsp.getFollowBeans());
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShort(ExceptionUtil.getHttpExceptionMessage(e));
                setLoadingVisibility(false);
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                setLoadingVisibility(false);
                mDisposable = null;
            }
        });
    }
}
