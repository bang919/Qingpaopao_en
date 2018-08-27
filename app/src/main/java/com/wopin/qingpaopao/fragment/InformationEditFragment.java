package com.wopin.qingpaopao.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class InformationEditFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback, View.OnClickListener {

    public static final String TAG = "InformationEditFragment";

    @Override
    protected int getLayout() {
        return R.layout.fragment_information_edit;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView listRv = rootView.findViewById(R.id.list_recyclerview);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        listRv.setAdapter(new MineListRvAdapter(new int[]{R.string.person_info, R.string.edit_bind_phone, R.string.edit_bind_third, R.string.edit_password}, this));
        rootView.findViewById(R.id.bt_safe_logout).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.information_edit);
    }

    @Override
    public void onListItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.person_info:
                new PersonInfoFragment().show(getFragmentManager(), PersonInfoFragment.TAG);
                break;
            case R.string.edit_bind_phone:
                break;
            case R.string.edit_bind_third:
                break;
            case R.string.edit_password:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_safe_logout:
                break;
        }
    }
}
