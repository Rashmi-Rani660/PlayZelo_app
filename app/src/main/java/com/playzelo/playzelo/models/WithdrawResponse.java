package com.playzelo.playzelo.models;

public class WithdrawResponse {

    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private String transactionId;
        private String upiId;
        private double amount;
        private double balanceBefore;
        private double balanceAfter;

        public String getTransactionId() {
            return transactionId;
        }

        public String getUpiId() {
            return upiId;
        }

        public double getAmount() {
            return amount;
        }

        public double getBalanceBefore() {
            return balanceBefore;
        }

        public double getBalanceAfter() {
            return balanceAfter;
        }
    }
}

