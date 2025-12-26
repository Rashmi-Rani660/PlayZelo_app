package com.playzelo.playzelo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.models.RealDepositResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoneyActivity extends BaseActivity {

    // ================= UI =================
    LinearLayout layoutScanPay, layoutVerifyPayment, layoutUpload;
    EditText etAmount, etUTR;
    TextView tvVerifyAmount;
    ImageView imgPreview;

    // Step indicator
    TextView tvStep1Circle, tvStep2Circle, tvStep1Text, tvStep2Text;
    View viewStepLine;

    Button btnEnterAmount, btnSubmit, btnBack;

    boolean isProceedEnabled = false;
    Uri screenshotUri = null;

    private static final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        // ===== INIT =====
        layoutScanPay = findViewById(R.id.layoutScanPay);
        layoutVerifyPayment = findViewById(R.id.layoutVerifyPayment);
        layoutUpload = findViewById(R.id.layoutUpload);

        etAmount = findViewById(R.id.etAmount);
        etUTR = findViewById(R.id.etUTR);
        tvVerifyAmount = findViewById(R.id.tvVerifyAmount);

        tvStep1Circle = findViewById(R.id.tvStep1Circle);
        tvStep2Circle = findViewById(R.id.tvStep2Circle);
        tvStep1Text = findViewById(R.id.tvStep1Text);
        tvStep2Text = findViewById(R.id.tvStep2Text);
        viewStepLine = findViewById(R.id.viewStepLine);

        btnEnterAmount = findViewById(R.id.btnEnterAmount);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        showStep1();

        // ===== AMOUNT VALIDATION =====
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                validateAmount();
            }
        });

        btnEnterAmount.setOnClickListener(v -> {
            if (isProceedEnabled) showStep2();
            else Toast.makeText(this, "Minimum ₹10 required", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> showStep1());

        layoutUpload.setOnClickListener(v -> openGallery());

        btnSubmit.setOnClickListener(v -> submitPayment());
    }

    // ================= STEP UI =================

    private void showStep1() {
        layoutScanPay.setVisibility(View.VISIBLE);
        layoutVerifyPayment.setVisibility(View.GONE);

        tvStep1Circle.setBackgroundResource(R.drawable.bg_step_active);
        tvStep2Circle.setBackgroundResource(R.drawable.bg_step_inactive);

        tvStep1Text.setTextColor(getColor(R.color.blue_600));
        tvStep2Text.setTextColor(getColor(R.color.gray_500));
        viewStepLine.setBackgroundColor(getColor(R.color.gray_300));
    }

    private void showStep2() {
        layoutScanPay.setVisibility(View.GONE);
        layoutVerifyPayment.setVisibility(View.VISIBLE);

        tvVerifyAmount.setText("₹" + etAmount.getText().toString());

        tvStep1Circle.setBackgroundResource(R.drawable.bg_step_active);
        tvStep2Circle.setBackgroundResource(R.drawable.bg_step_active);

        tvStep1Text.setTextColor(getColor(R.color.blue_600));
        tvStep2Text.setTextColor(getColor(R.color.blue_600));
        viewStepLine.setBackgroundColor(getColor(R.color.blue_600));
    }

    // ================= VALIDATION =================

    private void validateAmount() {
        String amt = etAmount.getText().toString().trim();

        if (!amt.isEmpty() && Integer.parseInt(amt) >= 10) {
            btnEnterAmount.setEnabled(true);
            btnEnterAmount.setText("Proceed to Pay");
            btnEnterAmount.setBackgroundTintList(getColorStateList(R.color.blue_600));
            isProceedEnabled = true;
        } else {
            btnEnterAmount.setEnabled(false);
            btnEnterAmount.setText("Enter Amount");
            btnEnterAmount.setBackgroundTintList(getColorStateList(R.color.gray_300));
            isProceedEnabled = false;
        }
    }

    // ================= IMAGE PICK =================

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            screenshotUri = data.getData();
            Toast.makeText(this, "Screenshot selected", Toast.LENGTH_SHORT).show();
        }
    }

    // ================= API =================

    private void submitPayment() {

        String amount = etAmount.getText().toString().trim();
        String utr = etUTR.getText().toString().trim();

        if (utr.isEmpty() && screenshotUri == null) {
            Toast.makeText(this, "Provide UTR or screenshot", Toast.LENGTH_LONG).show();
            return;
        }

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        Map<String, Object> body = new HashMap<>();
        body.put("amount", Integer.parseInt(amount));
        if (!utr.isEmpty()) body.put("utrNumber", utr);

        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.realDeposit(token, body).enqueue(new Callback<RealDepositResponse>() {
            @Override
            public void onResponse(Call<RealDepositResponse> call, Response<RealDepositResponse> response) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit");

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AddMoneyActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AddMoneyActivity.this,
                            "Deposit failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RealDepositResponse> call, Throwable t) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit");
                Toast.makeText(AddMoneyActivity.this,
                        "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
