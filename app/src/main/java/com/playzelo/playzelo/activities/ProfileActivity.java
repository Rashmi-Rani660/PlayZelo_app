package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.playzelo.playzelo.databinding.ActivityProfileBinding;
import com.playzelo.playzelo.models.ProfileResponse;
import com.playzelo.playzelo.models.User;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchUserProfile();
        setOnClickListener();
    }

    private void fetchUserProfile() {
        String token = SharedPrefManager.getInstance(this).getToken();
        Log.d("PROFILE", "Token from SharedPref: " + token);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "No token found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ProfileResponse> call = apiService.getProfile("Bearer " + token);
        Log.d("PROFILE", "Final Header: Bearer " + token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log the full response here
                    Log.d("PROFILE_JSON", new com.google.gson.Gson().toJson(response.body()));

                    User user = response.body().getData(); // getData() matches ProfileResponse
                    if (user != null) {
                        binding.username.setText(user.getUsername());

                        String initials = getInitialsFromName(user.getUsername());
                        Bitmap initialBitmap = generateInitialsBitmap(initials, 150,
                                Color.parseColor("#3F51B5"), Color.WHITE);
                        binding.profileImage.setImageBitmap(initialBitmap);
                    } else {
                        Toast.makeText(ProfileActivity.this, "User data not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("API_ERROR", "Code: " + response.code() +
                                " Body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ProfileActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage(), t);
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap generateInitialsBitmap(String initials, int size, int bgColor, int textColor) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setColor(bgColor);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        paint.setColor(textColor);
        paint.setTextSize(size / 2f);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float x = size / 2f;
        float y = size / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f;
        canvas.drawText(initials, x, y, paint);
        return bitmap;
    }

    private String getInitialsFromName(String username) {
        if (username == null || username.isEmpty()) return "";
        String[] parts = username.split("_|\\s+");
        String initials = "";

        if (parts.length > 0 && !parts[0].isEmpty()) {
            initials += parts[0].substring(0, 1).toUpperCase();
        }
        if (parts.length > 1 && !parts[1].isEmpty()) {
            initials += parts[1].substring(0, 1).toUpperCase();
        } else if (username.length() > 1) {
            initials += username.substring(1, 2).toUpperCase();
        }
        return initials;
    }

    private void setOnClickListener() {
//        binding.editProfileBtn.setOnClickListener(v -> redirectToEditProfile());
        binding.ivBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    private void redirectToGameHistory() {
        startActivity(new Intent(ProfileActivity.this, GameHistoryActivity.class));
    }

//    private void redirectToEditProfile() {
//        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
//    }
}
