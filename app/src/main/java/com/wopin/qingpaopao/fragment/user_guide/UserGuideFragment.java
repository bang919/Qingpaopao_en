package com.wopin.qingpaopao.fragment.user_guide;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class UserGuideFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback {

    public static final String TAG = "UserGuideFragment";

    @Override
    protected String setBarTitle() {
        return getString(R.string.user_guide);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_third_bind;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView userGuideRv = rootView.findViewById(R.id.list_recyclerview);
        userGuideRv.setLayoutManager(new LinearLayoutManager(getContext()));
        userGuideRv.setAdapter(new MineListRvAdapter(new int[]{R.string.help_center, R.string.service_center, R.string.system_using_introduce}, this));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onListItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.help_center:
                new HelpCenterFragment().show(getChildFragmentManager(), HelpCenterFragment.TAG);
                break;
            case R.string.service_center:
                new ServiceCenterFragment().show(getChildFragmentManager(), ServiceCenterFragment.TAG);
                break;
            case R.string.system_using_introduce:
                new SystemUsingIntroduceFragment().show(getChildFragmentManager(), SystemUsingIntroduceFragment.TAG);
                break;
        }
    }
}
