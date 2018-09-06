package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.manager.BleManager;

public class DrinkingStartView extends Fragment implements View.OnClickListener {
    private View mRootView;
    private OnDrinkingStartCallback mOnDrinkingStartCallback;
    private TextView mCurrentDrinkTV;
    private TextView mTotalDrinkTV;
    private TextView mTimeTv;
    private SeekBar mSeekBar;
    private TextView mSwitchElectrolyzeBtn;
    private Handler mHandler;
    private boolean isSeekbarOntouch;
    private Runnable mSeekbarMinusRunnable;

    public void setOnDrinkingStartCallback(OnDrinkingStartCallback onDrinkingStartCallback) {
        mOnDrinkingStartCallback = onDrinkingStartCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_drinking_start, container, false);
        mCurrentDrinkTV = mRootView.findViewById(R.id.number_current_drink_quantity);
        mCurrentDrinkTV = mRootView.findViewById(R.id.number_total_drink_quantity);
        mTimeTv = mRootView.findViewById(R.id.tv_time);
        mSeekBar = mRootView.findViewById(R.id.seek_bar);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler = new Handler();
        mSeekbarMinusRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isSeekbarOntouch) {
                    int progress = mSeekBar.getProgress() - 1;
                    if (progress > -1) {
                        mSeekBar.setProgress(progress);
                    } else {
                        switchSeekbarMinusRunnable(false);
                        return;
                    }
                }
                mHandler.postDelayed(mSeekbarMinusRunnable, 1000);
            }
        };
        mRootView.findViewById(R.id.iv_device_list).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_device_list).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_light_setting).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_cup_clean).setOnClickListener(this);
        mSwitchElectrolyzeBtn = mRootView.findViewById(R.id.btn_switch_electrolyze);
        mSwitchElectrolyzeBtn.setOnClickListener(this);

        mSeekBar.setMax(20 * 60);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String m = "0" + progress / 60;
                String s = "0" + progress % 60;
                mTimeTv.setText(m.substring(m.length() - 2).concat(":").concat(s.substring(s.length() - 2)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekbarOntouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarOntouch = false;
            }
        });
        mSeekBar.setProgress(5 * 60);
    }

    private void switchSeekbarMinusRunnable(boolean start) {
        BleManager bleManager = BleManager.getInstance();
        if (bleManager != null) {
            mSwitchElectrolyzeBtn.setSelected(start);
            mSwitchElectrolyzeBtn.setText(start ? R.string.stop_electrolysis : R.string.start_electrolysis);
            bleManager.switchCupElectrolyze(start);
        }
        if (start) {
            mHandler.postDelayed(mSeekbarMinusRunnable, 1000);
        } else {
            mHandler.removeCallbacks(mSeekbarMinusRunnable);
        }
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mSeekbarMinusRunnable);
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_device_list:
            case R.id.tv_device_list:
                if (mOnDrinkingStartCallback != null) {
                    mOnDrinkingStartCallback.showDrinkingDeviceList();
                }
                break;
            case R.id.btn_switch_electrolyze:
                switchSeekbarMinusRunnable(!v.isSelected());
                break;
            case R.id.iv_light_setting:
                new LightSettingFragment().show(getChildFragmentManager(), LightSettingFragment.TAG);
                break;
            case R.id.iv_cup_clean:
                new CleanCupFragment().show(getChildFragmentManager(), CleanCupFragment.TAG);
                break;
        }
    }

    public interface OnDrinkingStartCallback {
        void showDrinkingDeviceList();
    }
}
