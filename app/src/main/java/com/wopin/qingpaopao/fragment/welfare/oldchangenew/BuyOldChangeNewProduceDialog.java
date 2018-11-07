package com.wopin.qingpaopao.fragment.welfare.oldchangenew;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.fragment.welfare.address.AddressListFragment;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.ArrayList;

public class BuyOldChangeNewProduceDialog extends DialogFragment implements View.OnClickListener, AddressListFragment.AddressListClickCallback {

    public static final String TAG = "EnchangeScoreMarketProduceDialog";
    private static final String NEW_PRODUCE = "new produce";
    private static final String OLD_PRODUCE = "old produce";
    private View mRootView;
    private TextView mBuyCountTv;
    private int mBuyCount;
    private TextView mAddressTv;
    private LoginRsp.ResultBean.AddressListBean mCurrentAddress;
    private BuyOldChangeNewCallback mBuyOldChangeNewCallback;
    private ProductContent mProductContent;
    private ProductContent.AttributeBean mOldProduct;

    public static BuyOldChangeNewProduceDialog build(ProductContent productContent, ProductContent.AttributeBean oldProduct) {
        BuyOldChangeNewProduceDialog enchangeScoreMarketProduceDialog = new BuyOldChangeNewProduceDialog();
        Bundle args = new Bundle();
        args.putParcelable(NEW_PRODUCE, productContent);
        args.putParcelable(OLD_PRODUCE, oldProduct);
        enchangeScoreMarketProduceDialog.setArguments(args);
        return enchangeScoreMarketProduceDialog;
    }

    public void setBuyOldChangeNewCallback(BuyOldChangeNewCallback buyOldChangeNewCallback) {
        mBuyOldChangeNewCallback = buyOldChangeNewCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
        getDialog().getWindow().setWindowAnimations(R.style.animate_dialog_bottom_to_top);

        Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);

        mRootView = inflater.inflate(R.layout.dialog_buy_old_change_new_produce, container, false);
        mBuyCountTv = mRootView.findViewById(R.id.tv_number);
        mBuyCount = Integer.valueOf(mBuyCountTv.getText().toString());
        mAddressTv = mRootView.findViewById(R.id.tv_receiver_address_value);
        mAddressTv.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_add).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_minus).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_buy_at_once).setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProductContent = getArguments().getParcelable(NEW_PRODUCE);
        mOldProduct = getArguments().getParcelable(OLD_PRODUCE);
        ((TextView) mRootView.findViewById(R.id.tv_produce_price_value)).setText(Html.fromHtml(getString(R.string.del_price_number, mProductContent.getPrice())));
        ((TextView) mRootView.findViewById(R.id.tv_old_change_new_price_value))
                .setText(String.format(getString(R.string.price_number), mOldProduct == null ? "0" : mOldProduct.getName()));

        ArrayList<LoginRsp.ResultBean.AddressListBean> addressList = LoginPresenter.getAccountMessage().getResult().getAddressList();
        if (addressList != null) {
            for (LoginRsp.ResultBean.AddressListBean addressListBean : addressList) {
                if (addressListBean.isIsDefault()) {
                    setAddressMessage(addressListBean);
                    break;
                }
            }
        }
        notifyTotalScore();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_add:
                mBuyCountTv.setText(String.valueOf(++mBuyCount));
                notifyTotalScore();
                break;
            case R.id.btn_minus:
                if (mBuyCount > 1) {
                    mBuyCountTv.setText(String.valueOf(--mBuyCount));
                    notifyTotalScore();
                }
                break;
            case R.id.btn_buy_at_once:
                if (mCurrentAddress == null) {
                    ToastUtils.showShort(R.string.need_choose_address);
                    return;
                }
                if (mBuyOldChangeNewCallback != null) {
                    mBuyOldChangeNewCallback.OnBuyInformation(mBuyCount, mCurrentAddress.getAddressId());
                    dismiss();
                }
                break;
            case R.id.tv_receiver_address_value:
                AddressListFragment addressListFragment = new AddressListFragment();
                addressListFragment.setAddressListClickCallback(this);
                addressListFragment.show(getChildFragmentManager(), AddressListFragment.TAG);
                break;
        }
    }

    private void notifyTotalScore() {
        int priceEach = Integer.valueOf(mProductContent.getPrice());
        int totalPrice = priceEach * mBuyCount;
        int finallyPrice = totalPrice - (mOldProduct != null ? Integer.valueOf(mOldProduct.getName()) : 0);
        ((TextView) mRootView.findViewById(R.id.tv_need_pay_value)).setText(String.format(getString(R.string.price_number), String.valueOf(finallyPrice)));
    }

    @Override
    public void onAddressItemClick(LoginRsp.ResultBean.AddressListBean addressListBean) {
        setAddressMessage(addressListBean);
    }

    private void setAddressMessage(LoginRsp.ResultBean.AddressListBean addressListBean) {
        mCurrentAddress = addressListBean;
        mAddressTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mAddressTv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mAddressTv.setTextColor(Color.BLACK);
        mAddressTv.setText(addressListBean.getUserName() + " " + addressListBean.getTel() + "\n" + addressListBean.getAddress1() + addressListBean.getAddress2());
    }

    public interface BuyOldChangeNewCallback {
        void OnBuyInformation(int number, String addressId);
    }
}
