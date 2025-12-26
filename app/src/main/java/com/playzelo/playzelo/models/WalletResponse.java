package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;

public class WalletResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    // ===== GETTERS =====

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    // ================== INNER DATA CLASS ==================

    public static class Data {

        @SerializedName("_id")
        private String id;

        @SerializedName("userId")
        private String userId;   // ✅ STRING (not object)

        @SerializedName("chips")
        private String chips;    // ✅ STRING

        @SerializedName("balance")
        private String balance;  // ✅ STRING

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("updatedAt")
        private String updatedAt;

        // ===== GETTERS =====

        public String getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getChips() {
            return chips;
        }

        public String getBalance() {
            return balance;
        }


        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}
