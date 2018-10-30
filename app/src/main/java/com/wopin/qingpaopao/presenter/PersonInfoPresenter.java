package com.wopin.qingpaopao.presenter;

import android.support.v4.app.Fragment;

import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.MineModel;
import com.wopin.qingpaopao.view.PersonInfoView;

import java.io.File;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

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
        final MineModel mineModel = new MineModel();
        subscribeNetworkTask(getClass().getSimpleName().concat("updateImage"),
                mineModel.uploadImage(file).flatMap(new Function<NormalRsp, ObservableSource<LoginRsp>>() {
                    @Override
                    public ObservableSource<LoginRsp> apply(NormalRsp normalRsp) throws Exception {
                        return mineModel.getUserData();
                    }
                }), new MyObserver<LoginRsp>() {
                    @Override
                    public void onMyNext(LoginRsp normalRsp) {
                        deletePhotoFile();
                        mView.onUserDataRefresh();
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
