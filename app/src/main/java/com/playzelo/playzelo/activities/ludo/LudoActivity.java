package com.playzelo.playzelo.activities.ludo;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class LudoActivity extends BaseActivity {

    private WebView webView;

    private static final String LUDO_URL =
            "https://playzelo.fun/ludo/690db1e64b1ddd1a42719654";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ludo);

        webView = findViewById(R.id.ludoWebView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebChromeClient(new WebChromeClient());

        // ✅ IMPORTANT: inject data AFTER page load
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // 🔥 Inject fresh login data AFTER page is ready
                syncLoginWithWeb();

                // 🔁 Optional: ask web to re-fetch wallet/profile
                view.evaluateJavascript(
                        "if(window.onAndroidLogin){window.onAndroidLogin();}",
                        null
                );
            }
        });

        webView.addJavascriptInterface(new LudoWebInterface(), "Android");

        // ❌ DO NOT call syncLoginWithWeb() here
        webView.loadUrl(LUDO_URL);
    }

    // 🔥 Send Android login data to Web (CORRECT WAY)
    private void syncLoginWithWeb() {

        SharedPrefManager pref = SharedPrefManager.getInstance(this);

        String token = pref.getToken();
        String userId = pref.getUserId();
        String username = pref.getUsername();

        if (token == null || token.isEmpty()) return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // ✅ IMPORTANT: set COOKIE ON DOMAIN (not full URL)
        cookieManager.setCookie(
                ".playzelo.fun",
                "token=" + token + "; path=/"
        );
        cookieManager.flush();

        // ✅ Clear old data + inject fresh data
        webView.evaluateJavascript(
                "localStorage.clear();" +
                        "localStorage.setItem('token','" + token + "');" +
                        "localStorage.setItem('userId','" + userId + "');" +
                        "localStorage.setItem('username','" + username + "');",
                null
        );
    }

    // 🔥 Android logout → Web logout
    public void logoutWeb() {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.flush();

        webView.evaluateJavascript(
                "localStorage.clear();", null);

        webView.clearCache(true);
        webView.clearHistory();
    }

    // 🔥 Web → Android bridge
    class LudoWebInterface {

        @JavascriptInterface
        public void logoutFromWeb() {

            runOnUiThread(() -> {

                SharedPrefManager.getInstance(LudoActivity.this).logout();

                logoutWeb();

                Intent i = new Intent(LudoActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            });
        }
    }
}
