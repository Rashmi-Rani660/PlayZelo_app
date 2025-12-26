//forgot

package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityForgotPasswordBinding;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends BaseActivity {


    private ActivityForgotPasswordBinding binding;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSubmit.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.setError("Invalid email address");
                return;
            }

            sendForgotPasswordRequest(email);
        });
    }

    private void sendForgotPasswordRequest(String email) {
        try {
            // Prepare JSON body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Create request
            Request request = new Request.Builder()
                    .url("https://gamer-lk3e.onrender.com/api/auth/forgot-password")
                    .post(body)
                    .build();

            // Execute async
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() ->
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Failed to connect to server.",
                                    Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body() != null ? response.body().string() : "";
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "OTP sent to your email.",
                                    Toast.LENGTH_LONG).show();

                            // Move to OTP screen
                            Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                            intent.putExtra("email", email); // pass email to next screen
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Error: " + res,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            Log.e("Something went wrong: "+e.getMessage(), e.toString());
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}
