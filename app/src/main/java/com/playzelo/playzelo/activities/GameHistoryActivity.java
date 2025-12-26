package com.playzelo.playzelo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.GameHistoryItem;
import com.playzelo.playzelo.R;
import com.playzelo.playzelo.adapter.GameHistoryAdapter;
import com.playzelo.playzelo.models.GameHistoryResponse;
import com.playzelo.playzelo.network.ApiClient;
import com.playzelo.playzelo.network.ApiService;
import com.playzelo.playzelo.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameHistoryActivity extends BaseActivity {

    List<GameHistoryItem> list = new ArrayList<>();
    GameHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game_history);

        RecyclerView rv = findViewById(R.id.recyclerGameHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameHistoryAdapter(list);
        rv.setAdapter(adapter);

        loadHistory();
    }

    private void loadHistory() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        api.getGameHistory(token, 1)
                .enqueue(new Callback<GameHistoryResponse>() {
                    @Override
                    public void onResponse(Call<GameHistoryResponse> call,
                                           Response<GameHistoryResponse> res) {
                        if (res.isSuccessful() && res.body() != null) {
                            list.clear();
                            list.addAll(res.body().getData());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<GameHistoryResponse> call, Throwable t) {
                        Toast.makeText(GameHistoryActivity.this,
                                t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
