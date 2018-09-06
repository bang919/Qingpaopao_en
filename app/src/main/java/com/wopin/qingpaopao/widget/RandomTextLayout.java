package com.wopin.qingpaopao.widget;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Random;

public class RandomTextLayout extends ViewGroup {

    private Random mRandom;
    private ArrayList<Rect> mRects;
    private ArrayList<BluetoothDevice> mBluetoothDevices;
    private OnDeviceClickListener mOnDeviceClickListener;
    private boolean isFull;

    public RandomTextLayout(Context context) {
        this(context, null);
    }

    public RandomTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRandom = new Random();
        mRects = new ArrayList<>();
        mBluetoothDevices = new ArrayList<>();
    }

    public void setOnDeviceClickListener(OnDeviceClickListener onDeviceClickListener) {
        mOnDeviceClickListener = onDeviceClickListener;
    }

    public void addBluetoothDevice(BluetoothDevice bluetoothDevice) {
        TextView textView = new TextView(getContext());
        textView.setText(bluetoothDevice.getName());
        textView.setTextSize(20 + mRandom.nextInt(10));
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        addView(textView);
        mBluetoothDevices.add(bluetoothDevice);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        for (int i = 0; i < cCount; i++) {
            View childAt = getChildAt(i);
            int cWidth = childAt.getMeasuredWidth();
            int cHeight = childAt.getMeasuredHeight();

            if (mRects.size() > i) {
                Rect rect = mRects.get(i);
                childAt.layout(rect.left, rect.top, rect.right, rect.bottom);
            } else {
                setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight, 10);
            }
            final int finalI = i;
            childAt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeviceClickListener != null) {
                        mOnDeviceClickListener.onBlueToothDeviceClick(mBluetoothDevices.get(finalI), finalI);
                    }
                }
            });
        }
    }

    private void setLocationView(int screenWidth, int screenHeight, View childAt, int cWidth, int cHeight, int retryTime) {
        if (isFull) {
            return;
        }
        if (retryTime < 0) {
            ToastUtils.showShort(R.string.device_too_much);
            isFull = true;
            return;
        }
        int cl;
        int ct;
        int cr;
        int cb;

        int outr = screenWidth / 2;
        /*这里随机的是结束点*/
        int randomWidth = mRandom.nextInt(screenWidth);
        int randomHeight = mRandom.nextInt(screenHeight);

        cl = randomWidth - cWidth;
        ct = randomHeight - cHeight;
        cr = randomWidth;
        cb = randomHeight;
        int distanceX = Math.abs(outr - randomWidth);
        int distanceY = Math.abs(outr - randomHeight);

        //点击位置与圆心的直线距离
        int distanceZ = (int) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        if (cl < 0 || ct < 0 || distanceZ > outr) {
            /*这里应该重新获取点重新计算*/
            setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight, --retryTime);
            return;
        }

        /*这里判断的是是否重合*/
        Rect currentRect = new Rect(cl, ct, cr, cb);
        for (int i = 0; i < mRects.size(); i++) {
            Rect rect = mRects.get(i);
            if (Rect.intersects(rect, currentRect)) {
                setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight, --retryTime);
                return;
            }
        }
        childAt.layout(cl, ct, cr, cb);
        mRects.add(new Rect(cl, ct, cr, cb));
    }

    public interface OnDeviceClickListener {
        void onBlueToothDeviceClick(BluetoothDevice bluetoothDevice, int position);
    }

}
