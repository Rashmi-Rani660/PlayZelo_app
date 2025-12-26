package com.playzelo.playzelo.activities.birdShooting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityBirdSplashBinding;


public class BirdSplashActivity extends BaseActivity {

    private ActivityBirdSplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBirdSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(BirdSplashActivity.this, BirdShootingActivity.class));
            finish();
        }, 5000);
    }

    private void startAnimation() {

        binding.imgGame.setScaleX(0.8f);
        binding.imgGame.setScaleY(0.8f);
        binding.imgGame.setAlpha(0f);

        binding.txtTitle.setAlpha(0f);
        binding.txtSub.setAlpha(0f);

        binding.imgGame.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        binding.txtTitle.animate()
                .alpha(1f)
                .setStartDelay(600)
                .setDuration(600)
                .start();

        binding.txtSub.animate()
                .alpha(1f)
                .setStartDelay(900)
                .setDuration(600)
                .start();
    }
}
