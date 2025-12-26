package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityResetPasswordBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetPasswordActivity extends BaseActivity {

    private ActivityResetPasswordBinding binding;
    private final OkHttpClient client = new OkHttpClient();

    private String email; // from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get email passed from OtpVerificationActivity
        email = getIntent().getStringExtra("email");

        binding.btnResetPassword.setOnClickListener(v -> {
            String newPassword = binding.etNewPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                binding.etNewPassword.setError("Enter new password");
                return;
            }

            if (newPassword.length() < 6) {
                binding.etNewPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                binding.etConfirmPassword.setError("Passwords do not match");
                return;
            }

            resetPassword(email, newPassword);
        });
    }

    private void resetPassword(String email, String newPassword) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("newPassword", newPassword);
            jsonBody.put("confirmPassword", newPassword); // ADD THIS

            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder().url("https://gamer-lk3e.onrender.com/api/auth/reset-password").post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(ResetPasswordActivity.this, "Failed to connect to server.", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body() != null ? response.body().string() : "";
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Password reset successfully!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Error: " + res, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Log.e("Error due to: ", Objects.requireNonNull(e.getLocalizedMessage()));
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}