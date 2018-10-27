package com.wopin.qingpaopao.presenter;

import android.support.v4.app.Fragment;

import com.wopin.qingpaopao.model.MineModel;
import com.wopin.qingpaopao.view.PersonInfoView;

import java.io.File;

public class PersonInfoPresenter extends PhotoPresenter<PersonInfoView> {

    private File portraitFile;

    public PersonInfoPresenter(Fragment fragment, PersonInfoView view) {
        super(fragment, view);
    }

    @Override
    public void onLuBanError(Throwable e) {
        mView.onError(e.getLocalizedMessage());
    }

    @Override
    public void onLuBanSuccess(File file) {
        portraitFile = file;
        MineModel mineModel = new MineModel();
//        subscribeNetworkTask(getClass().getSimpleName().concat("updateImage"), mineModel.uploadImage(file), new MyObserver<UploadImageRsp>() {
//            @Override
//            public void onMyNext(UploadImageRsp normalRsp) {
//                deletePhotoFile();
//
//                mView.onImageGet(normalRsp.getUrl());
//            }
//
//            @Override
//            public void onMyError(String errorMessage) {
//                deletePhotoFile();
//                mView.onError(errorMessage);
//            }
//        });
    }

    public void deletePhotoFile() {
        if (portraitFile != null) {
            deletePhotoFile(portraitFile.getPath());
            portraitFile = null;
        }
    }
}
