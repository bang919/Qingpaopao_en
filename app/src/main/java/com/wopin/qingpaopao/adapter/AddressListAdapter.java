package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.LoginRsp;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddrssViewHolder> {

    private ArrayList<LoginRsp.ResultBean.AddressListBean> mAddressListBeans;
    private AddressListAdapterCallback mAddressListAdapterCallback;

    public void setAddressListBeans(ArrayList<LoginRsp.ResultBean.AddressListBean> addressListBeans) {
        mAddressListBeans = addressListBeans;
        notifyDataSetChanged();
    }

    public void setAddressListAdapterCallback(AddressListAdapterCallback addressListAdapterCallback) {
        mAddressListAdapterCallback = addressListAdapterCallback;
    }

    @NonNull
    @Override
    public AddrssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddrssViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddrssViewHolder holder, int position) {
        final LoginRsp.ResultBean.AddressListBean addressListBean = mAddressListBeans.get(position);
        holder.mNameTv.setText(addressListBean.getUserName());
        holder.mAddressTv.setText(addressListBean.getAddress1().concat(addressListBean.getAddress2()));
        holder.mDefaultView.setSelected(addressListBean.isIsDefault());
        if (mAddressListAdapterCallback != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddressListAdapterCallback.onItemClick(addressListBean);
                }
            });
            holder.mDefaultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddressListAdapterCallback.onDefaultClick(addressListBean);
                }
            });
            holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddressListAdapterCallback.onEditClick(addressListBean);
                }
            });
            holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddressListAdapterCallback.onDeleteClick(addressListBean);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mAddressListBeans == null ? 0 : mAddressListBeans.size();
    }

    class AddrssViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTv;
        private TextView mAddressTv;
        private View mDefaultBtn;
        private View mDefaultView;
        private Button mDeleteBtn;
        private Button mEditBtn;

        public AddrssViewHolder(View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.tv_name);
            mAddressTv = itemView.findViewById(R.id.tv_address);
            mDefaultBtn = itemView.findViewById(R.id.btn_default_address);
            mDefaultView = itemView.findViewById(R.id.img_default_address);
            mDeleteBtn = itemView.findViewById(R.id.btn_delete_address);
            mEditBtn = itemView.findViewById(R.id.btn_edit_address);
        }
    }

    public interface AddressListAdapterCallback {
        void onItemClick(LoginRsp.ResultBean.AddressListBean addressListBean);

        void onDefaultClick(LoginRsp.ResultBean.AddressListBean addressListBean);

        void onEditClick(LoginRsp.ResultBean.AddressListBean addressListBean);

        void onDeleteClick(LoginRsp.ResultBean.AddressListBean addressListBean);
    }
}
