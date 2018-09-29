package com.wopin.qingpaopao.model;

import android.content.Context;
import android.text.TextUtils;

import com.mywopin.com.cup.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.GetWechatPayReq;
import com.wopin.qingpaopao.bean.request.PaySuccessChangeOrderReq;
import com.wopin.qingpaopao.bean.response.GetWechatPayResponse;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeiXinPayModel {

    private MyWXPayEntryActivityCallback mWxPayEntryActivityCallback;

    public WeiXinPayModel() {
        mWxPayEntryActivityCallback = new MyWXPayEntryActivityCallback();
    }

    // http://wifi.h2popo.com:8081/getWeChatPaySign
    public Observable<NormalRsp> payOrderByWechat(final Context context, final OrderBean orderBean) {
        final GetWechatPayReq getWechatPayReq = new GetWechatPayReq();
        float amout = Float.valueOf("0" + orderBean.getSinglePrice()) * Integer.valueOf(orderBean.getNum());
        if (!TextUtils.isEmpty(orderBean.getOfferPrice())) {
            amout = amout - Integer.valueOf(orderBean.getOfferPrice());
        }
        getWechatPayReq.setAmount(amout);
        getWechatPayReq.setSubject(orderBean.getAddress().getAddress1().concat(orderBean.getAddress().getAddress2()));
        getWechatPayReq.setBody(orderBean.getTitle());
        getWechatPayReq.setOrderId(orderBean.getOrderId());
        return HttpClient.getApiInterface().getWeChatPaySign(getWechatPayReq)
                .flatMap(new Function<GetWechatPayResponse, ObservableSource<BaseResp>>() {
                    @Override
                    public ObservableSource<BaseResp> apply(final GetWechatPayResponse getWechatPayResponse) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<BaseResp>() {
                            @Override
                            public void subscribe(final ObservableEmitter<BaseResp> emitter) throws Exception {
                                if (!pay(context, getWechatPayResponse)) {
                                    emitter.onError(new Throwable(context.getString(R.string.pay_error)));
                                } else {
                                    addWxActivityCallback(emitter);
                                }
                            }
                        });
                    }
                })
                .flatMap(new Function<BaseResp, ObservableSource<NormalRsp>>() {
                    @Override
                    public ObservableSource<NormalRsp> apply(BaseResp baseResp) throws Exception {
                        int errCode = baseResp.errCode;
                        if (errCode == -1) {
                            throw new Exception(context.getString(R.string.pay_error));
                        } else if (errCode == -2) {
                            throw new Exception(context.getString(R.string.pay_cancel));
                        }
                        return paySuccessChangeOrderStatus(orderBean.getOrderId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private boolean pay(Context context, GetWechatPayResponse getWechatPayResponse) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
// 将该app注册到微信
        msgApi.registerApp(Constants.WECHAT_APPID);

        PayReq request = new PayReq();
        request.appId = Constants.WECHAT_APPID;

        GetWechatPayResponse.WechatPayBean wechatPayBean = getWechatPayResponse.getWechatPayBean();
        request.partnerId = wechatPayBean.getPartnerid();
        request.prepayId = wechatPayBean.getPrepayid();
        request.packageValue = wechatPayBean.getPackageX();
        request.nonceStr = wechatPayBean.getNoncestr();
        request.timeStamp = wechatPayBean.getTimestamp();
        request.sign = wechatPayBean.getSign();
        return msgApi.sendReq(request);
    }

    private Observable<NormalRsp> paySuccessChangeOrderStatus(String orderId) {
        PaySuccessChangeOrderReq paySuccessChangeOrderReq = new PaySuccessChangeOrderReq();
        paySuccessChangeOrderReq.setOrderId(orderId);
        paySuccessChangeOrderReq.setStatus(1);
        return HttpClient.getApiInterface().paySuccessChangeOrderStatus(paySuccessChangeOrderReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void addWxActivityCallback(ObservableEmitter<BaseResp> observableEmitter) {
        mWxPayEntryActivityCallback.setCurrentObservableEmitter(observableEmitter);
        WXPayEntryActivity.putCallback(mWxPayEntryActivityCallback);
    }

    public void removeWxActivityCallback() {
        WXPayEntryActivity.removeCallback(mWxPayEntryActivityCallback);
    }

    class MyWXPayEntryActivityCallback implements WXPayEntryActivity.WXPayEntryActivityCallback {

        private ObservableEmitter<BaseResp> mObservableEmitter;

        public void setCurrentObservableEmitter(ObservableEmitter<BaseResp> observableEmitter) {
            mObservableEmitter = observableEmitter;
        }

        @Override
        public void onResp(BaseResp resp) {
            if (mObservableEmitter != null) {
                mObservableEmitter.onNext(resp);
                mObservableEmitter = null;
            }
            removeWxActivityCallback();
        }
    }
}
