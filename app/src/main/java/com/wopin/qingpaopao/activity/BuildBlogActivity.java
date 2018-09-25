package com.wopin.qingpaopao.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.BuildBlogPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.BuildBlogView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuildBlogActivity extends BaseActivity<BuildBlogPresenter> implements BuildBlogView, View.OnClickListener {

    @BindView(R.id.wv_container)
    WebView mWebView;
    @BindView(R.id.fl_action)
    FrameLayout flAction;
    @BindView(R.id.ll_action_bar_container)
    LinearLayout llActionBarContainer;
    @BindView(R.id.et_title)
    EditText mTitleEt;

    private ProgressDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_build_blog;
    }

    @Override
    protected BuildBlogPresenter initPresenter() {
        return new BuildBlogPresenter(this, this);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_top_right).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.bg_progressdialog_white);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setMessage("Please wait, logging in...");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small_Inverse);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBlogBuildSuccess() {
        progressDialog.dismiss();
        ToastUtils.showShort(R.string.success_new_blog);
        finish();
    }

    @Override
    public void onError(String errorString) {
        progressDialog.dismiss();
        ToastUtils.showShort(errorString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_top_right:
                //发布
                progressDialog.show();
                mPresenter.newBlog(mTitleEt.getText().toString(), "暂时hard code 内容测试。");
                break;
        }
    }
}
