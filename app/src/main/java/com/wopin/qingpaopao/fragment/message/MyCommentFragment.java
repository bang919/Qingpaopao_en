package com.wopin.qingpaopao.fragment.message;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MyCommentsAdapter;
import com.wopin.qingpaopao.bean.response.MyCommentsRsp;
import com.wopin.qingpaopao.dialog.NormalDialog;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.MyCommentPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.MyCommentView;

import java.util.List;

public class MyCommentFragment extends BaseBarDialogFragment<MyCommentPresenter> implements MyCommentView, View.OnClickListener {

    public static final String TAG = "MyCommentFragment";
    private RecyclerView mRecyclerView;
    private MyCommentsAdapter mMyCommentListAdapter;

    private View mRootView;
    private ConstraintLayout mCommentLayou;
    private EditText mCommentEt;
    private int keyBoardHeight;
    private View.OnLayoutChangeListener mKeyBoardListener;
    private int postId, commentId;
    private final static int COMMENT_PAGE_NUMBER = 10;
    private int commentPage = 1;

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
        return new MyCommentPresenter(getContext(), this, COMMENT_PAGE_NUMBER);
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyCommentListAdapter = new MyCommentsAdapter(R.layout.item_rv_my_comments);
        mMyCommentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final MyCommentsRsp.ResultBean.CommentsBean comment = (MyCommentsRsp.ResultBean.CommentsBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.delete_btn:
                        new NormalDialog(getContext(), getString(R.string.confirm), getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setLoadingVisibility(true);
                                mPresenter.deleteComment(comment.getId());
                            }
                        }, null).show();
                        break;
                    case R.id.additional_comment:
                        if (comment.getRelatedPostsBean() != null) {
                            MyCommentFragment.this.postId = comment.getRelatedPostsBean().getId();
                            MyCommentFragment.this.commentId = comment.getId();
                            mCommentLayou.setVisibility(View.VISIBLE);
                            setKeyBoardShow(mCommentEt, true);
                            mCommentEt.setText(null);
                            mRootView.addOnLayoutChangeListener(mKeyBoardListener);
                        }
                        break;
                }
            }
        });
        mMyCommentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initEvent();
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mMyCommentListAdapter);

        mCommentLayou = rootView.findViewById(R.id.bottom_comment_layout);
        mCommentEt = rootView.findViewById(R.id.et_comment);
        rootView.findViewById(R.id.btn_send).setOnClickListener(this);

        keyBoardHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 3;
    }

    @Override
    protected void initEvent() {
        setLoadingVisibility(true);
        mPresenter.getMyComments(commentPage, COMMENT_PAGE_NUMBER);
    }

    @Override
    public void onMyCommentsRsp(int page, MyCommentsRsp myCommentsRsp) {
        if (page == 1) {
            commentPage = 1;
            mMyCommentListAdapter.setNewData(null);
        }
        setLoadingVisibility(false);

        MyCommentsRsp.ResultBean result = myCommentsRsp.getResult();
        if (result != null) {
            List<MyCommentsRsp.ResultBean.CommentsBean> comments = result.getComments();
            mMyCommentListAdapter.addData(comments);

            int replyMeSize = myCommentsRsp.getResult().getCommentsReplyMe() != null ? myCommentsRsp.getResult().getCommentsReplyMe().size() : 0;
            if (comments.size() + replyMeSize < COMMENT_PAGE_NUMBER) {
                mMyCommentListAdapter.loadMoreEnd();
            } else {
                commentPage++;
                mMyCommentListAdapter.loadMoreComplete();
            }
        } else {
            mMyCommentListAdapter.loadMoreEnd();
        }

    }

    @Override
    public void onError(String errorMessage) {
        mMyCommentListAdapter.loadMoreFail();
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMessage);
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
