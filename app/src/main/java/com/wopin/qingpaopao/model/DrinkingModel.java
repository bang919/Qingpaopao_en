package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.CupColorReq;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.manager.MqttConnectManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DrinkingModel {

    public Observable<NormalRsp> addOrUpdateACup(String type, String uuid, String name, String address, boolean add) {
        CupUpdateReq cupUpdateReq = new CupUpdateReq();
        cupUpdateReq.setType(type);
        cupUpdateReq.setUuid(uuid);
        cupUpdateReq.setName(name);
        cupUpdateReq.setAddress(address);
        cupUpdateReq.setAdd(add);
        return HttpClient.getApiInterface().addOrUpdateACup(cupUpdateReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> updateCupColor(String uuid, int cupColor) {
        CupColorReq cupColorReq = new CupColorReq();
        cupColorReq.setUuid(uuid);
        cupColorReq.setCupColor(cupColor);
        return HttpClient.getApiInterface().updateCupColor(cupColorReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CupListRsp> getCupList() {
        return HttpClient.getApiInterface().getCupList()
                .doOnNext(new Consumer<CupListRsp>() {
                    @Override
                    public void accept(CupListRsp cupListRsp) throws Exception {
                        for (CupListRsp.CupBean cupBean : cupListRsp.getResult()) {
                            if (cupBean.getType().equals(Constants.WIFI)) {
                                if (!cupBean.isConnecting()) {
                                    MqttConnectManager.getInstance().connectDevice(cupBean.getUuid());
                                }
                            } else if (cupBean.getType().equals(Constants.BLE)) {
                                if (!cupBean.isConnecting()) {
                                    BleConnectManager.getInstance().connectDevice(cupBean.getAddress());
                                }
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> deleteACup(String uuid) {
        CupUpdateReq cupUpdateReq = new CupUpdateReq();
        cupUpdateReq.setUuid(uuid);
        return HttpClient.getApiInterface().deleteACup(cupUpdateReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> drink() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"target\":\"8\"}");
        return HttpClient.getApiInterface().drink(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DrinkListTotalRsp> getDrinkList() {
        return HttpClient.getApiInterface().getDrinkList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DrinkListTodayRsp> getTodayDrinkList() {
        return HttpClient.getApiInterface().getTodayDrinkList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
