package com.wopin.qingpaopao.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

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
    private ArrayList<CupListRsp.CupBean> mCupBeans;//API获取连接过的设备
    private ArrayList<String> mOnlineCups;//可以控制的设备，记录uuid
    private CupListRsp.CupBean mCurrentControlCup;//当前控制的设备
    private Updater<BleConnectManager.BleUpdaterBean> mBleUpdater;
    private Updater<MqttConnectManager.MqttUpdaterBean> mMqttUpdater;

    private Object mFirstTimeAddDevice;//会在onConnectDevice后清空  BluetoothDevice/WifiRsp

    public DrinkingPresenter(Context context, DrinkingView view) {
        super(context, view);
        mDrinkingModel = new DrinkingModel();
        mOnlineCups = new ArrayList<>();
        mBleUpdater = new Updater<BleConnectManager.BleUpdaterBean>() {
            @Override
            public void onConnectDevice(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                if (mOnlineCups.indexOf(bleUpdaterBean.getUuid()) < 0) {
                    mOnlineCups.add(bleUpdaterBean.getUuid());
                }
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
                    updateCupListUi();
                }
                mFirstTimeAddDevice = null;
            }

            @Override
            public void onDissconnectDevice(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                String address = bleUpdaterBean.getAddress();
                for (CupListRsp.CupBean cupBean : mCupBeans) {
                    if (address.equals(cupBean.getAddress())) {
                        mOnlineCups.remove(cupBean.getUuid());
                        updateCupListUi();
                        break;
                    }
                }
            }

            @Override
            public void onDatasUpdate(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                byte[] values = bleUpdaterBean.getValues();
                String s = DataUtil.byteArrayToHex(values);
                parseBleData(bleUpdaterBean.getUuid(), s);
            }
        };
        mMqttUpdater = new Updater<MqttConnectManager.MqttUpdaterBean>() {
            @Override
            public void onConnectDevice(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                if (mOnlineCups.indexOf(mqttUpdaterBean.getSsid()) < 0) {
                    mOnlineCups.add(mqttUpdaterBean.getSsid());
                }
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
                    updateCupListUi();
                }
                mFirstTimeAddDevice = null;
            }

            @Override
            public void onDissconnectDevice(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                mOnlineCups.remove(mqttUpdaterBean.getSsid());
                updateCupListUi();
            }

            @Override
            public void onDatasUpdate(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                String message = mqttUpdaterBean.getMessage();
                String[] split = message.split(":");
                if (split[0].equals("P")) {
                    for (CupListRsp.CupBean cupBean : mCupBeans) {
                        if (cupBean.getUuid().equals(mqttUpdaterBean.getSsid())) {
                            cupBean.setElectric(split[1].substring(0, 2).concat("%"));
                            break;
                        }
                    }
                } else if (split[2].equals("H")) {
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

    private void parseBleData(String uuid, String data) {
        if (data.startsWith("AA CC DD 01 ") && data.endsWith(" DD CC AA")) {//电量数据
            for (CupListRsp.CupBean cupBean : mCupBeans) {
                if (cupBean.getUuid().equals(uuid)) {
                    String hexElectric = data.replaceFirst("AA CC DD 01 ", "").replace(" DD CC AA", "");
                    cupBean.setElectric(Integer.valueOf(hexElectric, 16) + "%");
                    break;
                }
            }
        }
    }

    //更新在线设备的UI
    private void updateCupListUi() {
        for (CupListRsp.CupBean cupBean : mCupBeans) {
            boolean isConnect = mOnlineCups.indexOf(cupBean.getUuid()) != -1;
            if (isConnect && mCurrentControlCup == null) {
                mCurrentControlCup = cupBean;
            }
            if (isConnect && !cupBean.isConnecting()) {//第一次连接，可以clean
                cupBean.setCanClean(true);
            }
            cupBean.setConnecting(isConnect);
        }
        mView.onCupList(mCupBeans, mCurrentControlCup);
    }

    @Override
    public void destroy() {
        BleConnectManager.getInstance().removeUpdater(mBleUpdater);
        MqttConnectManager.getInstance().removeUpdater(mMqttUpdater);
        super.destroy();
    }

    public void setCurrentControlCup(CupListRsp.CupBean cup) {
        mCurrentControlCup = cup;
    }

    public CupListRsp.CupBean getCurrentControlCup() {
        return mCurrentControlCup;
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
//            mView.onLoading();//手动连接还是不要loading啦，不然如果wifi模块不在线怎么办？或者设置一个超时也行
            MqttConnectManager.getInstance().connectDevice(cupBean.getUuid());
        } else {
            mView.onError(mContext.getString(R.string.known_error));
        }
    }

    /**
     * 断开杯子连接
     */
    public void disconnectCup(CupListRsp.CupBean cupBean) {
        if (mCurrentControlCup != null && cupBean.getUuid().equals(mCurrentControlCup.getUuid())) {
            mCurrentControlCup = null;
        }
        if (cupBean != null) {
            if (cupBean.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().disconnectDevice(cupBean.getAddress());
            } else {//Wifi
                MqttConnectManager.getInstance().disconnectDevice(cupBean.getUuid());
            }
        }
    }

    /**
     * 开关电解
     */
    public void switchCupElectrolyze(int time) {
        if (mCurrentControlCup != null) {
            if (mCurrentControlCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupElectrolyze(mCurrentControlCup.getAddress(), time);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupElectrolyze(mCurrentControlCup.getUuid(), time);
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
        if (mCurrentControlCup != null) {
            if (mCurrentControlCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupLight(mCurrentControlCup.getAddress(), isLightOn);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupLight(mCurrentControlCup.getUuid(), isLightOn);
            }
        }
    }

    /**
     * 清洗杯子
     */
    public void switchCupClean(boolean isClean) {
        if (mCurrentControlCup != null) {
            if (mCurrentControlCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().switchCupClean(mCurrentControlCup.getAddress(), isClean);
            } else {//Wifi
                MqttConnectManager.getInstance().switchCupClean(mCurrentControlCup.getUuid(), isClean);
            }
        }
    }

    /**
     * 调节颜色
     */
    public void setColor(String color) {
        if (mCurrentControlCup != null) {
            if (mCurrentControlCup.getType().equals(Constants.BLE)) {//Ble
                BleConnectManager.getInstance().setColor(mCurrentControlCup.getAddress(), color);
            } else {//Wifi
                MqttConnectManager.getInstance().setColor(mCurrentControlCup.getUuid(), color);
            }
        }
    }

    public void getCupList() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getCupList"), mDrinkingModel.getCupList(), new MyObserver<CupListRsp>() {
            @Override
            public void onMyNext(CupListRsp cupListRsp) {
                mCupBeans = cupListRsp.getResult();
                updateCupListUi();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void deleteACup(String uuid) {
        if (mCurrentControlCup != null && mCurrentControlCup.getUuid().equals(uuid)) {
            mCurrentControlCup = null;
        }
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
