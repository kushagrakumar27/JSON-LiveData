package com.example.hackernews.REST;

import com.example.hackernews.Model.ArticleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkCall {

    @GET("v0/topstories.json?print=pretty")
    Call<List<Integer>> getTopStories();

    @GET("v0/item/{articleID}.json?print=pretty")
    Call<ArticleResponse> getArticle(@Path("articleID") int id);
}
