package com.wopin.qingpaopao.fragment.explore;

import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.ExploreCommentsAdapter;
import com.wopin.qingpaopao.bean.response.CommentRsp;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.ExploreDetailPresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ExploreDetailView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ExploreDetailFragment extends BaseBarDialogFragment<ExploreDetailPresenter> implements View.OnClickListener, ExploreDetailView, ExploreCommentsAdapter.ExploreCommentsAdapterCallback {

    public static final String TAG = "ExploreDetailFragment";
    private ExploreListRsp.PostsBean mPostsBean;
    private View mRootView;
    private Button mFollowBtn;
    private WebView mDetailWebView;
    private ImageView mStartIv;
    private ImageView mLikeIv;
    private EditText mCommentEt;
    private int mCurrentCommentTarget;
    private RecyclerView mCommentRv;
    private ExploreCommentsAdapter mExploreCommentsAdapter;
    private View.OnLayoutChangeListener mKeyBoardListener;
    private int keyBoardHeight;
    private ExploreDetailBtnListener mExploreDetailBtnListener;//用于更改Bean，让下一次点进来可以更改UI

    public ExploreDetailFragment() {
        mKeyBoardListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyBoardHeight)) {
                    //KeyBoard show
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyBoardHeight)) {
                    //KeyBoard dismiss
                    //软键盘消失后重置commentEt，取消回复他人状态
                    mRootView.removeOnLayoutChangeListener(mKeyBoardListener);
                    resetCommentEditTest();
                }
            }
        };
    }

    public void setExploreDetailBtnListener(ExploreDetailBtnListener exploreDetailBtnListener) {
        mExploreDetailBtnListener = exploreDetailBtnListener;
    }

    public static ExploreDetailFragment build(ExploreListRsp.PostsBean postsBean) {
        ExploreDetailFragment exploreDetailFragment = new ExploreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, postsBean);
        exploreDetailFragment.setArguments(args);
        return exploreDetailFragment;
    }

    @Override
    public void onDestroy() {
        mRootView.removeOnLayoutChangeListener(mKeyBoardListener);
        mDetailWebView.loadUrl("");
        mDetailWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.explore);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_explore_detail;
    }

    @Override
    protected ExploreDetailPresenter initPresenter() {
        return new ExploreDetailPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
        mDetailWebView = rootView.findViewById(R.id.webv);
        mCommentEt = rootView.findViewById(R.id.et_comment);
        mStartIv = rootView.findViewById(R.id.iv_stars);
        mLikeIv = rootView.findViewById(R.id.iv_like);
        mCommentRv = rootView.findViewById(R.id.comment_recyclerview);

        mStartIv.setOnClickListener(this);
        mLikeIv.setOnClickListener(this);
        rootView.findViewById(R.id.iv_comment).setOnClickListener(this);
        rootView.findViewById(R.id.iv_share).setOnClickListener(this);
        rootView.findViewById(R.id.btn_send).setOnClickListener(this);
        mFollowBtn = rootView.findViewById(R.id.btn_follow);
        mFollowBtn.setOnClickListener(this);

        mCommentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mExploreCommentsAdapter = new ExploreCommentsAdapter(this);
        mCommentRv.setAdapter(mExploreCommentsAdapter);

        keyBoardHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 3;

        //解决scrollview嵌套recyclerview滑动不流畅
        //TODO 不建议scrollview嵌套recyclerview，数据多了可能会出现crash，目前时间紧迫先这样做
        mCommentRv.setHasFixedSize(true);
        mCommentRv.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initEvent() {
        new Handler().postDelayed(new Runnable() {//延时加载
            @Override
            public void run() {
                mPostsBean = (ExploreListRsp.PostsBean) getArguments().getSerializable(TAG);

                mStartIv.setSelected(mPostsBean.isMyStar());
                mLikeIv.setSelected(mPostsBean.isMyLike());

                ((TextView) mRootView.findViewById(R.id.explore_title)).setText(mPostsBean.getTitle());
                GlideUtils.loadImage((ImageView) mRootView.findViewById(R.id.author_icon), -1, mPostsBean.getAuthor().getAvatar_URL(),
                        new CenterCrop(), new CircleCrop());
                ((TextView) mRootView.findViewById(R.id.author_name)).setText(mPostsBean.getAuthor().getName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                ((TextView) mRootView.findViewById(R.id.time_explore_item)).setText(TimeFormatUtils.formatToTime(mPostsBean.getDate(), format));
                ((TextView) mRootView.findViewById(R.id.read_count)).setText(getString(R.string.read_count, mPostsBean.getRead()));

                ((TextView) mRootView.findViewById(R.id.tv_like)).setText(String.valueOf(mPostsBean.getLikes()));
                ((TextView) mRootView.findViewById(R.id.tv_comment)).setText(String.valueOf(mPostsBean.getComments()));

                loadWebView();

                setLoadingVisibility(true);
                mPresenter.getComments(String.valueOf(mPostsBean.getId()));
                mPresenter.checkFollow(mPostsBean.getAuthor().getId());
            }
        }, 300);
    }

    private void loadWebView() {
        mDetailWebView.loadData(getHtmlData(mPostsBean.getContent()), "text/html; charset=UTF-8", null);
        WebSettings settings = mDetailWebView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        mDetailWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //等待证书响应
                handler.proceed();
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
                mPresenter.setFollowAuthor(mPostsBean.getAuthor().getId(), !v.isSelected());
                break;
            case R.id.iv_stars:
                v.setSelected(!v.isSelected());
                mPresenter.setCollectBlogPost(String.valueOf(mPostsBean.getId()), v.isSelected());
                if (mExploreDetailBtnListener != null) {
                    mExploreDetailBtnListener.onStarBtnClick(v.isSelected());
                }
                break;
            case R.id.iv_like:
                v.setSelected(!v.isSelected());
                mPresenter.setLikeBlogPost(String.valueOf(mPostsBean.getId()), v.isSelected());
                if (mExploreDetailBtnListener != null) {
                    mExploreDetailBtnListener.onLikeBtnClick(v.isSelected());
                }
                break;
            case R.id.iv_comment:
                resetCommentEditTest();
                setKeyBoardShow(mCommentEt, true);
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.btn_send:
                String commentString = mCommentEt.getText().toString();
                if (TextUtils.isEmpty(commentString)) {
                    ToastUtils.showShort(R.string.comment_hint);
                    return;
                }
                setLoadingVisibility(true);
                mPresenter.sendComment(mPostsBean.getId(), commentString, mCurrentCommentTarget);
                resetCommentEditTest();
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mPostsBean.getURL());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mPostsBean.getTitle());
        oks.setImageUrl(mPostsBean.getFeatured_image());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mPostsBean.getURL());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mPostsBean.getURL());

// 启动分享GUI
        oks.show(getContext());
    }

    private void resetCommentEditTest() {
        setKeyBoardShow(mCommentEt, false);
        mCurrentCommentTarget = 0;
        mCommentEt.setText(null);
        mCommentEt.setHint(R.string.comment_hint);
    }

    @Override
    public void isAuthorFollowed(boolean isFollow) {
        mFollowBtn.setSelected(isFollow);
        if (isFollow) {
            mFollowBtn.setText(R.string.had_follow);
        } else {
            mFollowBtn.setText(R.string.add_follow);
        }
    }

    @Override
    public void onComments(CommentRsp commentRsp) {
        setLoadingVisibility(false);
        mExploreCommentsAdapter.setCommentRsp(commentRsp);
    }

    @Override
    public void onError(String errorString) {
        setLoadingVisibility(false);
    }

    @Override
    public void onCommentLikeBtnClick(View likeBtn, CommentRsp.CommentBean commentBean) {
        likeBtn.setSelected(!likeBtn.isSelected());
        mPresenter.setLikeBlogComment(String.valueOf(commentBean.getId()), likeBtn.isSelected());
        commentBean.setMyLike(likeBtn.isSelected());
    }

    @Override
    public void onReplyOtherBtnClick(CommentRsp.CommentBean commentBean) {
        mCurrentCommentTarget = commentBean.getId();
        mCommentEt.setHint(getString(R.string.comment_reply_someone, commentBean.getAuthor_name()));
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

    public interface ExploreDetailBtnListener {
        void onStarBtnClick(boolean isStar);

        void onLikeBtnClick(boolean isLike);
    }
}
