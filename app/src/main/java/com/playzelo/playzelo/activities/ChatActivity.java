package com.playzelo.playzelo.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.databinding.ActivityChatBinding;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private final String[] options = {
            "What does the quality meter mean?",
            "How can I improve my quality meter?",
            "My superstar level isn't improving",
            "When will my daily leaderboard winning be credited?",
            "When will I receive my monthly leaderboard winning prize?",
            "Can I transfer my Superstar Wallet balance to Main Wallet?",
            "I have still not received my referral money",
            "My query is not listed"
    };

    private final String[] responses = {
            "The quality meter reflects your game activity and behavior on the app.",
            "Play regularly, avoid quitting mid-games, and maintain fair play to improve quality.",
            "Your superstar level depends on consistent participation and quality score.",
            "Leaderboard winnings are usually credited within 24 hours after results.",
            "Monthly leaderboard prizes are disbursed within the first 7 days of next month.",
            "Yes, once eligible, you can transfer Superstar Wallet balance to Main Wallet.",
            "Referral money is sent once the referred user meets the play condition.",
            "Please reach out to our support team for queries not listed here."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showWelcomeMessage();
        showOptionButtons();

        binding.refreshButton.setOnClickListener(v -> {
            binding.chatContainer.removeAllViews();
            showWelcomeMessage();
            showOptionButtons();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showWelcomeMessage() {
        TextView welcome = new TextView(this);
        welcome.setText("Hey 921****345, welcome to WinZO! How can I help you? Please let us know your query");
        welcome.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        welcome.setTextSize(16);
        welcome.setPadding(8, 8, 8, 24);
        binding.chatContainer.addView(welcome);
    }

    private void showOptionButtons() {
        for (int i = 0; i < options.length; i++) {
            addOptionButton(options[i], responses[i]);
        }
    }

    private void addOptionButton(String text, String response) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        btn.setBackgroundResource(R.drawable.chat_option_bg);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 8, 0, 8);
        btn.setLayoutParams(lp);

        btn.setOnClickListener(v -> {
            showUserMessage(text);
            showBotResponse(response);
        });

        binding.chatContainer.addView(btn);
    }

    private void showUserMessage(String message) {
        TextView userMsg = new TextView(this);
        userMsg.setText(message);
        userMsg.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        userMsg.setBackgroundResource(R.drawable.user_msg_bg);
        userMsg.setPadding(24, 16, 24, 16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 12, 0, 4);
        lp.gravity = View.TEXT_ALIGNMENT_TEXT_END;
        userMsg.setLayoutParams(lp);
        binding.chatContainer.addView(userMsg);
        scrollToBottom();
    }

    private void showBotResponse(String reply) {
        TextView botMsg = new TextView(this);
        botMsg.setText(reply);
        botMsg.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        botMsg.setBackgroundResource(R.drawable.bot_msg_bg);
        botMsg.setPadding(24, 16, 24, 16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 4, 0, 12);
        botMsg.setLayoutParams(lp);
        binding.chatContainer.addView(botMsg);
        scrollToBottom();
    }

    private void scrollToBottom() {
        binding.chatScrollView.post(() -> binding.chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
}
