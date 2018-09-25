package com.wopin.qingpaopao.presenter;

import android.app.Activity;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.UploadImageRsp;
import com.wopin.qingpaopao.model.BuildBlogModel;
import com.wopin.qingpaopao.view.BuildBlogView;

import java.io.File;

public class BuildBlogPresenter extends PhotoPresenter<BuildBlogView> {

    private BuildBlogModel mBuildBlogModel;
    private File portraitFile;

    public BuildBlogPresenter(Activity activity, BuildBlogView view) {
        super(activity, view);
        mBuildBlogModel = new BuildBlogModel();
    }

    public void newBlog(String title, String content) {
        subscribeNetworkTask(getClass().getSimpleName().concat("newBlog"), mBuildBlogModel.newBlog(title, content), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onBlogBuildSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    @Override
    public void onLuBanError(Throwable e) {
        mView.onError(e.getLocalizedMessage());
    }

    @Override
    public void onLuBanSuccess(File file) {
        portraitFile = file;
        updateImage(file);
    }

    private void updateImage(File file) {
        mView.onImageUpdating();
        subscribeNetworkTask(getClass().getSimpleName().concat("updateImage"), mBuildBlogModel.uploadImage(file), new MyObserver<UploadImageRsp>() {
            @Override
            public void onMyNext(UploadImageRsp normalRsp) {
                deletePhotoFile();
                mView.onImageGet(normalRsp.getUrl());
            }

            @Override
            public void onMyError(String errorMessage) {
                deletePhotoFile();
                mView.onError(errorMessage);
            }
        });
    }

    public void deletePhotoFile() {
        if (portraitFile != null) {
            deletePhotoFile(portraitFile.getPath());
            portraitFile = null;
        }
    }
}
