package com.playzelo.playzelo.activities.highstakedice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.activities.mines.MinesSplashActivity;
import com.playzelo.playzelo.databinding.ActivityHighStakeDiceSplashBinding;

public class HighStakeDiceSplashActivity extends BaseActivity {

    private String userId, username, authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHighStakeDiceSplashBinding binding = ActivityHighStakeDiceSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        username = intent.getStringExtra("username");
        authToken = intent.getStringExtra("auth_token");


        Log.d("MinesSplashActivity", "User ID: " + userId + "Username: " + username + " token: " + authToken);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Next activity start karo
            startActivity(new Intent(HighStakeDiceSplashActivity.this, HighStakeDiceActivity.class));
            finish(); // Splash screen ko close karo
        }, 5000);
    }
}
