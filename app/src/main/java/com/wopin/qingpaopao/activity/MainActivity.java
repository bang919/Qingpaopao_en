package com.wopin.qingpaopao.activity;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MainViewPagerAdapter;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.manager.MqttConnectManager;
import com.wopin.qingpaopao.manager.WifiConnectManager;
import com.wopin.qingpaopao.presenter.MainPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MainView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    private long doubleClickToExitTime;
    private ViewPager mViewPager;
    private MainViewPagerAdapter mViewpagerAdapter;
    private TabLayout mTablayout;

    @Override
    protected int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    protected void onDestroy() {
        BleConnectManager.getInstance().destroy();
        MqttConnectManager.getInstance().destroy();
        WifiConnectManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.main_viewpager);
        mTablayout = findViewById(R.id.main_tablayout);
    }

    @Override
    protected void initData() {
        mViewpagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.attendViewpagerAndTablayout(mViewPager, mTablayout);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Proof SwipeRefreshLayout's function when ViewPager dragging
                BaseMainFragment fragment = mViewpagerAdapter.getItem(mViewPager.getCurrentItem());
            }
        });
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
