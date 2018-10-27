package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.util.Log;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.bean.response.OrderOneResponse;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.model.MyOrderModel;
import com.wopin.qingpaopao.model.OldChangeNewContentDetailModel;
import com.wopin.qingpaopao.model.WeiXinPayModel;
import com.wopin.qingpaopao.model.WelfareModel;
import com.wopin.qingpaopao.view.OldChangeNewContentDetailView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OldChangeNewContentDetailPresenter extends BasePresenter<OldChangeNewContentDetailView> {

    private OldChangeNewContentDetailModel mOldChangeNewContentDetailModel;
    private MyOrderModel mMyOrderModel;
    private WeiXinPayModel mWeiXinPayModel;

    public OldChangeNewContentDetailPresenter(Context context, OldChangeNewContentDetailView view) {
        super(context, view);
        mOldChangeNewContentDetailModel = new OldChangeNewContentDetailModel();
        mMyOrderModel = new MyOrderModel();
        mWeiXinPayModel = new WeiXinPayModel();
    }

    public void getOldGoodList() {
        WelfareModel welfareModel = new WelfareModel();
        subscribeNetworkTask(getClass().getSimpleName().concat("getOldGoodList"),
                welfareModel.getProductContent(19)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                new MyObserver<ArrayList<ProductContent>>() {
                    @Override
                    public void onMyNext(ArrayList<ProductContent> productContents) {
                        mView.onOldGoodsList(productContents);
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void payMentExchange(String addressId, String title, String image, int goodsId, int number, int singlePrice, int offerPrice) {
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setAddressId(addressId);
        paymentBean.setTitle(title);
        paymentBean.setImage(image);
        paymentBean.setGoodsId(goodsId);
        paymentBean.setNum(number);
        paymentBean.setSinglePrice(singlePrice);
        paymentBean.setOfferPrice(offerPrice);
        subscribeNetworkTask(getClass().getSimpleName().concat("payMentExchange"), mOldChangeNewContentDetailModel.payMentExchange(paymentBean),
                new MyObserver<OrderOneResponse>() {
                    @Override
                    public void onMyNext(OrderOneResponse orderBean) {
//                        pay(orderBean.getResult());
                        mView.onPayMentExchangeSubmit(orderBean.getResult());
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void exchangeOrderUpdateAndPay(final Context context, final OrderBean orderBean, TrackingNumberSettingBean trackingNumberSettingBean) {
        subscribeNetworkTask(getClass().getSimpleName().concat("exchangeOrderUpdateAndPay"),
                mMyOrderModel.exchangeOrderUpdate(trackingNumberSettingBean),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        payOrderByWechat(context, orderBean);
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    private void payOrderByWechat(Context context, final OrderBean orderBean) {
        subscribeNetworkTask(getClass().getSimpleName().concat("payOrderByWechat"), mWeiXinPayModel.payOrderByWechat(context, orderBean), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onPaySuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(mContext.getString(R.string.pay_failure));
            }
        });
    }

    @Override
    public void destroy() {
        if (mWeiXinPayModel != null) {
            mWeiXinPayModel.removeWxActivityCallback();
        }
        super.destroy();
    }
}
