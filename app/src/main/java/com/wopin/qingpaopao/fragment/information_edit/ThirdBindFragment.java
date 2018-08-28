package com.wopin.qingpaopao.fragment.information_edit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.ThirdBindRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.ThirdLoginModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.HttpUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ThirdBindFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback, ThirdLoginModel.ThirdLoginCallback {

    public static final String TAG = "ThirdBindFragment";

    private ThirdLoginModel mThirdLoginModel;
    private MineListRvAdapter mAdapter;
    private String[] bindings;

    @Override
    protected String setBarTitle() {
        return getString(R.string.edit_bind_third);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_third_bind;
    }

    @Override
    protected BasePresenter initPresenter() {
        mThirdLoginModel = new ThirdLoginModel();
        return null;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView thirdListRv = rootView.findViewById(R.id.list_recyclerview);
        thirdListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MineListRvAdapter(
                new int[]{R.string.ssdk_wechat, R.string.ssdk_qq, R.string.ssdk_sinaweibo},
                new int[]{R.mipmap.t_weixin1, R.mipmap.t_qq1, R.mipmap.t_weibo1},
                this
        );
        thirdListRv.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        HttpUtil.subscribeNetworkTask(mThirdLoginModel.getThirdBind(), new BasePresenter.MyObserver<ThirdBindRsp>() {
            @Override
            public void onMyNext(ThirdBindRsp thirdBindRsp) {
                bindings = new String[]{getString(R.string.no_binding), getString(R.string.no_binding), getString(R.string.no_binding)};
                List<ThirdBindRsp.ResultBean> result = thirdBindRsp.getResult();
                for (ThirdBindRsp.ResultBean resultBean : result) {
                    String thirdType = resultBean.getThirdType();
                    if (thirdType.equals(Wechat.NAME)) {
                        bindings[0] = getString(R.string.yes_binding);
                    } else if (thirdType.equals(QQ.NAME)) {
                        bindings[1] = getString(R.string.yes_binding);
                    } else if (thirdType.equals(SinaWeibo.NAME)) {
                        bindings[2] = getString(R.string.yes_binding);
                    }
                }
                mAdapter.setDatas(bindings);
            }

            @Override
            public void onMyError(String errorMessage) {
                ToastUtils.showShort(R.string.get_binding_failure);
            }
        });
    }

    @Override
    public void onListItemClick(int textResource, int position) {
        if (bindings == null || bindings.length != 3) {
            return;
        }
        switch (textResource) {
            case R.string.ssdk_wechat:
                if (bindings[0].equals(getString(R.string.no_binding)))
                    mThirdLoginModel.loginByThird(ShareSDK.getPlatform(Wechat.NAME), this);
                break;
            case R.string.ssdk_qq:
                if (bindings[1].equals(getString(R.string.no_binding)))
                    mThirdLoginModel.loginByThird(ShareSDK.getPlatform(QQ.NAME), this);
                break;
            case R.string.ssdk_sinaweibo:
                if (bindings[2].equals(getString(R.string.no_binding)))
                    mThirdLoginModel.loginByThird(ShareSDK.getPlatform(SinaWeibo.NAME), this);
                break;
        }
    }

    @Override
    public void onThirdSuccess(String platformName, String userName, String userId, String userIcon) {
        HttpUtil.subscribeNetworkTask(mThirdLoginModel.thirdBinding(platformName, userId), new BasePresenter.MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                initEvent();
                ToastUtils.showShort(R.string.bind_success);
            }

            @Override
            public void onMyError(String errorMessage) {
                ToastUtils.showShort(errorMessage);
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }
}
