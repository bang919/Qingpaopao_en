package com.wopin.qingpaopao.widget;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.wopin.qingpaopao.adapter.CupListAdapter;


/**
 * Created by WANG on 18/3/14.
 */

public class PlusItemSlideCallback extends WItemTouchHelperPlus.Callback {
    String type;

    public PlusItemSlideCallback(String type) {
        this.type = type;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    int getSlideViewWidth() {
        return 0;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public String getItemSlideType() {
        return type;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof CupListAdapter.CupListHolder) {
            CupListAdapter.CupListHolder holder = (CupListAdapter.CupListHolder) viewHolder;
            int actionWidth = holder.mDeleteBtn.getWidth();

            if (dX < -actionWidth) {
                dX = -actionWidth;
            }
            holder.mLayout.setTranslationX(dX);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }
}
