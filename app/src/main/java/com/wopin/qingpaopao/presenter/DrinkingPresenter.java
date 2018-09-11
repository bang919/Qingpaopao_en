package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.manager.MqttConnectManager;
import com.wopin.qingpaopao.manager.Updater;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.view.DrinkingView;

import java.util.ArrayList;


public class DrinkingPresenter extends BasePresenter<DrinkingView> {

    private DrinkingModel mDrinkingModel;
    private ArrayList<CupListRsp.CupBean> mCupBeans;
    private CupListRsp.CupBean mCurrentOnlineCup;
    private Updater<BleConnectManager.BleUpdaterBean> mBleUpdater;
    private Updater<MqttConnectManager.MqttUpdaterBean> mMqttUpdater;

    private Object mFirstTimeAddDevice;//会在onConnectDevice后清空  BluetoothDevice/WifiRsp

    public DrinkingPresenter(Context context, DrinkingView view) {
        super(context, view);
        mDrinkingModel = new DrinkingModel();
        mBleUpdater = new Updater<BleConnectManager.BleUpdaterBean>() {
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
                    addOrUpdateACup(CupUpdateReq.BLE, bleUpdaterBean.getUuid(), ((BluetoothDevice) mFirstTimeAddDevice).getName(), bleUpdaterBean.getAddress(), true);
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
                parseBleData(s);
            }
        };
        mMqttUpdater = new Updater<MqttConnectManager.MqttUpdaterBean>() {
            @Override
            public void onConnectDevice(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                boolean needAdd = true;
                if (mCupBeans != null) {
                    for (CupListRsp.CupBean cupBean : mCupBeans) {
                        if (cupBean.getUuid().equals(mqttUpdaterBean.getSsid())) {
                            needAdd = false;
                        }
                    }
                }
                if (needAdd && mFirstTimeAddDevice != null) {
                    addOrUpdateACup(CupUpdateReq.WIFI, mqttUpdaterBean.getSsid(), mqttUpdaterBean.getSsid(), null, true);
                } else {
                    getCupList();
                }
                mFirstTimeAddDevice = null;
            }

            @Override
            public void onDissconnectDevice(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                getCupList();
            }

            @Override
            public void onDatasUpdate(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                String message = mqttUpdaterBean.getMessage();
                String[] split = message.split(":");
                if (split.length == 2 && split[0].equals("P") && mCurrentOnlineCup != null) {
                    mCurrentOnlineCup.setElectric(split[1].concat("%"));
                }
            }
        };
        BleConnectManager.getInstance().addUpdater(mBleUpdater);
        MqttConnectManager.getInstance().addUpdater(mMqttUpdater);
    }

    private void parseBleData(String data) {
        if (data.startsWith("AA CC DD 01 ") && data.endsWith(" DD CC AA")) {//电量数据
            if (mCurrentOnlineCup != null) {
                String hexElectric = data.replaceFirst("AA CC DD 01 ", "").replace(" DD CC AA", "");
                mCurrentOnlineCup.setElectric(Integer.valueOf(hexElectric, 16) + "%");
            }
        }
    }

    @Override
    public void destroy() {
        BleConnectManager.getInstance().removeUpdater(mBleUpdater);
        MqttConnectManager.getInstance().removeUpdater(mMqttUpdater);
        super.destroy();
    }

    /**
     * 连接杯子
     */
    public <T> void firstTimeAddBleCup(T device) {
        mFirstTimeAddDevice = device;
        if (device instanceof BluetoothDevice) {
            BleConnectManager.getInstance().connectDevice(((BluetoothDevice) device).getAddress());
        } else if (device instanceof WifiRsp) {
            MqttConnectManager.getInstance().connectDevice(((WifiRsp) device).getEssid());
        }
    }

    /**
     * 断开杯子连接
     */
    public void disconnectBleCup() {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(CupUpdateReq.BLE)) {//Ble
                BleConnectManager.getInstance().disconnectDevice();
            } else {//Wifi

            }
        }
    }

    /**
     * 开关电解
     */
    public void switchCupElectrolyze(boolean isOn) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(CupUpdateReq.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupElectrolyze(isOn);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupElectrolyze(isOn);
            }
        }
        if (isOn) {
            mDrinkingModel.drink().subscribe();
//            HttpUtil.handlerObserver()
        }
    }

    /**
     * 开关灯
     */
    public void switchCupLight(boolean isLightOn) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(CupUpdateReq.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupLight(isLightOn);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupLight(isLightOn);
            }
        }
    }

    /**
     * 清洗杯子
     */
    public void switchCupClean(boolean isClean) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(CupUpdateReq.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupClean(isClean);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupClean(isClean);
            }
        }
    }

    /**
     * 调节颜色
     */
    public void setColor(String color) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(CupUpdateReq.BLE)) {//Ble
                BleConnectManager.getInstance().setColor(color);
            } else {//Wifi
                MqttConnectManager.getInstance().setColor(color);
            }
        }
    }

    public void getCupList() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getCupList"), mDrinkingModel.getCupList(), new MyObserver<CupListRsp>() {
            @Override
            public void onMyNext(CupListRsp cupListRsp) {
                mCurrentOnlineCup = null;
                mCupBeans = cupListRsp.getResult();
                String bleCurrent = BleConnectManager.getInstance().getCurrentUuid();
                String mqttCurrent = MqttConnectManager.getInstance().getCurrentSsid();
                for (CupListRsp.CupBean cupBean : mCupBeans) {
                    if (cupBean.getUuid().equals(bleCurrent) || cupBean.getUuid().equals(mqttCurrent)) {
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

    public void getDrinkCount() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getDrinkList"), mDrinkingModel.getDrinkList(), new MyObserver<DrinkListTotalRsp>() {
            @Override
            public void onMyNext(DrinkListTotalRsp drinkListTotalRsp) {
                mView.onTotalDrink(drinkListTotalRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
        subscribeNetworkTask(getClass().getSimpleName().concat("getTodayDrinkList"), mDrinkingModel.getTodayDrinkList(), new MyObserver<DrinkListTodayRsp>() {
            @Override
            public void onMyNext(DrinkListTodayRsp drinkListTodayRsp) {
                mView.onTodayDrink(drinkListTodayRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
