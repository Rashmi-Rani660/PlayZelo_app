package com.playzelo.playzelo.activities.lottery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.playzelo.playzelo.LudoWebInterface;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityLotteryBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.Objects;

public class LotteryActivity extends BaseActivity {

    private ActivityLotteryBinding binding;
    private String userId;
    private String authToken;
    private String username;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLotteryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ================= GET USER DATA =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        authToken = pref.getToken();
        username = pref.getUsername();

        Log.d("LotteryGame", "userId=" + userId + " authToken=" + authToken + " username=" + username);

        // ================= WEBVIEW SETTINGS =================
        WebSettings webSettings = binding.lotteryWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // ================= JS INTERFACE =================
        binding.lotteryWebView.addJavascriptInterface(new LudoWebInterface(this), "AndroidApp");

        binding.lotteryWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);

                // Sync user data to web
                view.evaluateJavascript(
                        "javascript:if(window.onAndroidData){" +
                                "onAndroidData(JSON.parse(AndroidApp.getUserData()));" +
                                "}",
                        null
                );

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Objects.equals(url, "https://playzelo.fun/") || Objects.equals(url, "https://playzelo.fun")) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        binding.lotteryWebView.setWebChromeClient(new WebChromeClient());
        binding.lotteryWebView.setOnLongClickListener(v -> true);

        // ✅ Load game URL
        binding.lotteryWebView.loadUrl(
                "https://playzelo.fun/lottery/690db14b4b1ddd1a4271964b"
        );

        Log.d("LotteryGame", "Loading lottery Game");
    }

    @Override
    public void onBackPressed() {
        if (binding.lotteryWebView.canGoBack()) {
            binding.lotteryWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
