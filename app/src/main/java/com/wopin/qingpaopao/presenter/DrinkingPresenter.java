package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.manager.Updater;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;


public class DrinkingPresenter extends BasePresenter<DrinkingView> {

    private DrinkingModel mDrinkingModel;
    private ArrayList<CupListRsp.CupBean> mCupBeans;
    private CupListRsp.CupBean mCurrentOnlineCup;
    private final BleConnectManager mBleConnectManager;
    private Updater<BleConnectManager.BleUpdaterBean> mUpdater;

    private BluetoothDevice mFirstTimeAddDevice;//会在onConnectDevice后清空

    public DrinkingPresenter(Context context, DrinkingView view) {
        super(context, view);
        mDrinkingModel = new DrinkingModel();
        mBleConnectManager = BleConnectManager.getInstance();
        mUpdater = new Updater<BleConnectManager.BleUpdaterBean>() {
            @Override
            public void onConnectDevice(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                boolean needAdd = true;
                if (mCupBeans != null) {
                    for (CupListRsp.CupBean cupBean : mCupBeans) {
                        if (cupBean.getUuid().equals(bleUpdaterBean.getUuid())) {
                            needAdd = false;
                        }
                    }
                }
                if (needAdd && mFirstTimeAddDevice != null) {
                    addOrUpdateACup(CupUpdateReq.BLE, bleUpdaterBean.getUuid(), mFirstTimeAddDevice.getName(), bleUpdaterBean.getAddress(), true);
                } else {
                    getCupList();
                }
                mFirstTimeAddDevice = null;
            }

            @Override
            public void onDissconnectDevice(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                getCupList();
            }

            @Override
            public void onDatasUpdate(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                byte[] values = bleUpdaterBean.getValues();
                String s = DataUtil.byteArrayToHex(values);
                parseData(s);
            }
        };
        mBleConnectManager.addUpdater(mUpdater);
    }

    private void parseData(String data) {
        if (data.startsWith("AA CC DD 01 ") && data.endsWith(" DD CC AA")) {//电量数据
            if (mCurrentOnlineCup != null) {
                String hexElectric = data.replaceFirst("AA CC DD 01 ", "").replace(" DD CC AA", "");
                mCurrentOnlineCup.setElectric(Integer.valueOf(hexElectric, 16) + "%");
            }
        }
    }

    @Override
    public void destroy() {
        mBleConnectManager.removeUpdater(mUpdater);
        super.destroy();
    }

    /**
     * 连接杯子
     */
    public void firstTimeAddBleCup(BluetoothDevice bluetoothDevice) {
        mFirstTimeAddDevice = bluetoothDevice;
        connectBleCup(bluetoothDevice.getAddress());
    }

    public void connectBleCup(String address) {
        mBleConnectManager.connectDevice(address);
    }

    public void disconnectBleCup() {
        mBleConnectManager.disconnectDevice();
    }

    public void getCupList() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getCupList"), mDrinkingModel.getCupList(), new MyObserver<CupListRsp>() {
            @Override
            public void onMyNext(CupListRsp cupListRsp) {
                mCurrentOnlineCup = null;
                mCupBeans = cupListRsp.getResult();
                for (CupListRsp.CupBean cupBean : mCupBeans) {
                    if (cupBean.getUuid().equals(mBleConnectManager.getCurrentUuid())) {
                        cupBean.setConnecting(true);
                        mCurrentOnlineCup = cupBean;
                    } else {
                        cupBean.setConnecting(false);
                    }
                }
                mView.onCupList(mCupBeans, mCurrentOnlineCup);
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
