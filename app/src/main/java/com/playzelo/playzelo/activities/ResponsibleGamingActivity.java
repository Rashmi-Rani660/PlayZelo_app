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

public class ResponsibleGamingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsible_gaming);

        TextView rgTextView = findViewById(R.id.rgTextView);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        // Title
        appendBoldLarge(builder, "PlayZelo Responsible Gaming\n\n");

        // Section 1
        appendBold(builder, "1) Self-imposed Limits:\n");
        appendNormal(builder, "• Set personal limits on deposits, bet sizes, and playing time.\n");
        appendNormal(builder, "• Take breaks if playing for long periods to avoid fatigue.\n\n");

        // Section 2
        appendBold(builder, "2) Recognize Problem Behavior:\n");
        appendNormal(builder, "• Avoid chasing losses or gambling under stress, alcohol, or fatigue.\n");
        appendNormal(builder, "• Stop playing if you feel frustrated, anxious, or unable to control betting.\n\n");

        // Section 3
        appendBold(builder, "3) Support for Addiction:\n");
        appendNormal(builder, "• Contact PlayZelo support if you feel addicted or notice problematic gambling behavior.\n");
        appendNormal(builder, "• Use in-app tools or external resources for guidance.\n\n");

        // Section 4
        appendBold(builder, "4) Account Restrictions:\n");
        appendNormal(builder, "• PlayZelo may temporarily suspend or restrict accounts to protect players showing risky behavior.\n");
        appendNormal(builder, "• Limits can be applied to deposits, wagers, or session durations.\n\n");

        // Section 5
        appendBold(builder, "5) Parental/Guardian Responsibility:\n");
        appendNormal(builder, "• Users under 18 are strictly prohibited.\n");
        appendNormal(builder, "• Accounts must be KYC-verified.\n");
        appendNormal(builder, "• Parents/guardians should ensure minors do not access the platform.\n\n");

        // Section 6
        appendBold(builder, "6) Fair Play & Safety:\n");
        appendNormal(builder, "• Any abuse, collusion, or attempts to manipulate games can lead to account restrictions.\n");
        appendNormal(builder, "• Play fairly and report suspicious activity.\n\n");

        // Section 7
        appendBold(builder, "7) Education & Awareness:\n");
        appendNormal(builder, "• Learn the rules and risks before playing.\n");
        appendNormal(builder, "• Maintain a safe, fun experience on PlayZelo.\n\n");

        // Footer disclaimer
        appendNormal(builder, "Remember: Real-money gaming involves financial risk and may be habit forming. Play responsibly.");

        rgTextView.setText(builder);
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
