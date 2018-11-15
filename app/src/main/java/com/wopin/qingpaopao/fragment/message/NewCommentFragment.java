package com.wopin.qingpaopao.fragment.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.NewCommentListAdapter;
import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.MessageMainPresenter;

import java.util.ArrayList;

public class NewCommentFragment extends BaseBarDialogFragment implements NewCommentListAdapter.NewCommentClickListener {

    public static final String TAG = "NewCommentFragment";

    private RecyclerView mRecyclerView;
    private NewCommentListAdapter mNewCommentListAdapter;

    public static NewCommentFragment build(ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans) {
        NewCommentFragment newCommentFragment = new NewCommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, newCommentBeans);
        newCommentFragment.setArguments(args);
        return newCommentFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.new_comment);
    }

    @Override
    protected int getLayout() {
        return R.layout.recyclerview_list;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewCommentListAdapter = new NewCommentListAdapter(this);
        mRecyclerView.setAdapter(mNewCommentListAdapter);
    }

    @Override
    protected void initEvent() {
        ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans = (ArrayList<NewCommentRsp.ResultBean.NewCommentBean>) getArguments().getSerializable(TAG);
        mNewCommentListAdapter.setNewComments(newCommentBeans);
        MessageMainPresenter.clearNewCommentRsps();
    }


    @Override
    public void onNewCommentClick(NewCommentRsp.ResultBean.NewCommentBean newComment) {

    }
}
