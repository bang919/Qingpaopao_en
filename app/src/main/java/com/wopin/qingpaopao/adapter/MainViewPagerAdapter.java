package com.wopin.qingpaopao.adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseMainFragment;
import com.wopin.qingpaopao.fragment.drinking.DrinkingFragment;
import com.wopin.qingpaopao.fragment.explore.ExploreFragment;
import com.wopin.qingpaopao.fragment.MineFragment;
import com.wopin.qingpaopao.fragment.WelfareFragment;

import java.util.HashMap;

/**
 * Created by bigbang on 2018/4/10.
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private int[] tabTitles = {R.string.drinking, R.string.explore, R.string.welfare_discounts, R.string.mine};
    private int[] tabImages = {R.drawable.select_img_bottom_tab_tea, R.drawable.select_img_bottom_tab_toy,
            R.drawable.select_img_bottom_tab_gift, R.drawable.select_img_bottom_tab_profile};
    private HashMap<Integer, BaseMainFragment> mFragmentMap;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentMap = new HashMap<>();
        mFragmentMap.put(0, new DrinkingFragment());
    }

    public void attendViewpagerAndTablayout(ViewPager viewPager, final TabLayout tabLayout) {
        viewPager.setAdapter(this);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(i);
            if (tabAt != null) {
                View v = LayoutInflater.from(viewPager.getContext()).inflate(R.layout.item_main_viewpager, null);
                ImageView img = v.findViewById(R.id.iv_item_main_viewpager);
                img.setImageResource(tabImages[i]);
                TextView tv = v.findViewById(R.id.tv_item_main_viewpager);
                tv.setText(tabTitles[i]);
                setTabSelect(v, i == 0);
                tabAt.setCustomView(v);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabSelect(tab.getCustomView(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabSelect(tab.getCustomView(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabSelect(View view, boolean isSelect) {
        if (view != null) {
            view.setBackgroundColor(view.getContext().getResources().getColor(isSelect ? R.color.colorWhite2 : R.color.colorWhite));
            TextView tv = view.findViewById(R.id.tv_item_main_viewpager);
            view.findViewById(R.id.iv_item_main_viewpager).setSelected(isSelect);
            tv.setTextColor(tv.getContext().getResources().getColor(isSelect ? R.color.colorAccent : R.color.colorHalfBlack));
        }
    }

    @Override
    public BaseMainFragment getItem(int position) {
        BaseMainFragment f = mFragmentMap.get(position);
        if (f == null) {
            switch (position) {
                case 0:
                    f = new DrinkingFragment();
                    break;
                case 1:
                    f = new ExploreFragment();
                    break;
                case 2:
                    f = new WelfareFragment();
                    break;
                case 3:
                    f = new MineFragment();
                    break;
            }
            mFragmentMap.put(position, f);
        }
        return f;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
