package com.playzelo.playzelo.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.playzelo.playzelo.R;

public class GameManagementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1️⃣ Set the correct layout XML
        setContentView(R.layout.activity_game_management); // <-- your layout XML

        // 2️⃣ Now find the TextView
        TextView gmTextView = findViewById(R.id.gmTextView);

        // 3️⃣ Build your text
        SpannableStringBuilder builder = new SpannableStringBuilder();

        appendBoldLarge(builder, "PlayZelo Game Management\n\n");

        // 1) Game Rules & Formats
        appendBold(builder, "1) Game Rules & Formats:\n");
        appendNormal(builder, "• Each game has predefined rules, formats, and skill elements.\n");
        appendNormal(builder, "• All games (Ludo, Teen Patti, Jackpot, Lottery, Mines) follow server-validated logic.\n");
        appendNormal(builder, "• Tie, abort, or disconnection scenarios are handled automatically.\n\n");

        // 2) Fair Play & Anti-Cheating
        appendBold(builder, "2) Fair Play & Anti-Cheating Measures:\n");
        appendNormal(builder, "• Multi-accounting, bots, emulators, collusion, or gameplay manipulation is prohibited.\n");
        appendNormal(builder, "• Suspicious activity may result in suspension, voided winnings, or bans.\n");
        appendNormal(builder, "• Server-side RNG ensures fairness in card games, jackpots, and lotteries.\n\n");

        // 3) Matchmaking & Game Sessions
        appendBold(builder, "3) Matchmaking & Game Sessions:\n");
        appendNormal(builder, "• Players are matched based on skill, stake, and availability.\n");
        appendNormal(builder, "• Session timers enforce turns and prevent stalling.\n");
        appendNormal(builder, "• Disconnects or leaving mid-game can incur penalties or forfeitures.\n\n");

        // 4) Game Updates & Maintenance
        appendBold(builder, "4) Game Updates & Maintenance:\n");
        appendNormal(builder, "• Games may be temporarily paused for maintenance or updates.\n");
        appendNormal(builder, "• Notifications will be provided where possible to minimize disruption.\n\n");

        // 5) Winnings & Payouts
        appendBold(builder, "5) Winnings & Payouts:\n");
        appendNormal(builder, "• Winnings are credited to in-app wallets after verification.\n");
        appendNormal(builder, "• Transactions are subject to KYC, TDS, and applicable laws.\n");
        appendNormal(builder, "• Disputed outcomes are resolved using server logs and fair-play audits.\n\n");

        // 6) Game Audits & Logs
        appendBold(builder, "6) Game Audits & Logs:\n");
        appendNormal(builder, "• All game activity is logged for audit purposes.\n");
        appendNormal(builder, "• Logs help in dispute resolution, policy enforcement, and monitoring responsible play.\n\n");

        // 7) User Responsibilities
        appendBold(builder, "7) User Responsibilities in Games:\n");
        appendNormal(builder, "• Follow game instructions and guidelines.\n");
        appendNormal(builder, "• Report bugs, glitches, or suspicious activity to PlayZelo support.\n");
        appendNormal(builder, "• Avoid practices that manipulate or disrupt normal gameplay.\n\n");

        // 8) Game Termination & Refunds
        appendBold(builder, "8) Game Termination & Refunds:\n");
        appendNormal(builder, "• Games may be canceled or voided in case of technical failure or insufficient participants.\n");
        appendNormal(builder, "• Refunds (if applicable) are processed according to PlayZelo refund policy.\n\n");

        gmTextView.setText(builder);
    }

    // Helper methods
    private void appendBold(SpannableStringBuilder builder, String text) {
        int start = builder.length();
        builder.append(text);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendBoldLarge(SpannableStringBuilder builder, String text) {
        int start = builder.length();
        builder.append(text);
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1.3f), start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void appendNormal(SpannableStringBuilder builder, String text) {
        builder.append(text);
    }
}
