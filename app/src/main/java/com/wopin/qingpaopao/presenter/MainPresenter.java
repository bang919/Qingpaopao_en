package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.common.BasePresenter;
import com.wopin.qingpaopao.model.MainModel;
import com.wopin.qingpaopao.view.MainView;


/**
 * Created by Administrator on 2018/2/7.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private MainModel mMainModel;

    public MainPresenter(Context context, MainView view) {
        super(context, view);
        mMainModel = new MainModel();
    }

}
