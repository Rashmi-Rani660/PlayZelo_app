package com.playzelo.playzelo;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.playzelo.playzelo.utils.SharedPrefManager;

import org.json.JSONObject;

public class LudoWebInterface {

    private final Context context;

    public LudoWebInterface(Context context) {
        this.context = context;
    }

    // 🔥 JS ko ek hi baar me saara data bhejna (wallet/chips from SharedPref)
    @JavascriptInterface
    public String getUserData() {
        try {
            SharedPrefManager pref = SharedPrefManager.getInstance(context);
            JSONObject obj = new JSONObject();

            obj.put("userId", pref.getUserId() != null ? pref.getUserId() : "");
            obj.put("token", pref.getToken() != null ? pref.getToken() : "");
            obj.put("username", pref.getUsername() != null ? pref.getUsername() : "");
            obj.put("wallet", pref.getWalletBalance()); // double value from SharedPref

            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    // 🔄 Web → Android : wallet/chips update
    @JavascriptInterface
    public void onWalletUpdated(String newBalance) {
        if (newBalance == null || newBalance.isEmpty()) return;

        try {
            double balance = Double.parseDouble(newBalance);
            SharedPrefManager.getInstance(context).saveWalletBalance(balance);

            // Android log instead of System.out
            android.util.Log.d("LUDO_WEB", "Wallet updated from Web: " + balance);
        } catch (NumberFormatException e) {
            android.util.Log.e("LUDO_WEB", "Invalid wallet value from Web: " + newBalance, e);
        } catch (Exception e) {
            android.util.Log.e("LUDO_WEB", "Error updating wallet from Web", e);
        }
    }
}
