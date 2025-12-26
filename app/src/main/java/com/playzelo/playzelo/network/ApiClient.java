package com.playzelo.playzelo.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.playzelo.fun/api/v1/"; // ✅ Correct Base URL
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // ✅ Logging Interceptor (debugging ke liye)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // ✅ OkHttpClient with timeout
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // connection timeout
                    .readTimeout(30, TimeUnit.SECONDS)    // server read timeout
                    .writeTimeout(30, TimeUnit.SECONDS)   // data write timeout
                    .addInterceptor(logging)              // log all requests/responses
                    .build();

            // ✅ Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
