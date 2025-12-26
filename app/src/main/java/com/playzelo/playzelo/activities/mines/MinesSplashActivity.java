package com.playzelo.playzelo.activities.mines;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.playzelo.playzelo.activities.BaseActivity;
import com.playzelo.playzelo.databinding.ActivityMinesSplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

public class MinesSplashActivity extends BaseActivity {

    private ActivityMinesSplashBinding binding;
    private String userId, username, authToken, wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMinesSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ================= FETCH USER DATA =================
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        userId = pref.getUserId();
        username = pref.getUsername();
        authToken = pref.getToken();
        wallet = String.valueOf(pref.getWalletBalance());

        Log.d("MinesSplash", "User ID: " + userId + ", Username: " + username + ", Token: " + authToken);

        // ================= NAVIGATE TO MINES GAME =================
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MinesSplashActivity.this, MinesGameActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("username", username);
            intent.putExtra("authToken", authToken);
            intent.putExtra("wallet", wallet);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
