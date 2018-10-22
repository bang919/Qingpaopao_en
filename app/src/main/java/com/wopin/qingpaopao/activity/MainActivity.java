package com.wopin.qingpaopao.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MainViewPagerAdapter;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.manager.MessageProxy;
import com.wopin.qingpaopao.presenter.DrinkingPresenter;
import com.wopin.qingpaopao.presenter.MainPresenter;
import com.wopin.qingpaopao.utils.NotificationSettingUtil;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MainView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    private long doubleClickToExitTime;
    private ViewPager mViewPager;
    private MainViewPagerAdapter mViewpagerAdapter;
    private TabLayout mTablayout;
    private MessageProxy mMessageProxy;

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
    protected void onResume() {
        super.onResume();
        mMessageProxy.startListening();
        permissionToLocation();
    }

    @Override
    protected void onPause() {
        mMessageProxy.pauseListening();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMessageProxy.destroy();
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
        mMessageProxy = new MessageProxy();
        //看看是否开了Notification，是的话重新设置下
        if ((Boolean) SPUtils.get(this, Constants.DRINKING_NOTIFICATION, false)) {
            NotificationSettingUtil.startNotification(this);
        }
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


    private void permissionToLocation() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .zipWith(rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION), new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        return aBoolean && aBoolean2;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getLocation(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 调用本地GPS来获取经纬度
     *
     * @param context
     */
    private void getLocation(Context context) {
        String locationProvider;
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
            ToastUtils.showShort(R.string.please_turn_on_gps);
            return;
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        locationManager.requestLocationUpdates(locationProvider, 30, 5, mListener);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            showLocation(location);
        }
    }


    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.removeUpdates(this);
            }
        }
    };

    /**
     * 获取经纬度
     *
     * @param location
     */
    private void showLocation(Location location) {
        DrinkingPresenter.setLocation(location);
    }
}
