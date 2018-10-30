package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.UploadImageRsp;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.presenter.LoginPresenter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MineModel {

    public Observable<LoginRsp> getUserData() {
        return HttpClient.getApiInterface().getUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<LoginRsp>() {
                    @Override
                    public void accept(LoginRsp loginRsp) throws Exception {
                        LoginPresenter.updateLoginMessage(loginRsp);
                    }
                });
    }

    public Observable<NormalRsp> uploadImage(File portraitFile) {
        RequestBody requestBody = MultipartBody.create(MediaType.parse("application/x-jpg"), portraitFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",
                portraitFile.getName().concat(String.valueOf(System.currentTimeMillis())).concat(".jpg"),
                requestBody);

        return HttpClient.getApiInterface()
                .uploadPicture(part)
                .flatMap(new Function<UploadImageRsp, ObservableSource<NormalRsp>>() {
                    @Override
                    public ObservableSource<NormalRsp> apply(UploadImageRsp uploadImageRsp) throws Exception {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"icon\":\"" + uploadImageRsp.getUrl() + "\"}");
                        return HttpClient.getApiInterface().changeIcon(requestBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
