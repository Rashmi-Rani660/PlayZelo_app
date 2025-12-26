package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private User data; // maps to the "data" object in JSON

    // Getters
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public User getData() {
        return data;
    }

    // Optional: Override toString() for easy logging
    @Override
    public String toString() {
        return "ProfileResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
