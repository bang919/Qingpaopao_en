package com.wopin.qingpaopao.fragment.welfare.address;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lljjcoder.style.citythreelist.CityBean;
import com.lljjcoder.style.citythreelist.ProvinceActivity;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.StringUtils;
import com.wopin.qingpaopao.utils.ToastUtils;

public class AddOrEditAddressFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "AddOrEditAddressFragment";
    private LoginRsp.ResultBean.AddressListBean mAddressListBean;
    private EditText mConsigneeEt, mPhoneEt, mAddressEt;
    private TextView mAreaTv;
    private AddAddressCallback mAddAddressCallback;
    private boolean isShowing;

    public void setAddOrEditAddressCallback(AddAddressCallback addAddressCallback) {
        mAddAddressCallback = addAddressCallback;
    }

    public void show(FragmentManager manager, String tag) {
        show(manager, tag, null);
    }

    public void show(FragmentManager manager, String tag, LoginRsp.ResultBean.AddressListBean addressListBean) {
        super.show(manager, tag);
        if (addressListBean != null) {
            this.mAddressListBean = addressListBean;
        }
        isShowing = true;
    }

    @Override
    public void onDestroyView() {
        isShowing = false;
        mAddressListBean = null;
        super.onDestroyView();
    }

    public boolean isShowing() {
        return isShowing;
    }


    @Override
    protected String setBarTitle() {
        return getString(R.string.add_address);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_address;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mConsigneeEt = rootView.findViewById(R.id.et_consignee);
        mPhoneEt = rootView.findViewById(R.id.et_input_phone_number);
        mAddressEt = rootView.findViewById(R.id.et_address_detail);
        mAreaTv = rootView.findViewById(R.id.tv_area_value);
        mAreaTv.setOnClickListener(this);

        rootView.findViewById(R.id.bt_confirm).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        if (mConsigneeEt != null) {
            mConsigneeEt.setText(mAddressListBean == null ? "" : mAddressListBean.getUserName());
            mPhoneEt.setText(mAddressListBean == null ? "" : mAddressListBean.getTel());
            mAddressEt.setText(mAddressListBean == null ? "" : mAddressListBean.getAddress2());
            if (mAddressListBean != null) {
                mAreaTv.setText(mAddressListBean.getAddress1());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_area_value:
                Intent intent = new Intent(getContext(), ProvinceActivity.class);
                startActivityForResult(intent, ProvinceActivity.RESULT_DATA);
                break;
            case R.id.bt_confirm:
                if (mAddAddressCallback != null) {
                    String userName = mConsigneeEt.getText().toString();
                    String tel = mPhoneEt.getText().toString();
                    String address1 = mAreaTv.getText().toString();
                    String address2 = mAddressEt.getText().toString();

                    if (TextUtils.isEmpty(userName)) {
                        ToastUtils.showShort(R.string.please_input_real_name);
                        return;
                    }
                    if (TextUtils.isEmpty(tel)) {
                        ToastUtils.showShort(R.string.please_input_phone_number);
                        return;
                    }
                    if (getString(R.string.choose_area).equals(address1)) {
                        ToastUtils.showShort(R.string.choose_area);
                        return;
                    }
                    if (TextUtils.isEmpty(address2)) {
                        ToastUtils.showShort(R.string.address_detail);
                        return;
                    }

                    AddressBean addressBean = new AddressBean();
                    addressBean.setAddressId(mAddressListBean == null ? StringUtils.getRandomString2(16) : mAddressListBean.getAddressId());
                    addressBean.setUserName(userName);
                    addressBean.setIsDefault(false);
                    addressBean.setTel(tel);
                    addressBean.setAddress1(address1);
                    addressBean.setAddress2(address2);
                    mAddAddressCallback.onAddOrEditAddressCallback(addressBean);
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProvinceActivity.RESULT_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                //省份结果
                CityBean province = data.getParcelableExtra("province");
                //城市结果
                CityBean city = data.getParcelableExtra("city");
                //区域结果
                CityBean area = data.getParcelableExtra("area");

                mAreaTv.setText(province.getName().concat(city.getName().concat(area.getName())));
            }
        }
    }

    public interface AddAddressCallback {
        void onAddOrEditAddressCallback(AddressBean addressBean);
    }
}
