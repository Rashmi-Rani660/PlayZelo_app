package com.playzelo.playzelo.activities.ludo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.activities.LoginActivity;
import com.playzelo.playzelo.databinding.ActivityLudoSplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class LudoSplashActivity extends BaseActivity {

    private ActivityLudoSplashBinding binding;

    private String userId;
    private String username;
    private String authToken;
    private double walletBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLudoSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPrefManager pref = SharedPrefManager.getInstance(this);

        userId = pref.getUserId();
        username = pref.getUsername();
        authToken = pref.getToken();
        walletBalance = pref.getWalletBalance();

        if (userId == null || authToken == null || authToken.isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d("LudoSplash", "User=" + username + " Wallet=" + walletBalance);

        // ---------------- ANIMATIONS ----------------
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
        binding.logo.startAnimation(slideDown);

        binding.appName.animate()
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(1000)
                .start();

        binding.ludoDiceAnim.playAnimation();
        binding.boyRunningAnim.playAnimation();

        String safeName = (username != null) ? username : "Player";
        Toast.makeText(this,
                "Welcome " + safeName + "\nWallet: ₹" + walletBalance,
                Toast.LENGTH_SHORT).show();

        // ---------------- SINGLE DELAY ----------------
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // stop animations safely
            if (binding != null) {
                binding.ludoDiceAnim.cancelAnimation();
                binding.boyRunningAnim.cancelAnimation();
            }

            Intent intent = new Intent(LudoSplashActivity.this, LudoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("userId", userId);
            intent.putExtra("username", username);
            intent.putExtra("authToken", authToken);
            intent.putExtra("walletBalance", walletBalance);
            startActivity(intent);
            finish();

        }, 3000);
    }

    @Override
    public void onBackPressed() {
        // block back press
    }
}
