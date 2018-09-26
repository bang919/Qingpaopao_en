package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
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

//    private Handler handler = new Handler();

    public void setOnCupItemClickCallback(OnCupItemClickCallback onCupItemClickCallback) {
        mOnCupItemClickCallback = onCupItemClickCallback;
    }

    public void setCupBeans(ArrayList<CupListRsp.CupBean> cupBeans) {
        mCupBeans = cupBeans;
        notifyDataSetChanged();
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, 5000);
    }

    public void removeOneItem(int position) {
        if (mOnCupItemClickCallback != null) {
            mOnCupItemClickCallback.onCupItemDelete(mCupBeans.get(position), position);
            notifyItemRemoved(position);
        }
    }

    public void destroy() {
        //不能让这个Handler一直跑的，要停止，防止内存泄漏
//        if (handler != null) {
//            handler.removeCallbacks(runnable);
//            handler = null;
//        }
    }

    @NonNull
    @Override
    public CupListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CupListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuplist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CupListHolder holder, final int position) {
        final CupListRsp.CupBean cupBean = mCupBeans.get(position);
        holder.mNameTv.setText(cupBean.getName());
        holder.mLightIv.setSelected(cupBean.isConnecting());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCupItemClickCallback.onCupItemClick(cupBean, position);
            }
        });
        holder.mLightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mLightIv.isSelected()) {
                    //干脆不让他断链了
//                    Context context = holder.itemView.getContext();
//                    //弹窗询问是否断开连接
//                    new NormalDialog(context, context.getString(R.string.confirm), context.getString(R.string.cancel), context.getString(R.string.disconnect),
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mOnCupItemClickCallback.onCupItemTurn(cupBean, !holder.mLightIv.isSelected());
//                                }
//                            }, null).show();
                } else {
                    mOnCupItemClickCallback.onCupItemTurn(cupBean, !holder.mLightIv.isSelected());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCupBeans == null ? 0 : mCupBeans.size();
    }

    public class CupListHolder extends RecyclerView.ViewHolder {

        public TextView mNameTv;
        public ImageView mLightIv;

        public CupListHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tv_name);
            mLightIv = itemView.findViewById(R.id.iv_light);
        }
    }

    public interface OnCupItemClickCallback {
        void onCupItemClick(CupListRsp.CupBean cupBean, int position);

        void onCupItemDelete(CupListRsp.CupBean cupBean, int position);

        void onCupItemTurn(CupListRsp.CupBean cupBean, boolean isOn);
    }

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            for (CupListRsp.CupBean cupBean : mCupBeans) {
//                if (cupBean.getType().equals(Constants.WIFI)) {
//                    if (!cupBean.isConnecting()) {
//                        Log.d("CupListAdapter", "trying to reconnect the wifi module " + cupBean.getUuid());
//                        MqttConnectManager.getInstance().connectDevice(cupBean.getUuid());
//                    }
//                }
//            }
//            handler.postDelayed(runnable, 5000);
//        }
//    };
}
