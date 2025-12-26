package com.playzelo.playzelo;

public class GameHistoryItem {

    private double betAmount;
    private double winAmount;
    private String status;
    private String createdAt;
    private Game game;

    public double getBetAmount() { return betAmount; }
    public double getWinAmount() { return winAmount; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public Game getGame() { return game; }

    public static class Game {
        private String name;
        private String icon;

        public String getName() { return name; }
        public String getIcon() { return icon; }
    }
}

