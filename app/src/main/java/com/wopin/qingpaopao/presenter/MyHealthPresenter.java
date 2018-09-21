package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.request.BodyProfilesBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.model.MyHealthModel;
import com.wopin.qingpaopao.view.MyHealthView;

public class MyHealthPresenter extends BasePresenter<MyHealthView> {

    private MyHealthModel mMyHealthModel;

    public MyHealthPresenter(Context context, MyHealthView view) {
        super(context, view);
        mMyHealthModel = new MyHealthModel();
    }

    public void updateHeight(String height) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("height");
        bodyProfilesBean.setValue1(height);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateWeight(String weight) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("weight");
        bodyProfilesBean.setValue1(weight);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateAge(String age) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("age");
        bodyProfilesBean.setValue1(age);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateBloodSugar(String bloodSugarFull, String bloodSugarHungry) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("blood_sugar_full");
        bodyProfilesBean.setKey2("blood_sugar_hugry");
        bodyProfilesBean.setValue1(bloodSugarFull);
        bodyProfilesBean.setValue2(bloodSugarHungry);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateBloodLipid(String bloodLipidAll, String bloodLipid, String bloodLipidTG) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("blood_lipid_all");
        bodyProfilesBean.setKey2("blood_lipid");
        bodyProfilesBean.setKey3("blood_lipid_TG");
        bodyProfilesBean.setValue1(bloodLipidAll);
        bodyProfilesBean.setValue2(bloodLipid);
        bodyProfilesBean.setValue3(bloodLipidTG);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateBloodPressure(String bloodPressure, String bloodPressurePress) {
        BodyProfilesBean bodyProfilesBean = new BodyProfilesBean();
        bodyProfilesBean.setKey1("blood_pressure");
        bodyProfilesBean.setKey2("blood_pressure_press");
        bodyProfilesBean.setValue1(bloodPressure);
        bodyProfilesBean.setValue2(bloodPressurePress);
        updateBodyProfiles(bodyProfilesBean);
    }

    public void updateBodyProfiles(BodyProfilesBean bodyProfilesBean) {
        subscribeNetworkTask(getClass().getSimpleName().concat("updateBodyProfiles"), mMyHealthModel.updateBodyProfiles(bodyProfilesBean),
                new MyObserver<LoginRsp>() {
                    @Override
                    public void onMyNext(LoginRsp loginRsp) {
                        LoginPresenter.updateLoginMessage(loginRsp);
                        mView.onUpdateSuccess();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
