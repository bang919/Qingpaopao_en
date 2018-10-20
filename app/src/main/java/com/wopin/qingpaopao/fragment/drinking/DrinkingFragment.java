package com.wopin.qingpaopao.fragment.drinking;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.CupListAdapter;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.WifiConfigToCupRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.presenter.DrinkingPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class DrinkingFragment extends BaseMainFragment<DrinkingPresenter> implements DrinkingView, CupListAdapter.OnCupItemClickCallback, DrinkingListView.OnDrinkingListViewCallback, DrinkingStartView.OnDrinkingStartCallback {

    private View mRootView;
    private DrinkingListView mDrinkingListView;
    private DrinkingStartView mDrinkingStartView;
    private Fragment currentFragment;
    private ArrayList<CupListRsp.CupBean> mOnlineCups;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.removeUpdates(this);
            }
            mPresenter.setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected int getLayout() {
        return R.layout.fragment_drinking;
    }

    @Override
    protected DrinkingPresenter initPresenter() {
        return new DrinkingPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
    }

    private void setLoadingVisibility(boolean isVisibility) {
        mRootView.findViewById(R.id.progress_bar_layout).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        mDrinkingListView = new DrinkingListView();
        mDrinkingListView.setOnDrinkingListViewCallback(this);
        mDrinkingListView.setOnCupItemClickCallback(this);
        mDrinkingStartView = new DrinkingStartView();
        mDrinkingStartView.setPresenterAndCallback(mPresenter, this);
        switchFragment(mDrinkingListView);

        mPresenter.getCupList();
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (!targetFragment.isAdded()) {
            transaction
                    .add(R.id.fragment_layout, targetFragment)
                    .commit();
        } else {
            transaction
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //获取地理位置
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .zipWith(rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION), new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        return aBoolean && aBoolean2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            ToastUtils.showShort(R.string.have_no_permission_for_location);
                        } else {
                            getLocationMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(R.string.have_no_permission_for_location);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getLocationMessage() {
        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        if (locationManager != null) {
            List<String> providers = locationManager.getProviders(true);

            final String provider;

            if (!providers.contains(LocationManager.GPS_PROVIDER) && !providers.contains(LocationManager.NETWORK_PROVIDER)) {
                Intent i = new Intent();
                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(i);
                ToastUtils.showShort(R.string.please_turn_on_gps);
                return;
            } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                provider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else {
                provider = LocationManager.PASSIVE_PROVIDER;
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.removeUpdates(locationListener);
            locationManager.requestLocationUpdates(provider, 1000, 3, locationListener);

            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                mPresenter.setLocation(location);
            }
        }
    }

    @Override
    public void backToDrinkingStartView() {
        mPresenter.getDrinkCount();
        mDrinkingStartView.setOnlineCups(mOnlineCups);
        switchFragment(mDrinkingStartView);
    }

    @Override
    public void onBluetoothDeviceFind(BluetoothDevice bluetoothDevice) {
        //这里是从新搜索出来的Device，所以调用firstTimeAddBleCup
        mPresenter.firstTimeAddCup(bluetoothDevice);
    }

    @Override
    public void onWifiDeviceFind(WifiConfigToCupRsp wifiConfigToCupRsp) {
        mPresenter.firstTimeAddCup(wifiConfigToCupRsp);
        ToastUtils.showShort(R.string.success_setting_wifi);
    }

    @Override
    public void onLoading() {
        setLoadingVisibility(true);
    }

    @Override
    public void onCupList(final ArrayList<CupListRsp.CupBean> cupBeanList, final CupListRsp.CupBean currentConnectCup) {
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                setLoadingVisibility(false);
                mOnlineCups = new ArrayList<>();
                for (CupListRsp.CupBean cupBean : cupBeanList) {
                    if (cupBean.isConnecting()) {
                        mOnlineCups.add(cupBean);
                    }
                }
                mDrinkingListView.notifyCupList(cupBeanList, currentConnectCup);
            }
        });
    }

    @Override
    public void onTodayDrink(DrinkListTodayRsp drinkListTodayRsp) {
        mDrinkingStartView.setTodayDrink(drinkListTodayRsp);

    }

    @Override
    public void onTotalDrink(DrinkListTotalRsp drinkListTotalRsp) {
        mDrinkingStartView.setTotalDrink(drinkListTotalRsp);
    }

    @Override
    public void onError(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onCupItemClick(final CupListRsp.CupBean cupBean, int position) {
        DeviceDetailFragment.getDeviceDetailFragment(cupBean, new DeviceDetailFragment.DeviceDetailFragmentCallback() {
            @Override
            public void onRename(String newName) {
                mPresenter.renameCup(cupBean, newName);
            }

            @Override
            public void onColorChange(int cupColor) {
                mPresenter.updateCupColor(cupBean, cupColor);
            }
        }).show(getChildFragmentManager(), DeviceDetailFragment.TAG);
    }


    @Override
    public void onCupItemDelete(CupListRsp.CupBean cupBean, int position) {
        if (cupBean.isConnecting()) {
            mPresenter.disconnectCup(cupBean);
        }
        mPresenter.deleteACup(cupBean.getUuid());
    }

    @Override
    public void onCupItemTurn(CupListRsp.CupBean cupBean, boolean isOn) {
        if (cupBean.getType().equals(Constants.BLE) && isOn) {
            mPresenter.connectCup(cupBean);
        } else if (cupBean.getType().equals(Constants.WIFI) && isOn) {
            mPresenter.connectCup(cupBean);
        }
    }

    @Override
    public void showDrinkingDeviceList() {
        switchFragment(mDrinkingListView);
    }
}
