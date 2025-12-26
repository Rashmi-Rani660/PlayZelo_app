package com.playzelo.playzelo.network;

import com.playzelo.playzelo.models.GameHistoryResponse;
import com.playzelo.playzelo.models.LoginResponse;
import com.playzelo.playzelo.models.ProfileResponse;
import com.playzelo.playzelo.models.RealDepositResponse;
import com.playzelo.playzelo.models.SignUpResponse;
import com.playzelo.playzelo.models.TransactionResponse;
import com.playzelo.playzelo.models.WalletResponse;
import com.playzelo.playzelo.models.WinnersResponse;
import com.playzelo.playzelo.models.WithdrawResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Header;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // ✅ Login API
    @Headers("Content-Type: application/json")
    @POST("user/login")
    Call<LoginResponse> loginUser(@Body Map<String, String> body);

    // ✅ Register API
    @Headers("Content-Type: application/json")
    @POST("user/register")
    Call<SignUpResponse> registerUser(@Body Map<String, String> body);

    // ✅ Profile API (GET request with token in header)
    @Headers("Content-Type: application/json")
    @GET("user/profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

    // ✅ Wallet Balance API (GET)
    @Headers("Content-Type: application/json")
    @GET("wallet/me")
    Call<WalletResponse> getWalletBalance(@Header("Authorization") String token);

    // ✅ Deposit Money API (POST)
    @Headers("Content-Type: application/json")
    @POST("wallet/deposit")
    Call<WalletResponse> depositMoney(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );

    //    ✅ Withdraw Money API (POST)
    @POST("payment/withdraw-request")
    Call<WithdrawResponse> withdrawRequest(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );



    // ✅ Real Deposit API
    @Headers("Content-Type: application/json")
    @POST("payment/real-deposit")
    Call<RealDepositResponse> realDeposit(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );

    // ✅ Recent Winners API
    @GET("dashboard/recent-winners")
    Call<WinnersResponse> getRecentWinners();

    @GET("transaction")
    Call<TransactionResponse> getTransactions(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("user/history")
    Call<GameHistoryResponse> getGameHistory(
            @Header("Authorization") String token,
            @Query("page") int page
    );



}

