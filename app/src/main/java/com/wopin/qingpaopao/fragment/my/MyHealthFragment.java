package com.wopin.qingpaopao.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.presenter.MyHealthPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MyHealthView;

public class MyHealthFragment extends BaseBarDialogFragment<MyHealthPresenter> implements View.OnClickListener, MyHealthView {

    public static final String TAG = "MyHealthFragment";
    private TextView mHeightTv, mWeightTv, mAgeTv, mBloodPressureTv, mBloodFatTv, mBloodGlucose;

    @Override
    protected String setBarTitle() {
        return getString(R.string.my_health);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_health;
    }

    @Override
    protected MyHealthPresenter initPresenter() {
        return new MyHealthPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mHeightTv = rootView.findViewById(R.id.tv_height);
        mWeightTv = rootView.findViewById(R.id.tv_weight);
        mAgeTv = rootView.findViewById(R.id.tv_age);
        mBloodPressureTv = rootView.findViewById(R.id.tv_blood_pressure);
        mBloodFatTv = rootView.findViewById(R.id.tv_blood_fat);
        mBloodGlucose = rootView.findViewById(R.id.tv_blood_glucose);
    }

    @Override
    protected void initEvent() {
        mHeightTv.setOnClickListener(this);
        mWeightTv.setOnClickListener(this);
        mAgeTv.setOnClickListener(this);
        mBloodPressureTv.setOnClickListener(this);
        mBloodFatTv.setOnClickListener(this);
        mBloodGlucose.setOnClickListener(this);
        refreshDatas();
    }

    private void refreshDatas() {
        LoginRsp.ResultBean.ProfilesBean profiles = LoginPresenter.getAccountMessage().getResult().getProfiles();
        mHeightTv.setText(getString(R.string.height_key_value, Float.valueOf(profiles.getHeight())));
        mWeightTv.setText(getString(R.string.weight_key_value, Float.valueOf(profiles.getWeight())));
        mAgeTv.setText(getString(R.string.age_key_value, Integer.valueOf(profiles.getAge())));
        mBloodPressureTv.setText(getString(R.string.blood_pressure_key_value, Float.valueOf(profiles.getBlood_pressure())));
        mBloodFatTv.setText(getString(R.string.blood_fat_value, Float.valueOf(profiles.getBlood_lipid_all())));
        mBloodGlucose.setText(getString(R.string.blood_glucose_key_value, Float.valueOf(profiles.getBlood_sugar_hugry())));
    }

    @Override
    public void onClick(View v) {
        LoginRsp.ResultBean.ProfilesBean profiles = LoginPresenter.getAccountMessage().getResult().getProfiles();
        EditMyHealthFragment editMyHealthFragment = null;
        switch (v.getId()) {
            case R.id.tv_height:
                editMyHealthFragment = EditMyHealthFragment.build(null, getString(R.string.height_key1), profiles.getHeight());
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateHeight(value1);
                    }
                });
                break;
            case R.id.tv_weight:
                editMyHealthFragment = EditMyHealthFragment.build(null, getString(R.string.weight_key1), profiles.getWeight());
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateWeight(value1);
                    }
                });
                break;
            case R.id.tv_age:
                editMyHealthFragment = EditMyHealthFragment.build(null, getString(R.string.age_key1), profiles.getAge());
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateAge(value1);
                    }
                });
                break;
            case R.id.tv_blood_pressure:
                editMyHealthFragment = EditMyHealthFragment.build(
                        getString(R.string.edit_health_title, getString(R.string.blood_pressure)),
                        getString(R.string.blood_pressure_key1), profiles.getBlood_pressure(),
                        getString(R.string.blood_pressure_key2), profiles.getBlood_pressure_press()
                );
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateBloodPressure(value1, value2);
                    }
                });
                break;
            case R.id.tv_blood_fat:
                editMyHealthFragment = EditMyHealthFragment.build(
                        getString(R.string.edit_health_title, getString(R.string.blood_fat)),
                        getString(R.string.blood_fat_key1), profiles.getBlood_lipid_all(),
                        getString(R.string.blood_fat_key2), profiles.getBlood_lipid(),
                        getString(R.string.blood_fat_key3), profiles.getBlood_lipid_TG()
                );
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateBloodLipid(value1, value2, value3);
                    }
                });
                break;
            case R.id.tv_blood_glucose:
                editMyHealthFragment = EditMyHealthFragment.build(
                        getString(R.string.edit_health_title, getString(R.string.blood_glucose)),
                        getString(R.string.blood_glucose_key1), profiles.getBlood_sugar_hugry(),
                        getString(R.string.blood_glucose_key2), profiles.getBlood_sugar_full()
                );
                editMyHealthFragment.setEditMyHealthCallback(new EditMyHealthFragment.EditMyHealthCallback() {
                    @Override
                    public void onEditMyHealthCallback(String value1, String value2, String value3) {
                        mPresenter.updateBloodSugar(value1, value2);
                    }
                });
                break;
        }
        editMyHealthFragment.show(getChildFragmentManager(), EditMyHealthFragment.TAG);
    }

    @Override
    public void onUpdateSuccess() {
        refreshDatas();
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
