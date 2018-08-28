package com.wopin.qingpaopao.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.BasePresenter;

public abstract class BaseBarDialogFragment<P extends BasePresenter> extends BaseDialogFragment<P> {

    public boolean isDestroy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_bar, container, false);
        ConstraintLayout rootLayout = rootView.findViewById(R.id.root_base_bar);

        addChildToRootView(rootLayout, super.onCreateView(inflater, container, savedInstanceState));

        setBaseUi(rootLayout);

        return rootLayout;
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        super.onDestroy();
    }

    private void addChildToRootView(ConstraintLayout rootLayout, View child) {
        rootLayout.addView(child);
        ConstraintLayout.LayoutParams childLayoutParams = (ConstraintLayout.LayoutParams) child.getLayoutParams();
        childLayoutParams.width = 0;
        childLayoutParams.height = 0;
        childLayoutParams.topToBottom = R.id.actionbar_title;
        childLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        childLayoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        childLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        child.setLayoutParams(childLayoutParams);
    }

    private void setBaseUi(View rootLayout) {
        TextView titleBarView = rootLayout.findViewById(R.id.actionbar_title);
        titleBarView.setText(setBarTitle());
        TextView topRightTv = rootLayout.findViewById(R.id.tv_top_right);
        onTopRightCornerTextView(topRightTv);

        rootLayout.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootLayout.setBackgroundColor(setRootLayoutBackgroundColor());
    }

    protected abstract String setBarTitle();

    protected int setRootLayoutBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorWhite2);
    }

    protected void onTopRightCornerTextView(TextView textView) {
    }
}
