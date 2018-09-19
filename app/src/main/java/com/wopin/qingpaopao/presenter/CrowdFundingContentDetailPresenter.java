package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalMoneyRsp;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalPeopleRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.CrowdFundingContentDetailModel;
import com.wopin.qingpaopao.model.WelfareModel;
import com.wopin.qingpaopao.view.CrowdFundingDetailView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class CrowdFundingContentDetailPresenter extends BasePresenter<CrowdFundingDetailView> {

    private CrowdFundingContentDetailModel mCrowdFundingContentDetailModel;
    private WelfareModel mWelfareModel;

    public CrowdFundingContentDetailPresenter(Context context, CrowdFundingDetailView view) {
        super(context, view);
        mCrowdFundingContentDetailModel = new CrowdFundingContentDetailModel();
        mWelfareModel = new WelfareModel();
    }

    public void payMentCrowdfunding(String addressId, String title, String image, int goodsId, int number, int singlePrice) {
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setAddressId(addressId);
        paymentBean.setTitle(title);
        paymentBean.setImage(image);
        paymentBean.setGoodsId(goodsId);
        paymentBean.setNum(number);
        paymentBean.setSinglePrice(singlePrice);
        subscribeNetworkTask(getClass().getSimpleName().concat("payMentCrowdfunding"), mCrowdFundingContentDetailModel.payMentCrowdfunding(paymentBean),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        mView.onCrowdFundingPayMentSuccess();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void crowdfundingOrderTotalMoneyAndPeople(int goodsId, final String goodsPrice) {
        subscribeNetworkTask(getClass().getSimpleName().concat("crowdfundingOrderTotalMoneyAndPeople"), Observable.zip(
                mWelfareModel.crowdfundingOrderTotalMoney(goodsId)
                        .doOnNext(new Consumer<CrowdfundingOrderTotalMoneyRsp>() {
                            @Override
                            public void accept(CrowdfundingOrderTotalMoneyRsp crowdfundingOrderTotalMoneyRsp) throws Exception {
                                float currentPrice = 0;
                                for (CrowdfundingOrderTotalMoneyRsp.ResultBean r : crowdfundingOrderTotalMoneyRsp.getResult()) {
                                    currentPrice += r.getTotalPrice();
                                }
                                float percent = currentPrice / Float.valueOf(goodsPrice);
                                mView.onCrowdFundingPrice(goodsPrice, String.valueOf(currentPrice), percent);
                            }
                        }),
                mWelfareModel.crowdfundingOrderTotalPeople(goodsId)
                        .doOnNext(new Consumer<CrowdfundingOrderTotalPeopleRsp>() {
                            @Override
                            public void accept(CrowdfundingOrderTotalPeopleRsp crowdfundingOrderTotalMoneyRsp) throws Exception {
                                int supportCount = 0;
                                for (CrowdfundingOrderTotalPeopleRsp.ResultBean r : crowdfundingOrderTotalMoneyRsp.getResult()) {
                                    supportCount += r.getTotalPeople();
                                }
                                mView.onCrowdFundingSupportCount(supportCount);
                            }
                        }),
                new BiFunction<CrowdfundingOrderTotalMoneyRsp, CrowdfundingOrderTotalPeopleRsp, String>() {
                    @Override
                    public String apply(CrowdfundingOrderTotalMoneyRsp crowdfundingOrderTotalMoneyRsp, CrowdfundingOrderTotalPeopleRsp crowdfundingOrderTotalPeopleRsp) throws Exception {
                        return "Success";
                    }
                }), new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onDataSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
