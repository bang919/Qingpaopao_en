package com.wopin.qingpaopao.fragment.welfare.order;

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

public class OldChangeNewOrderFollowOrderFragment extends BaseBarDialogFragment {

    public static final String TAG = "OldChangeNewOrderFollowOrderFragment";
    private WebView mDetailWebView;

    public static OldChangeNewOrderFollowOrderFragment build(String searchOrderid) {
        OldChangeNewOrderFollowOrderFragment oldChangeNewOrderFollowOrderFragment = new OldChangeNewOrderFollowOrderFragment();
        Bundle args = new Bundle();
        args.putString(TAG, searchOrderid);
        oldChangeNewOrderFollowOrderFragment.setArguments(args);
        return oldChangeNewOrderFollowOrderFragment;
    }

    @Override
    public void onDestroy() {
        mDetailWebView.loadUrl("");
        mDetailWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.search_express);
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
        String searchOrderid = getArguments().getString(TAG);
        String webViewUrl = "http://m.kuaidi100.com/result.jsp?nu=".concat(searchOrderid);
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
