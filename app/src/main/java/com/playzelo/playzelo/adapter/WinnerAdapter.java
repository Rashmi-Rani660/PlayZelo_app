package com.playzelo.playzelo.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.Winner;

import java.util.List;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {
    private final List<Winner> list;

    public WinnerAdapter(List<Winner> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_winner, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Winner w = list.get(position);
        holder.txtRank.setText(w.getRank());
        holder.txtName.setText(w.getName());
        holder.txtId.setText(w.getId());
        holder.txtAmount.setText(w.getAmount());
        holder.txtGame.setText(w.getGame());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank, txtName, txtId, txtAmount, txtGame;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtName = itemView.findViewById(R.id.txtName);
            txtId = itemView.findViewById(R.id.txtId);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtGame = itemView.findViewById(R.id.txtGame);
        }
    }
}

