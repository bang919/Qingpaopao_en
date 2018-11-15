package com.wopin.qingpaopao.fragment.message;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MyCommentListAdapter;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.dialog.NormalDialog;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.MyCommentPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MyCommentView;

public class MyCommentFragment extends BaseBarDialogFragment<MyCommentPresenter> implements MyCommentView, MyCommentListAdapter.MyCommentListCallback, View.OnClickListener {

    public static final String TAG = "MyCommentFragment";
    private RecyclerView mRecyclerView;
    private MyCommentListAdapter mMyCommentListAdapter;

    private View mRootView;
    private ConstraintLayout mCommentLayou;
    private EditText mCommentEt;
    private int keyBoardHeight;
    private View.OnLayoutChangeListener mKeyBoardListener;
    private int postId, commentId;

    public MyCommentFragment() {
        mKeyBoardListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyBoardHeight)) {
                    //KeyBoard show
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyBoardHeight)) {
                    //KeyBoard dismiss
                    //软键盘消失后重置commentEt，取消回复他人状态
                    mRootView.removeOnLayoutChangeListener(mKeyBoardListener);
                    setKeyBoardShow(mCommentEt, false);
                    mCommentLayou.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.my_comment);
    }

    @Override
    public void onDestroy() {
        mRootView.removeOnLayoutChangeListener(mKeyBoardListener);
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_comment;
    }

    @Override
    protected MyCommentPresenter initPresenter() {
        return new MyCommentPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyCommentListAdapter = new MyCommentListAdapter(this);
        mRecyclerView.setAdapter(mMyCommentListAdapter);

        mCommentLayou = rootView.findViewById(R.id.bottom_comment_layout);
        mCommentEt = rootView.findViewById(R.id.et_comment);
        rootView.findViewById(R.id.btn_send).setOnClickListener(this);

        keyBoardHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 3;
    }

    @Override
    protected void initEvent() {
        setLoadingVisibility(true);
        mPresenter.getMyComments();
    }

    @Override
    public void onMyCommentsRsp(MyCommentsRsp myCommentsRsp) {
        setLoadingVisibility(false);
        mMyCommentListAdapter.setData(myCommentsRsp);
    }

    @Override
    public void onError(String errorMessage) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void onAddCommentClick(int postId, int commentId) {
        this.postId = postId;
        this.commentId = commentId;
        mCommentLayou.setVisibility(View.VISIBLE);
        setKeyBoardShow(mCommentEt, true);
        mCommentEt.setText(null);
        mRootView.addOnLayoutChangeListener(mKeyBoardListener);
    }

    public void setKeyBoardShow(View view, boolean isShow) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && isShow) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, 0);
        } else if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDeleteCommentClick(final MyCommentsRsp.ResultBean.CommentsBean comment) {
        new NormalDialog(getContext(), getString(R.string.confirm), getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadingVisibility(true);
                mPresenter.deleteComment(comment.getId());
            }
        }, null).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String commentString = mCommentEt.getText().toString();
                if (TextUtils.isEmpty(commentString)) {
                    ToastUtils.showShort(R.string.comment_hint);
                    return;
                }
                setLoadingVisibility(true);
                mPresenter.sendComment(postId, commentString, commentId);
                setKeyBoardShow(mCommentEt, false);
                break;
        }
    }
}
