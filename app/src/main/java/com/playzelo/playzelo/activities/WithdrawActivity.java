package com.playzelo.playzelo.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.models.WithdrawResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends BaseActivity {

    private TextView tvBalance;
    private EditText etAmount, etUpi;
    private Button btnWithdraw;

    private double availableBalance = 0.0;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        apiService = ApiClient.getClient().create(ApiService.class);

        initViews();
        loadWalletBalance();
        setupListeners();
    }

    private void initViews() {
        tvBalance = findViewById(R.id.tvBalance);
        etAmount = findViewById(R.id.etAmount);
        etUpi = findViewById(R.id.etUpi);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        findViewById(R.id.btn300).setOnClickListener(v -> setAmount(300));
        findViewById(R.id.btn500).setOnClickListener(v -> setAmount(500));
        findViewById(R.id.btn1000).setOnClickListener(v -> setAmount(1000));
        findViewById(R.id.btn2000).setOnClickListener(v -> setAmount(2000));
        findViewById(R.id.btn5000).setOnClickListener(v -> setAmount(5000));
    }

    private void setAmount(int amount) {
        etAmount.setText(String.valueOf(amount));
        etAmount.setSelection(etAmount.getText().length());
    }

    private void loadWalletBalance() {
        availableBalance = SharedPrefManager.getInstance(this).getWalletBalance();
        tvBalance.setText("₹ " + String.format("%.2f", availableBalance));
    }

    private void setupListeners() {

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                validateAmountLive();
            }
        });

        btnWithdraw.setOnClickListener(v -> validateAndWithdraw());
    }

    private void validateAmountLive() {
        String amountStr = etAmount.getText().toString().trim();

        if (TextUtils.isEmpty(amountStr)) {
            btnWithdraw.setEnabled(false);
            return;
        }

        double amount = Double.parseDouble(amountStr);
        btnWithdraw.setEnabled(amount >= 300 && amount <= availableBalance);
    }

    private void validateAndWithdraw() {

        String amountStr = etAmount.getText().toString().trim();
        String upi = etUpi.getText().toString().trim();

        if (TextUtils.isEmpty(amountStr)) {
            etAmount.setError("Enter amount");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (amount < 300) {
            etAmount.setError("Minimum withdrawal ₹300");
            return;
        }

        if (amount > availableBalance) {
            etAmount.setError("Insufficient balance");
            return;
        }

        if (TextUtils.isEmpty(upi) || !upi.contains("@")) {
            etUpi.setError("Enter valid UPI ID");
            return;
        }

        callWithdrawApi(amount, upi);
    }

    private void callWithdrawApi(double amount, String upi) {

        btnWithdraw.setEnabled(false);
        btnWithdraw.setText("Processing...");

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("upiId", upi);

        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        apiService.withdrawRequest(token, body).enqueue(new Callback<WithdrawResponse>() {
            @Override
            public void onResponse(Call<WithdrawResponse> call, Response<WithdrawResponse> response) {

                btnWithdraw.setEnabled(true);
                btnWithdraw.setText("WITHDRAW NOW");

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    WithdrawResponse.Data data = response.body().getData();

                    availableBalance = data.getBalanceAfter();
                    SharedPrefManager.getInstance(WithdrawActivity.this)
                            .saveWalletBalance(availableBalance);

                    tvBalance.setText("₹ " + String.format("%.2f", availableBalance));
                    etAmount.setText("");
                    etUpi.setText("");

                    Toast.makeText(
                            WithdrawActivity.this,
                            "Withdrawal Successful ₹" + data.getAmount(),
                            Toast.LENGTH_LONG
                    ).show();

                } else {
                    Toast.makeText(
                            WithdrawActivity.this,
                            "Withdraw failed. Try again",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<WithdrawResponse> call, Throwable t) {
                btnWithdraw.setEnabled(true);
                btnWithdraw.setText("WITHDRAW NOW");

                Toast.makeText(
                        WithdrawActivity.this,
                        "Server error. Please try later",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
