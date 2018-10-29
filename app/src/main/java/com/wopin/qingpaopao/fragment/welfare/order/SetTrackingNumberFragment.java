package com.wopin.qingpaopao.fragment.welfare.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;

public class SetTrackingNumberFragment extends BaseBarDialogFragment {

    public static final String TAG = "SetTrackingNumberFragment";
    private EditText mTrackingNumberEt, mTrackingCompanyEt, mNameEt, mPhoneNumberEt;
    private Button mAffirmBtn;
    private OrderBean mOrderBean;
    private TrackingNumberSettingCallback mTrackingNumberSettingCallback;

    public static SetTrackingNumberFragment build(OrderBean orderBean) {
        SetTrackingNumberFragment setTrackingNumberFragment = new SetTrackingNumberFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, orderBean);
        setTrackingNumberFragment.setArguments(args);
        return setTrackingNumberFragment;
    }

    public void setTrackingNumberSettingCallback(TrackingNumberSettingCallback trackingNumberSettingCallback) {
        mTrackingNumberSettingCallback = trackingNumberSettingCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.input_order_message);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_set_tracking_number;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mTrackingNumberEt = rootView.findViewById(R.id.et_input_tracking_number);
        mTrackingCompanyEt = rootView.findViewById(R.id.et_input_tracking_company);
        mNameEt = rootView.findViewById(R.id.et_input_name);
        mPhoneNumberEt = rootView.findViewById(R.id.et_input_phone_number);
        mAffirmBtn = rootView.findViewById(R.id.bt_confirm);
        mOrderBean = (OrderBean) getArguments().getSerializable(TAG);
        if (!TextUtils.isEmpty(mOrderBean.getExpressReturnId())) {
            mTrackingNumberEt.setText(mOrderBean.getExpressReturnId());
        }
        if (!TextUtils.isEmpty(mOrderBean.getExpressReturnName())) {
            mTrackingCompanyEt.setText(mOrderBean.getExpressReturnName());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoUserName())) {
            mNameEt.setText(mOrderBean.getInfoUserName());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoPhone())) {
            mPhoneNumberEt.setText(mOrderBean.getInfoPhone());
        }

//        if (mOrderBean.getOrderStatus().equals("等待付款")) {
//            mAffirmBtn.setVisibility(View.VISIBLE);
//        } else {
//            mAffirmBtn.setVisibility(View.GONE);
//            mTrackingNumberEt.setFocusable(false);
//            mTrackingNumberEt.setFocusableInTouchMode(false);
//            mTrackingCompanyEt.setFocusable(false);
//            mTrackingCompanyEt.setFocusableInTouchMode(false);
//            mNameEt.setFocusable(false);
//            mNameEt.setFocusableInTouchMode(false);
//            mPhoneNumberEt.setFocusable(false);
//            mPhoneNumberEt.setFocusableInTouchMode(false);
//        }
    }

    @Override
    protected void initEvent() {
        mAffirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trackingNumber = mTrackingNumberEt.getText().toString();
                if (TextUtils.isEmpty(trackingNumber)) {
                    ToastUtils.showShort(R.string.please_input_tracking_number);
                    return;
                }
                String trackingCompany = mTrackingCompanyEt.getText().toString();
                if (TextUtils.isEmpty(trackingCompany)) {
                    ToastUtils.showShort(R.string.please_input_tracking_company);
                    return;
                }
                String name = mNameEt.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort(R.string.please_input_real_name);
                    return;
                }
                String phoneNumber = mPhoneNumberEt.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showShort(R.string.please_input_phone_number);
                    return;
                }
                if (mTrackingNumberSettingCallback != null) {
                    TrackingNumberSettingBean trackingNumberSettingBean = new TrackingNumberSettingBean();
                    trackingNumberSettingBean.setOrderId(mOrderBean.getOrderId());
                    trackingNumberSettingBean.setExpressId(trackingNumber);
                    trackingNumberSettingBean.setExpressName(trackingCompany);
                    trackingNumberSettingBean.setInfoUserName(name);
                    trackingNumberSettingBean.setInfoPhone(phoneNumber);
                    mTrackingNumberSettingCallback.onTrackingNumberSetting(trackingNumberSettingBean);
                    dismiss();
                }
            }
        });
    }

    public interface TrackingNumberSettingCallback {
        void onTrackingNumberSetting(TrackingNumberSettingBean trackingNumberSettingBean);
    }
}
