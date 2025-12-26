package com.playzelo.playzelo.activities.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;

import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityLotterySplashBinding;

public class LotterySplashActivity extends BaseActivity {

    private ActivityLotterySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLotterySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(LotterySplashActivity.this, LotteryActivity.class));
            finish();
        }, 3000);
    }

    private void startAnimation() {

        binding.logo.setScaleX(0.85f);
        binding.logo.setScaleY(0.85f);
        binding.logo.setAlpha(0f);

        binding.txtLoading.setAlpha(0f);
        binding.txtLoading.setAlpha(0f);

        binding.logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(900)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        binding.txtLoading.animate()
                .alpha(1f)
                .setStartDelay(500)
                .setDuration(500)
                .start();

        binding.txtLoading.animate()
                .alpha(1f)
                .setStartDelay(700)
                .setDuration(500)
                .start();
    }
}
