package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;
import com.playzelo.playzelo.Winner;

import java.util.List;

public class WinnersResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Winner> data;

    // ===== GETTERS =====

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Winner> getData() {
        return data;
    }
}
