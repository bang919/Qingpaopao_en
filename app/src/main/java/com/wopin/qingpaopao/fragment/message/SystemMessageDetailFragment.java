package com.wopin.qingpaopao.fragment.message;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SystemMessageDetailFragment extends BaseBarDialogFragment {

    public static final String TAG = "SystemMessageDetailFragment";

    public static SystemMessageDetailFragment build(SystemMessageRsp.ResultBean systemMessage) {
        SystemMessageDetailFragment systemMessageDetailFragment = new SystemMessageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, systemMessage);
        systemMessageDetailFragment.setArguments(args);
        return systemMessageDetailFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.system_message);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_system_message_detail;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        SystemMessageRsp.ResultBean systemMessage = (SystemMessageRsp.ResultBean) getArguments().getSerializable(TAG);
        ((TextView) rootView.findViewById(R.id.explore_title)).setText(systemMessage.getTitle());
        ((TextView) rootView.findViewById(R.id.system_message_content)).setText(Html.fromHtml(systemMessage.getContent()));

        GlideUtils.loadImage((ImageView) rootView.findViewById(R.id.author_icon), -1, systemMessage.getAuthor().getAvatar_URL(),
                new CenterCrop(), new CircleCrop());
        ((TextView) rootView.findViewById(R.id.author_name)).setText(systemMessage.getAuthor().getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        ((TextView) rootView.findViewById(R.id.time_explore_item)).setText(TimeFormatUtils.formatToTime(systemMessage.getDate(), format));
        ((TextView) rootView.findViewById(R.id.read_count)).setText(getString(R.string.read_count, systemMessage.getRead()));
    }

    @Override
    protected void initEvent() {

    }
}
