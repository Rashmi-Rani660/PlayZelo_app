package com.playzelo.playzelo.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.Winner;
import com.playzelo.playzelo.adapter.WinnerAdapter;
import com.playzelo.playzelo.models.WinnersResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopWinnersActivity extends BaseActivity {

    RecyclerView recyclerView;
    WinnerAdapter adapter;
    List<Winner> winnersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_winners);

        recyclerView = findViewById(R.id.recyclerWinners);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WinnerAdapter(winnersList);
        recyclerView.setAdapter(adapter);

        fetchWinners();
    }

    private void fetchWinners() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getRecentWinners().enqueue(new Callback<WinnersResponse>() {
            @Override
            public void onResponse(Call<WinnersResponse> call, Response<WinnersResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()) {

                    winnersList.clear();
                    int rank = 1;

                    for (Winner w : response.body().getData()) {
                        w.setRank(String.valueOf(rank++));
                        winnersList.add(w);
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(
                            TopWinnersActivity.this,
                            "Failed to load winners",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<WinnersResponse> call, Throwable t) {
                Toast.makeText(
                        TopWinnersActivity.this,
                        "Failed to load winners",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
