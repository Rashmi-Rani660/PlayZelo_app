package com.playzelo.playzelo.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityTransactionDetailsBinding;

public class TransactionDetailsActivity extends BaseActivity {


    private ActivityTransactionDetailsBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Dummy Data (pass real data from intent)
        binding.tvAmount.setText(getIntent().getStringExtra("amount"));
        binding.tvTransactionId.setText("400004809249847");
        binding.tvCredited.setText(getIntent().getStringExtra("amount"));
        binding.tvRank.setText("1");
        binding.tvEntryFee.setText("₹3");
        binding.tvTournamentId.setText("175402199782787");
        binding.tvGameName.setText("Ludo Supreme");
        binding.tvDescription.setText("Winnings for 2 PLAYERS • 1 WINNER");
        binding.tvDateTime.setText(getIntent().getStringExtra("date"));
    }
}
