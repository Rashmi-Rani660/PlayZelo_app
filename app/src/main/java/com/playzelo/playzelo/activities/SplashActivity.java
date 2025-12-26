package com.playzelo.playzelo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.databinding.ActivitySplashBinding;
import com.playzelo.playzelo.utils.SharedPrefManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends  BaseActivity{

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.your_bg_color));
        }


        // Logo zoom animation
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        binding.logoImage.startAnimation(zoomIn);

        // Text appear after center animation
        new Handler().postDelayed(() -> {
            binding.splashText.setVisibility(View.VISIBLE);
            binding.splashText.setAlpha(0f);
            binding.splashText.setScaleX(0.8f);
            binding.splashText.setScaleY(0.8f);
            binding.splashText.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(1000)
                    .start();
        }, 2200);

        // Check if Terms accepted or not
        SharedPreferences prefs = getSharedPreferences("PlayZeloPrefs", MODE_PRIVATE);
        boolean isAccepted = prefs.getBoolean("isAccepted", false);

        if (!isAccepted) {
            // Show Terms Dialog immediately
            new Handler().postDelayed(this::showTermsAndConditionsDialog, 2500);
        } else {
            // Navigate after splash
            new Handler().postDelayed(this::proceedToApp, 5000);
        }
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("PlayZelo - Terms & Conditions");

        String terms = "📜 Please read the Terms & Conditions carefully:\n\n" +

                "1. You must be 18 years or older to use PlayZelo and participate in real money games.\n\n" +
                "2. All games are skill-based, but outcomes may involve chance. Play responsibly.\n\n" +
                "3. Winnings will be credited to your PlayZelo wallet only after verification.\n\n" +
                "4. Deposits and withdrawals must comply with government regulations and KYC norms.\n\n" +
                "5. PlayZelo is not responsible for losses due to network failure, device issues, or disconnections.\n\n" +
                "6. Any fraudulent activity, use of multiple accounts, or unfair play may lead to account suspension.\n\n" +
                "7. By continuing, you agree to share accurate KYC documents when required.\n\n" +
                "8. State laws apply. Players from restricted states cannot participate in real money games.\n\n" +
                "9. Play responsibly. Stop if gaming affects your mental health or financial stability.\n\n" +
                "10. PlayZelo reserves the right to modify rules, terms, or suspend services without prior notice.\n\n\n" +

                "🎮 Game-wise Terms:\n\n" +
                "• Ludo: Dice rolls are algorithm-based, fair, and cannot be influenced.\n" +
                "• Jackpot: Winning depends on ticket purchase; results are auto-generated.\n" +
                "• Lottery: Results are random and system-driven; no manual intervention.\n" +
                "• Mines: Selection is final; mistakes cannot be reversed once submitted.\n" +
                "• Teen Patti: Cards are digitally shuffled; fair-play algorithms applied.\n" +
                "• HighStakeDice: Bets once placed cannot be cancelled; winnings based on dice roll.\n" +
                "• Bird Shooting: Scores depend on accuracy & timing; cheating leads to ban.\n\n" +

                "✅ By tapping 'Accept & Continue', you agree to all above Terms & Conditions.";

        builder.setMessage(terms);

        builder.setPositiveButton("Accept & Continue", (dialog, which) -> {
            SharedPreferences.Editor editor = getSharedPreferences("PlayZeloPrefs", MODE_PRIVATE).edit();
            editor.putBoolean("isAccepted", true);
            editor.apply();

            proceedToApp();
            dialog.dismiss();
        });

        builder.setNegativeButton("Decline", (dialog, which) -> {
            dialog.dismiss();
            finishAffinity(); // Exit app completely
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void proceedToApp() {
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}
