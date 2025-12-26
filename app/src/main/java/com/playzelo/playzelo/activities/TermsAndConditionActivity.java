package com.playzelo.playzelo.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.playzelo.playzelo.R;
import androidx.appcompat.app.AppCompatActivity;

public class TermsAndConditionActivity extends BaseActivity {

    private TextView tvTermsContent;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        tvTermsContent = findViewById(R.id.tvTermsContent);
        btnBack = findViewById(R.id.btnBack);

        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Full Terms & Conditions + Privacy Policy + Game-wise Add-ons
        String termsText = "📜 Terms & Conditions (General for All Games)\n\n"
                + "Effective Date: 01/09/2025\n"
                + "Last Updated: 01/09/2025\n\n"
                + "Welcome to PlayZelo (“we”, “our”, “us”). By downloading, accessing, or using our games, "
                + "including Ludo, Jackpot, Mines, Lottery, Shooting Bird, HighStakeDice, and TeenPatti, you "
                + "agree to abide by these Terms & Conditions. If you do not agree, please do not use our Games.\n\n"

                + "1. Eligibility\n"
                + "• You must be 18 years or older to play real-money games.\n"
                + "• By using our Games, you confirm that you meet the eligibility criteria.\n\n"

                + "2. Account Registration\n"
                + "• Provide accurate, complete, and updated information.\n"
                + "• You are responsible for safeguarding your login credentials.\n"
                + "• Accounts are personal and non-transferable.\n\n"

                + "3. Game Rules\n"
                + "• Each Game has its own rules (Ludo tokens, Jackpot spin, Teen Patti cards, etc.).\n"
                + "• Fair play only. No cheats, bots, or external tools allowed.\n"
                + "• Violations may result in suspension or ban.\n\n"

                + "4. Deposits & Withdrawals\n"
                + "• Transactions only via approved payment methods.\n"
                + "• Withdrawals processed after verification (up to 3-5 working days).\n\n"

                + "5. Winnings & Taxes\n"
                + "• Winnings are credited to in-app wallet.\n"
                + "• Users are responsible for taxes. TDS may apply as per law.\n\n"

                + "6. Responsible Gaming\n"
                + "• Play responsibly. Set personal deposit/spending limits.\n\n"

                + "7. Prohibited Jurisdictions\n"
                + "• Users from states/jurisdictions where online real-money gaming is banned cannot participate.\n\n"

                + "8. Intellectual Property\n"
                + "• All content, logos, and software belong to PlayZelo.\n\n"

                + "9. Limitation of Liability\n"
                + "• We are not liable for losses due to technical issues or misuse.\n\n"

                + "10. Governing Law\n"
                + "• These Terms are governed under Indian law.\n\n"

                + "------------------------------------------------------\n\n"

                + "🔒 Privacy Policy\n\n"
                + "1. Information We Collect\n"
                + "• Personal Info: Name, Email, Phone, DOB, KYC docs.\n"
                + "• Payment Info: UPI, Bank details (secured).\n"
                + "• Usage Data: Device, IP, gameplay logs.\n\n"

                + "2. How We Use Your Information\n"
                + "• Account creation, payments, gameplay, and legal compliance.\n\n"

                + "3. Data Security\n"
                + "• Industry-standard encryption used.\n\n"

                + "4. Children’s Privacy\n"
                + "• Not for users under 18 years.\n\n"

                + "------------------------------------------------------\n\n"

                + "🎮 Game-Specific Terms\n\n"

                + "Ludo:\n"
                + "• Standard Ludo rules. RNG-based dice.\n"
                + "• Disconnections may lead to forfeit.\n\n"

                + "Jackpot:\n"
                + "• RNG-based spin outcomes. No manual influence.\n"
                + "• Bets refunded if technical failure occurs.\n\n"

                + "Mines:\n"
                + "• Strategy + luck game. Avoid mines.\n"
                + "• Cash-out before hitting a mine.\n\n"

                + "Lottery:\n"
                + "• Tickets non-refundable. Draws via RNG.\n"
                + "• Winners notified in-app/email.\n\n"

                + "Shooting Bird:\n"
                + "• Score-based game. No external aim-assist.\n"
                + "• Crashes = last score considered.\n\n"

                + "HighStakeDice:\n"
                + "• Highest dice total wins. RNG fairness ensured.\n"
                + "• In ties, winnings split or replayed.\n\n"

                + "Teen Patti:\n"
                + "• Standard 3-card rules. Highest hand wins.\n"
                + "• Collusion or multiple accounts = ban.\n\n"

                + "------------------------------------------------------\n\n"
                + "📧 Support: support@playzelo.com\n"
                + "📞 Helpline: +91-XXXXXXXXXX\n";

        tvTermsContent.setText(termsText);
    }
}
