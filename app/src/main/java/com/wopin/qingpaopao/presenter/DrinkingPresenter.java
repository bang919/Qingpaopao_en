package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.WifiConfigToCupRsp;
import com.wopin.qingpaopao.common.Constants;
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
                    addOrUpdateACup(Constants.BLE, bleUpdaterBean.getUuid(), ((BluetoothDevice) mFirstTimeAddDevice).getName(), bleUpdaterBean.getAddress(), true);
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
                    addOrUpdateACup(Constants.WIFI, mqttUpdaterBean.getSsid(), mqttUpdaterBean.getSsid(), null, true);
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
                if ( split[0].equals("P") && mCurrentOnlineCup != null) {
                    mCurrentOnlineCup.setElectric(split[1].substring(0, 2).concat("%"));
                } else if (split[2].equals("H") && mCurrentOnlineCup != null) {
                    //Update hydro timer  --> if split[4] == M and split[5] == 0
                    //Update clean timer --> if split[4] == M and split[5] == 1
                    //If hydro timer == 0 and split[5] == 0 --> Hydro Finish
                    //If hydro timer == 0 and split[5] == 1 --> Clean Finish
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
     * 第一次手动连接杯子
     */
    public <T> void firstTimeAddCup(T device) {
        mFirstTimeAddDevice = device;
        if (device instanceof BluetoothDevice) {//连接没有失败的回调，暂时不加loading，以后可以加个超时
            BleConnectManager.getInstance().connectDevice(((BluetoothDevice) device).getAddress());
        } else if (device instanceof WifiConfigToCupRsp) {
            mView.onLoading();
            MqttConnectManager.getInstance().connectDevice(((WifiConfigToCupRsp) device).getDevice_id());
        } else {
            mView.onError(mContext.getString(R.string.known_error));
        }
    }

    /**
     * 点击list连接杯子
     */
    public void connectCup(CupListRsp.CupBean cupBean) {
        if (cupBean.getType().equals(Constants.BLE)) {
            BleConnectManager.getInstance().connectDevice(cupBean.getAddress());
        } else if (cupBean.getType().equals(Constants.WIFI)) {
            mView.onLoading();
            MqttConnectManager.getInstance().connectDevice(cupBean.getUuid());
        } else {
            mView.onError(mContext.getString(R.string.known_error));
        }
    }

    /**
     * 断开杯子连接
     */
    public void disconnectCup() {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().disconnectDevice();
            } else {//Wifi
                MqttConnectManager.getInstance().disconnectDevice();
            }
        }
    }

    /**
     * 开关电解
     */
    public void switchCupElectrolyze(int time) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupElectrolyze(time);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupElectrolyze(time);
            }
        }
        if (time > 0) {
            mDrinkingModel.drink().subscribe();
//            HttpUtil.handlerObserver()
        }
    }

    /**
     * 开关灯
     */
    public void switchCupLight(boolean isLightOn) {
        if (mCurrentOnlineCup != null) {
            if (mCurrentOnlineCup.getType().equals(Constants.BLE)) {//Ble
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
            if (mCurrentOnlineCup.getType().equals(Constants.BLE)) {//Ble
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
            if (mCurrentOnlineCup.getType().equals(Constants.BLE)) {//Ble
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
