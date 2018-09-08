package com.wopin.qingpaopao.fragment.information_edit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;

public class PersonInfoFragment extends BaseBarDialogFragment implements MineListRvAdapter.MineListRvCallback {

    public static final String TAG = "PersonInfoFragment";
    private MineListRvAdapter mMineListRvAdapter;

    @Override
    protected String setBarTitle() {
        return getString(R.string.person_info);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_information_edit;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.bt_safe_logout).setVisibility(View.GONE);

        RecyclerView listRv = rootView.findViewById(R.id.list_recyclerview);
        listRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mMineListRvAdapter = new MineListRvAdapter(new int[]{R.string.head_portrait, R.string.username}, this);
        listRv.setAdapter(mMineListRvAdapter);
    }

    @Override
    protected void initEvent() {
        String userName = LoginPresenter.getAccountMessage().getResult().getUserName();
        mMineListRvAdapter.setDatas(new String[]{null, userName});
    }

    @Override
    public void onListItemClick(int textResource, int position) {
        switch (textResource) {
            case R.string.head_portrait:
                break;
            case R.string.username:
                EditUsernameFragment editUsernameFragment = new EditUsernameFragment();
                editUsernameFragment.show(getChildFragmentManager(), EditUsernameFragment.TAG);
                editUsernameFragment.setOnBaseBarDialogFragmentCallback(new OnBaseBarDialogFragmentCallback() {
                    @Override
                    public void onDismiss() {
                        initEvent();
                    }
                });
                break;
        }
    }
}
