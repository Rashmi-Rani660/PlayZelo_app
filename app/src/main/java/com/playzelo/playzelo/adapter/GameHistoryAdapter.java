package com.playzelo.playzelo.adapter; // ← match your package name



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.GameHistoryItem;
import com.playzelo.playzelo.R;
import com.playzelo.playzelo.models.GameHistory;

import java.util.List;

public class GameHistoryAdapter
        extends RecyclerView.Adapter<GameHistoryAdapter.Holder> {

    private final List<GameHistoryItem> list;

    public GameHistoryAdapter(List<GameHistoryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int i) {

        GameHistoryItem g = list.get(i);

        h.txtGame.setText(g.getGame().getIcon() + " " + g.getGame().getName());
        h.txtBet.setText("₹" + g.getBetAmount());
        h.txtWin.setText("₹" + g.getWinAmount());

        // DATE
        h.txtDate.setText(g.getCreatedAt().substring(0, 16)
                .replace("T", " "));

        // STATUS COLOR
        h.txtStatus.setText(g.getStatus().toUpperCase());
        if ("won".equalsIgnoreCase(g.getStatus())) {
            h.txtStatus.setTextColor(Color.parseColor("#16A34A"));
        } else if ("ongoing".equalsIgnoreCase(g.getStatus())) {
            h.txtStatus.setTextColor(Color.parseColor("#F59E0B"));
        } else {
            h.txtStatus.setTextColor(Color.parseColor("#EF4444"));
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtGame, txtDate, txtBet, txtWin, txtStatus;
        Holder(View v) {
            super(v);
            txtGame = v.findViewById(R.id.txtGame);
            txtDate = v.findViewById(R.id.txtDate);
            txtBet = v.findViewById(R.id.txtBet);
            txtWin = v.findViewById(R.id.txtWin);
            txtStatus = v.findViewById(R.id.txtStatus);
        }
    }
}
