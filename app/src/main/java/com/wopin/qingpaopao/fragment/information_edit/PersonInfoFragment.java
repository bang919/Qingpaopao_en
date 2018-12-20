package com.wopin.qingpaopao.fragment.information_edit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MineListRvAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.presenter.PersonInfoPresenter;
import com.wopin.qingpaopao.presenter.PhotoPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.PersonInfoView;

public class PersonInfoFragment extends BaseBarDialogFragment<PersonInfoPresenter> implements MineListRvAdapter.MineListRvCallback, PersonInfoView {

    public static final String TAG = "PersonInfoFragment";
    private MineListRvAdapter mMineListRvAdapter;
    private PersonInfoDestroyCallback mPersonInfoDestroyCallback;

    public void setPersonInfoDestroyCallback(PersonInfoDestroyCallback personInfoDestroyCallback) {
        mPersonInfoDestroyCallback = personInfoDestroyCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.person_info);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_information_edit;
    }

    @Override
    protected PersonInfoPresenter initPresenter() {
        return new PersonInfoPresenter(this, this);
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
                mPresenter.requestPermissionTodo(PhotoPresenter.REQUEST_PERMISSION_ALBUM);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        mPresenter.deletePhotoFile();
        if (mPersonInfoDestroyCallback != null) {
            mPersonInfoDestroyCallback.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onUserDataRefresh() {
        ToastUtils.showShort(R.string.person_info_had_change);
    }

    @Override
    public void onError(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    public interface PersonInfoDestroyCallback {
        void onDestroy();
    }
}
