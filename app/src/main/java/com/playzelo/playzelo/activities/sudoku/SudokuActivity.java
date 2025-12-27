package com.playzelo.playzelo.activities.sudoku;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class SudokuActivity extends BaseActivity {

    private WebView webView;
    private String userId, authToken, username;
    private boolean loginInjected = false;

    private static final String SUDOKU_URL =
            "https://playzelo.fun/sudoku/690db6aacd0cff473aed5f40";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ FULL SCREEN GAME MODE
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        setContentView(R.layout.activity_sudoku);

        webView = findViewById(R.id.sudokuWebView);

        fetchUserData();
        setupWebView();
        clearWebDataOnce();
        injectLoginAndLoadGame();
    }

    /* ================= USER DATA ================= */
    private void fetchUserData() {
        userId = getIntent().getStringExtra("userId");
        authToken = getIntent().getStringExtra("authToken");
        username = getIntent().getStringExtra("username");

        if (authToken == null || authToken.isEmpty()) {
            SharedPrefManager pref = SharedPrefManager.getInstance(this);
            userId = pref.getUserId();
            authToken = pref.getToken();
            username = pref.getUsername();
        }
    }

    /* ================= WEBVIEW SETUP ================= */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        s.setMediaPlaybackRequiresUserGesture(false);

        // ✅ GAME SCALING
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!loginInjected) {
                    injectLogin();
                    loginInjected = true;
                }

                // ✅ FORCE PERFECT GAME FIT
                view.evaluateJavascript(
                        "(function(){" +
                                "var meta=document.querySelector('meta[name=viewport]');" +
                                "if(!meta){meta=document.createElement('meta');meta.name='viewport';document.head.appendChild(meta);}" +
                                "meta.content='width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1.0,user-scalable=no';" +
                                "document.body.style.margin='0';" +
                                "document.body.style.padding='0';" +
                                "document.body.style.overflow='hidden';" +
                                "})();",
                        null
                );
            }
        });

        webView.addJavascriptInterface(new SudokuWebInterface(), "Android");
    }

    /* ================= LOGIN SYNC ================= */
    private void injectLoginAndLoadGame() {
        if (authToken == null || authToken.isEmpty()) return;

        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(webView, true);
        cm.setCookie(".playzelo.fun", "token=" + authToken + "; path=/");
        cm.flush();

        webView.evaluateJavascript(
                "localStorage.setItem('token','" + authToken + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');",
                v -> webView.loadUrl(SUDOKU_URL)
        );
    }

    private void injectLogin() {
        webView.evaluateJavascript(
                "localStorage.setItem('token','" + authToken + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');",
                null
        );
    }

    /* ================= CLEAR ================= */
    private void clearWebDataOnce() {
        webView.clearCache(true);
        webView.clearHistory();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
        webView.evaluateJavascript("localStorage.clear();", null);
    }

    /* ================= LOGOUT FROM WEB ================= */
    class SudokuWebInterface {
        @JavascriptInterface
        public void logoutFromWeb() {
            runOnUiThread(() -> {
                SharedPrefManager.getInstance(SudokuActivity.this).logout();
                clearWebDataOnce();
                finish();
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
