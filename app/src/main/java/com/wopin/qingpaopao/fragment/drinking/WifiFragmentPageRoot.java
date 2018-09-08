package com.wopin.qingpaopao.fragment.drinking;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MyFragmentPageAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class WifiFragmentPageRoot extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "WifiFragmentPageRoot";
    private MyFragmentPageAdapter mMyFragmentPageAdapter;

    @Override
    protected String setBarTitle() {
        return getString(R.string.link_wifi);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_wifi_rootpage;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {
        mMyFragmentPageAdapter = new MyFragmentPageAdapter(this, R.id.content_layout);
        WifiFragmentPage1 wifiFragmentPage1 = new WifiFragmentPage1();
        wifiFragmentPage1.setOnClickListener(this);
        mMyFragmentPageAdapter.switchToFragment(wifiFragmentPage1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step_btn://第一页按了next
                WifiFragmentPage2 wifiFragmentPage2 = new WifiFragmentPage2();
                wifiFragmentPage2.setOnClickListener(this);
                mMyFragmentPageAdapter.switchToFragment(wifiFragmentPage2, true);
                break;
            case R.id.btn_scan://第二页扫描按钮
                break;
            case R.id.btn_link_by_hand://第二页手动连接按钮
                new LinkWifiByhandFragment().show(getChildFragmentManager(), LinkWifiByhandFragment.TAG);
                break;
        }
    }


}
