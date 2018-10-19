package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.CupColorReq;
import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.request.LocalBean;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.manager.MqttConnectManager;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * 请求杯子列表，请求这个API可以调用connectDevice，以唤醒ConnectManager，并及时自动更新Cup列表状态
     */
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

    public Observable<NormalRsp> drink(String uuid) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"uuid\":\"" + uuid + "\",\"target\":\"8\"}");
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

    public Observable<NormalRsp> sendLocal(String uuid, double latitude, double longitude) {
        LocalBean localBean = new LocalBean();
        localBean.setDevice_id(uuid);
        localBean.setLat(String.valueOf(latitude));
        localBean.setLongX(String.valueOf(longitude));
        String url = "https://www.latlong.net/c/?lat=" + latitude + "&long=" + longitude;
        localBean.setLink(url);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        localBean.setTime(simpleDateFormat.format(date));
        return HttpClient.getApiInterface().sendLocal(localBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
