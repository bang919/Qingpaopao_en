package com.wopin.qingpaopao.fragment.welfare.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.OldChangeNewOrderListAdapter;
import com.wopin.qingpaopao.adapter.ScoreAndCrowdOrderListAdapter;
import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.dialog.NormalDialog;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.MyOrderPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MyOrderView;

public class MyOrderFragment extends BaseBarDialogFragment<MyOrderPresenter> implements View.OnClickListener, MyOrderView {

    public static final String TAG = "MyOrderFragment";
    private View mScoreChangeView;
    private View mOldChangeNewView;
    private View mCrowdFundingView;
    private RecyclerView mOrderListRv;
    private ScoreAndCrowdOrderListAdapter mScoreMarketOrderAdapter;
    private ScoreAndCrowdOrderListAdapter mCrowdfundingOrderAdapter;
    private OldChangeNewOrderListAdapter mOldChangeNewOrderListAdapter;

    @Override
    protected String setBarTitle() {
        return getString(R.string.my_order);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected MyOrderPresenter initPresenter() {
        return new MyOrderPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mOrderListRv = rootView.findViewById(R.id.rv_order_list);
        mScoreChangeView = rootView.findViewById(R.id.tv_score_change);
        mOldChangeNewView = rootView.findViewById(R.id.tv_old_change_new);
        mCrowdFundingView = rootView.findViewById(R.id.tv_crowd_funding);
        mScoreChangeView.setOnClickListener(this);
        mOldChangeNewView.setOnClickListener(this);
        mCrowdFundingView.setOnClickListener(this);
        setItemSelected(mScoreChangeView);

        mOrderListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mScoreMarketOrderAdapter = new ScoreAndCrowdOrderListAdapter() {
            @Override
            public int getPriceFormat() {
                return R.string.score_number;
            }
        };
        mCrowdfundingOrderAdapter = new ScoreAndCrowdOrderListAdapter() {
            @Override
            public int getPriceFormat() {
                return R.string.price_number;
            }
        };
        mOldChangeNewOrderListAdapter = new OldChangeNewOrderListAdapter();
        mOrderListRv.setAdapter(mScoreMarketOrderAdapter);
    }

    @Override
    protected void initEvent() {
        mPresenter.getOrderListDatas();
        setLoadingVisibility(true);
        mScoreMarketOrderAdapter.setScoreAndCrowdOrderListAdapterCallback(new ScoreAndCrowdOrderListAdapter.ScoreAndCrowdOrderListAdapterCallback() {
            @Override
            public void onFollwOrderBtnClick(OrderResponse.OrderBean orderBean) {
                OrderFollowOrderFragment.build(orderBean).show(getChildFragmentManager(), OrderFollowOrderFragment.TAG);
            }

            @Override
            public void onRemoveOrderBtnClick(OrderResponse.OrderBean orderBean) {
                //ScoreMarket不能删除订单
            }

            @Override
            public void onPaymentOrderBtnClick(OrderResponse.OrderBean orderBean) {
                //ScoreMarket不用付款
            }
        });
        mCrowdfundingOrderAdapter.setScoreAndCrowdOrderListAdapterCallback(new ScoreAndCrowdOrderListAdapter.ScoreAndCrowdOrderListAdapterCallback() {
            @Override
            public void onFollwOrderBtnClick(OrderResponse.OrderBean orderBean) {
                OrderFollowOrderFragment.build(orderBean).show(getChildFragmentManager(), OrderFollowOrderFragment.TAG);
            }

            @Override
            public void onRemoveOrderBtnClick(final OrderResponse.OrderBean orderBean) {
                new NormalDialog(getContext(), getString(R.string.confirm), getString(R.string.cancel), getString(R.string.remove_order), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.deleteOrder(orderBean.getOrderId());
                    }
                }, null).show();
            }

            @Override
            public void onPaymentOrderBtnClick(OrderResponse.OrderBean orderBean) {
                mPresenter.payOrder(orderBean);

            }
        });
        mOldChangeNewOrderListAdapter.setOldChangeNewOrderListAdapterCalblack(new OldChangeNewOrderListAdapter.OldChangeNewOrderListAdapterCalblack() {
            @Override
            public void onSetTrackingNumberBtnClick(final OrderResponse.OrderBean orderBean) {

                SetTrackingNumberFragment setTrackingNumberFragment = SetTrackingNumberFragment.build(orderBean.getExpressReturnId());
                setTrackingNumberFragment.setTrackingNumberSettingCallback(new SetTrackingNumberFragment.TrackingNumberSettingCallback() {
                    @Override
                    public void onTrackingNumberSetting(String trackingNumber) {
                        mPresenter.exchangeOrderUpdate(orderBean.getOrderId(), trackingNumber);
                    }
                });
                setTrackingNumberFragment.show(getChildFragmentManager(), SetTrackingNumberFragment.TAG);
            }

            @Override
            public void onOrderDetailBtnClick(final OrderResponse.OrderBean orderBean) {
                OldChangeNewOrderDetailFragment oldChangeNewOrderDetailFragment = OldChangeNewOrderDetailFragment.build(orderBean);
                oldChangeNewOrderDetailFragment.setOldChangeNewOrderDetailCallback(new OldChangeNewOrderDetailFragment.OldChangeNewOrderDetailCallback() {
                    @Override
                    public void onRemoveOrderBtnClick() {
                        mPresenter.deleteOrder(orderBean.getOrderId());
                    }
                });
                oldChangeNewOrderDetailFragment.show(getChildFragmentManager(), OldChangeNewOrderDetailFragment.TAG);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_score_change:
                setItemSelected(v);
                mOrderListRv.setAdapter(mScoreMarketOrderAdapter);
                break;
            case R.id.tv_old_change_new:
                mOrderListRv.setAdapter(mOldChangeNewOrderListAdapter);
                setItemSelected(v);
                break;
            case R.id.tv_crowd_funding:
                setItemSelected(v);
                mOrderListRv.setAdapter(mCrowdfundingOrderAdapter);
                break;
        }
    }

    private void setItemSelected(View view) {
        mScoreChangeView.setSelected(mScoreChangeView == view);
        mOldChangeNewView.setSelected(mOldChangeNewView == view);
        mCrowdFundingView.setSelected(mCrowdFundingView == view);
    }

    @Override
    public void onScoresOrder(OrderResponse scoreOrder) {
        mScoreMarketOrderAdapter.setOrderBeans(scoreOrder.getOrderBeans());
    }

    @Override
    public void onExchangeOrder(OrderResponse exchangeOrder) {
        mOldChangeNewOrderListAdapter.setOrderBeans(exchangeOrder.getOrderBeans());
    }

    @Override
    public void onCrowdfundingOrder(OrderResponse crowdfundingOrder) {
        mCrowdfundingOrderAdapter.setOrderBeans(crowdfundingOrder.getOrderBeans());
    }

    @Override
    public void onDataResponseSuccess() {
        setLoadingVisibility(false);
    }

    @Override
    public void onDataRefresh() {
        setLoadingVisibility(true);
        mPresenter.getOrderListDatas();
    }

    @Override
    public void onError(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }
}
