package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CupListRsp;

import java.util.ArrayList;

public class ControlDeviceAdapter extends RecyclerView.Adapter<ControlDeviceAdapter.ControlDeviceViewHolder> {

    private ArrayList<CupListRsp.CupBean> onlineCups;
    private ControlDeviceAdapterCallback mControlDeviceAdapterCallback;

    public ControlDeviceAdapter(ArrayList<CupListRsp.CupBean> onlineCups, ControlDeviceAdapterCallback controlDeviceAdapterCallback) {
        this.onlineCups = onlineCups;
        this.mControlDeviceAdapterCallback = controlDeviceAdapterCallback;
    }

    @NonNull
    @Override
    public ControlDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlDeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mine_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ControlDeviceViewHolder holder, int position) {
        final CupListRsp.CupBean cupBean = onlineCups.get(position);
        holder.mTitleTv.setText(cupBean.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlDeviceAdapterCallback.onDeviceDlick(cupBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return onlineCups != null ? onlineCups.size() : 0;
    }

    class ControlDeviceViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;

        public ControlDeviceViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.iv_item_rv_mine_list).setVisibility(View.GONE);
            itemView.findViewById(R.id.next_item_rv_mine_list).setVisibility(View.GONE);
            mTitleTv = itemView.findViewById(R.id.tv_item_rv_mine_list);
        }
    }

    public interface ControlDeviceAdapterCallback {
        void onDeviceDlick(CupListRsp.CupBean cupBean);
    }
}
