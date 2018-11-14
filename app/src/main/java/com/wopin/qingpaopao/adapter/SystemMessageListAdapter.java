package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;

public class SystemMessageListAdapter extends RecyclerView.Adapter<SystemMessageListAdapter.SystemMessageHolder> {

    private ArrayList<SystemMessageRsp.ResultBean> systemMessages;
    private SystemMessageItemListener mSystemMessageItemListener;

    public SystemMessageListAdapter(SystemMessageItemListener systemMessageItemListener) {
        mSystemMessageItemListener = systemMessageItemListener;
    }

    public void setSystemMessages(ArrayList<SystemMessageRsp.ResultBean> systemMessages) {
        this.systemMessages = systemMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SystemMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SystemMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_system_message_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SystemMessageHolder holder, int position) {
        final SystemMessageRsp.ResultBean systemMessage = systemMessages.get(position);
        GlideUtils.loadImage(holder.mIcon, -1, systemMessage.getFeatured_image());
        holder.mTitle.setText(systemMessage.getTitle());
        holder.mSubtitle.setText(Html.fromHtml(systemMessage.getContent()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSystemMessageItemListener.onSystemMessageClick(systemMessage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return systemMessages != null ? systemMessages.size() : 0;
    }

    class SystemMessageHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mTitle;
        TextView mSubtitle;

        public SystemMessageHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon_system_message_item);
            mTitle = itemView.findViewById(R.id.tv_system_message_title);
            mSubtitle = itemView.findViewById(R.id.tv_system_message_subtitle);
        }
    }

    public interface SystemMessageItemListener {
        void onSystemMessageClick(SystemMessageRsp.ResultBean systemMessage);
    }
}
