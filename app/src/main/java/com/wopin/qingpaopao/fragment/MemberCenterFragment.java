package com.wopin.qingpaopao.fragment;

import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class MemberCenterFragment extends BaseBarDialogFragment {

    public static final String TAG = "MemberCenterFragment";
    private WebView mDetailWebView;

    @Override
    protected String setBarTitle() {
        return getString(R.string.member_center);
    }

    @Override
    public void onDestroy() {
        mDetailWebView.loadUrl("");
        mDetailWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_webview;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mDetailWebView = rootView.findViewById(R.id.webv);
    }

    @Override
    protected void initEvent() {
        String webViewUrl = "http://www.h-popo.com";
        mDetailWebView.loadUrl(webViewUrl);
        WebSettings settings = mDetailWebView.getSettings();
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        mDetailWebView.setWebChromeClient(new WebChromeClient());
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
