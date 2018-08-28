package com.wopin.qingpaopao.fragment.information_edit;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.HttpUtil;
import com.wopin.qingpaopao.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditUsernameFragment extends BaseBarDialogFragment {

    public static final String TAG = "EditUsernameFragment";
    private EditText mUsernameEt;
    private EditUsernameCallback mEditUsernameCallback;

    public void setEditUsernameCallback(EditUsernameCallback editUsernameCallback) {
        mEditUsernameCallback = editUsernameCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.edit_username);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_username;
    }

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.save);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUsername();
            }
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mUsernameEt = rootView.findViewById(R.id.et_input_username);
    }

    @Override
    protected void initEvent() {

    }

    private void editUsername() {
        final String newUserName = mUsernameEt.getText().toString();
        if (TextUtils.isEmpty(newUserName)) {
            ToastUtils.showShort(R.string.please_input_new_username);
            return;
        }
        ThirdReq thirdReq = new ThirdReq();
        thirdReq.setUserName(newUserName);
        HttpUtil.subscribeNetworkTask(
                HttpClient.getApiInterface().changeUsername(thirdReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()),
                new BasePresenter.MyObserver<NormalRsp>() {
                    @Override
                    public void onMyNext(NormalRsp normalRsp) {
                        LoginRsp accountMessage = LoginPresenter.getAccountMessage();
                        accountMessage.getResult().setUserName(newUserName);
                        LoginPresenter.updateLoginMessage(accountMessage);
                        ToastUtils.showShort(R.string.edit_username_success);
                        if (!isDestroy) {
                            dismiss();
                        }
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        if (!isDestroy) {
                            ToastUtils.showShort(errorMessage);
                        }
                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mEditUsernameCallback != null) {
            mEditUsernameCallback.onEditUsernameDismiss();
        }
    }

    public interface EditUsernameCallback {
        void onEditUsernameDismiss();
    }
}
