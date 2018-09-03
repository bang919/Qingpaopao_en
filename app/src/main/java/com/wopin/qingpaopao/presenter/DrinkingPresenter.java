package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.AndroidBleApiModel;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;


public class DrinkingPresenter extends BasePresenter<DrinkingView> {

    private DrinkingModel mDrinkingModel;
    private AndroidBleApiModel mAndroidBleApiModel;
    private ArrayList<CupListRsp.CupBean> mCupBeans;
    private String mCurrentUuid;

    public DrinkingPresenter(Context context, DrinkingView view) {
        super(context, view);
        mDrinkingModel = new DrinkingModel();
        mAndroidBleApiModel = new AndroidBleApiModel(context, new AndroidBleApiModel.AndroidBleApiModelCallback() {
            @Override
            public void onConnectedBluetoothDevice(BluetoothDevice bluetoothDevice, String uuid) {
                mCurrentUuid = uuid;
                boolean needAdd = true;
                if (mCupBeans != null) {
                    for (CupListRsp.CupBean cupBean : mCupBeans) {
                        if (cupBean.getUuid().equals(uuid)) {
                            needAdd = false;
                        }
                    }
                }
                if (needAdd) {
                    addOrUpdateACup(CupUpdateReq.BLE, uuid, bluetoothDevice.getName(), bluetoothDevice.getAddress(), true);
                } else {
                    getCupList();
                }
            }

            @Override
            public void onDisConnectedBluetoothDevice(String uuid) {
                if (uuid.equals(mCurrentUuid)) {
                    mCurrentUuid = null;
                }
                getCupList();
            }
        });
    }

    @Override
    public void destroy() {
        mAndroidBleApiModel.destroy();
        super.destroy();
    }

    /**
     * 连接杯子
     */
    public void connectACup(BluetoothDevice bluetoothDevice) {
        mAndroidBleApiModel.connect(bluetoothDevice);
    }

    /**
     * 断开杯子连接
     */
    public void disconnectCurrentBleDevice() {
        mAndroidBleApiModel.disconnectCurrentBleDevice();
    }

    /**
     * 开/关杯子电解
     */
    public void switchCupElectrolyze(boolean electrolyze) {
        mAndroidBleApiModel.switchCupElectrolyze(electrolyze);
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
