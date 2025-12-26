package com.playzelo.playzelo.activities.mines;

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

public class MinesGameActivity extends BaseActivity {

    private ActivityMinesGameBinding binding;
    private String userId, username, authToken, wallet;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinesGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ================= FETCH DATA FROM INTENT =================
        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        authToken = getIntent().getStringExtra("authToken");
        wallet = getIntent().getStringExtra("wallet");

        // fallback: SharedPrefManager
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        if(userId == null) userId = SharedPrefManager.getInstance(this).getUserId();
        if(authToken == null) authToken = SharedPrefManager.getInstance(this).getToken();
        if(username == null) username = SharedPrefManager.getInstance(this).getUsername();


        Log.d("MinesGame", "ID=" + userId + ", Name=" + username + ", Token=" + authToken + ", Wallet=" + wallet);

        setupWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = binding.mineswebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Pass Android → JS bridge data
        binding.mineswebView.addJavascriptInterface(
                new LudoWebInterface(this),
                "AndroidApp"
        );



        binding.mineswebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);

                // optional: hide home button or apply UI fixes
                view.evaluateJavascript(
                        "(function(){ console.log('Android data:', AndroidApp.getUserData()); })()",
                        null
                );
            }
        });

        binding.mineswebView.setWebChromeClient(new WebChromeClient());
        binding.mineswebView.setOnLongClickListener(v -> true);

        // ✅ Load game URL
        binding.mineswebView.loadUrl("https://playzelo.fun/mines/690db0894b1ddd1a42719648");
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
