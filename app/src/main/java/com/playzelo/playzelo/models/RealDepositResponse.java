package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;

public class RealDepositResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DepositData data;

    // ===== GETTERS =====
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public DepositData getData() {
        return data;
    }

    // ================= INNER DATA CLASS =================
    public static class DepositData {

        @SerializedName("_id")
        private String id;

        @SerializedName("userId")
        private String userId;

        @SerializedName("amount")
        private int amount;

        @SerializedName("upiId")
        private String upiId;

        @SerializedName("utrNumber")
        private String utrNumber;

        @SerializedName("screenshotUrl")
        private String screenshotUrl;

        @SerializedName("transactionId")
        private String transactionId;

        @SerializedName("status")
        private String status;

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

        public int getAmount() {
            return amount;
        }

        public String getUpiId() {
            return upiId;
        }

        public String getUtrNumber() {
            return utrNumber;
        }

        public String getScreenshotUrl() {
            return screenshotUrl;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getStatus() {
            return status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}
