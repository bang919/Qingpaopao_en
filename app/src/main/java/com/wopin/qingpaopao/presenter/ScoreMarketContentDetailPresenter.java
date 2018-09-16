package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.ScoreMarketPayment;
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
        ScoreMarketPayment scoreMarketPayment = new ScoreMarketPayment();
        scoreMarketPayment.setAddressId(addressId);
        scoreMarketPayment.setTitle(title);
        scoreMarketPayment.setImage(image);
        scoreMarketPayment.setGoodsId(goodsId);
        scoreMarketPayment.setNum(number);
        scoreMarketPayment.setSinglePrice(singlePrice);
        subscribeNetworkTask(getClass().getSimpleName().concat("payMentScores"), mScoreMarketContentDetailModel.payMentScores(scoreMarketPayment),
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
