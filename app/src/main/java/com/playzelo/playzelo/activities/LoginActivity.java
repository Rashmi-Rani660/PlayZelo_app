package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityLoginBinding;
import com.playzelo.playzelo.models.LoginResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import android.webkit.CookieManager;
import android.webkit.WebStorage;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Redirect immediately if user is already logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getClient().create(ApiService.class);

        binding.btnSubmit.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });

        binding.tvForgot.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

        binding.tvSignUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.putExtra("openFragment", "wallet");
            startActivity(intent);
            finish();
        });
    }

    private void loginUser(String username, String password) {

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        Call<LoginResponse> call = apiService.loginUser(body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call,
                                   @NonNull Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse login = response.body();
                    LoginResponse.User user = login.getData();

                    SharedPrefManager pref =
                            SharedPrefManager.getInstance(LoginActivity.this);

                    // 🔥 CLEAR OLD SESSION
                    // 🔥 CLEAR OLD ANDROID SESSION
                    pref.logout();

// 🔥 CLEAR OLD WEBVIEW (LUDO) SESSION
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();
                    WebStorage.getInstance().deleteAllData();

                    if (user != null) {
                        pref.saveToken(login.getToken());
                        pref.saveUserId(user.getId());
                        pref.saveUsername(user.getUsername());
                        pref.saveEmail(user.getEmail());
                        pref.saveDob(user.getDob());
                        pref.saveCountry(user.getCountry());
                        pref.saveCity(user.getCity());
                        pref.saveAddress(user.getAddress());
                        pref.saveProfileImage(user.getProfileImage());
                        pref.setLoggedIn(true);
                    }


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
