package com.wopin.qingpaopao.fragment.system_setting;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class SystemSettingFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback {

    public static final String TAG = "SystemSettingFragment";

    @Override
    protected String setBarTitle() {
        return getString(R.string.system_setting);
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
        userGuideRv.setAdapter(new MineListRvAdapter(new int[]{R.string.system_update, R.string.about_us, R.string.language_setting}, this));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onListItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.system_update:
                new SystemUpdateFragment().show(getChildFragmentManager(), SystemUpdateFragment.TAG);
                break;
            case R.string.about_us:
                new AboutUsFragment().show(getChildFragmentManager(), AboutUsFragment.TAG);
                break;
            case R.string.language_setting:
                new LanguageSettingFragment().show(getChildFragmentManager(), LanguageSettingFragment.TAG);
                break;
        }
    }
}
