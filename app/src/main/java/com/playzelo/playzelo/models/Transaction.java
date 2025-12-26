package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("_id")
    private String id;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("userId")
    private User userId; // ✅ keep as User object

    @SerializedName("type")
    private String type;

    @SerializedName("amount")
    private double amount;

    @SerializedName("balanceBefore")
    private double balanceBefore;

    @SerializedName("balanceAfter")
    private double balanceAfter;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    public String getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public User getUserId() { return userId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceBefore() { return balanceBefore; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}
