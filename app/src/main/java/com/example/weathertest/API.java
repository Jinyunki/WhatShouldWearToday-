package com.example.weathertest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    public static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/";
    private static HttpLoggingInterceptor logging;
    private static OkHttpClient httpClient;
    private static Retrofit retrofit;

    private static HttpLoggingInterceptor getLogging() {
        if (logging == null) {
            logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return logging;
    }

    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(getLogging()).build();
        }

        return httpClient;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build();
        }

        return retrofit;
    }
}
