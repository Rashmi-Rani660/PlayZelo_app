package com.playzelo.playzelo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.CookieManager;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";

    // Auth keys
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    // Profile keys
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "dob";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    // Login flag
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Wallet
    private static final String KEY_WALLET_BALANCE = "wallet_balance";

    private static SharedPrefManager mInstance;
    private final SharedPreferences sharedPreferences;

    private SharedPrefManager(Context context) {
        sharedPreferences =
                context.getApplicationContext()
                        .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    // ------------------- Singleton -------------------
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // ------------------- Auth -------------------
    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void saveUserId(String userId) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void saveUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // ------------------- Profile -------------------
    public void saveEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void saveDob(String dob) {
        sharedPreferences.edit().putString(KEY_DOB, dob).apply();
    }

    public String getDob() {
        return sharedPreferences.getString(KEY_DOB, null);
    }

    public void saveCountry(String country) {
        sharedPreferences.edit().putString(KEY_COUNTRY, country).apply();
    }

    public String getCountry() {
        return sharedPreferences.getString(KEY_COUNTRY, null);
    }

    public void saveCity(String city) {
        sharedPreferences.edit().putString(KEY_CITY, city).apply();
    }

    public String getCity() {
        return sharedPreferences.getString(KEY_CITY, null);
    }

    public void saveAddress(String address) {
        sharedPreferences.edit().putString(KEY_ADDRESS, address).apply();
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, null);
    }

    public void saveProfileImage(String imageUrl) {
        sharedPreferences.edit().putString(KEY_PROFILE_IMAGE, imageUrl).apply();
    }

    public String getProfileImage() {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE, "");
    }

    // ------------------- Wallet Balance -------------------
    public void saveWalletBalance(double balance) {
        sharedPreferences.edit()
                .putString(KEY_WALLET_BALANCE, String.valueOf(balance))
                .apply();
    }

    public double getWalletBalance() {
        try {
            return Double.parseDouble(
                    sharedPreferences.getString(KEY_WALLET_BALANCE, "0")
            );
        } catch (Exception e) {
            return 0;
        }
    }

    // ------------------- Login Flag -------------------
    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, loggedIn)
                .apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // ------------------- Utility -------------------
    public void logout() {
        // Clear SharedPreferences
        sharedPreferences.edit().clear().apply();

        // Clear WebView cookies
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        // Reset singleton instance
        mInstance = null;
    }
}
