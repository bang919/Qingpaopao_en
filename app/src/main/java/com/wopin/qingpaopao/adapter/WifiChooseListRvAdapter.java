package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.fragment.drinking.WifiFragmentPageChooseListDialog;

import java.util.ArrayList;

public class WifiChooseListRvAdapter extends RecyclerView.Adapter<WifiChooseListRvAdapter.WifiChooseListViewHolder> {

    private ArrayList<WifiRsp> wifiRsps;
    private WifiFragmentPageChooseListDialog.WifiChooseCallback mWifiChooseCallback;

    public WifiChooseListRvAdapter(ArrayList<WifiRsp> wifiRsps, WifiFragmentPageChooseListDialog.WifiChooseCallback wifiChooseCallback) {
        this.wifiRsps = wifiRsps;
        this.mWifiChooseCallback = wifiChooseCallback;
    }

    @NonNull
    @Override
    public WifiChooseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WifiChooseListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mine_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final WifiChooseListViewHolder holder, final int position) {
        final WifiRsp wifiRsp = position == 0 ? null : wifiRsps.get(position - 1);
        if (position == 0) {
            holder.mTitleTv.setText(R.string.input_ssid);
            holder.mTitleTv.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent3));
        } else {
            holder.mTitleTv.setText(wifiRsp.getEssid());
            holder.mTitleTv.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorBlack));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiChooseCallback.onWifiChoose(position == 0 ? null : wifiRsp.getEssid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return wifiRsps.size() + 1;
    }

    class WifiChooseListViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;

        public WifiChooseListViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.iv_item_rv_mine_list).setVisibility(View.GONE);
            itemView.findViewById(R.id.next_item_rv_mine_list).setVisibility(View.GONE);
            mTitleTv = itemView.findViewById(R.id.tv_item_rv_mine_list);
        }
    }
}
