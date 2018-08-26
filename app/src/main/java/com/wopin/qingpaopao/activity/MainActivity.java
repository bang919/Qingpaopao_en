package com.wopin.qingpaopao.activity;


import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.BaseActivity;
import com.wopin.qingpaopao.presenter.MainPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MainView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    private long doubleClickToExitTime;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {

    }


    @Override
    public void onSearchError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - doubleClickToExitTime < 1000) {
            finish();
        } else {
            ToastUtils.showShort(R.string.double_click_to_exit);
            doubleClickToExitTime = System.currentTimeMillis();
        }
    }
}
