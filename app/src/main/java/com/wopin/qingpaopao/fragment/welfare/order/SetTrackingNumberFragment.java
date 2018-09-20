package com.wopin.qingpaopao.fragment.welfare.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;

public class SetTrackingNumberFragment extends BaseBarDialogFragment {

    public static final String TAG = "SetTrackingNumberFragment";
    private EditText mTrackingNumberEt;
    private Button mAffirmBtn;
    private TrackingNumberSettingCallback mTrackingNumberSettingCallback;

    public static SetTrackingNumberFragment build(String expressId) {
        SetTrackingNumberFragment setTrackingNumberFragment = new SetTrackingNumberFragment();
        Bundle args = new Bundle();
        args.putString(TAG, expressId);
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
        mAffirmBtn = rootView.findViewById(R.id.bt_confirm);
        String expressId = getArguments().getString(TAG);
        if (!TextUtils.isEmpty(expressId)) {
            mTrackingNumberEt.setText(expressId);
        }
    }

    @Override
    protected void initEvent() {
        mAffirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mTrackingNumberEt.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showShort(R.string.please_input_tracking_number);
                    return;
                }
                if (mTrackingNumberSettingCallback != null) {
                    mTrackingNumberSettingCallback.onTrackingNumberSetting(s);
                    dismiss();
                }
            }
        });
    }

    public interface TrackingNumberSettingCallback {
        void onTrackingNumberSetting(String trackingNumber);
    }
}
