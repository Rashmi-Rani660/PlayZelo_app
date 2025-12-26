package com.playzelo.playzelo.activities.ludo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.playzelo.playzelo.LudoWebInterface;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityLudoBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class LudoActivity extends BaseActivity {

    private ActivityLudoBinding binding;
    private double walletBalance;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ FIRST inflate & set content view
        binding = ActivityLudoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Back button (NOW SAFE)
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        resetWebView();

        if (!loadUserData()) return;

        setupWebView();

        binding.ludoWebView.loadUrl(
                "https://playzelo.fun/ludo/690db1e64b1ddd1a42719654"
        );
    }

    private void resetWebView() {
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        binding.ludoWebView.clearCache(true);
        binding.ludoWebView.clearHistory();
        binding.ludoWebView.loadUrl("about:blank");
    }

    private boolean loadUserData() {
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        walletBalance = pref.getWalletBalance();

        if (pref.getUserId() == null || pref.getToken() == null || pref.getToken().isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        Log.d("LUDO_USER", "Wallet=" + walletBalance);
        return true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = binding.ludoWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.ludoWebView.addJavascriptInterface(
                new LudoWebInterface(this),
                "AndroidApp"
        );

        binding.ludoWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(ProgressBar.GONE);

                updateChipsUI(walletBalance);

                view.evaluateJavascript(
                        "javascript:if(window.AndroidApp && window.onAndroidData){ " +
                                "onAndroidData(JSON.parse(AndroidApp.getUserData())); }",
                        null
                );
            }
        });

        binding.ludoWebView.setWebChromeClient(new WebChromeClient());
        binding.ludoWebView.setOnLongClickListener(v -> true);
    }

    public void updateChipsUI(double balance) {
        walletBalance = balance;
        SharedPrefManager.getInstance(this).saveWalletBalance(balance);

        String js =
                "javascript:(function(){" +
                        "let span=document.querySelector('div.absolute span:nth-child(2)');" +
                        "if(span){span.innerText='" + balance + " Chips';}" +
                        "})()";

        binding.ludoWebView.evaluateJavascript(js, null);
    }

    @Override
    public void onBackPressed() {
        if (binding.ludoWebView.canGoBack()) {
            binding.ludoWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        binding.ludoWebView.destroy();
        super.onDestroy();
    }
}
