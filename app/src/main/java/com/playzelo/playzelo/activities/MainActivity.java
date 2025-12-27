package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.playzelo.playzelo.R;
import com.playzelo.playzelo.activities.Teenpatti.TeenPattiSplashActivity;
//import com.playzelo.playzelo.activities.birdShooting.BirdSplashActivity;
//import com.playzelo.playzelo.activities.colorMatching.ColorSplashActivity;
import com.playzelo.playzelo.activities.birdShooting.BirdSplashActivity;
import com.playzelo.playzelo.activities.colorMatching.ColorSplashActivity;
import com.playzelo.playzelo.activities.highstakedice.HighStakeDiceSplashActivity;
import com.playzelo.playzelo.activities.jackpot.JackpotSplashActivity;
import com.playzelo.playzelo.activities.lottery.LotterySplashActivity;
import com.playzelo.playzelo.activities.ludo.LudoActivity;
import com.playzelo.playzelo.activities.mines.MinesSplashActivity;
import com.playzelo.playzelo.activities.sudoku.SudokuSplashActivity;
import com.playzelo.playzelo.databinding.ActivityMainBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.Random;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private String userId, authToken, username, profileImage;


    private final int[] popupIcons = {
            R.drawable.ic_trophy,
            R.drawable.ic_money,
            R.drawable.ic_fire,
            R.drawable.ic_trophy,
            R.drawable.target,
            R.drawable.ic_dice,
            R.drawable.ic_ticket
    };

    private Handler popupHandler = new Handler();
    private Runnable popupRunnable;

    private final String[] centerPopupTitles = {
            "Khelo Jeeto!",
            "Amazing Bonus!",
            "Jackpot Alert!",
            "HighScore Winner!"
    };

    private final String[] centerPopupDescriptions = {
            "Ramesh won ₹500 in Ludo!",
            "Bonus ₹100 added to your wallet!",
            "Jackpot starts in 5 minutes!",
            "Priya scored 2000 points in Teen Patti!"
    };

    private final String[] popupMessages = {
            "Rohit won ₹50",
            "New bonus unlocked!",
            "Big win just now!",
            "Someone hit a jackpot!",
            "Lucky spin winner!",
            "Fast cash-out done!",
            "Mega reward claimed!"
    };


    private final int[] centerPopupIcons = {
            R.drawable.ic_trophy,
            R.drawable.ic_money,
            R.drawable.ic_fire,
            R.drawable.target
    };

    private Handler centerPopupHandler = new Handler();
    private Runnable centerPopupRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load balance from SharedPref
        updateWalletBalanceUI();


        startPopupLoop();
        startAnimatedCenterPopupLoop();

        if (binding.progressBar != null) binding.progressBar.setVisibility(View.VISIBLE);
        if (binding.topBar != null) binding.topBar.setVisibility(View.GONE);

        try {
            // Load user info from SharedPref
            userId = SharedPrefManager.getInstance(this).getUserId();
            authToken = SharedPrefManager.getInstance(this).getToken();
            username = SharedPrefManager.getInstance(this).getUsername();
            profileImage = SharedPrefManager.getInstance(this).getProfileImage();

            // If user is not logged in, go to login
            if (userId == null || authToken == null) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // Display username
            binding.tvUsername.setText(username != null ? username : "Player");

            // Display profile image
            showProfileImage(profileImage);

            // Set all click listeners
            setClickListeners();

        } catch (Exception e) {
            Log.e("MainActivity", "Error: " + e.getMessage(), e);
            binding.tvUsername.setText("Player");
            binding.ivProfile.setImageResource(R.drawable.ic_avtar);
        } finally {
            binding.progressBar.setVisibility(View.GONE);
            binding.topBar.setVisibility(View.VISIBLE);
        }
    }

    private void updateWalletBalanceUI() {
        double balance = SharedPrefManager.getInstance(this).getWalletBalance();
        if (binding.tvChips != null) {
            binding.tvChips.setText(String.format("%.2f", balance)); // ✅ Only shows number, no ₹ sign
        }
    }



    // =======================
    // Profile Image Helper
    // =======================
    private void showProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("not available")) {
            Glide.with(this).load(imageUrl).circleCrop().into(binding.ivProfile);
        } else {
            binding.ivProfile.setImageResource(R.drawable.ic_avtar);
        }
    }

    // Update profile image dynamically (from signup or settings)
    public void updateProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("not available")) {
            SharedPrefManager.getInstance(this).saveProfileImage(imageUrl);
            showProfileImage(imageUrl);
        }
    }

    // =======================
    // Bottom Popup
    // =======================
    private void showPopup(String message, int iconRes) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_notification, null);

        TextView text = popupView.findViewById(R.id.popupText);
        ImageView icon = popupView.findViewById(R.id.popupIcon);

        text.setText(message);
        icon.setImageResource(iconRes);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        popupWindow.setElevation(10f);

        int yOffset = 200;
        popupWindow.showAtLocation(findViewById(android.R.id.content),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);

        new Handler().postDelayed(popupWindow::dismiss, 3000);
    }

    private void startPopupLoop() {
        popupRunnable = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int index = random.nextInt(popupMessages.length);
                showPopup(popupMessages[index], popupIcons[index]);
                int delay = 10000 + random.nextInt(5000);
                popupHandler.postDelayed(this, delay);
            }
        };
        popupHandler.postDelayed(popupRunnable, 5000);
    }

    // =======================
    // Center Animated Popup
    // =======================
    private void showAnimatedCenterPopup(String title, String description, int iconRes) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_center_animated, null);

        ImageView icon = popupView.findViewById(R.id.popupCenterIcon);
        TextView txtTitle = popupView.findViewById(R.id.popupCenterTitle);
        TextView txtDesc = popupView.findViewById(R.id.popupCenterDescription);
        ImageView close = popupView.findViewById(R.id.popupCenterClose);

        icon.setImageResource(iconRes);
        txtTitle.setText(title);
        txtDesc.setText(description);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        popupView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in));
        popupWindow.showAtLocation(findViewById(android.R.id.content),
                Gravity.CENTER, 0, 0);

        close.setOnClickListener(v -> popupWindow.dismiss());
        new Handler().postDelayed(popupWindow::dismiss, 4000);
    }

    private void startAnimatedCenterPopupLoop() {
        centerPopupRunnable = new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int index = random.nextInt(centerPopupTitles.length);
                showAnimatedCenterPopup(centerPopupTitles[index],
                        centerPopupDescriptions[index],
                        centerPopupIcons[index]);
                int delay = 8000 + random.nextInt(4000);
                centerPopupHandler.postDelayed(this, delay);
            }
        };
        centerPopupHandler.postDelayed(centerPopupRunnable, 5000);
    }

    // =======================
    // Click listeners & helpers
    // =======================
    private void setClickListeners() {
        safeClick(binding.layoutProfile,  () -> startActivity(new Intent(this, DashboardActivity.class)));
        safeClick(binding.layoutChips,  () -> startActivity(new Intent(this, WalletActivity.class)));

        safeClick(binding.btnLudo, () -> openGame(LudoActivity.class));
        safeClick(binding.btnTeenpatti, () -> openGame(TeenPattiSplashActivity.class));
        safeClick(binding.btnHunt, () -> openGame(BirdSplashActivity.class));
        safeClick(binding.btnLottery, () -> openGame(LotterySplashActivity.class));
        safeClick(binding.btnMines, () -> openGame(MinesSplashActivity.class));
        safeClick(binding.btnColor, () -> openGame(ColorSplashActivity.class));
        safeClick(binding.btnHighStake, () -> openGame(HighStakeDiceSplashActivity.class));
        safeClick(binding.btnSudoku, () -> openGame(SudokuSplashActivity.class));
        safeClick(binding.btnJackpot, () -> openGame(JackpotSplashActivity.class));
        safeClick(binding.btnWinners, () -> startActivity(new Intent(this, TopWinnersActivity.class)));
        safeClick(binding.btnWallet, () -> startActivity(new Intent(this, WalletActivity.class)));
//        safeClick(binding.btnRefer, () -> startActivity(new Intent(this, ReferActivity.class)));
        safeClick(binding.btnDeposit, () -> startActivity(new Intent(this, AddMoneyActivity.class)));
    }

    private void safeClick(View view, Runnable action) {
        if (view != null) view.setOnClickListener(v -> action.run());
    }



    private void openGame(Class<?> splashActivity) {
        try {
            Intent intent = new Intent(this, splashActivity);
            intent.putExtra("userId", userId);
            intent.putExtra("auth_token", authToken);
            intent.putExtra("username", username);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("MainActivity", "Game open failed: " + e.getMessage(), e);
            showToast("Game not available");
        }
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popupHandler.removeCallbacks(popupRunnable);
        centerPopupHandler.removeCallbacks(centerPopupRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWalletBalanceUI();
    }

}

