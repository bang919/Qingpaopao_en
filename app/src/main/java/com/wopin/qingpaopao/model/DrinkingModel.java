package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.CupUpdateReq;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DrinkingModel {

    public Observable<NormalRsp> addOrUpdateACup(String type, String uuid, String name, String address, boolean add) {
        CupUpdateReq cupUpdateReq = new CupUpdateReq();
        cupUpdateReq.setType(type);
        cupUpdateReq.setUuid(uuid);
        cupUpdateReq.setName(name);
//        cupUpdateReq.setAddress(address);
        cupUpdateReq.setAdd(add);
        return HttpClient.getApiInterface().addOrUpdateACup(cupUpdateReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CupListRsp> getCupList() {
        return HttpClient.getApiInterface().getCupList()
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
}
