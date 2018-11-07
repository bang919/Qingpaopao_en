package com.wopin.qingpaopao.fragment.system_setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class SystemUpdateFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback {

    public static final String TAG = "SystemUpdateFragment";
    private MineListRvAdapter mAdapter;

    @Override
    protected String setBarTitle() {
        return getString(R.string.system_update);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_system_update;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView systemVersionRv = rootView.findViewById(R.id.list_recyclerview);
        systemVersionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MineListRvAdapter(new int[]{R.string.system_version}, this);
        systemVersionRv.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        try {
            PackageInfo packageInfo = MyApplication.getMyApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);
            String versionName = packageInfo.versionName;
            mAdapter.setDatas(new String[]{versionName + getString(R.string.newest)});
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int textResource, int position) {

    }
}
