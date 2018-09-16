package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.model.AddressModel;
import com.wopin.qingpaopao.view.AddressView;

public class AddressPresenter extends BasePresenter<AddressView> {

    private AddressModel mAddressModel;

    public AddressPresenter(Context context, AddressView view) {
        super(context, view);
        mAddressModel = new AddressModel();
    }

    public void addOrUpdateAddress(AddressBean addressBean) {
        subscribeNetworkTask(getClass().getSimpleName().concat("addOrUpdateAddress"), mAddressModel.addOrUpdateAddress(addressBean),
                new MyObserver<LoginRsp>() {
                    @Override
                    public void onMyNext(LoginRsp loginRsp) {
                        mView.onAddressChange();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }

    public void setDefaultAddress(String addressId) {
        AddressBean addressBean = new AddressBean();
        addressBean.setAddressId(addressId);
        subscribeNetworkTask(getClass().getSimpleName().concat("setDefaultAddress"), mAddressModel.setDefaultAddress(addressBean),
                new MyObserver<LoginRsp>() {
                    @Override
                    public void onMyNext(LoginRsp loginRsp) {
                        mView.onAddressChange();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
