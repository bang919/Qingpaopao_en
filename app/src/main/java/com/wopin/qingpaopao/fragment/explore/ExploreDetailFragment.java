package com.wopin.qingpaopao.fragment.explore;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class ExploreDetailFragment extends BaseBarDialogFragment {

    public static final String TAG = "ExploreDetailFragment";
    private WebView mDetailWebView;

    public static ExploreDetailFragment build(String webViewUrl) {
        ExploreDetailFragment exploreDetailFragment = new ExploreDetailFragment();
        Bundle args = new Bundle();
        args.putString(TAG, webViewUrl);
        exploreDetailFragment.setArguments(args);
        return exploreDetailFragment;
    }

    @Override
    public void onDestroy() {
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
        mDetailWebView = rootView.findViewById(R.id.webv_explore_detail);
    }

    @Override
    protected void initEvent() {
        String webViewUrl = getArguments().getString(TAG);
        mDetailWebView.loadUrl(webViewUrl);
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
}
