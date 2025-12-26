package com.playzelo.playzelo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.playzelo.playzelo.R;
import com.playzelo.playzelo.models.Transaction;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> list;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = list.get(position);

        // 🔹 TYPE (GAME BET / WITHDRAW etc.)
        holder.txtResult.setText(formatType(t.getType()));

        // 🔹 STATUS
        holder.txtStatus.setText(t.getStatus());
        holder.txtStatus.setTextColor(getStatusColor(holder, t.getStatus()));

        // 🔹 AMOUNT (left)
        double amount = t.getAmount();
        if (amount < 0) {
            holder.txtBetAmount.setText("-₹" + formatAmount(Math.abs(amount)));
            holder.txtBetAmount.setTextColor(
                    holder.itemView.getResources().getColor(R.color.red_600)
            );
        } else {
            holder.txtBetAmount.setText("₹" + formatAmount(amount));
            holder.txtBetAmount.setTextColor(
                    holder.itemView.getResources().getColor(R.color.green_600)
            );
        }

        // 🔹 BALANCE (right)
        holder.txtWinAmount.setText("₹" + formatAmount(t.getBalanceAfter()));

        // 🔹 DATE + TIME (single line like web)
        holder.txtDate.setText(formatDateTime(t.getCreatedAt()));

        // 🔹 TRANSACTION ID
        holder.txtTransactionId.setText(t.getTransactionId());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    /* ================= HELPERS ================= */

    private String formatType(String type) {
        if (type == null) return "-";
        return type.replace("_", " ");
    }

    private int getStatusColor(ViewHolder holder, String status) {
        switch (status) {
            case "SUCCESS":
                return holder.itemView.getResources().getColor(R.color.green_600);
            case "FAILED":
                return holder.itemView.getResources().getColor(R.color.red_600);
            default:
                return holder.itemView.getResources().getColor(R.color.orange_600);
        }
    }

    private String formatAmount(double amount) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "IN"));
        nf.setMaximumFractionDigits(2);
        return nf.format(amount);
    }

    private String formatDateTime(String createdAt) {
        try {
            SimpleDateFormat in = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault()
            );
            Date d = in.parse(createdAt);
            return new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
            ).format(d);
        } catch (Exception e) {
            return "-";
        }
    }

    /* ================= VIEW HOLDER ================= */

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtResult, txtStatus;
        TextView txtBetAmount, txtWinAmount;
        TextView txtDate, txtTransactionId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtResult = itemView.findViewById(R.id.txtResult);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtBetAmount = itemView.findViewById(R.id.txtBetAmount);
            txtWinAmount = itemView.findViewById(R.id.txtWinAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTransactionId = itemView.findViewById(R.id.txtTransactionId);
        }
    }
}
