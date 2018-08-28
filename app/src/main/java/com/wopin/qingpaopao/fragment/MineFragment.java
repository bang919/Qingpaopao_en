package com.wopin.qingpaopao.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineGridRvAdapter;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.fragment.information_edit.InformationEditFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;

public class MineFragment extends BaseMainFragment implements MineGridRvAdapter.MineGridRvCallback, MineListRvAdapter.MineListRvCallback, View.OnClickListener {

    private ImageView mMessageIv;
    private ImageView mHeadIconIv;
    private TextView mPhoneNumberTv;
    private TextView mUsernameTv;
    private TextView mScoreTv;
    private TextView mCurrentDrinkTv;
    private TextView mTotalDrinkTv;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView gridRecyclerView = rootView.findViewById(R.id.grid_recyclerview);
        gridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        gridRecyclerView.setAdapter(new MineGridRvAdapter(this));

        RecyclerView listRecyclerView = rootView.findViewById(R.id.list_recyclerview);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listRecyclerView.setAdapter(new MineListRvAdapter(
                new int[]{R.string.my_collect, R.string.my_fans, R.string.my_focus},
                new int[]{R.mipmap.pc_source, R.mipmap.h_profile2, R.mipmap.r_head},
                this
        ));

        mMessageIv = rootView.findViewById(R.id.iv_message);
        mHeadIconIv = rootView.findViewById(R.id.iv_person_head_icon);
        mPhoneNumberTv = rootView.findViewById(R.id.tv_phone_number);
        mUsernameTv = rootView.findViewById(R.id.tv_username);
        mScoreTv = rootView.findViewById(R.id.score);
        mCurrentDrinkTv = rootView.findViewById(R.id.number_current_drink_quantity);
        mTotalDrinkTv = rootView.findViewById(R.id.number_total_drink_quantity);

        rootView.findViewById(R.id.tv_sign_in).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void refreshData() {
        LoginRsp.ResultBean result = LoginPresenter.getAccountMessage().getResult();
        mPhoneNumberTv.setText(result.getPhone());
        mUsernameTv.setText(result.getUserName());
        mScoreTv.setText(result.getScores() + " " + getString(R.string.score));
        String iconUrl = result.getIcon();
        GlideUtils.loadImage(mHeadIconIv, R.mipmap.i_bigicon, iconUrl, new CircleCrop(), new CenterCrop());
    }

    @Override
    public void onGridItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.my_drinking:
                break;
            case R.string.my_health:
                break;
            case R.string.information_edit:
                new InformationEditFragment().show(getFragmentManager(), InformationEditFragment.TAG);
                break;
            case R.string.invite_friends:
                break;
            case R.string.focus_subtitle:
                break;
            case R.string.history_list:
                break;
            case R.string.user_guide:
                break;
            case R.string.system_setting:
                break;
        }
    }

    @Override
    public void onListItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.my_collect:
                break;
            case R.string.my_fans:
                break;
            case R.string.my_focus:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_in://签到
                break;
        }
    }
}
