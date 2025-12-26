package com.playzelo.playzelo.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.adapter.TransactionAdapter;
import com.playzelo.playzelo.models.Transaction;
import com.playzelo.playzelo.models.TransactionResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryActivity extends BaseActivity {

    private RecyclerView recycler;
    private TransactionAdapter adapter;
    private List<Transaction> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        recycler = findViewById(R.id.recyclerTransactions);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TransactionAdapter(list);
        recycler.setAdapter(adapter);

        findViewById(R.id.ivBack).setOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );

        loadTransactions();
    }

    private void loadTransactions() {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        String token = SharedPrefManager.getInstance(this).getToken();
        String myUserId = SharedPrefManager.getInstance(this).getUserId();

        if (token == null || myUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        api.getTransactions("Bearer " + token, 1, 50)
                .enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                            list.clear();
                            List<Transaction> transactions = response.body().getTransactions();

                            if (transactions != null && !transactions.isEmpty()) {
                                for (Transaction t : transactions) {
                                    if (t != null && t.getUserId() != null
                                            && myUserId.equals(t.getUserId().getId())) { // ✅ compare with user id
                                        list.add(t);
                                    }
                                }

                                adapter.notifyDataSetChanged();

                                if (list.isEmpty()) {
                                    Toast.makeText(TransactionHistoryActivity.this,
                                            "No transactions found for this user",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(TransactionHistoryActivity.this,
                                        "No transaction data",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(TransactionHistoryActivity.this,
                                    "Failed to load transactions",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Toast.makeText(TransactionHistoryActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
