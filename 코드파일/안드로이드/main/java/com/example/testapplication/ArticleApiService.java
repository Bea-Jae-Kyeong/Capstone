package com.example.testapplication;

import java.security.Key;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApiService {
    @GET("api/article_get_with_keyword_no_date")
    Call<List<ArticleModel>> getArticle_with_keyword_no_date(@Query("kwd") String keyword);

    @GET("api/article_get_with_keyword")
    Call<List<ArticleModel>> getArticle_with_keyword(@Query("kwd") String keyword, @Query("date") String date);

    @GET("api/article_get_with_keyword_relevance")
    Call<List<ArticleModel>> getArticle_with_keyword2(@Query("kwd") String keyword, @Query("date") String date);

    @GET("api/article_get_with_id")
    Call<List<ArticleModel2>> getArticle_with_id(@Query("news_id") int id, @Query("user_id") String user_id);

    @GET("api/save_userdata")
    Call<List<UserModel>> saveUserData(@Query("user_id") String user_id, @Query("keyword1") String keyword1,@Query("keyword2") String keyword2, @Query("keyword3") String keyword3);

    @GET("api/article_get_latest")
    Call<List<ArticleModel>> getArticle_latest();

    @GET("api/keyword_get")
    Call<List<KeywordModel>> getKeyword(@Query("full_name") String full_name);

    @GET("api/article_recommend_get")
    Call<List<ArticleModel>> getArticle_recommend(@Query("user_id") String user_id);
}
