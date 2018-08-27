package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wopin.qingpaopao.R;

public class MineListRvAdapter extends RecyclerView.Adapter<MineListRvAdapter.MineListViewHolder> {

    private int[] icons;
    private int[] texts;
    private String[] datas;

    private MineListRvCallback mMineListRvCallback;

    public MineListRvAdapter(int[] texts, MineListRvCallback mineListRvCallback) {
        this(texts, null, mineListRvCallback);
    }

    public MineListRvAdapter(int[] texts, int[] icons, MineListRvCallback mineListRvCallback) {
        this.texts = texts;
        this.icons = icons;
        mMineListRvCallback = mineListRvCallback;
    }

    public void setDatas(String[] datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MineListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mine_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MineListViewHolder holder, final int position) {
        if (icons == null) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.mImageView.setImageResource(icons[position]);
        }
        if (datas != null && datas.length > position) {
            holder.mDataTv.setText(datas[position]);
        } else {
            holder.mDataTv.setText(null);
        }
        holder.mTextView.setText(texts[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMineListRvCallback.onListItemClick(texts[position], position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }

    class MineListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTextView;
        private TextView mDataTv;

        public MineListViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item_rv_mine_list);
            mTextView = itemView.findViewById(R.id.tv_item_rv_mine_list);
            mDataTv = itemView.findViewById(R.id.data_item_rv_mine_list);
        }
    }

    public interface MineListRvCallback {
        void onListItemClick(int textResource, int position);
    }
}
