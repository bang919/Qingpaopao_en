package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.NewBlogReq;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.UploadImageRsp;
import com.wopin.qingpaopao.http.HttpClient;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BuildBlogModel {

    public Observable<NormalRsp> newBlog(String title, String content) {
        NewBlogReq newBlogReq = new NewBlogReq();
        newBlogReq.setTitle(title);
        newBlogReq.setContent(content);
        return HttpClient.getApiInterface().newBlog(newBlogReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UploadImageRsp> uploadImage(File portraitFile) {
        RequestBody requestBody = MultipartBody.create(MediaType.parse("application/x-jpg"), portraitFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",
                portraitFile.getName().concat(String.valueOf(System.currentTimeMillis())).concat(".jpg"),
                requestBody);

        return HttpClient.getApiInterface()
                .uploadPicture(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
