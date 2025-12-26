package com.playzelo.playzelo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.playzelo.playzelo.R;
import com.playzelo.playzelo.databinding.ActivityDashboardBinding;
import com.playzelo.playzelo.databinding.BottomSheetLogoutBinding;
import com.playzelo.playzelo.models.ProfileResponse;
import com.playzelo.playzelo.models.User;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());


        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchProfileData(); // tumhara existing code

        updateWalletBalance(); // 👈 YE ADD KARO
    }

    // ==================== CLICK LISTENERS ====================
    private void setupClickListeners() {
        binding.walletLayout.getRoot().setOnClickListener(v -> openWallet());
        binding.layoutHelp.getRoot().setOnClickListener(v -> openHelp());
        binding.layoutGaming.getRoot().setOnClickListener(v -> openGamingTools());
//        binding.profileLink.setOnClickListener(v -> openProfile());
        binding.viewMore.setOnClickListener(v -> toggleMoreOptions());
        binding.layoutGame.getRoot().setOnClickListener(v -> openGameManagement());
        binding.layoutAbout.getRoot().setOnClickListener(v -> openAbout());
        binding.layoutLogout.getRoot().setOnClickListener(v -> showLogoutBottomSheet());
    }

    @SuppressLint("SetTextI18n")
    private void toggleMoreOptions() {
        if (!isExpanded) {
            binding.moreOptionsContainer.setVisibility(View.VISIBLE);
            binding.viewMore.setText("VIEW LESS OPTIONS");
        } else {
            binding.moreOptionsContainer.setVisibility(View.GONE);
            binding.viewMore.setText("VIEW MORE OPTIONS");
        }
        isExpanded = !isExpanded;
    }

    private void openWallet() {
        startActivity(new Intent(this, WalletActivity.class));
    }

    private void openHelp() {
        startActivity(new Intent(this, ChatActivity.class));
    }

//    private void openProfile() {
//        startActivity(new Intent(this, ProfileActivity.class));
//    }

    private void openGamingTools() {
        startActivity(new Intent(this, ResponsibleGamingActivity.class));
    }

    private void setLanguage(String language) {
        Toast.makeText(this, "Language: " + language, Toast.LENGTH_SHORT).show();
    }

    private void openGameManagement() {
        startActivity(new Intent(this, GameManagementActivity.class));
    }

    private void openAbout() {
        startActivity(new Intent(this, TermsAndConditionActivity.class));
    }

    private void showLogoutBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomSheetLogoutBinding logoutBinding = BottomSheetLogoutBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(logoutBinding.getRoot());

        logoutBinding.btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        logoutBinding.btnLogout.setOnClickListener(v -> performLogout());

        bottomSheetDialog.show();
    }

    private void performLogout() {
        SharedPrefManager.getInstance(this).logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ==================== PROFILE FETCH ====================
    private void fetchProfileData() {
        String token = SharedPrefManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            handleSessionExpired();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ProfileResponse> call = apiService.getProfile("Bearer " + token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profile = response.body();
                    Log.d("DASHBOARD_PROFILE", profile.toString());

                    User user = profile.getData();
                    if (user != null) {
                        updateProfileUI(user);
                    } else {
                        Toast.makeText(DashboardActivity.this, "User data not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch profile: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        if (response.errorBody() != null)
                            Log.e("API_ERROR", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }


    private void updateWalletBalance() {

        double balance =
                SharedPrefManager.getInstance(this).getWalletBalance();

        // include layout ka TextView
        binding.walletLayout.tvWalletBalance.setText(
                String.format("%.2f", balance)
        );
    }


    private void updateProfileUI(User user) {
        binding.username.setText(user.getUsername());
        binding.userNumber.setText(user.getEmail());

        String initials = getInitialsFromName(user.getUsername());
        Bitmap bitmap = generateInitialsBitmap(initials, Color.parseColor("#3F51B5"));
        binding.profileImage.setImageBitmap(bitmap);

//        // Optional: You can add followers/following count if available
//        binding.followersCount.setText("0");
//        binding.followingCount.setText("0");
    }

    private String getInitialsFromName(String username) {
        if (username == null || username.isEmpty()) return "??";
        String[] parts = username.split("_|\\s+");
        StringBuilder initials = new StringBuilder();

        if (parts.length > 0 && !parts[0].isEmpty())
            initials.append(parts[0].substring(0, 1).toUpperCase());
        if (parts.length > 1 && !parts[1].isEmpty())
            initials.append(parts[1].substring(0, 1).toUpperCase());
        else if (username.length() > 1)
            initials.append(username.substring(1, 2).toUpperCase());

        return initials.length() == 0 ? "??" : initials.toString();
    }

    private Bitmap generateInitialsBitmap(String initials, int bgColor) {
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Draw background circle
        paint.setColor(bgColor);
        canvas.drawCircle(75f, 75f, 75f, paint);

        // Draw initials
        paint.setColor(Color.WHITE);
        paint.setTextSize(75f);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float x = 75f;
        float y = 75f - (fm.ascent + fm.descent) / 2f;
        canvas.drawText(initials, x, y, paint);

        return bitmap;
    }

    private void handleSessionExpired() {
        Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
        SharedPrefManager.getInstance(this).logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
