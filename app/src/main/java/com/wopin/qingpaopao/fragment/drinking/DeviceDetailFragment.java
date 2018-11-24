package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.PopupWindowListAdapter;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.SigninCheckRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.dialog.EditCupNameDialog;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.manager.MessageProxy;
import com.wopin.qingpaopao.manager.MessageProxyCallback;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.NotificationSettingUtil;
import com.wopin.qingpaopao.utils.PopupWindowUtil;
import com.wopin.qingpaopao.utils.SPUtils;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DeviceDetailFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "DeviceDetailFragment";
    private CupListRsp.CupBean mCupBean;
    private TextView mElectricTv;
    private TextView mDeviceNameTv;
    private TextView mCupColorTv;
    private TextView mDeviceStatusTv;
    private TextView mDeviceStatusTime;
    private SwitchCompat mNotificationSwitch;
    private TextView mActivateTv;
    private DeviceDetailFragmentCallback mDeviceDetailFragmentCallback;
    private Disposable checkSigninDisposable;

    public static DeviceDetailFragment getDeviceDetailFragment(CupListRsp.CupBean cupBean, DeviceDetailFragmentCallback deviceDetailFragmentCallback) {
        DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, cupBean);
        deviceDetailFragment.setArguments(args);
        deviceDetailFragment.setDeviceDetailFragmentCallback(deviceDetailFragmentCallback);
        return deviceDetailFragment;
    }

    private void setDeviceDetailFragmentCallback(DeviceDetailFragmentCallback deviceDetailFragmentCallback) {
        mDeviceDetailFragmentCallback = deviceDetailFragmentCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.device_manager);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_device_manager;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mCupBean = getArguments().getParcelable(TAG);
        mElectricTv = rootView.findViewById(R.id.value_electric);
        mDeviceNameTv = rootView.findViewById(R.id.value_device_name);
        mCupColorTv = rootView.findViewById(R.id.value_cup_color);
        mDeviceStatusTv = rootView.findViewById(R.id.value_device_status);
        mDeviceStatusTime = rootView.findViewById(R.id.value_device_time);
        mNotificationSwitch = rootView.findViewById(R.id.switch_notification);
        mNotificationSwitch.setChecked((Boolean) SPUtils.get(getContext(), Constants.DRINKING_NOTIFICATION, false));

        mActivateTv = rootView.findViewById(R.id.tv_activate);
    }

    private void setActivateBtnEnable(boolean enable) {
        mActivateTv.setVisibility(View.VISIBLE);
        if (enable) {
            mActivateTv.setText(R.string.activate);
            mActivateTv.setClickable(true);
            mActivateTv.setOnClickListener(this);
        } else {
            mActivateTv.setText(R.string.activated);
            mActivateTv.setClickable(false);
        }
    }

    @Override
    protected void initEvent() {
        mDeviceNameTv.setText(mCupBean.getName());
        mDeviceNameTv.setOnClickListener(this);
        mCupColorTv.setText(Constants.CUP_COLOR_NAME[mCupBean.getColor()]);
        mCupColorTv.setOnClickListener(this);
        if (mCupBean.isConnecting()) {
            mDeviceStatusTv.setText(R.string.bind);
            mDeviceStatusTv.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            mElectricTv.setText(TextUtils.isEmpty(mCupBean.getElectric()) ? "0%" : mCupBean.getElectric());
            MessageProxy.addMessageProxyCallback(mCupBean.getUuid(), new MessageProxyCallback() {
                @Override//电池
                public void onBattery(String uuid, int battery) {
                    mElectricTv.setText(String.valueOf(battery + "%"));
                }

                @Override//电解时间
                public void onTime(String uuid, String minute, String second) {
                    mDeviceStatusTime.setText(minute + ":" + second);
                }

                @Override//电解中
                public void onElectrolyzing(String uuid) {
                    mDeviceStatusTv.setText(R.string.electrolyzing);
                }

                @Override//电解结束
                public void onElectrolyzeEnd(String uuid) {
                    mDeviceStatusTv.setText(R.string.bind);
                    mDeviceStatusTime.setText("-");
                }


                //冲洗中
                public void onCleaning(String uuid) {
                    mDeviceStatusTv.setText(R.string.cleaning);
                }

                //冲洗结束
                public void onCleaneEnd(String uuid) {
                    mDeviceStatusTv.setText(R.string.bind);
                    mDeviceStatusTime.setText("-");
                }
            });
        }
        mNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NotificationSettingUtil.startNotification(getContext());
                } else {
                    NotificationSettingUtil.stopNotification(getContext());
                }
            }
        });

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"cupId\":\"" + mCupBean.getUuid() + "\"}");
        HttpClient.getApiInterface()
                .checkSigninCup(requestBody)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SigninCheckRsp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        checkSigninDisposable = d;
                    }

                    @Override
                    public void onNext(SigninCheckRsp signinCheckRsp) {
                        setActivateBtnEnable(!signinCheckRsp.getResult().isSigninCup());
                    }

                    @Override
                    public void onError(Throwable e) {
                        checkSigninDisposable = null;
                    }

                    @Override
                    public void onComplete() {
                        checkSigninDisposable = null;
                    }
                });
    }

    @Override
    public void onDestroy() {
        MessageProxy.clearMessageProxyCallback();
        if (checkSigninDisposable != null) {
            checkSigninDisposable.dispose();
            checkSigninDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.value_device_name: {
                new EditCupNameDialog(getContext(), mCupBean.getName(), new EditCupNameDialog.OnEditNickNameDialogCallback() {
                    @Override
                    public void onEditNickNameDialog(String newNickName) {
                        if (mDeviceDetailFragmentCallback != null) {
                            mDeviceDetailFragmentCallback.onRename(newNickName);
                            mDeviceNameTv.setText(newNickName);
                        }
                    }
                }).show();
                break;
            }
            case R.id.value_cup_color: {
                ArrayList<String> colors = new ArrayList<>();
                for (int colorInt : Constants.CUP_COLOR_NAME) {
                    colors.add(getString(colorInt));
                }
                PopupWindowUtil.buildListPpp(v, colors, 150, 500, new PopupWindowListAdapter.PopupWindowListAdapterCallback() {
                    @Override
                    public void onItemClick(String name, int position) {
                        mDeviceDetailFragmentCallback.onColorChange(Constants.CUP_COLOR_INT[position]);
                        mCupColorTv.setText(name);
                    }
                });
                break;
            }
            case R.id.tv_activate:
                ActivateFragment.build(mCupBean.getUuid(), new ActivateFragment.ActivateCallback() {
                    @Override
                    public void onSigninSuccess() {
                        setActivateBtnEnable(false);
                    }
                }).show(getChildFragmentManager(), ActivateFragment.TAG);
                break;
        }
    }

    public interface DeviceDetailFragmentCallback {
        void onRename(String newName);

        void onColorChange(int cupColor);
    }
}
