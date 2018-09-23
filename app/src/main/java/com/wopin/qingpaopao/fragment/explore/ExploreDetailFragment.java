package com.wopin.qingpaopao.fragment.explore;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.GlideUtils;
import com.wopin.qingpaopao.utils.TimeFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ExploreDetailFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "ExploreDetailFragment";
    private ExploreListRsp.PostsBean mPostsBean;
    private View mRootView;
    private WebView mDetailWebView;
    private ImageView mStartIv;
    private ImageView mLikeIv;
    private EditText mCommentEt;

    public static ExploreDetailFragment build(ExploreListRsp.PostsBean postsBean) {
        ExploreDetailFragment exploreDetailFragment = new ExploreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG, postsBean);
        exploreDetailFragment.setArguments(args);
        return exploreDetailFragment;
    }

    @Override
    public void onDestroy() {
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
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mRootView = rootView;
        mDetailWebView = rootView.findViewById(R.id.webv);
        mCommentEt = rootView.findViewById(R.id.et_comment);
        mStartIv = rootView.findViewById(R.id.iv_starts);
        mLikeIv = rootView.findViewById(R.id.iv_like);

        mStartIv.setOnClickListener(this);
        mLikeIv.setOnClickListener(this);
        rootView.findViewById(R.id.iv_share).setOnClickListener(this);
        rootView.findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        new Handler().postDelayed(new Runnable() {//延时加载
            @Override
            public void run() {
                mPostsBean = (ExploreListRsp.PostsBean) getArguments().getSerializable(TAG);

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
            case R.id.iv_starts:
                break;
            case R.id.iv_like:
                break;
            case R.id.iv_share:
                break;
            case R.id.btn_send:
                break;
        }
    }
}
