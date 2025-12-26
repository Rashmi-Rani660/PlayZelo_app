package com.playzelo.playzelo.activities.sudoku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivitySudokuSplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

@SuppressLint("CustomSplashScreen")
public class SudokuSplashActivity extends BaseActivity {

    private ActivitySudokuSplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySudokuSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get intent data
        String userId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String authToken = getIntent().getStringExtra("auth_token");

        // Save to Shared Preferences
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        if (userId != null) pref.saveUserId(userId);
        if (username != null) pref.saveUsername(username);
        if (authToken != null) pref.saveToken(authToken);

        Log.d("SudokuSplash", "userId=" + userId + " username=" + username + " token=" + authToken);

        // Navigate to SudokuActivity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SudokuSplashActivity.this, SudokuActivity.class));
            finish();
        }, 3000);
    }
}
