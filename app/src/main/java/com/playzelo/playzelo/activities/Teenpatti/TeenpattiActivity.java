package com.playzelo.playzelo.activities.Teenpatti;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.playzelo.playzelo.LudoWebInterface;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityTeenpattiBinding;

public class TeenpattiActivity extends BaseActivity {

    private ActivityTeenpattiBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ 1. Inflate layout FIRST
        binding = ActivityTeenpattiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ 2. Back button (SAFE – no null)
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // 🔒 FORCE LANDSCAPE
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        // ------------------- WEBVIEW SETTINGS -------------------
        WebSettings ws = binding.teenpattiWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // ------------------- ANDROID → JS BRIDGE -------------------
        binding.teenpattiWebView.addJavascriptInterface(
                new LudoWebInterface(this),
                "Android"
        );

        // ------------------- WEBVIEW CLIENT -------------------
        binding.teenpattiWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                injectUiFixJs(view);

                // send user data to JS if needed
                view.evaluateJavascript(
                        "javascript:(function(){ " +
                                "if(window.onAndroidData){ " +
                                "   onAndroidData(JSON.parse(Android.getUserData())); " +
                                "}" +
                                "})()",
                        null
                );
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("https://playzelo.fun/") ||
                        url.equals("https://playzelo.fun")) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });

        binding.teenpattiWebView.setWebChromeClient(new WebChromeClient());
        binding.teenpattiWebView.setOnLongClickListener(v -> true);

        // ------------------- LOAD GAME -------------------
        binding.teenpattiWebView.loadUrl(
                "https://playzelo.fun/teenpatti/690db1bf4b1ddd1a42719651"
        );
    }

    // ================= JS INJECTION =================
    private void injectUiFixJs(WebView view) {
        view.evaluateJavascript(
                "(function(){" +

                        "function forceFullWidthTable(){" +
                        " if(window.innerWidth <= window.innerHeight) return;" +
                        " document.body.style.margin='0';" +
                        " document.body.style.padding='0';" +
                        " document.body.style.overflow='hidden';" +
                        " document.querySelectorAll('[class*=\"max-w\"]').forEach(el=>{" +
                        "  el.style.maxWidth='100vw';el.style.width='100vw';el.style.margin='0';});" +
                        "}" +

                        "function hideDailyBonus(){" +
                        " document.querySelectorAll('*').forEach(el=>{" +
                        "  if(el.innerText && el.innerText.trim()==='Daily Bonus'){" +
                        "   let box=el.closest('div');if(box) box.remove();}});" +
                        "}" +

                        "function fixTeenPattiMobile(){" +
                        " if(window.innerWidth <= window.innerHeight) return;" +
                        " var board=document.querySelector('.relative.w-screen.h-screen');" +
                        " if(board){board.style.transform='scale(1.25)';}" +
                        " document.querySelectorAll('aside').forEach(a=>a.remove());" +
                        "}" +

                        "forceFullWidthTable();" +
                        "hideDailyBonus();" +
                        "fixTeenPattiMobile();" +

                        "setInterval(forceFullWidthTable,500);" +
                        "setInterval(hideDailyBonus,300);" +
                        "setInterval(fixTeenPattiMobile,500);" +

                        "new MutationObserver(()=>{" +
                        " forceFullWidthTable();hideDailyBonus();fixTeenPattiMobile();" +
                        "}).observe(document.body,{childList:true,subtree:true});" +

                        "})();",
                null
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // ✅ prevent memory leak
    }
}
