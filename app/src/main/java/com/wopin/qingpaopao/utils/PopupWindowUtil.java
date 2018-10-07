package com.wopin.qingpaopao.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.PopupWindowListAdapter;

import java.util.List;

public class PopupWindowUtil {

    public static PopupWindow buildListPpp(View viewDropDown, List<String> datas, int widthDp, int heightDp, final PopupWindowListAdapter.PopupWindowListAdapterCallback popupWindowListAdapterCallback) {
        // 用于PopupWindow的View
        Context context = viewDropDown.getContext();
        View contentView = LayoutInflater.from(context).inflate(R.layout.recyclerview_list, null, false);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, ScreenUtils.dip2px(context, widthDp), ScreenUtils.dip2px(context, heightDp), true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(viewDropDown, 0, 0, Gravity.BOTTOM);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new PopupWindowListAdapter(datas, new PopupWindowListAdapter.PopupWindowListAdapterCallback() {
            @Override
            public void onItemClick(String name, int position) {
                popupWindowListAdapterCallback.onItemClick(name, position);
                window.dismiss();
            }
        }));

        return window;
    }
}
