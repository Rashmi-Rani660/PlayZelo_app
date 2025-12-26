package com.playzelo.playzelo;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


public class JackpotWebInterface {

    private final String userId;
    private final String authToken;
    private final String username;
    private final Context context;

    public JackpotWebInterface(String userId, String authToken, String username, Context context) {
        this.userId = userId != null ? userId : "";
        this.authToken = authToken != null ? authToken : "";
        this.username = username != null ? username : "";
        this.context = context;
    }

    /**
     * JS can call this to get userId
     */
    @JavascriptInterface
    public String getUserId() {
        return userId;
    }

    /**
     * JS can call this to get authToken
     */
    @JavascriptInterface
    public String getAuthToken() {
        return authToken;
    }

    /**
     * JS can call this to get username
     */
    @JavascriptInterface
    public String getUsername() {
        return username;
    }

    /**
     * Optional: show toast from JS (for debugging / notifications)
     */
    @JavascriptInterface
    public void showToast(String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Optional: handle logout from JS
     */
    @JavascriptInterface
    public void logout() {
        // You can implement app logout logic here
        // Example: clear SharedPrefManager, redirect to login
        if (context != null) {
            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show();
            // TODO: implement logout logic
        }
    }
}
