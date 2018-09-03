package com.wopin.qingpaopao.fragment.drinking;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.CupListAdapter;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.presenter.BlueToothPresenter;
import com.wopin.qingpaopao.presenter.DrinkingPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;

public class DrinkingFragment extends BaseMainFragment<DrinkingPresenter> implements DrinkingView, CupListAdapter.OnCupItemClickCallback, DrinkingListView.OnDrinkingListViewCallback, DrinkingStartView.OnDrinkingStartCallback {

    private DrinkingListView mDrinkingListView;
    private DrinkingStartView mDrinkingStartView;
    private Fragment currentFragment;
    private BlueToothPresenter mBlueToothPresenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_drinking;
    }

    @Override
    protected DrinkingPresenter initPresenter() {
        return new DrinkingPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {
        mDrinkingListView = new DrinkingListView();
        mDrinkingListView.setOnDrinkingListViewCallback(this);
        mDrinkingListView.setOnCupItemClickCallback(this);
        mDrinkingStartView = new DrinkingStartView();
        mDrinkingStartView.setDrinkingPresenter(mPresenter);
        mDrinkingStartView.setOnDrinkingStartCallback(this);
        switchFragment(mDrinkingListView);

        mPresenter.getCupList();
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (!targetFragment.isAdded()) {
            transaction
                    .add(R.id.fragment_layout, targetFragment)
                    .commit();
        } else {
            transaction
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void backToDrinkingStartView() {
        switchFragment(mDrinkingStartView);
    }

    @Override
    public void onBluetoothDeviceFind(BluetoothDevice bluetoothDevice) {
        mPresenter.connectACup(bluetoothDevice);
    }

    @Override
    public void onCupList(ArrayList<CupListRsp.CupBean> cupBeanList, CupListRsp.CupBean currentConnectCup) {
        mDrinkingListView.notifyCupList(cupBeanList, currentConnectCup);
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onCupItemClick(CupListRsp.CupBean cupBean, int position) {
        /*if (mBlueToothPresenter != null) {
            mBlueToothPresenter.destroy();
        }
        mBlueToothPresenter = new BlueToothPresenter(this, new BlueToothPresenter.BlueToothPresenterCallback() {
            @Override
            public void onDevicesFind(TreeMap<String, BluetoothDevice> devices, BluetoothDevice newDevice) {
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtils.showShort(errorMsg);
            }
        });
        mBlueToothPresenter.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBlueToothPresenter != null) {
                    mBlueToothPresenter.destroy();
                }
            }
        }, 5000);*/
//        mPresenter.connectACup(cupBean);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mBlueToothPresenter != null) {
            mBlueToothPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCupItemDelete(CupListRsp.CupBean cupBean, int position) {
        if (cupBean.isConnecting() && cupBean.getType().equals(CupUpdateReq.BLE)) {
            mPresenter.disconnectCurrentBleDevice();
        }
        mPresenter.deleteACup(cupBean.getUuid());
    }

    @Override
    public void showDrinkingDeviceList() {
        switchFragment(mDrinkingListView);
    }
}
