package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wopin.qingpaopao.R;

public class MineGridRvAdapter extends RecyclerView.Adapter<MineGridRvAdapter.MineGridRvHolder> {

    private int[] icons = {R.mipmap.c_tea, R.mipmap.c_grass, R.mipmap.c_text, R.mipmap.c_letter,
            R.mipmap.c_head, R.mipmap.c_clock, R.mipmap.c_phone, R.mipmap.c_setting};
    private int[] texts = {R.string.my_drinking, R.string.my_health, R.string.information_edit, R.string.invite_friends,
            R.string.focus_subtitle, R.string.history_list, R.string.user_guide, R.string.system_setting};

    private MineGridRvCallback mMineGridRvCallback;

    public MineGridRvAdapter(MineGridRvCallback mineGridRvCallback) {
        mMineGridRvCallback = mineGridRvCallback;
    }

    @NonNull
    @Override
    public MineGridRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MineGridRvHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mine_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MineGridRvHolder holder, final int position) {
        holder.mImageView.setImageResource(icons[position]);
        holder.mTextView.setText(texts[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMineGridRvCallback.onGridItemClick(texts[position], position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class MineGridRvHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTextView;

        public MineGridRvHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item_rv_mine_grid);
            mTextView = itemView.findViewById(R.id.tv_item_rv_mine_grid);
        }
    }

    public interface MineGridRvCallback {
        void onGridItemClick(int textResource, int position);
    }
}
