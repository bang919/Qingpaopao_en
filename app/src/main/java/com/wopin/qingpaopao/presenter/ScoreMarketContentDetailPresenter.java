package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.ScoreMarketContentDetailModel;
import com.wopin.qingpaopao.view.ScoreMarketContentDetailView;

public class ScoreMarketContentDetailPresenter extends BasePresenter<ScoreMarketContentDetailView> {

    private ScoreMarketContentDetailModel mScoreMarketContentDetailModel;

    public ScoreMarketContentDetailPresenter(Context context, ScoreMarketContentDetailView view) {
        super(context, view);
        mScoreMarketContentDetailModel = new ScoreMarketContentDetailModel();
    }

    public void payMentScores(String addressId, String title, String image, int goodsId, int number, int singlePrice) {
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setAddressId(addressId);
        paymentBean.setTitle(title);
        paymentBean.setImage(image);
        paymentBean.setGoodsId(goodsId);
        paymentBean.setNum(number);
        paymentBean.setSinglePrice(singlePrice);
        subscribeNetworkTask(getClass().getSimpleName().concat("payMentScores"), mScoreMarketContentDetailModel.payMentScores(paymentBean),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        mView.onExchangeSuccess();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
