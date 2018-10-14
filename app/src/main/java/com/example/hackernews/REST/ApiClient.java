package com.example.hackernews.REST;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static NetworkCall REST_CLIENT;

    static {
        setUpRestClient();
    }

    private static void setUpRestClient(){

        OkHttpClient httpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hacker-news.firebaseio.com/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = retrofit.create(NetworkCall.class);
    }
}

