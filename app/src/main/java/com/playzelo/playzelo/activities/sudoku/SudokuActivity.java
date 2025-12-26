package com.playzelo.playzelo.activities.sudoku;

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
import com.playzelo.playzelo.databinding.ActivityMinesGameBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.Objects;

public class SudokuActivity extends BaseActivity {

    private ActivityMinesGameBinding binding;
    private String userId;
    private String authToken;
    private String username;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinesGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ================= GET USER DATA =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        authToken = pref.getToken();
        username = pref.getUsername();

        Log.d("SudokuGame", "userId=" + userId + " authToken=" + authToken + " username=" + username);

        // ================= WEBVIEW SETTINGS =================
        WebSettings webSettings = binding.mineswebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // ================= JS INTERFACE =================
        binding.mineswebView.addJavascriptInterface(new LudoWebInterface(this), "AndroidApp");

        binding.mineswebView.setWebViewClient(new WebViewClient() {

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

        binding.mineswebView.setWebChromeClient(new WebChromeClient());

        // ❌ Disable long press (no web feel)
        binding.mineswebView.setOnLongClickListener(v -> true);

        // ✅ Load game URL
        binding.mineswebView.loadUrl(
                "https://playzelo.fun/sudoku/690db6aacd0cff473aed5f40"
        );

        Log.d("SudokuGame", "Loading Sudoku Game");
    }

    @Override
    public void onBackPressed() {
        if (binding.mineswebView.canGoBack()) {
            binding.mineswebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
