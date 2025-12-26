package com.playzelo.playzelo.activities.Teenpatti;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityTeenpattiSplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class TeenpattiSplashActivity extends BaseActivity {

    private ActivityTeenpattiSplashBinding binding;
    private String userId, username, authToken;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTeenpattiSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animateLogo();
        fetchUserData();

        handler.postDelayed(this::navigateNext, 3000);
    }

    /* ================= FETCH USER DATA ================= */
    private void fetchUserData() {
        Intent intent = getIntent();

        if (intent != null) {
            userId = intent.getStringExtra("userId");
            authToken = intent.getStringExtra("authToken");
            username = intent.getStringExtra("username");
        }

        // Agar intent se nahi mila to SharedPref se fetch kare
        if (userId == null || authToken == null) {
            SharedPrefManager pref = SharedPrefManager.getInstance(this);
            userId = pref.getUserId();
            authToken = pref.getToken();
            username = pref.getUsername();
        }

        Log.d("TeenpattiSplash", "userId=" + userId + " username=" + username);
    }

    /* ================= LOGO ANIMATION ================= */
    private void animateLogo() {
        binding.imgLogo.setScaleX(0.7f);
        binding.imgLogo.setScaleY(0.7f);
        binding.imgLogo.setAlpha(0f);

        binding.imgLogo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(900)
                .setStartDelay(300)
                .start();
    }

    /* ================= NAVIGATION ================= */
    private void navigateNext() {
        Intent intent = new Intent(this, TeenpattiActivity.class);

        if (userId != null && authToken != null) {
            intent.putExtra("userId", userId);
            intent.putExtra("authToken", authToken);
            intent.putExtra("username", username);
        }

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
