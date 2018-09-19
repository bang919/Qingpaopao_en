package com.wopin.qingpaopao.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalMoneyRsp;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.model.WelfareModel;
import com.wopin.qingpaopao.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CrowdFundingListAdapter extends RecyclerView.Adapter<CrowdFundingListAdapter.CrowdFundingViewHolder> {

    private ArrayList<ProductContent> mCrowdFundingDatas;
    private CrowdFundingListAdapterCallback mCrowdFundingListAdapterCallback;
    private WelfareModel mWelfareModel;

    public CrowdFundingListAdapter() {
        mWelfareModel = new WelfareModel();
    }

    public void setCrowdFundingDatas(ArrayList<ProductContent> numberousGoodsDatas) {
        mCrowdFundingDatas = numberousGoodsDatas;
        notifyDataSetChanged();
    }

    public void setCrowdFundingListAdapterCallback(CrowdFundingListAdapterCallback crowdFundingListAdapterCallback) {
        mCrowdFundingListAdapterCallback = crowdFundingListAdapterCallback;
    }

    @NonNull
    @Override
    public CrowdFundingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrowdFundingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_crowd_funding_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CrowdFundingViewHolder holder, int position) {
        Object tag = holder.itemView.getTag();
        if (tag != null && tag instanceof Disposable) {
            ((Disposable) tag).dispose();
        }
        final ProductContent productContent = mCrowdFundingDatas.get(position);
        GlideUtils.loadImage(holder.mImageView, -1, productContent.getDescriptionImage().size() == 0 ? null : productContent.getDescriptionImage().get(0), new CenterCrop());
        holder.mTitleTv.setText(productContent.getName());
        List<ProductContent.AttributeBean> attributes = productContent.getAttributes();
        if (attributes != null && attributes.size() > 0) {
            holder.mPriceTv.setText(String.format(holder.mPriceTv.getContext().getString(R.string.price_float),
                    Float.parseFloat(attributes.get(0).getName())));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCrowdFundingListAdapterCallback != null) {
                    mCrowdFundingListAdapterCallback.onItemClick(productContent);
                }
            }
        });
        holder.mProgressBar.setProgress(0);
        holder.mPercentTv.setText("");
        mWelfareModel.crowdfundingOrderTotalMoney(productContent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CrowdfundingOrderTotalMoneyRsp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        holder.itemView.setTag(d);
                    }

                    @Override
                    public void onNext(CrowdfundingOrderTotalMoneyRsp crowdfundingOrderTotalMoneyRsp) {
                        if (holder.mProgressBar.getContext() != null && crowdfundingOrderTotalMoneyRsp.getResult() != null) {
                            float currentPrice = 0;
                            for (CrowdfundingOrderTotalMoneyRsp.ResultBean r : crowdfundingOrderTotalMoneyRsp.getResult()) {
                                currentPrice += r.getTotalPrice();
                            }
                            float percent = currentPrice / Float.valueOf(productContent.getPrice()) * 100;
                            holder.mProgressBar.setProgress((int) Math.min(percent, 100));
                            holder.mPercentTv.setText(
                                    String.format(holder.itemView.getContext().getString(R.string.percent_float), percent)
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        holder.itemView.setTag(null);
                    }

                    @Override
                    public void onComplete() {
                        holder.itemView.setTag(null);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mCrowdFundingDatas == null ? 0 : mCrowdFundingDatas.size();
    }

    class CrowdFundingViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTitleTv;
        private ProgressBar mProgressBar;
        private TextView mPriceTv;
        private TextView mPercentTv;

        public CrowdFundingViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item);
            mTitleTv = itemView.findViewById(R.id.tv_title);
            mProgressBar = itemView.findViewById(R.id.progress);
            mPriceTv = itemView.findViewById(R.id.tv_price_value);
            mPercentTv = itemView.findViewById(R.id.tv_percent);
        }
    }

    public interface CrowdFundingListAdapterCallback {
        void onItemClick(ProductContent productContent);
    }
}
