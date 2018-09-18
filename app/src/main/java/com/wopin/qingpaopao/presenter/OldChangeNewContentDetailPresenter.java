package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.model.OldChangeNewContentDetailModel;
import com.wopin.qingpaopao.model.WelfareModel;
import com.wopin.qingpaopao.view.OldChangeNewContentDetailView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OldChangeNewContentDetailPresenter extends BasePresenter<OldChangeNewContentDetailView> {

    private OldChangeNewContentDetailModel mOldChangeNewContentDetailModel;

    public OldChangeNewContentDetailPresenter(Context context, OldChangeNewContentDetailView view) {
        super(context, view);
        mOldChangeNewContentDetailModel = new OldChangeNewContentDetailModel();
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
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        mView.onPayMentExchangeSubmit();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
