package com.playzelo.playzelo.models;


import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("chips")
    private double chips;

    @SerializedName("balance")
    private double balance;

    @SerializedName("totalBet")
    private double totalBet;

    @SerializedName("totalWin")
    private double totalWin;

    @SerializedName("netPL")
    private double netPL;

    // ✅ REQUIRED GETTERS

    public double getChips() {
        return chips;
    }

    public double getBalance() {
        return balance;
    }

    public double getTotalBet() {
        return totalBet;
    }

    public double getTotalWin() {
        return totalWin;
    }

    public double getNetPL() {
        return netPL;
    }
}
