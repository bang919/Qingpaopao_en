package com.wopin.qingpaopao.fragment.drinking;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseDialogFragment;
import com.wopin.qingpaopao.manager.BleConnectManager;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class CleanCupFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "CleanCupFragment";
    private Handler mHandler;
    private Runnable mTimeRunnable;
    private int mTimeStamp;
    private TextView mCleanTimeTv;

    @Override
    protected int getLayout() {
        return R.layout.fragment_clean_cup;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mTimeRunnable);
        super.onDestroy();
    }

    @Override
    protected void initView(View rootView) {
        mCleanTimeTv = rootView.findViewById(R.id.tv_clean_time);
        rootView.findViewById(R.id.clean_notice_stop).setOnClickListener(this);
        rootView.findViewById(R.id.clean_notice_know).setOnClickListener(this);
        rootView.findViewById(R.id.btn_close_clean).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        mTimeStamp = 5 * 60;
        mHandler = new Handler();
        mTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (--mTimeStamp > 0) {
                    String m = "0" + mTimeStamp / 60;
                    String s = "0" + mTimeStamp % 60;
                    mCleanTimeTv.setText(m.substring(m.length() - 2).concat(":").concat(s.substring(s.length() - 2)));
                    mHandler.postDelayed(mTimeRunnable, 1000);
                } else {
                    stopClean();
                }
            }
        };
        mHandler.postDelayed(mTimeRunnable, 1000);
        BleConnectManager bleConnectManager = BleConnectManager.getInstance();
        if (bleConnectManager != null) {
            bleConnectManager.switchCupClean(true);
        }
    }

    private void stopClean() {
        mHandler.removeCallbacks(mTimeRunnable);
        BleConnectManager bleConnectManager = BleConnectManager.getInstance();
        if (bleConnectManager != null) {
            bleConnectManager.switchCupClean(false);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_notice_know:
                dismiss();
                break;
            case R.id.clean_notice_stop:
            case R.id.btn_close_clean:
                stopClean();
                break;
        }
    }
}
