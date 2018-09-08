package com.wopin.qingpaopao.adapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.wopin.qingpaopao.R;

public class MyFragmentPageAdapter {

    private FragmentActivity mFragmentActivity;
    private Fragment mFragment;
    private int containerViewId;
    private Fragment currentFragment;
    private boolean isFirstPage = true;//如果是第一页，不会addToBackStack，且不加动画

    public MyFragmentPageAdapter(FragmentActivity fragmentActivity, @IdRes int containerViewId) {
        mFragmentActivity = fragmentActivity;
        this.containerViewId = containerViewId;
        mFragmentActivity.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            void onCreate(LifecycleOwner lifecycleOwner) {
                View decorView = mFragmentActivity.getWindow().getDecorView();
                decorView.setFocusableInTouchMode(true);
                decorView.requestFocus();
                decorView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) && event.getAction() == KeyEvent.ACTION_DOWN) {
                            return mFragmentActivity.getSupportFragmentManager().popBackStackImmediate();
                        }
                        return false;
                    }
                });
                mFragmentActivity.getLifecycle().removeObserver(this);
            }
        });
    }

    public MyFragmentPageAdapter(Fragment fragment, @IdRes int containerViewId) {
        mFragment = fragment;
        this.containerViewId = containerViewId;
        mFragment.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void onResume(LifecycleOwner lifecycleOwner) {
                //需要Resume后才能获得getView
                if (mFragment.getView() != null) {
                    mFragment.getView().setFocusableInTouchMode(true);
                    mFragment.getView().requestFocus();
                    mFragment.getView().setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) && event.getAction() == KeyEvent.ACTION_DOWN) {
                                return mFragment.getChildFragmentManager().popBackStackImmediate();
                            }
                            return false;
                        }
                    });
                }
                mFragment.getLifecycle().removeObserver(this);
            }
        });
    }

    public void switchToFragment(Fragment targetFragment) {
        switchToFragment(targetFragment, false);
    }

    public void switchToFragment(Fragment targetFragment, boolean isAddToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isFirstPage) {//如果是第一页，不会addToBackStack，且不加动画
            isAddToBackStack = false;
            isFirstPage = false;
        } else {
            transaction.setCustomAnimations(
                    R.anim.anim_dialog_next_enter,
                    R.anim.anim_dialog_exit,
                    R.anim.anim_dialog_enter,
                    R.anim.anim_dialog_next_exit
            );
        }

        if (!isAddToBackStack) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            if (!targetFragment.isAdded()) {
                transaction.add(containerViewId, targetFragment);
            } else {
                transaction.show(targetFragment);
            }
            transaction.commit();
            currentFragment = targetFragment;
        } else {
            transaction.replace(containerViewId, targetFragment, targetFragment.getClass().getName());
            transaction.addToBackStack(targetFragment.getClass().getName());
            transaction.commitAllowingStateLoss();
        }
    }

    private FragmentManager getFragmentManager() {
        return mFragmentActivity != null ? mFragmentActivity.getSupportFragmentManager() : mFragment.getChildFragmentManager();
    }
}
