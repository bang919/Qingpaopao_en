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


/**
 * Created by Administrator on 2017/11/21.
 */

public class NormalEdittextDialog extends Dialog {


    private EditText mTvDesc;
    private TextView mTvYes;
    private TextView mTvNo;

    private String mTextDesc;
    private String mTextYes;
    private String mTextNo;
    private NormalEdittextDialogETextCallback mYesClickListener;
    private View.OnClickListener mNoClickListener;

    public NormalEdittextDialog(Context context, String buttonYesTitle, String buttonNoTitle, String hint,
                                NormalEdittextDialogETextCallback clickListener, View.OnClickListener noClickListener) {
        super(context, R.style.FullScreenDialog);
        mTextDesc = hint;
        mTextYes = buttonYesTitle;
        mTextNo = buttonNoTitle;
        mYesClickListener = clickListener;
        mNoClickListener = noClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable());
//        getWindow().setBackgroundDrawableResource(R.mipmap.icon_bg);

        setContentView(R.layout.dialog_edittext_normal);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        initData();
    }

    private void initData() {
        mTvDesc.setHint(mTextDesc);
        if (!TextUtils.isEmpty(mTextYes)) {
            mTvYes.setText(mTextYes);
        } else {
            mTvYes.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTextNo)) {
            mTvNo.setText(mTextNo);
        } else {
            mTvNo.setVisibility(View.GONE);
        }
        mTvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYesClickListener != null) {
                    mYesClickListener.onEdittextInput(mTvDesc.getText().toString());
                }
                dismiss();
            }
        });
        mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNoClickListener != null) {
                    mNoClickListener.onClick(view);
                }
                dismiss();
            }
        });
    }

    private void initView() {
        mTvDesc = findViewById(R.id.tv_content);
        mTvYes = findViewById(R.id.tv_yes);
        mTvNo = findViewById(R.id.tv_no);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return true;
    }

    public interface NormalEdittextDialogETextCallback {
        void onEdittextInput(String etString);
    }
}
