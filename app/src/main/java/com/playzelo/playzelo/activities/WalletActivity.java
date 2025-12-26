package com.playzelo.playzelo.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.models.WalletResponse;
import com.playzelo.playzelo.databinding.ActivityWalletBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends BaseActivity {

    private ActivityWalletBinding binding;
    private int currentBalance = 0; // will be updated from API
    private double chips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Back button functionality
        binding.ivBack.setOnClickListener(v -> finish());

        // Saved Payment Modes
        binding.layoutGameHistory.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, GameHistoryActivity.class);
            startActivity(intent);
        });

        // Transaction History
        binding.layoutTransactionHistory.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        });

        // Add Cash button click
        binding.btnAddCash.setOnClickListener(v -> {
            startActivity(new Intent(this, AddMoneyActivity.class));
        });

        // Withdraw button click
        binding.btnWithdraw.setOnClickListener(v -> {
            startActivity(new Intent(this, WithdrawActivity.class));
        });

        // ✅ Fetch wallet balance from API
        fetchWalletBalance();


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Option 1: Fetch from API
        fetchWalletBalance();

        // Option 2 (optional): Use saved balance from SharedPrefManager
        double savedBalance = SharedPrefManager.getInstance(this).getWalletBalance();
        binding.tvBalance.setText(String.format("%.2f", savedBalance)); // ✅ Removed ₹ sign
        currentBalance = (int) savedBalance;
    }


    private void fetchWalletBalance() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        String token = SharedPrefManager.getInstance(this).getToken();

        if (token == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getWalletBalance("Bearer " + token)
                .enqueue(new Callback<WalletResponse>() {
                    @Override
                    public void onResponse(Call<WalletResponse> call,
                                           Response<WalletResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null) {

                            // ✅ chips is STRING → convert safely
                            String chipsStr = response.body()
                                    .getData()
                                    .getChips();

                            double chipsValue = 0.0;
                            try {
                                chipsValue = Double.parseDouble(chipsStr);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            currentBalance = (int) chipsValue;

                            binding.tvBalance.setText(
                                    String.format("%.2f", chipsValue)
                            );

                            // Save locally
                            SharedPrefManager.getInstance(WalletActivity.this)
                                    .saveWalletBalance(chipsValue);

                        } else {
                            Toast.makeText(WalletActivity.this,
                                    "Failed: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WalletResponse> call, Throwable t) {
                        Toast.makeText(WalletActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}