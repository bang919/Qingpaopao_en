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
    private EditText mTrackingNumberEt, mTrackingCompanyEt, mNameEt, mSexEt, mPhoneNumberEt, mCupTypeEt, mCupColorEt, mBuyTimeEt, mUseStatusEt;
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
        mSexEt = rootView.findViewById(R.id.et_input_sex);
        mPhoneNumberEt = rootView.findViewById(R.id.et_input_phone_number);
        mCupTypeEt = rootView.findViewById(R.id.et_input_cup_type);
        mCupColorEt = rootView.findViewById(R.id.et_input_cup_color);
        mBuyTimeEt = rootView.findViewById(R.id.et_input_buy_time);
        mUseStatusEt = rootView.findViewById(R.id.et_input_use_status);
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
        if (!TextUtils.isEmpty(mOrderBean.getInfoSex())) {
            mSexEt.setText(mOrderBean.getInfoSex());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoPhone())) {
            mPhoneNumberEt.setText(mOrderBean.getInfoPhone());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoCupModel())) {
            mCupTypeEt.setText(mOrderBean.getInfoCupModel());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoCupColor())) {
            mCupColorEt.setText(mOrderBean.getInfoCupColor());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoBuyTime())) {
            mBuyTimeEt.setText(mOrderBean.getInfoBuyTime());
        }
        if (!TextUtils.isEmpty(mOrderBean.getInfoUsage())) {
            mUseStatusEt.setText(mOrderBean.getInfoUsage());
        }
        if (mOrderBean.getOrderStatus().equals("等待付款")) {
            mAffirmBtn.setVisibility(View.VISIBLE);
        } else {
            mAffirmBtn.setVisibility(View.GONE);
            mTrackingNumberEt.setFocusable(false);
            mTrackingNumberEt.setFocusableInTouchMode(false);
            mTrackingCompanyEt.setFocusable(false);
            mTrackingCompanyEt.setFocusableInTouchMode(false);
            mNameEt.setFocusable(false);
            mNameEt.setFocusableInTouchMode(false);
            mSexEt.setFocusable(false);
            mSexEt.setFocusableInTouchMode(false);
            mPhoneNumberEt.setFocusable(false);
            mPhoneNumberEt.setFocusableInTouchMode(false);
            mCupTypeEt.setFocusable(false);
            mCupTypeEt.setFocusableInTouchMode(false);
            mCupColorEt.setFocusable(false);
            mCupColorEt.setFocusableInTouchMode(false);
            mBuyTimeEt.setFocusable(false);
            mBuyTimeEt.setFocusableInTouchMode(false);
            mUseStatusEt.setFocusable(false);
            mUseStatusEt.setFocusableInTouchMode(false);
        }
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
                String sex = mSexEt.getText().toString();
                if (TextUtils.isEmpty(sex)) {
                    ToastUtils.showShort(R.string.please_input_sex);
                    return;
                }
                String phoneNumber = mPhoneNumberEt.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showShort(R.string.please_input_phone_number);
                    return;
                }
                String cupType = mCupTypeEt.getText().toString();
                if (TextUtils.isEmpty(cupType)) {
                    ToastUtils.showShort(R.string.please_input_cup_type);
                    return;
                }
                String cupColor = mCupColorEt.getText().toString();
                if (TextUtils.isEmpty(cupColor)) {
                    ToastUtils.showShort(R.string.please_input_cup_color);
                    return;
                }
                String buyTime = mBuyTimeEt.getText().toString();
                if (TextUtils.isEmpty(buyTime)) {
                    ToastUtils.showShort(R.string.please_input_buy_time);
                    return;
                }
                String useStatus = mUseStatusEt.getText().toString();
                if (TextUtils.isEmpty(useStatus)) {
                    ToastUtils.showShort(R.string.please_input_use_status);
                    return;
                }
                if (mTrackingNumberSettingCallback != null) {
                    TrackingNumberSettingBean trackingNumberSettingBean = new TrackingNumberSettingBean();
                    trackingNumberSettingBean.setOrderId(mOrderBean.getOrderId());
                    trackingNumberSettingBean.setExpressId(trackingNumber);
                    trackingNumberSettingBean.setExpressName(trackingCompany);
                    trackingNumberSettingBean.setInfoUserName(name);
                    trackingNumberSettingBean.setInfoSex(sex);
                    trackingNumberSettingBean.setInfoPhone(phoneNumber);
                    trackingNumberSettingBean.setInfoCupModel(cupType);
                    trackingNumberSettingBean.setInfoCupColor(cupColor);
                    trackingNumberSettingBean.setInfoBuyTime(buyTime);
                    trackingNumberSettingBean.setInfoUsage(useStatus);
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
