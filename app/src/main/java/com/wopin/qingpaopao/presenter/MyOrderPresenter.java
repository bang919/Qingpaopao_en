package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.bean.response.OrderListResponse;
import com.wopin.qingpaopao.model.MyOrderModel;
import com.wopin.qingpaopao.model.WeiXinPayModel;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MyOrderView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class MyOrderPresenter extends BasePresenter<MyOrderView> {

    private MyOrderModel mMyOrderModel;
    private WeiXinPayModel mWeiXinPayModel;

    public MyOrderPresenter(Context context, MyOrderView view) {
        super(context, view);
        mMyOrderModel = new MyOrderModel();
        mWeiXinPayModel = new WeiXinPayModel();
    }

    public void getOrderListDatas() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getOrderListDatas"), Observable.zip(
                mMyOrderModel.getScoresOrder()
                        .doOnNext(new Consumer<OrderListResponse>() {
                            @Override
                            public void accept(OrderListResponse orderListResponse) throws Exception {
                                mView.onScoresOrder(orderListResponse);
                            }
                        }),
                mMyOrderModel.getExchangeOrder()
                        .doOnNext(
                                new Consumer<OrderListResponse>() {
                                    @Override
                                    public void accept(OrderListResponse orderListResponse) throws Exception {
                                        mView.onExchangeOrder(orderListResponse);
                                    }
                                }
                        ),
                mMyOrderModel.getCrowdfundingOrder()
                        .doOnNext(new Consumer<OrderListResponse>() {
                            @Override
                            public void accept(OrderListResponse orderListResponse) throws Exception {
                                mView.onCrowdfundingOrder(orderListResponse);
                            }
                        }),
                new Function3<OrderListResponse, OrderListResponse, OrderListResponse, String>() {
                    @Override
                    public String apply(OrderListResponse normalRsp, OrderListResponse normalRsp2, OrderListResponse normalRsp3) throws Exception {
                        return "success";
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                new MyObserver<String>() {
                    @Override
                    public void onMyNext(String s) {
                        mView.onDataResponseSuccess();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void exchangeOrderUpdate(String orderId, String trackingNumber) {
        TrackingNumberSettingBean trackingNumberSettingBean = new TrackingNumberSettingBean();
        trackingNumberSettingBean.setOrderId(orderId);
        trackingNumberSettingBean.setExpressId(trackingNumber);
        subscribeNetworkTask(getClass().getSimpleName().concat("exchangeOrderUpdate"), mMyOrderModel.exchangeOrderUpdate(trackingNumberSettingBean),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        ToastUtils.showShort(R.string.submit_success);
                        mView.onDataRefresh();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void deleteOrder(String orderId) {
        subscribeNetworkTask(getClass().getSimpleName().concat("deleteOrder"), mMyOrderModel.deleteOrder(orderId), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                ToastUtils.showShort(R.string.cancel_success);
                mView.onDataRefresh();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void payOrderByWechat(Context context, final OrderBean orderBean) {
        subscribeNetworkTask(getClass().getSimpleName().concat("payOrderByWechat"), mWeiXinPayModel.payOrderByWechat(context, orderBean), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                ToastUtils.showShort(R.string.pay_success);
                getOrderListDatas();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    @Override

    public void destroy() {
        mWeiXinPayModel.removeWxActivityCallback();
        super.destroy();
    }


}
