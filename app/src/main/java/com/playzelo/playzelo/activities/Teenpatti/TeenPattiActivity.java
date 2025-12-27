package com.playzelo.playzelo.activities.Teenpatti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.activities.LoginActivity;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class TeenPattiActivity extends BaseActivity {

    private WebView webView;

    private String userId;
    private String authToken;
    private String username;

    private static final String TEENPATTI_URL =
            "https://playzelo.fun/teenpatti/690db1bf4b1ddd1a42719651";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_teenpatti);

        webView = findViewById(R.id.teenPattiWebView);

        fetchUserData();
        setupWebView();
    }

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

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());

        // ✅ Inject login data AFTER page is loaded
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                syncLoginWithWeb();

                // Optional JS call to refresh wallet/profile immediately
                view.evaluateJavascript(
                        "if(window.onAndroidLogin){window.onAndroidLogin();}",
                        null
                );
            }
        });

        webView.addJavascriptInterface(new TeenPattiWebInterface(), "Android");

        // ✅ Load URL after WebViewClient is set
        webView.loadUrl(TEENPATTI_URL);
    }

    private void syncLoginWithWeb() {
        if (authToken == null || authToken.isEmpty()) return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // ✅ Use domain-level cookie for all games
        cookieManager.setCookie(".playzelo.fun", "token=" + authToken + "; path=/");
        cookieManager.flush();

        // ✅ Inject token into localStorage
        webView.evaluateJavascript(
                "localStorage.clear();" +
                        "localStorage.setItem('token','" + authToken + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');",
                null
        );
    }

    private void logoutWeb() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        webView.evaluateJavascript("localStorage.clear();", null);
        webView.clearCache(true);
        webView.clearHistory();
    }

    class TeenPattiWebInterface {
        @JavascriptInterface
        public void logoutFromWeb() {
            runOnUiThread(() -> {
                SharedPrefManager.getInstance(TeenPattiActivity.this).logout();
                logoutWeb();

                Intent i = new Intent(TeenPattiActivity.this, LoginActivity.class);
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
