package com.wopin.qingpaopao.fragment.welfare.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.dialog.NormalDialog;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.GlideUtils;

public class OldChangeNewOrderDetailFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "OldChangeNewOrderDetailFragment";
    private Button mFollowOrderBtn;
    private Button mRemoveOrderBtn;
    private Button mPaymentBtn;
    private OldChangeNewOrderDetailCallback mOldChangeNewOrderDetailCallback;
    private OrderResponse.OrderBean mOrderBean;

    public static OldChangeNewOrderDetailFragment build(OrderResponse.OrderBean orderBean) {
        OldChangeNewOrderDetailFragment oldChangeNewOrderDetailFragment = new OldChangeNewOrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, orderBean);
        oldChangeNewOrderDetailFragment.setArguments(args);
        return oldChangeNewOrderDetailFragment;
    }

    public void setOldChangeNewOrderDetailCallback(OldChangeNewOrderDetailCallback oldChangeNewOrderDetailCallback) {
        mOldChangeNewOrderDetailCallback = oldChangeNewOrderDetailCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.order_detail);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_old_change_new_order_detail;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mOrderBean = (OrderResponse.OrderBean) getArguments().getSerializable(TAG);
        ((TextView) rootView.findViewById(R.id.tv_order_status)).setText(mOrderBean.getOrderStatus());
        ((TextView) rootView.findViewById(R.id.tv_order_id)).setText(getString(R.string.order_id, mOrderBean.get_id()));
        ((TextView) rootView.findViewById(R.id.tv_order_time)).setText(getString(R.string.order_time, mOrderBean.getCreateDate()));
        GlideUtils.loadImage((ImageView) rootView.findViewById(R.id.iv_order_image), -1, mOrderBean.getImage(), new CenterCrop());
        ((TextView) rootView.findViewById(R.id.tv_name)).setText(mOrderBean.getTitle());
        ((TextView) rootView.findViewById(R.id.tv_price)).setText(getString(R.string.price_number, String.valueOf(mOrderBean.getFinalPrice())));
        OrderResponse.OrderBean.AddressBean addressBean = mOrderBean.getAddress();
        ((TextView) rootView.findViewById(R.id.receiver_message)).setText(getString(R.string.receiver_message, addressBean.getUserName(), addressBean.getTel(),
                addressBean.getAddress1() + addressBean.getAddress2()));

        mFollowOrderBtn = rootView.findViewById(R.id.btn_follow_order);
        mRemoveOrderBtn = rootView.findViewById(R.id.remove_order);
        mPaymentBtn = rootView.findViewById(R.id.payment);

        if (mOrderBean.getOrderStatus().equals("等待付款")) {
            mRemoveOrderBtn.setVisibility(View.VISIBLE);
            mRemoveOrderBtn.setOnClickListener(this);
            mPaymentBtn.setVisibility(View.VISIBLE);
            mPaymentBtn.setOnClickListener(this);
        } else if (mOrderBean.getOrderStatus().equals("待发货")) {
        } else if (mOrderBean.getOrderStatus().equals("已发货")) {
            mFollowOrderBtn.setVisibility(View.VISIBLE);
            mFollowOrderBtn.setOnClickListener(this);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_order://删除订单
                if (mOldChangeNewOrderDetailCallback != null) {
                    new NormalDialog(getContext(), getString(R.string.confirm), getString(R.string.cancel), getString(R.string.remove_order), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOldChangeNewOrderDetailCallback.onRemoveOrderBtnClick();
                            dismiss();
                        }
                    }, null).show();
                }
                break;
            case R.id.btn_follow_order://查看物流
                OldChangeNewOrderFollowOrderFragment.build(mOrderBean.getOrderId()).show(getChildFragmentManager(), OldChangeNewOrderFollowOrderFragment.TAG);
                break;
            case R.id.payment://付款
                break;
        }
    }

    public interface OldChangeNewOrderDetailCallback {
        void onRemoveOrderBtnClick();
    }
}
