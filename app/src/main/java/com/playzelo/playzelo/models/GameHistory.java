package com.playzelo.playzelo.models;

public class GameHistory {

    private String gameName;
    private String date;
    private String amount;
    private String status; // WIN / LOSS / REFUND etc.

    public GameHistory(String gameName, String date, String amount, String status) {
        this.gameName = gameName;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public String getGameName() {
        return gameName;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
