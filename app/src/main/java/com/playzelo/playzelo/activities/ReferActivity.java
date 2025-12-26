package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.databinding.ActivityReferBinding;

public class ReferActivity extends BaseActivity {

    ActivityReferBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> {
            // Finish current activity and return to the previous one
            getOnBackPressedDispatcher().onBackPressed(); // this handles system back with animation
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });


//        binding.btnLeaderboard.setOnClickListener(v -> {
//            Intent intent = new Intent(ReferActivity.this, LeaderboardActivity.class);
//            startActivity(intent);
//        });

        binding.btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ReferActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        });


        binding.btnReferNow.setOnClickListener(v ->
                Toast.makeText(this, "Share your referral link!", Toast.LENGTH_SHORT).show());

        binding.btnWithdraw.setOnClickListener(v ->
                Toast.makeText(this, "No balance to withdraw yet", Toast.LENGTH_SHORT).show());

        binding.btnCommissionInfo.setOnClickListener(v ->
                Toast.makeText(this, "You earn 2.5% on each referral’s activity!", Toast.LENGTH_LONG).show());
    }
}
