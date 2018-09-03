package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CupListRsp;

import java.util.ArrayList;

public class CupListAdapter extends RecyclerView.Adapter<CupListAdapter.CupListHolder> {

    private ArrayList<CupListRsp.CupBean> mCupBeans;
    private OnCupItemClickCallback mOnCupItemClickCallback;

    public void setOnCupItemClickCallback(OnCupItemClickCallback onCupItemClickCallback) {
        mOnCupItemClickCallback = onCupItemClickCallback;
    }

    public void setCupBeans(ArrayList<CupListRsp.CupBean> cupBeans) {
        mCupBeans = cupBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CupListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CupListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuplist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CupListHolder holder, final int position) {
        final CupListRsp.CupBean cupBean = mCupBeans.get(position);
        holder.mNameTv.setText(cupBean.getName());
        holder.mLightIv.setSelected(cupBean.isConnecting());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCupItemClickCallback.onCupItemClick(cupBean, position);
            }
        });
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCupItemClickCallback.onCupItemDelete(cupBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCupBeans == null ? 0 : mCupBeans.size();
    }

    public class CupListHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout mLayout;
        public TextView mNameTv;
        public ImageView mLightIv;
        public TextView mDeleteBtn;

        public CupListHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.cup_layout);
            mNameTv = itemView.findViewById(R.id.tv_name);
            mLightIv = itemView.findViewById(R.id.iv_light);
            mDeleteBtn = itemView.findViewById(R.id.tv_delete);
        }
    }

    public interface OnCupItemClickCallback {
        void onCupItemClick(CupListRsp.CupBean cupBean, int position);

        void onCupItemDelete(CupListRsp.CupBean cupBean, int position);
    }
}
