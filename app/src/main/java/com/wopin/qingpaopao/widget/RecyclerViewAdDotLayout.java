package com.wopin.qingpaopao.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.utils.ScreenUtils;

public class RecyclerViewAdDotLayout extends LinearLayout {

    private int totalScroll;
    private int dotCount;

    public RecyclerViewAdDotLayout(Context context) {
        super(context);
    }

    public RecyclerViewAdDotLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void attendToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int width = recyclerView.getWidth();
                if (width != 0 && getChildCount() != 0) {
                    int currentPage = (totalScroll / width + width * getChildCount()) % getChildCount();
                    setlectBannerDot(currentPage);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalScroll += dx;
            }
        });
    }

    public void setDotCount(int size) {
        totalScroll = 0;
        removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView dot = new ImageView(getContext());
            float diameter = ScreenUtils.dip2px(getContext(), 6);
            float margin = ScreenUtils.dip2px(getContext(), 4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) diameter, (int) diameter);
            dot.setImageResource(R.drawable.banner_dot_selector);
            if (i != 0) {
                params.leftMargin = (int) margin;
            } else {
                dot.setSelected(true);
            }
            addView(dot, params);
        }
    }

    private void setlectBannerDot(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(position == i);
        }
    }
}
