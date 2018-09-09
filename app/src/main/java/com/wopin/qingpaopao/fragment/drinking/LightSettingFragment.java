package com.wopin.qingpaopao.fragment.drinking;

import android.text.TextUtils;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.DrinkingPresenter;

public class LightSettingFragment extends BaseDialogFragment implements View.OnClickListener, ColorPicker.OnColorChangedListener {

    public static final String TAG = "LightSettingFragment";
    private ColorPicker mColorPicker;
    private View mLightBtn, mLightIv;
    private String mNowColor;
    private DrinkingPresenter mDrinkingPresenter;

    public void setDrinkingPresenter(DrinkingPresenter drinkingPresenter) {
        mDrinkingPresenter = drinkingPresenter;
    }

    @Override
    public void onDestroy() {
        mDrinkingPresenter = null;
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_light_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mLightBtn = rootView.findViewById(R.id.btn_light);
        mLightIv = rootView.findViewById(R.id.iv_light);
        mLightBtn.setOnClickListener(this);
        mLightBtn.setSelected(true);
        mLightIv.setSelected(true);

        mColorPicker = rootView.findViewById(R.id.picker);
        SVBar svBar = rootView.findViewById(R.id.svbar);
        mColorPicker.addSVBar(svBar);
        mColorPicker.setOldCenterColor(mColorPicker.getColor());
        mColorPicker.setShowOldCenterColor(false);
        mColorPicker.setShowNewCenterColor(false);
        mColorPicker.setOnColorChangedListener(this);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.btn_light:
                boolean isOn = !v.isSelected();
                mLightIv.setSelected(isOn);
                mLightBtn.setSelected(isOn);
                mDrinkingPresenter.switchCupLight(isOn);
                if (!TextUtils.isEmpty(mNowColor)) {
                    mDrinkingPresenter.setColor(mNowColor);
                }
                break;
        }
    }

    @Override
    public void onColorChanged(int color) {
        String s = Integer.toHexString(color);
        String colorString = s.toUpperCase().replaceFirst("FF", "");
        mNowColor = colorString;
        mDrinkingPresenter.setColor(colorString);
    }
}