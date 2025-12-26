package com.playzelo.playzelo.models;

import com.playzelo.playzelo.GameHistoryItem;

import java.util.List;

public class GameHistoryResponse {
    private boolean success;
    private List<GameHistoryItem> data;

    public boolean isSuccess() { return success; }
    public List<GameHistoryItem> getData() { return data; }
}
