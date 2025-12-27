package com.playzelo.playzelo.activities.lottery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class LotteryActivity extends BaseActivity {

    private WebView webView;
    private String userId, authToken, username;

    private static final String LOTTERY_URL = "https://playzelo.fun/lottery/690db14b4b1ddd1a4271964b";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);

        webView = findViewById(R.id.lotteryWebView);

        fetchUserData();
        setupWebView();
    }

    /* ================= FETCH USER DATA ================= */
    private void fetchUserData() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            authToken = intent.getStringExtra("authToken");
            username = intent.getStringExtra("username");
        }

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
                syncLoginWithWeb();

                // Optional JS wallet/profile refresh
                view.evaluateJavascript("if(window.onAndroidLogin){window.onAndroidLogin();}", null);
            }
        });

        webView.addJavascriptInterface(new LotteryWebInterface(), "Android");

        // Load the lottery game page
        webView.loadUrl(LOTTERY_URL);
    }

    /* ================= LOGIN SYNC ================= */
    private void syncLoginWithWeb() {
        if (authToken == null || authToken.isEmpty()) return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // Set domain-level cookie for all games
        cookieManager.setCookie(".playzelo.fun", "token=" + authToken + "; path=/");
        cookieManager.flush();

        // Inject token into localStorage
        webView.evaluateJavascript(
                "localStorage.clear();" +
                        "localStorage.setItem('token','" + authToken + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');",
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
    class LotteryWebInterface {
        @JavascriptInterface
        public void logoutFromWeb() {
            runOnUiThread(() -> {
                SharedPrefManager.getInstance(LotteryActivity.this).logout();
                logoutWeb();

                Intent i = new Intent(LotteryActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
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
