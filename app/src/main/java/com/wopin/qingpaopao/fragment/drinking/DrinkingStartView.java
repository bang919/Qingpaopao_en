package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.DrinkingPresenter;

public class DrinkingStartView extends Fragment implements View.OnClickListener {

    private DrinkingPresenter mDrinkingPresenter;
    private View mRootView;
    private OnDrinkingStartCallback mOnDrinkingStartCallback;
    private TextView mCurrentDrinkTV;
    private TextView mTotalDrinkTV;
    private TextView mTimeTv;
    private SeekBar mSeekBar;

    public void setOnDrinkingStartCallback(OnDrinkingStartCallback onDrinkingStartCallback) {
        mOnDrinkingStartCallback = onDrinkingStartCallback;
    }

    public void setDrinkingPresenter(DrinkingPresenter drinkingPresenter) {
        this.mDrinkingPresenter = drinkingPresenter;
    }

    @Override
    public void onDestroy() {
        mDrinkingPresenter = null;
        super.onDestroy();
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
        mRootView.findViewById(R.id.iv_device_list).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_device_list).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_light_setting).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_cup_clean).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_switch_electrolyze).setOnClickListener(this);
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
                if (mDrinkingPresenter != null) {
                    v.setSelected(!v.isSelected());
                    ((TextView) v).setText(!v.isSelected() ? R.string.start_electrolysis : R.string.stop_electrolysis);
                    mDrinkingPresenter.switchCupElectrolyze(v.isSelected());
                }
                break;
            case R.id.iv_light_setting:
                break;
            case R.id.iv_cup_clean:
                break;
        }
    }

    public interface OnDrinkingStartCallback {
        void showDrinkingDeviceList();
    }
}
