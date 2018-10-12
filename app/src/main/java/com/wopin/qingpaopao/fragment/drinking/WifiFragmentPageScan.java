package com.wopin.qingpaopao.fragment.drinking;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.utils.ToastUtils;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class WifiFragmentPageScan extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mZXingScannerView;
    private WifiScanCallback mWifiScanCallback;
    private boolean isDestroy;

    public void setWifiScanCallback(WifiScanCallback wifiScanCallback) {
        mWifiScanCallback = wifiScanCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi_page_scan, container, false);
        mZXingScannerView = rootView.findViewById(R.id.zxing);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            startScan();
                        } else {
                            ToastUtils.showShort(R.string.need_permission);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startScan() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDestroy) {
                    mZXingScannerView.setResultHandler(WifiFragmentPageScan.this);
                    mZXingScannerView.setAutoFocus(true);
                    mZXingScannerView.setLaserEnabled(false);
                    mZXingScannerView.setAspectTolerance(0.5f);
                    mZXingScannerView.startCamera();
                }
            }
        }, 300);//页面动画结束
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        mZXingScannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void handleResult(Result result) {
        final String resultText = result.getText();
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new Exception(getString(R.string.need_permission));
                        }
                        return rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION);
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
                        if (aBoolean) {
                            //WIFI:S:bigbang24G;       T:WPA;     P:bigbangge     ;;
                            String[] split = resultText.split(";");
                            String[] split0 = split[0].split(":");
                            String[] split1 = split[1].split(":");
                            String[] split2 = split[2].split(":");
                            String encryption = split1[split1.length - 1];
                            String ssid = split0[split0.length - 1];
                            String password = split2[split2.length - 1];
                            if (mWifiScanCallback != null) {
                                mWifiScanCallback.onWifiScanCallback(ssid, password, encryption);
                            }
                        } else {
                            onError(new Exception(getString(R.string.need_permission)));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public interface WifiScanCallback {
        void onWifiScanCallback(String ssid, String password, String encryption);
    }
}
