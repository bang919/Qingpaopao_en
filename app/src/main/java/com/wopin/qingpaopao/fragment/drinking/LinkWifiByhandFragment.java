package com.wopin.qingpaopao.fragment.drinking;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.WifiCupPostModel;
import com.wopin.qingpaopao.presenter.BasePresenter;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LinkWifiByhandFragment extends BaseBarDialogFragment {

    public static final String TAG = "LinkWifiByhandFragment";
    private LinkWifiByhandCallback mLinkWifiByhandCallback;
    private WifiCupPostModel mWifiCupPostModel;
    private Disposable mDisposable;

    public void setLinkWifiByhandCallback(LinkWifiByhandCallback linkWifiByhandCallback) {
        mLinkWifiByhandCallback = linkWifiByhandCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.link_wifi);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_link_wifi_byhand;
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
        mWifiCupPostModel = new WifiCupPostModel();
        mWifiCupPostModel.getWifiList().subscribe(new Observer<ArrayList<WifiRsp>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(ArrayList<WifiRsp> wifiRsps) {
                if (wifiRsps != null && mLinkWifiByhandCallback != null) {
                    mLinkWifiByhandCallback.onWifiLinkByhand();
                    dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                mDisposable = null;
            }

            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        super.onDestroy();
    }

    public interface LinkWifiByhandCallback {
        void onWifiLinkByhand();
    }
}
