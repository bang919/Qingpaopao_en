package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wopin.qingpaopao.R;

public class WifiFragmentPage2 extends Fragment {

    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi_page_2, container, false);
        rootView.findViewById(R.id.btn_scan).setOnClickListener(mOnClickListener);
        rootView.findViewById(R.id.btn_link_by_hand).setOnClickListener(mOnClickListener);
        return rootView;
    }
}