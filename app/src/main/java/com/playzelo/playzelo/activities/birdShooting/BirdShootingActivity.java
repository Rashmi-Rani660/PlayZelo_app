package com.playzelo.playzelo.activities.birdShooting;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
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

public class BirdShootingActivity extends BaseActivity {

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

        // Force landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        // ================= GET USER DATA =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        authToken = pref.getToken();
        username = pref.getUsername();

        Log.d("BirdShooting", "userId=" + userId + " authToken=" + authToken + " username=" + username);

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

                // Existing UI tweaks & canvas fixes
                view.evaluateJavascript(
                        "(function(){" +
                                "function hideGoHome(){" +
                                " document.querySelectorAll('button.swal2-cancel').forEach(function(btn){" +
                                "  if(btn.innerText && btn.innerText.includes('Go to Home')){ btn.style.display='none'; }" +
                                " });" +
                                "}" +

                                "function fixLayout(){" +
                                " var root = document.querySelector('div[style*=\"font-family\"]');" +
                                " if(!root) return;" +
                                " var isLandscape = window.innerWidth > window.innerHeight;" +
                                " root.style.display='flex'; root.style.flexDirection='column';" +
                                " root.style.alignItems='center'; root.style.justifyContent='center';" +
                                " root.style.minHeight='100vh'; root.style.padding = isLandscape ? '8px' : '16px';" +
                                "}" +

                                "function fixStatsGrid(){" +
                                " var grid = document.querySelector('.grid');" +
                                " if(!grid) return;" +
                                " var isLandscape = window.innerWidth > window.innerHeight;" +
                                " grid.style.display='grid'; grid.style.gridTemplateColumns='repeat(2,1fr)';" +
                                " grid.style.gap = isLandscape ? '8px' : '12px';" +
                                " grid.style.width='100%'; grid.style.maxWidth='720px';" +
                                " grid.querySelectorAll('div').forEach(function(card){" +
                                "  card.style.padding = isLandscape ? '6px' : '8px';" +
                                "  card.style.fontSize = isLandscape ? '12px' : '13px';" +
                                "  card.style.minHeight = isLandscape ? '34px' : '40px';" +
                                "  card.style.borderRadius='8px';" +
                                " });" +
                                " var btn = grid.querySelector('button');" +
                                " if(btn){ btn.style.gridColumn='span 2'; btn.style.height = isLandscape ? '38px' : '44px';" +
                                " btn.style.fontSize = isLandscape ? '14px' : '15px'; btn.style.padding='6px'; btn.style.marginTop='4px';}" +
                                "}" +

                                "function fixCanvasResponsive(){" +
                                " var root = document.querySelector('div[style*=\"font-family\"]');" +
                                " if(!root) return;" +
                                " var canvasBox = root.querySelector('.aspect\\\\[9\\\\/5\\\\]');" +
                                " if(!canvasBox) return;" +
                                " var canvas = canvasBox.querySelector('canvas');" +
                                " if(!canvas) return;" +
                                " var isLandscape = window.innerWidth > window.innerHeight;" +
                                " root.style.height='auto'; root.style.overflowY='auto'; root.style.webkitOverflowScrolling='touch';" +
                                " var vw = window.innerWidth; var vh = window.innerHeight;" +
                                " var reservedHeight = isLandscape ? 120 : 200; var maxH = vh - reservedHeight;" +
                                " var ratio = 9/5; var w = maxH * ratio;" +
                                " if(w > vw){ w = vw - 16; maxH = w / ratio; }" +
                                " canvasBox.style.width = w + 'px'; canvasBox.style.height = maxH + 'px';" +
                                " canvasBox.style.margin = '12px auto'; canvas.style.width='100%'; canvas.style.height='100%';" +
                                "}" +

                                "function applyAll(){ hideGoHome(); fixLayout(); fixStatsGrid(); fixCanvasResponsive(); }" +
                                "applyAll(); setInterval(applyAll,400);" +
                                "window.addEventListener('resize', applyAll);" +
                                "new MutationObserver(applyAll).observe(document.body,{childList:true,subtree:true});" +
                                "})();",
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
        binding.mineswebView.setOnLongClickListener(v -> true);

        // Load game
        binding.mineswebView.loadUrl(
                "https://playzelo.fun/birdshooting/690db21c4b1ddd1a42719657"
        );

        Log.d("BirdShooting", "Loading shooting Game");
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
