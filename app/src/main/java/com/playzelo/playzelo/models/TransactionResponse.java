package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("transactions") // ✅ Correct key
    private List<Transaction> transactions;

    @SerializedName("page")
    private int currentPage;

    @SerializedName("totalPages")
    private int totalPages;

    // ===== GETTERS =====
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
