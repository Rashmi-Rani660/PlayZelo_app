package com.playzelo.playzelo.activities.colorMatching;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityMinesGameBinding;

public class ColorMatchingActivity extends BaseActivity {

    private ActivityMinesGameBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinesGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebSettings webSettings = binding.mineswebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.mineswebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);

//                view.evaluateJavascript(
//
//                        "(function(){" +
//                                "  var headerDiv = document.querySelector('div.bg\\\\[\\\\#0f1425\\\\]');" +
//                                "  if(headerDiv){ " +
//                                "    headerDiv.style.setProperty('background', '#4B9DA9', 'important');" + // 🔹 Your desired color
//                                "  }" +
//
//                                // function to hide Go to Home
//                                "function hideGoHome() {" +
//                                "  var links = document.querySelectorAll('a');" +
//                                "  links.forEach(function(link) {" +
//                                "    if(link.innerText && link.innerText.trim().includes('Go to Home')) {" +
//                                "      var parent = link.closest('div');" +
//                                "      if(parent) parent.style.display = 'none';" +
//                                "      link.style.display = 'none';" +
//                                "    }" +
//                                "  });" +
//                                "}" +
//
//                                // run immediately
//                                "hideGoHome();" +
//
//                                // keep checking (for SPA / React re-render)
//                                "setInterval(hideGoHome, 500);" +
//
//                                // observe DOM changes
//                                "var observer = new MutationObserver(function() { hideGoHome(); });" +
//                                "observer.observe(document.body, { childList: true, subtree: true });" +
//                                /* ---------- FIND & FIX MINES PANEL ---------- */
//                                "function stylePanel(){" +
//
//                                /* find panel by content */
//                                " var panel=null;" +
//                                " document.querySelectorAll('div').forEach(function(d){" +
//                                "  if(d.innerText && d.innerText.includes('Current Bet') && d.innerText.includes('Safe Picks')){" +
//                                "   panel=d;" +
//                                "  }" +
//                                " });" +
//
//                                " if(!panel) return;" +
//                                " if(panel.dataset.fixed) return;" +
//                                " panel.dataset.fixed='true';" +
//
//                                /* 🔥 FORCE GRID */
//                                " panel.style.display='grid';" +
//                                " panel.style.gridTemplateColumns='1fr 1fr';" +
//                                " panel.style.gap='10px';" +
//                                " panel.style.padding='12px';" +
//                                " panel.style.borderRadius='14px';" +
//
//                                " var items=[...panel.children];" +
//
//                                /* 2x2 STATS */
//                                " for(var i=0;i<4;i++){" +
//                                "  if(items[i]){" +
//                                "   items[i].style.gridColumn='span 1';" +
//                                "   items[i].style.height='46px';" +
//                                "   items[i].style.fontSize='12px';" +
//                                "   items[i].style.borderRadius='10px';" +
//                                "  }" +
//                                " }" +
//
//                                /* BET INPUT */
//                                " if(items[4]) items[4].style.gridColumn='span 2';" +
//
//                                /* MINES SELECT */
//                                " if(items[5]) items[5].style.gridColumn='span 2';" +
//
//                                /* START GAME CTA */
//                                " if(items[6]){" +
//                                "  items[6].style.gridColumn='span 2';" +
//                                "  var btn=items[6].querySelector('button');" +
//                                "  if(btn){" +
//                                "   btn.style.width='75%';" +
//                                "   btn.style.margin='8px auto';" +
//                                "   btn.style.padding='14px';" +
//                                "   btn.style.fontSize='15px';" +
//                                "   btn.style.borderRadius='14px';" +
//                                "  }" +
//                                " }" +
//                                "}" +
//
//                                /* ---------- RUN & OBSERVE ---------- */
//                                "hideGoHome();" +
//                                "stylePanel();" +
//
//                                "setInterval(stylePanel,300);" +
//
//                                "new MutationObserver(stylePanel)" +
//                                ".observe(document.body,{childList:true,subtree:true});" +
//
//
//                                "})()",
//                        null
//                );
//


                super.onPageFinished(view, url);
            }

            // 🔒 Block redirect to website home
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals("https://playzelo.fun/") || url.equals("https://playzelo.fun")) {
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
                "https://playzelo.fun/colormatching/690db680cd0cff473aed5f3d"
        );

        Log.d("ColorMatchingGame", "Loading Game");
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
