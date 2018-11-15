package com.wopin.qingpaopao.fragment.message;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.SystemMessageListAdapter;
import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.MessageMainPresenter;
import com.wopin.qingpaopao.utils.NotificationSettingUtil;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MessageMainView;

import java.util.ArrayList;

public class MessageMainFragment extends BaseBarDialogFragment<MessageMainPresenter> implements View.OnClickListener, MessageMainView, SystemMessageListAdapter.SystemMessageItemListener {

    public static final String TAG = "MessageMainFragment";
    private SwitchCompat mNotificationSwitch;
    private RecyclerView mSystemMessageRv;
    private TextView mNewMessageCountTv;
    private SystemMessageListAdapter mSystemMessageListAdapter;
    private ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans;
    private MessageMainFragmentCallback mMessageMainFragmentCallback;

    public void setMessageMainFragmentCallback(MessageMainFragmentCallback messageMainFragmentCallback) {
        mMessageMainFragmentCallback = messageMainFragmentCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.message);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mMessageMainFragmentCallback != null) {
            mMessageMainFragmentCallback.onDismiss();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected MessageMainPresenter initPresenter() {
        return new MessageMainPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mNewMessageCountTv = rootView.findViewById(R.id.count_new_message);
        mNotificationSwitch = rootView.findViewById(R.id.switch_notification);
        mNotificationSwitch.setChecked((Boolean) SPUtils.get(getContext(), Constants.DRINKING_NOTIFICATION, false));
        mSystemMessageRv = rootView.findViewById(R.id.rv_system_message);
        mSystemMessageRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mSystemMessageListAdapter = new SystemMessageListAdapter(this);
        mSystemMessageRv.setAdapter(mSystemMessageListAdapter);

        rootView.findViewById(R.id.icon_new_message).setOnClickListener(this);
        rootView.findViewById(R.id.icon_favor).setOnClickListener(this);
        rootView.findViewById(R.id.icon_my_comment).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        mNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NotificationSettingUtil.startNotification(getContext());
                } else {
                    NotificationSettingUtil.stopNotification(getContext());
                }
            }
        });
        setLoadingVisibility(true);
        mPresenter.getMessages();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_new_message:
                NewCommentFragment.build(newCommentBeans).show(getChildFragmentManager(), NewCommentFragment.TAG);
                break;
            case R.id.icon_favor:
                new MyLikeExploresFragment().show(getChildFragmentManager(), MyLikeExploresFragment.TAG);
                break;
            case R.id.icon_my_comment:
                break;
        }
    }

    @Override
    public void onNewCommentCount(ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans) {
        this.newCommentBeans = newCommentBeans;
        int size = newCommentBeans.size();
        if (size > 0) {
            mNewMessageCountTv.setVisibility(View.VISIBLE);
            mNewMessageCountTv.setText(String.valueOf(size));
        }
    }

    @Override
    public void onSystemMessage(SystemMessageRsp systemMessageRsp) {
        mSystemMessageListAdapter.setSystemMessages(systemMessageRsp.getResult());
    }

    @Override
    public void onFinishRequest() {
        setLoadingVisibility(false);
    }

    @Override
    public void onError(String errorMessage) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void onSystemMessageClick(SystemMessageRsp.ResultBean systemMessage) {
        SystemMessageDetailFragment.build(systemMessage).show(getChildFragmentManager(), SystemMessageDetailFragment.TAG);
    }

    public interface MessageMainFragmentCallback{
        void onDismiss();
    }
}
