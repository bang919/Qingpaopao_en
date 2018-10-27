package com.wopin.qingpaopao.fragment.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class EditMyHealthFragment extends BaseBarDialogFragment {

    public static final String TAG = "EditMyHealthFragment";
    public static final String TITLE = "Title";
    public static final String KEY1 = "Key1";
    public static final String KEY2 = "Key2";
    public static final String KEY3 = "Key3";
    public static final String VALUE1 = "Value1";
    public static final String VALUE2 = "Value2";
    public static final String VALUE3 = "Value3";

    private TextView mKeyTv1;
    private TextView mKeyTv2;
    private TextView mKeyTv3;
    private TextView mValueTv1;
    private TextView mValueTv2;
    private TextView mValueTv3;

    private EditMyHealthCallback mEditMyHealthCallback;

    public static EditMyHealthFragment build(String title, String key1, String value1) {
        return build(title, key1, value1, null, null);
    }

    public static EditMyHealthFragment build(String title, String key1, String value1, String key2, String value2) {
        return build(title, key1, value1, key2, value2, null, null);
    }

    public static EditMyHealthFragment build(String title, String key1, String value1, String key2, String value2, String key3, String value3) {
        EditMyHealthFragment editMyHealthFragment = new EditMyHealthFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(KEY1, key1);
        args.putString(KEY2, key2);
        args.putString(KEY3, key3);
        args.putString(VALUE1, value1);
        args.putString(VALUE2, value2);
        args.putString(VALUE3, value3);
        editMyHealthFragment.setArguments(args);
        return editMyHealthFragment;
    }

    public void setEditMyHealthCallback(EditMyHealthCallback editMyHealthCallback) {
        mEditMyHealthCallback = editMyHealthCallback;
    }

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.save);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMyHealthCallback != null) {
                    mEditMyHealthCallback.onEditMyHealthCallback(mValueTv1.getText().toString(), mValueTv2.getText().toString(), mValueTv3.getText().toString());
                    dismiss();
                }
            }
        });
    }

    @Override
    protected String setBarTitle() {
        String title = getArguments().getString(TITLE);
        String key1 = getArguments().getString(KEY1);
        return TextUtils.isEmpty(title) ? getString(R.string.edit_health_title, key1) : title;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_my_health;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mKeyTv1 = rootView.findViewById(R.id.key1);
        mKeyTv2 = rootView.findViewById(R.id.key2);
        mKeyTv3 = rootView.findViewById(R.id.key3);
        mValueTv1 = rootView.findViewById(R.id.value1);
        mValueTv2 = rootView.findViewById(R.id.value2);
        mValueTv3 = rootView.findViewById(R.id.value3);

        Bundle arguments = getArguments();
        String key1 = arguments.getString(KEY1);
        String key2 = arguments.getString(KEY2);
        String key3 = arguments.getString(KEY3);
        String value1 = arguments.getString(VALUE1);
        String value2 = arguments.getString(VALUE2);
        String value3 = arguments.getString(VALUE3);

        mKeyTv1.setText(key1);
        mKeyTv2.setText(key2);
        mKeyTv3.setText(key3);
        mValueTv1.setText(value1);
        mValueTv2.setText(value2);
        mValueTv3.setText(value3);

        mKeyTv2.setVisibility(TextUtils.isEmpty(key2) ? View.GONE : View.VISIBLE);
        mValueTv2.setVisibility(TextUtils.isEmpty(key2) ? View.GONE : View.VISIBLE);
        mKeyTv3.setVisibility(TextUtils.isEmpty(key3) ? View.GONE : View.VISIBLE);
        mValueTv3.setVisibility(TextUtils.isEmpty(key3) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initEvent() {

    }

    public interface EditMyHealthCallback {
        void onEditMyHealthCallback(String value1, String value2, String value3);
    }
}
