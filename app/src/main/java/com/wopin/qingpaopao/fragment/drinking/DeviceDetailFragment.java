package com.wopin.qingpaopao.fragment.drinking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.manager.MessageProxy;
import com.wopin.qingpaopao.manager.MessageProxyCallback;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.receiver.AlarmNotificationReceiver;
import com.wopin.qingpaopao.server.AlarmNotificationService;
import com.wopin.qingpaopao.utils.SPUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class DeviceDetailFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "DeviceDetailFragment";
    private CupListRsp.CupBean mCupBean;
    private TextView mElectricTv;
    private TextView mDeviceNameTv;
    private TextView mCupColorTv;
    private TextView mDeviceStatusTv;
    private TextView mDeviceStatusTime;
    private SwitchCompat mNotificationSwitch;

    public static DeviceDetailFragment getDeviceDetailFragment(CupListRsp.CupBean cupBean) {
        DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, cupBean);
        deviceDetailFragment.setArguments(args);
        return deviceDetailFragment;
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
    }

    @Override
    protected void initEvent() {
        mDeviceNameTv.setText(mCupBean.getName());
        mDeviceNameTv.setOnClickListener(this);
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
                    startNotification();
                } else {
                    stopNotification();
                }
            }
        });
    }

    private void startNotification() {
        Intent intent = new Intent(getActivity(), AlarmNotificationService.class);
        Calendar calendar = getCalendar();
        intent.putExtra(Constants.AlarmNotificationReceiver_0730, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_0900, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1100, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1300, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_1500, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_1730, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        calendar.add(Calendar.MINUTE, 30);
        intent.putExtra(Constants.AlarmNotificationReceiver_2000, calendar.getTimeInMillis());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        intent.putExtra(Constants.AlarmNotificationReceiver_2200, calendar.getTimeInMillis());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startService(intent);
//发送一条启动闹铃图标的广播
//        Intent intentIcon = new Intent("com.gaozhidong.android.Color");
//        intentIcon.putExtra("noteId", 0);
//        getActivity().sendBroadcast(intentIcon);

        SPUtils.put(getContext(), Constants.DRINKING_NOTIFICATION, true);
    }

    private void stopNotification() {
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //取消警报
        for (String key : Constants.AlarmNotificationReceivers) {
            stopEachNotification(am, key);
        }

        SPUtils.put(getContext(), Constants.DRINKING_NOTIFICATION, false);
    }

    private void stopEachNotification(AlarmManager alarmManager, String key) {
        Intent intent = new Intent(getContext(), AlarmNotificationReceiver.class);
        intent.setAction(key);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private Calendar getCalendar() {
        //得到日历实例，主要是为了下面的获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        //设置在几分提醒  设置的为25分
        calendar.set(Calendar.MINUTE, 30);
        //下面这两个看字面意思也知道
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //上面设置的就是13点25分的时间点

        //获取上面设置的13点25分的毫秒值
        long selectTime = calendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return calendar;
    }

    @Override
    public void onDestroy() {
        MessageProxy.clearMessageProxyCallback();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.value_device_name:
//                BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
//                BluetoothAdapter adapter = bluetoothManager.getAdapter();
//                adapter.setName("Hahahaha");
                break;
        }
    }
}
