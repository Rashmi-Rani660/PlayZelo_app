package com.playzelo.playzelo.activities.jackpot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityJackpotSplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class JackpotSplashActivity extends BaseActivity {

    private ActivityJackpotSplashBinding binding;
    private String userId, username, authToken;

    private static final int SPLASH_DURATION = 5000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJackpotSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Logo slide-down animation
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
        binding.logo.startAnimation(slideDown);

        // Fade in the app name
        binding.appName.setAlpha(0f);
        binding.appName.animate()
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(1000)
                .start();

        // Start Lottie slot animation (Jackpot style)
        binding.animation.setSpeed(1.1f);
        binding.animation.playAnimation();

        // Auto-stop Lottie after splash duration
        new Handler().postDelayed(() -> {
            binding.animation.cancelAnimation();
        }, SPLASH_DURATION);

        // Get user data from Intent extras or SharedPrefManager
        Intent intent = getIntent();
        userId = intent != null ? intent.getStringExtra("userId") : null;
        authToken = intent != null ? intent.getStringExtra("authToken") : null;
        username = intent != null ? intent.getStringExtra("username") : null;

        if (userId == null || authToken == null || username == null) {
            userId = SharedPrefManager.getInstance(this).getUserId();
            authToken = SharedPrefManager.getInstance(this).getToken();
            username = SharedPrefManager.getInstance(this).getUsername();
        }

        if (userId != null && authToken != null) {
            Toast.makeText(this, "Jackpot started for user: " + username, Toast.LENGTH_SHORT).show();
        } else {
            Log.e("JackpotSplash", "User data missing from Intent or SharedPrefManager");
            Toast.makeText(this, "Error: User data missing!", Toast.LENGTH_SHORT).show();
        }

        // Navigate to JackpotGameActivity after splash
        new Handler().postDelayed(() -> {
            Intent next = new Intent(JackpotSplashActivity.this, JackpotActivity.class);
            next.putExtra("userId", userId);
            next.putExtra("authToken", authToken);
            next.putExtra("username", username);
            startActivity(next);
            finish();
        }, SPLASH_DURATION);
    }
}
