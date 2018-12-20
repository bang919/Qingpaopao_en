package com.wopin.qingpaopao.activity;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.even.mricheditor.ActionType;
import com.even.mricheditor.RichEditorAction;
import com.even.mricheditor.RichEditorCallback;
import com.even.mricheditor.ui.ActionImageView;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.m_richeditor.fragment.EditHyperlinkFragment;
import com.wopin.qingpaopao.m_richeditor.fragment.EditTableFragment;
import com.wopin.qingpaopao.m_richeditor.fragment.EditorMenuFragment;
import com.wopin.qingpaopao.m_richeditor.interfaces.OnActionPerformListener;
import com.wopin.qingpaopao.m_richeditor.keyboard.KeyboardHeightObserver;
import com.wopin.qingpaopao.m_richeditor.keyboard.KeyboardHeightProvider;
import com.wopin.qingpaopao.m_richeditor.keyboard.KeyboardUtils;
import com.wopin.qingpaopao.presenter.BuildBlogPresenter;
import com.wopin.qingpaopao.presenter.PhotoPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.BuildBlogView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuildBlogActivity extends BaseActivity<BuildBlogPresenter> implements BuildBlogView, View.OnClickListener, KeyboardHeightObserver {

    @BindView(R.id.wv_container)
    WebView mWebView;
    @BindView(R.id.fl_action)
    FrameLayout flAction;
    @BindView(R.id.ll_action_bar_container)
    LinearLayout llActionBarContainer;
    @BindView(R.id.et_title)
    EditText mTitleEt;

    private ProgressDialog progressDialog;

    /**
     * The keyboard height provider
     */
    private KeyboardHeightProvider keyboardHeightProvider;
    private boolean isKeyboardShowing;

    private RichEditorAction mRichEditorAction;
    private RichEditorCallback mRichEditorCallback;

    private EditorMenuFragment mEditorMenuFragment;

    private List<ActionType> mActionTypeList =
            Arrays.asList(ActionType.BOLD, ActionType.ITALIC, ActionType.UNDERLINE,
                    ActionType.STRIKETHROUGH, ActionType.SUBSCRIPT, ActionType.SUPERSCRIPT,
                    ActionType.NORMAL, ActionType.H1, ActionType.H2, ActionType.H3, ActionType.H4,
                    ActionType.H5, ActionType.H6, ActionType.INDENT, ActionType.OUTDENT,
                    ActionType.JUSTIFY_LEFT, ActionType.JUSTIFY_CENTER, ActionType.JUSTIFY_RIGHT,
                    ActionType.JUSTIFY_FULL, ActionType.ORDERED, ActionType.UNORDERED, ActionType.LINE,
                    ActionType.BLOCK_CODE, ActionType.BLOCK_QUOTE, ActionType.CODE_VIEW);

    private List<Integer> mActionTypeIconList =
            Arrays.asList(R.drawable.ic_format_bold, R.drawable.ic_format_italic,
                    R.drawable.ic_format_underlined, R.drawable.ic_format_strikethrough,
                    R.drawable.ic_format_subscript, R.drawable.ic_format_superscript,
                    R.drawable.ic_format_para, R.drawable.ic_format_h1, R.drawable.ic_format_h2,
                    R.drawable.ic_format_h3, R.drawable.ic_format_h4, R.drawable.ic_format_h5,
                    R.drawable.ic_format_h6, R.drawable.ic_format_indent_decrease,
                    R.drawable.ic_format_indent_increase, R.drawable.ic_format_align_left,
                    R.drawable.ic_format_align_center, R.drawable.ic_format_align_right,
                    R.drawable.ic_format_align_justify, R.drawable.ic_format_list_numbered,
                    R.drawable.ic_format_list_bulleted, R.drawable.ic_line, R.drawable.ic_code_block,
                    R.drawable.ic_format_quote, R.drawable.ic_code_review);

    private static final int REQUEST_CODE_CHOOSE = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_build_blog;
    }

    @Override
    protected BuildBlogPresenter initPresenter() {
        return new BuildBlogPresenter(this, this);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_top_right).setOnClickListener(this);

        initRichViews();
    }

    @Override
    protected void initEvent() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                getResources().getDisplayMetrics());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9,
                getResources().getDisplayMetrics());
        for (int i = 0, size = mActionTypeList.size(); i < size; i++) {
            final ActionImageView actionImageView = new ActionImageView(this);
            actionImageView.setLayoutParams(new LinearLayout.LayoutParams(width, width));
            actionImageView.setPadding(padding, padding, padding, padding);
            actionImageView.setActionType(mActionTypeList.get(i));
            actionImageView.setTag(mActionTypeList.get(i));
            actionImageView.setActivatedColor(R.color.colorAccent);
            actionImageView.setDeactivatedColor(R.color.colorWhite5);
            actionImageView.setRichEditorAction(mRichEditorAction);
            actionImageView.setBackgroundResource(R.drawable.btn_colored_material);
            actionImageView.setImageResource(mActionTypeIconList.get(i));
            actionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionImageView.command();
                }
            });
            llActionBarContainer.addView(actionImageView);
        }

        mEditorMenuFragment = new EditorMenuFragment();
        mEditorMenuFragment.setActionClickListener(new MOnActionPerformListener(mRichEditorAction));
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fl_action, mEditorMenuFragment, EditorMenuFragment.class.getName())
                .commit();
    }

    private void initRichViews() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mRichEditorCallback = new MRichEditorCallback();
        mWebView.addJavascriptInterface(mRichEditorCallback, "MRichEditor");
        mWebView.loadUrl("file:///android_asset/richEditor.html");
        mRichEditorAction = new RichEditorAction(mWebView);

        keyboardHeightProvider = new KeyboardHeightProvider(this);
        findViewById(R.id.fl_container).post(new Runnable() {
            @Override
            public void run() {
                keyboardHeightProvider.start();
            }
        });
    }

    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.bg_progressdialog_white);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small_Inverse);
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
        if (flAction.getVisibility() == View.INVISIBLE) {
            flAction.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.deletePhotoFile();
        KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
        keyboardHeightProvider.close();
        mWebView.destroy();
    }

    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        isKeyboardShowing = height > 0;
        if (height != 0) {
            flAction.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = flAction.getLayoutParams();
            params.height = height;
            flAction.setLayoutParams(params);
            performInputSpaceAndDel();
        } else if (flAction.getVisibility() != View.VISIBLE) {
            flAction.setVisibility(View.GONE);
        }
    }

    //TODO not a good solution
    private void performInputSpaceAndDel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Instrumentation instrumentation = new Instrumentation();
                    instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_SPACE);
                    instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DEL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.iv_action)
    void onClickAction() {
        if (flAction.getVisibility() == View.VISIBLE) {
            flAction.setVisibility(View.GONE);
        } else {
            if (isKeyboardShowing) {
                KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
            }
            flAction.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_action_undo)
    void onClickUndo() {
        mRichEditorAction.undo();
    }

    @OnClick(R.id.iv_action_redo)
    void onClickRedo() {
        mRichEditorAction.redo();
    }

    @OnClick(R.id.iv_action_txt_color)
    void onClickTextColor() {
        mRichEditorAction.foreColor("blue");
    }

    @OnClick(R.id.iv_action_txt_bg_color)
    void onClickHighlight() {
        mRichEditorAction.backColor("red");
    }

    @OnClick(R.id.iv_action_line_height)
    void onClickLineHeight() {
        mRichEditorAction.lineHeight(20);
    }

    @OnClick(R.id.iv_action_insert_image)
    void onClickInsertImage() {
        mPresenter.requestPermissionTodo(PhotoPresenter.REQUEST_PERMISSION_ALBUM);
        KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
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

    @OnClick(R.id.iv_action_insert_link)
    void onClickInsertLink() {
        KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
        EditHyperlinkFragment fragment = new EditHyperlinkFragment();
        fragment.setOnHyperlinkListener(new EditHyperlinkFragment.OnHyperlinkListener() {
            @Override
            public void onHyperlinkOK(String address, String text) {
                mRichEditorAction.createLink(text, address);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment, EditHyperlinkFragment.class.getName())
                .commit();
    }

    @OnClick(R.id.iv_action_table)
    void onClickInsertTable() {
        KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
        EditTableFragment fragment = new EditTableFragment();
        fragment.setOnTableListener(new EditTableFragment.OnTableListener() {
            @Override
            public void onTableOK(int rows, int cols) {
                mRichEditorAction.insertTable(rows, cols);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment, EditHyperlinkFragment.class.getName())
                .commit();
    }

    @Override
    public void onBlogBuildSuccess() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ToastUtils.showShort(R.string.success_new_blog);
        finish();
    }

    @Override
    public void onImageUpdating() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onImageGet(String imageUrl) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        mRichEditorAction.insertImageUrl(imageUrl);
    }

    @Override
    public void onError(String errorString) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        ToastUtils.showShort(errorString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_top_right:
                //发布
                KeyboardUtils.hideSoftInput(BuildBlogActivity.this);
                progressDialog.show();
                mRichEditorAction.refreshHtml(mRichEditorCallback, new RichEditorCallback.OnGetHtmlListener() {
                    @Override
                    public void getHtml(String html) {
                        if (TextUtils.isEmpty(html)) {
                            ToastUtils.showShort("Empty Html String");
                            return;
                        }
                        mPresenter.newBlog(mTitleEt.getText().toString(), html);
                    }
                });
                break;
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    class MRichEditorCallback extends RichEditorCallback {

        @Override
        public void notifyFontStyleChange(ActionType type, final String value) {
            ActionImageView actionImageView =
                    (ActionImageView) llActionBarContainer.findViewWithTag(type);
            if (actionImageView != null) {
                actionImageView.notifyFontStyleChange(type, value);
            }

            if (mEditorMenuFragment != null) {
                mEditorMenuFragment.updateActionStates(type, value);
            }
        }
    }

    public class MOnActionPerformListener implements OnActionPerformListener {
        private RichEditorAction mRichEditorAction;

        public MOnActionPerformListener(RichEditorAction mRichEditorAction) {
            this.mRichEditorAction = mRichEditorAction;
        }

        @Override
        public void onActionPerform(ActionType type, Object... values) {
            if (mRichEditorAction == null) {
                return;
            }

            String value = null;
            if (values != null && values.length > 0) {
                value = (String) values[0];
            }

            switch (type) {
                case SIZE:
                    mRichEditorAction.fontSize(Double.valueOf(value));
                    break;
                case LINE_HEIGHT:
                    mRichEditorAction.lineHeight(Double.valueOf(value));
                    break;
                case FORE_COLOR:
                    mRichEditorAction.foreColor(value);
                    break;
                case BACK_COLOR:
                    mRichEditorAction.backColor(value);
                    break;
                case FAMILY:
                    mRichEditorAction.fontName(value);
                    break;
                case IMAGE:
                    onClickInsertImage();
                    break;
                case LINK:
                    onClickInsertLink();
                    break;
                case TABLE:
                    onClickInsertTable();
                    break;
                case BOLD:
                case ITALIC:
                case UNDERLINE:
                case SUBSCRIPT:
                case SUPERSCRIPT:
                case STRIKETHROUGH:
                case JUSTIFY_LEFT:
                case JUSTIFY_CENTER:
                case JUSTIFY_RIGHT:
                case JUSTIFY_FULL:
                case CODE_VIEW:
                case ORDERED:
                case UNORDERED:
                case INDENT:
                case OUTDENT:
                case BLOCK_QUOTE:
                case BLOCK_CODE:
                case NORMAL:
                case H1:
                case H2:
                case H3:
                case H4:
                case H5:
                case H6:
                case LINE:
                    ActionImageView actionImageView =
                            (ActionImageView) llActionBarContainer.findViewWithTag(type);
                    if (actionImageView != null) {
                        actionImageView.performClick();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
