package com.playzelo.playzelo.activities.birdShooting;

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

public class BirdShootingActivity extends BaseActivity {

    private WebView webView;
    private String userId, authToken, username;

    private static final String BIRD_SHOOTING_URL = "https://playzelo.fun/birdshooting/690db21c4b1ddd1a42719657";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_shooting);

        webView = findViewById(R.id.birdShootingWebView);

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

        // Inject login after page load
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                syncLoginWithWeb();

                // Optional: trigger JS wallet/profile refresh
                view.evaluateJavascript(
                        "if(window.onAndroidLogin){window.onAndroidLogin();}",
                        null
                );
            }
        });

        webView.addJavascriptInterface(new BirdShootingWebInterface(), "Android");

        // ✅ Load URL after client is ready
        webView.loadUrl(BIRD_SHOOTING_URL);
    }

    /* ================= LOGIN SYNC ================= */
    private void syncLoginWithWeb() {
        if (authToken == null || authToken.isEmpty()) return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // Use domain-level cookie for all games
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
    class BirdShootingWebInterface {
        @JavascriptInterface
        public void logoutFromWeb() {
            runOnUiThread(() -> {
                SharedPrefManager.getInstance(BirdShootingActivity.this).logout();
                logoutWeb();

                Intent i = new Intent(BirdShootingActivity.this, LoginActivity.class);
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
