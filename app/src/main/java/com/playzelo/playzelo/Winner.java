package com.playzelo.playzelo;

public class Winner {

    private String user;
    private String id;
    private String game;
    private String amount;
    private String timeAgo;

    private String rank; // UI ke liye

    public String getName() {
        return user;
    }

    public String getGame() {
        return game;
    }

    public String getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }


    public String getTimeAgo() {
        return timeAgo;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
