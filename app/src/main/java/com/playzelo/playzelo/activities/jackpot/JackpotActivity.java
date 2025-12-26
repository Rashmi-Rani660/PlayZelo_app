package com.playzelo.playzelo.activities.jackpot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.playzelo.playzelo.LudoWebInterface;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityJackpotBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class JackpotActivity extends BaseActivity {

    private ActivityJackpotBinding binding;

    private String userId;
    private String authToken;
    private String username;

    private static final String BASE_URL = "https://playzelo.fun";
    private static final String GAME_URL =
            "https://playzelo.fun/jackpot/690db1754b1ddd1a4271964e";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJackpotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // ================= GET DATA FROM SHARED PREF =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        authToken = pref.getToken();
        username = pref.getUsername();

        Log.d("JackpotActivity", "userId=" + userId);
        Log.d("JackpotActivity", "authToken=" + authToken);
        Log.d("JackpotActivity", "username=" + username);

        if (authToken == null || authToken.isEmpty()) {
            finish();
            return;
        }

        // ================= WEBVIEW SETTINGS =================
        WebSettings settings = binding.jackpotwebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        WebView.setWebContentsDebuggingEnabled(true);

        // ================= COOKIES =================
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(binding.jackpotwebView, true);

        cookieManager.setCookie(
                BASE_URL,
                "authToken=" + authToken + "; path=/; Secure; SameSite=None"
        );

        // ================= ADD JS INTERFACE =================
        binding.jackpotwebView.addJavascriptInterface(
                new LudoWebInterface(this),
                "AndroidApp"
        );

        // ================= WEBVIEW CLIENT =================
        binding.jackpotwebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // 🔐 Force login (SPA safe)
                view.evaluateJavascript(
                        "localStorage.setItem('authToken','" + authToken + "');" +
                                "localStorage.setItem('isLoggedIn','true');",
                        null
                );

                // Optional: sync user data from Android → Web
                view.evaluateJavascript(
                        "javascript:if(window.onAndroidData){" +
                                "onAndroidData(JSON.parse(AndroidApp.getUserData()));" +
                                "}",
                        null
                );

                // 🔼 UI tweaks for SPA
                view.evaluateJavascript(
                        "(function(){" +
                                "function forceMoveHeader(){" +
                                "document.querySelectorAll('div.fixed').forEach(function(d){" +
                                "  if(d.innerText && d.innerText.includes('Playing For Fun')){" +
                                "    d.style.transform = 'translateY(100dp)';" +
                                "    d.style.transition = 'transform 0.2s ease';" +
                                "    d.style.zIndex = '9999';" +
                                "  }" +
                                "});" +
                                "}" +
                                "forceMoveHeader();" +
                                "setInterval(forceMoveHeader, 300);" +
                                "new MutationObserver(forceMoveHeader).observe(document.body,{childList:true,subtree:true});" +
                                "})();",
                        null
                );

                // 🔼 FIX LIVE WINNERS (BOTTOM)
                view.evaluateJavascript(
                        "(function(){" +
                                "function fixWinners(){" +
                                "document.querySelectorAll('div.fixed').forEach(function(d){" +
                                " if(d.innerText && d.innerText.includes('Live Winners')){" +
                                "   d.style.bottom='90px';" +
                                "   d.style.zIndex='9999';" +
                                " }" +
                                "});" +
                                "}" +
                                "fixWinners();" +
                                "setInterval(fixWinners,500);" +
                                "})();",
                        null
                );
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(BASE_URL) || url.equals(BASE_URL + "/")) {
                    Log.d("JackpotActivity", "Blocked redirect to home");
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        binding.jackpotwebView.setWebChromeClient(new WebChromeClient());
        binding.jackpotwebView.setOnLongClickListener(v -> true);
        binding.jackpotwebView.setHapticFeedbackEnabled(false);

        // ================= LOAD GAME =================
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Accept", "application/json");

        binding.jackpotwebView.loadUrl(GAME_URL, headers);

        Log.d("JackpotActivity", "Jackpot game loaded successfully");
    }

    @Override
    public void onBackPressed() {
        if (binding.jackpotwebView.canGoBack()) {
            binding.jackpotwebView.goBack();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    protected void onDestroy() {
        if (binding.jackpotwebView != null) {
            binding.jackpotwebView.loadUrl("about:blank");
            binding.jackpotwebView.clearHistory();
            binding.jackpotwebView.removeAllViews();
            binding.jackpotwebView.destroy();
        }
        super.onDestroy();
    }
}
