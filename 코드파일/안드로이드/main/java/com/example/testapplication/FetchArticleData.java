package com.example.testapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewDebug;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class FetchArticleData {
    public static final String BASE_URL = "http://115.126.237.33:8090/";
    public static Retrofit retrofit = null;
    public List<ArticleModel> articleModels;
    public String keyword;
        public String date;

        public Call<List<ArticleModel>> call;

        FetchArticleData(String keyword,String date){
            this.keyword = keyword;
            this.date = date;
        }

        abstract class DefaultCallback<ArticleModel> implements Callback<List<ArticleModel>> {

        public void onResponse(retrofit2.Response<List<ArticleModel>> call, retrofit2.Response<List<ArticleModel>> response) {
            if (response.isSuccessful()) {
                onSuccess(response.body(), response.code());
            }
        }

        public abstract void onSuccess(final List<ArticleModel> response, int code);

    }


    public void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

        // 특정 table데이터 모두 가져올때
//        Call<List<ArticleModel>> call = articleApiService.getData();

//         특정 key로 디비에서 데이터 가져올떄
        call = articleApiService.getArticle_with_keyword(this.keyword,this.date);

        // Post 할때 쓰는 거
    //        HashMap<String,Object> input = new HashMap<>();
    //        input.put("title","title3");
    //        input.put("subtitle","subtitle3");
    //        input.put("content","content3");
    //        input.put("date","2019-06-06");
    //        Call<List<ArticleModel>> call = articleApiService.postData(input);


        call.enqueue(new Callback<List<ArticleModel>>() {
            @Override
            public void onResponse( Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                if(response.isSuccessful()){
                    articleModels = response.body();
                    if(articleModels != null){
  //                      Callback.onSuccess(response, response.code());
//                        Callback.onResponse(call, response);
                        Log.d("getData",articleModels.get(1).getTitle());
                        Log.d("getData",articleModels.get(0).getSource());
                        Log.d("getData",articleModels.get(0).getImgSrc());
                        Log.d("getData",articleModels.get(0).getDate());
                    }
                    else{
                        Log.d("getData","fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                Log.e("Test2",t.getLocalizedMessage());
            }
        });

    }

    public List<ArticleModel> getArticleLists(){

        RetrofitBackground retrofitBackground = new RetrofitBackground();
        retrofitBackground.execute();
//        try{
//            Thread.sleep(3000);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        if(articleModels != null){
            Log.d("getData",articleModels.get(0).getTitle());
            Log.d("getData",articleModels.get(0).getSource());
            Log.d("getData",articleModels.get(0).getImgSrc());
            Log.d("getData",articleModels.get(0).getDate());
        }
        else{
            Log.d("getData","fail");
        }
        return this.articleModels;
    }


    private class RetrofitBackground extends AsyncTask<Void,Void,List<ArticleModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }

        @Override
        protected List<ArticleModel> doInBackground(Void... params) {

            final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

            call = articleApiService.getArticle_with_keyword(keyword,"2019-11-15");

            try {
                Response<List<ArticleModel>> response = call.execute();
                List<ArticleModel> am = response.body();

                return am;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ArticleModel> am) {
            super.onPostExecute(am);
            articleModels = am;
        }
    }

}

