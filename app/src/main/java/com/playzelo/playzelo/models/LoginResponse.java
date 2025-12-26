package com.playzelo.playzelo.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private User data;

    @SerializedName("token")
    private String token;

    public String getToken() { return token; }


    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public User getData() { return data; }

    // Inner User class (matches "data" object from API)
    public static class User {
        @SerializedName("_id")
        private String id;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("dob")
        private String dob;

        @SerializedName("country")
        private String country;

        @SerializedName("address")
        private String address;

        @SerializedName("city")
        private String city;

        @SerializedName("profileImage")
        private String profileImage;

        @SerializedName("blocked")
        private boolean blocked;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("updatedAt")
        private String updatedAt;

        @SerializedName("__v")
        private int v;

        // Getters
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getDob() { return dob; }
        public String getCountry() { return country; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getProfileImage() { return profileImage; }
        public boolean isBlocked() { return blocked; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
        public int getV() { return v; }
    }
}
