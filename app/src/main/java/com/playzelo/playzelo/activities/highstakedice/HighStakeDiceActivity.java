package com.playzelo.playzelo.activities.highstakedice;

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
import com.playzelo.playzelo.databinding.ActivityHighStakeDiceBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.Objects;

public class HighStakeDiceActivity extends BaseActivity {

    private ActivityHighStakeDiceBinding binding;

    private String userId;
    private String authToken;
    private String username;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHighStakeDiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // ================= GET USER DATA FROM SHARED PREF =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        authToken = pref.getToken();
        username = pref.getUsername();

        Log.d("HighStakeDice", "userId=" + userId + " authToken=" + authToken + " username=" + username);

        // ================= WEBVIEW SETTINGS =================
        WebSettings webSettings = binding.highStakeDiceWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        // ================= ADD JS INTERFACE =================
        binding.highStakeDiceWebView.addJavascriptInterface(
                new LudoWebInterface(this),
                "AndroidApp"
        );

        // ================= WEBVIEW CLIENT =================
        binding.highStakeDiceWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);

                // 🔄 Sync user data to web
                view.evaluateJavascript(
                        "javascript:if(window.onAndroidData){" +
                                "onAndroidData(JSON.parse(AndroidApp.getUserData()));" +
                                "}",
                        null
                );

                // Apply UI fixes (existing logic)
                view.evaluateJavascript(
                        "(function () {" +

                                "function applyFixes() {" +

                                // 🔹 Hide 'Go to Home'"
                                "document.querySelectorAll('a').forEach(function(a) {" +
                                "  if (a.innerText && a.innerText.includes('Go to Home')) {" +
                                "    a.style.display = 'none';" +
                                "  }" +
                                "});" +

                                // 🔹 Hide bottom fixed footer
                                "document.querySelectorAll('div.fixed.bottom-0.left-0.right-0')" +
                                ".forEach(function(d){ d.style.display='none'; });" +

                                // 🎯 Center MOBILE container
                                "var panel = document.querySelector('.block.lg\\\\:hidden');" +
                                "if (!panel) return;" +
                                "if (panel.dataset.centered) return;" +
                                "panel.dataset.centered = 'true';" +

                                "var wrapper = document.createElement('div');" +
                                "wrapper.style.position = 'fixed';" +
                                "wrapper.style.top = '50%';" +
                                "wrapper.style.left = '50%';" +
                                "wrapper.style.transform = 'translate(-50%, -50%)';" +
                                "wrapper.style.zIndex = '9999';" +
                                "wrapper.style.width = '96%';" +
                                "wrapper.style.maxWidth = '480px';" +
                                "wrapper.style.maxHeight = '95vh';" +
                                "wrapper.style.overflow = 'auto';" +

                                "panel.parentNode.insertBefore(wrapper, panel);" +
                                "wrapper.appendChild(panel);" +
                                "panel.style.margin = '0';" +

                                "}" +

                                applyIntervalAndObserver() +

                                "})()",
                        null
                );

                super.onPageFinished(view, url);
            }

            // 🔒 Block redirect to home
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Objects.equals(url, "https://playzelo.fun/") || Objects.equals(url, "https://playzelo.fun")) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        binding.highStakeDiceWebView.setWebChromeClient(new WebChromeClient());
        binding.highStakeDiceWebView.setOnLongClickListener(v -> true);

        // ✅ Load game URL
        binding.highStakeDiceWebView.loadUrl(
                "https://playzelo.fun/stakedice/690db611cd0cff473aed5f3a"
        );

        Log.d("HighStakeDice", "Loading dice Game");
    }

    // ---------------- HELPER: Apply interval & DOM observer ----------------
    private String applyIntervalAndObserver() {
        return "setInterval(applyFixes, 300);" +
                "new MutationObserver(applyFixes)" +
                ".observe(document.body, { childList: true, subtree: true });";
    }

    @Override
    public void onBackPressed() {
        if (binding.highStakeDiceWebView.canGoBack()) {
            binding.highStakeDiceWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
