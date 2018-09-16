package com.wopin.qingpaopao.fragment.welfare.address;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.AddressListAdapter;
import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.AddressPresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.AddressView;

import java.util.ArrayList;

public class AddressListFragment extends BaseBarDialogFragment<AddressPresenter> implements AddressView, AddressListAdapter.AddressListAdapterCallback, AddOrEditAddressFragment.AddAddressCallback {

    public static final String TAG = "AddressListFragment";
    private RecyclerView mListRv;
    private AddressListAdapter mAddressListAdapter;
    private AddressListClickCallback mAddressListClickCallback;

    public void setAddressListClickCallback(AddressListClickCallback addressListClickCallback) {
        mAddressListClickCallback = addressListClickCallback;
    }

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.new_add);
        textView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Add New Address
                AddOrEditAddressFragment mAddOrEditAddressFragment = new AddOrEditAddressFragment();
                mAddOrEditAddressFragment.setAddOrEditAddressCallback(AddressListFragment.this);
                mAddOrEditAddressFragment.show(getChildFragmentManager(), AddOrEditAddressFragment.TAG);
            }
        });
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.receiver_address);
    }

    @Override
    protected int getLayout() {
        return R.layout.recyclerview_list;
    }

    @Override
    protected AddressPresenter initPresenter() {
        return new AddressPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mListRv = rootView.findViewById(R.id.recyclerview);
    }

    @Override
    protected void initEvent() {

        mListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAddressListAdapter = new AddressListAdapter();
        mAddressListAdapter.setAddressListAdapterCallback(this);
        mListRv.setAdapter(mAddressListAdapter);
        onAddressChange();
    }

    @Override
    public void onAddressChange() {
        ArrayList<LoginRsp.ResultBean.AddressListBean> addressList = LoginPresenter.getAccountMessage().getResult().getAddressList();
        mAddressListAdapter.setAddressListBeans(addressList);
    }

    @Override
    public void onError(String error) {
        ToastUtils.showShort(error);
    }

    @Override
    public void onItemClick(LoginRsp.ResultBean.AddressListBean addressListBean) {
        if (mAddressListClickCallback != null) {
            mAddressListClickCallback.onAddressItemClick(addressListBean);
            dismiss();
        }
    }

    @Override
    public void onDefaultClick(LoginRsp.ResultBean.AddressListBean addressListBean) {
        mPresenter.setDefaultAddress(addressListBean.getAddressId());
    }

    @Override
    public void onEditClick(LoginRsp.ResultBean.AddressListBean addressListBean) {
        AddOrEditAddressFragment mAddOrEditAddressFragment = new AddOrEditAddressFragment();
        mAddOrEditAddressFragment.setAddOrEditAddressCallback(AddressListFragment.this);
        mAddOrEditAddressFragment.show(getChildFragmentManager(), AddOrEditAddressFragment.TAG, addressListBean);
    }

    @Override
    public void onDeleteClick(LoginRsp.ResultBean.AddressListBean addressListBean) {

    }

    @Override
    public void onAddOrEditAddressCallback(AddressBean addressBean) {
        mPresenter.addOrUpdateAddress(addressBean);
    }

    public interface AddressListClickCallback {
        void onAddressItemClick(LoginRsp.ResultBean.AddressListBean addressListBean);
    }
}
