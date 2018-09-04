package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.manager.BleManager;
import com.wopin.qingpaopao.manager.Updater;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;


public class DrinkingPresenter extends BasePresenter<DrinkingView> {

    private DrinkingModel mDrinkingModel;
    private ArrayList<CupListRsp.CupBean> mCupBeans;
    private final BleManager mBleManager;
    private Updater<BleManager.BleUpdaterBean> mUpdater;

    private BluetoothDevice mFirstTimeAddDevice;//会在onConnectDevice后清空
    private String mCurrentUuid;
    private String mCurrentAddress;

    public DrinkingPresenter(Context context, DrinkingView view) {
        super(context, view);
        mDrinkingModel = new DrinkingModel();
        mBleManager = BleManager.getInstance();
        mUpdater = new Updater<BleManager.BleUpdaterBean>() {
            @Override
            public void onConnectDevice(BleManager.BleUpdaterBean bleUpdaterBean) {
                mCurrentAddress = bleUpdaterBean.getAddress();
                mCurrentUuid = bleUpdaterBean.getUuid();
                boolean needAdd = true;
                if (mCupBeans != null) {
                    for (CupListRsp.CupBean cupBean : mCupBeans) {
                        if (cupBean.getUuid().equals(mCurrentUuid)) {
                            needAdd = false;
                        }
                    }
                }
                if (needAdd && mFirstTimeAddDevice != null) {
                    addOrUpdateACup(CupUpdateReq.BLE, mCurrentUuid, mFirstTimeAddDevice.getName(), mCurrentAddress, true);
                } else {
                    getCupList();
                }
                mFirstTimeAddDevice = null;
            }

            @Override
            public void onDissconnectDevice(BleManager.BleUpdaterBean bleUpdaterBean) {
                mCurrentUuid = null;
                mCurrentAddress = null;
                getCupList();
            }

            @Override
            public void onDatasUpdate(BleManager.BleUpdaterBean bleUpdaterBean) {
            }
        };
        mBleManager.addUpdater(mUpdater);
    }

    @Override
    public void destroy() {
        mBleManager.removeUpdater(mUpdater);
        super.destroy();
    }

    public String getCurrentAddress() {
        return mCurrentAddress;
    }

    /**
     * 连接杯子
     */
    public void firstTimeAddBleCup(BluetoothDevice bluetoothDevice) {
        mFirstTimeAddDevice = bluetoothDevice;
        connectBleCup(bluetoothDevice.getAddress());
    }

    public void connectBleCup(String address) {
        mBleManager.connectDevice(address);
    }

    public void disconnectBleCup() {
        mBleManager.disconnectDevice(mCurrentAddress);
    }

    public void switchBleCupElectrolyze(boolean isOn) {
        mBleManager.switchCupElectrolyze(mCurrentAddress, isOn);
    }

    public void getCupList() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getCupList"), mDrinkingModel.getCupList(), new MyObserver<CupListRsp>() {
            @Override
            public void onMyNext(CupListRsp cupListRsp) {
                mCupBeans = cupListRsp.getResult();
                CupListRsp.CupBean currentConnectCup = null;
                for (CupListRsp.CupBean cupBean : mCupBeans) {
                    if (cupBean.getUuid().equals(mCurrentUuid)) {
                        cupBean.setConnecting(true);
                        currentConnectCup = cupBean;
                    } else {
                        cupBean.setConnecting(false);
                    }
                }
                mView.onCupList(mCupBeans, currentConnectCup);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void deleteACup(String uuid) {
        subscribeNetworkTask(getClass().getSimpleName().concat("deleteACup"), mDrinkingModel.deleteACup(uuid), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                getCupList();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    private void addOrUpdateACup(String type, String uuid, String name, String address, boolean add) {
        subscribeNetworkTask(getClass().getSimpleName().concat("addOrUpdateACup"), mDrinkingModel.addOrUpdateACup(type, uuid, name, address, add),
                new MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        getCupList();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
