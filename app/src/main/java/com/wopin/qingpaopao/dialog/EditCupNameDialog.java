package com.wopin.qingpaopao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.utils.ToastUtils;


/**
 * Created by Administrator on 2017/11/21.
 */

public class EditCupNameDialog extends Dialog {


    private EditText mTvDesc;
    private TextView mTvYes;
    private TextView mTvNo;
    private String oldName;
    private OnEditNickNameDialogCallback mYesClickListener;

    public EditCupNameDialog(Context context, String oldName, OnEditNickNameDialogCallback clickListener) {
        super(context, R.style.FullScreenDialog);
        this.oldName = oldName;
        mYesClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable());
//        getWindow().setBackgroundDrawableResource(R.mipmap.icon_bg);

        setContentView(R.layout.dialog_edit_cup_name);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        initData();
    }

    private void initData() {
        mTvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mTvDesc.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showShort(R.string.change_device_name_cant_be_empty);
                    return;
                }
                if (mYesClickListener != null) {
                    mYesClickListener.onEditNickNameDialog(s);
                }
                dismiss();
            }
        });
        mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initView() {
        mTvDesc = findViewById(R.id.et_new_nick_name);
        mTvYes = findViewById(R.id.tv_yes);
        mTvNo = findViewById(R.id.tv_no);
        mTvDesc.setText(oldName);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return true;
    }

    public interface OnEditNickNameDialogCallback {
        void onEditNickNameDialog(String newNickName);
    }
}
