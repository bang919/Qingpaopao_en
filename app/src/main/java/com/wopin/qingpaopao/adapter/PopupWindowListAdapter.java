package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;

import java.util.List;

public class PopupWindowListAdapter extends RecyclerView.Adapter<PopupWindowListAdapter.PopupWindowListViewHolder> {

    private List<String> datas;
    private PopupWindowListAdapterCallback mPopupWindowListAdapterCallback;

    public PopupWindowListAdapter(List<String> datas, PopupWindowListAdapterCallback popupWindowListAdapterCallback) {
        this.datas = datas;
        this.mPopupWindowListAdapterCallback = popupWindowListAdapterCallback;
    }

    @NonNull
    @Override
    public PopupWindowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopupWindowListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popupwindow_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopupWindowListViewHolder holder, final int position) {
        final String s = datas.get(position);
        holder.mTitleTv.setText(s);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowListAdapterCallback.onItemClick(s, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class PopupWindowListViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;

        public PopupWindowListViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.iv_item_rv_mine_list).setVisibility(View.GONE);
            itemView.findViewById(R.id.next_item_rv_mine_list).setVisibility(View.GONE);
            mTitleTv = itemView.findViewById(R.id.tv_item_rv_mine_list);
        }
    }

    public interface PopupWindowListAdapterCallback {
        void onItemClick(String name, int position);
    }
}
