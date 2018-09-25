package com.wopin.qingpaopao.fragment.explore;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BuildBlogPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.BuildBlogView;

public class BuildBlogFragment extends BaseBarDialogFragment<BuildBlogPresenter> implements BuildBlogView {

    public static final String TAG = "BuildBlogFragment";
    private EditText mTitleEt;

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.issue);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布
                setLoadingVisibility(true);
                mPresenter.newBlog(mTitleEt.getText().toString(), "暂时hard code 内容测试。");
            }
        });
    }

    @Override
    protected String setBarTitle() {
        return null;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_build_blog;
    }

    @Override
    protected BuildBlogPresenter initPresenter() {
        return new BuildBlogPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mTitleEt = rootView.findViewById(R.id.et_title);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBlogBuildSuccess() {
        setLoadingVisibility(false);
        ToastUtils.showShort(R.string.success_new_blog);
        dismiss();
    }

    @Override
    public void onError(String errorString) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorString);
    }
}
