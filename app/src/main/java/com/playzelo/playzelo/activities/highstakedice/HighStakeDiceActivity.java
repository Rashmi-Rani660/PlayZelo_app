package com.playzelo.playzelo.activities.highstakedice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.activities.LoginActivity;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class HighStakeDiceActivity extends BaseActivity {

    private WebView webView;
    private String userId, authToken, username;

    private static final String HIGHSTAKEDICE_URL = "https://playzelo.fun/stakedice/690db611cd0cff473aed5f3a";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_stakes_dice);

        webView = findViewById(R.id.highStakeDiceWebView);

        fetchUserData();
        setupWebView();
    }

    /* ================= FETCH USER DATA ================= */
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
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Inject login after page load
                injectLoginData();

                // Trigger JS wallet/profile refresh
                view.evaluateJavascript(
                        "if(window.onAndroidLogin){window.onAndroidLogin();}",
                        null
                );
            }
        });

        webView.addJavascriptInterface(new HighStakeDiceWebInterface(), "Android");

        // Load the game URL
        webView.loadUrl(HIGHSTAKEDICE_URL);
    }

    /* ================= INJECT LOGIN DATA ================= */
    private void injectLoginData() {
        if (authToken == null || authToken.isEmpty() || webView == null) return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // Set domain-level cookie
        cookieManager.setCookie(".playzelo.fun", "token=" + authToken + "; path=/");
        cookieManager.flush();

        // Inject localStorage data
        webView.evaluateJavascript(
                "localStorage.setItem('token','" + authToken + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');" +
                        "if(window.onAndroidLogin){window.onAndroidLogin();}",
                null
        );
    }

    /* ================= LOGOUT ================= */
    private void logoutWeb() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        webView.evaluateJavascript("localStorage.clear();", null);
        webView.clearCache(true);
        webView.clearHistory();
    }

    /* ================= JS → ANDROID BRIDGE ================= */
    class HighStakeDiceWebInterface {
        @JavascriptInterface
        public void logoutFromWeb() {
            runOnUiThread(() -> {
                SharedPrefManager.getInstance(HighStakeDiceActivity.this).logout();
                logoutWeb();

                // Reload the game page after logout
                webView.loadUrl(HIGHSTAKEDICE_URL);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        injectLoginData(); // Refresh login every time activity resumes
    }

    @Override
    protected void onDestroy() {
        if (webView != null) webView.destroy();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
