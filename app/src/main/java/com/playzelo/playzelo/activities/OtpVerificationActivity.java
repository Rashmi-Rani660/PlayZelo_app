//verification

package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityOtpVerificationBinding;

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

public class OtpVerificationActivity extends BaseActivity {
    private ActivityOtpVerificationBinding binding;
    private String email;
    private final OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get email from ForgotPasswordActivity
        email = getIntent().getStringExtra("email");

        binding.btnVerify.setOnClickListener(v -> {
            String otp = binding.etOtp.getText().toString().trim();

            if (TextUtils.isEmpty(otp)) {
                binding.etOtp.setError("Enter OTP");
                return;
            }

            verifyOtp(email, otp);
        });
    }

    private void verifyOtp(String email, String otp) {
        try {
            // Prepare JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("otp", otp);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Create request
            Request request = new Request.Builder()
                    .url("https://gamer-lk3e.onrender.com/api/auth/verify-otp")
                    .post(body)
                    .build();

            // Send request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(OtpVerificationActivity.this,
                                    "Failed to connect to server.",
                                    Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body() != null ? response.body().string() : "";
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(OtpVerificationActivity.this,
                                    "OTP Verified Successfully!",
                                    Toast.LENGTH_LONG).show();

                            // Move to Reset Password screen
                            Intent intent = new Intent(OtpVerificationActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email); // pass email for reset
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(OtpVerificationActivity.this,
                                    "Invalid OTP. " + res,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Log.e("Error: ", Objects.requireNonNull(e.getLocalizedMessage()));
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}
