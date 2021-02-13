package com.example.testapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    RecyclerView homeList;
    ArticleAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<ArticleModel> articleitems;
    Context context;
    ProgressBar progressBar;

    FetchArticleData fetchArticleData;

    private static Retrofit retrofit = null;
    private Call<List<ArticleModel>> call;

    private String date;

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        context = view.getContext();
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        loadNewArticle();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewArticle();
    }

    private void setUpRecyclerView(@NonNull View view) {
        homeList = view.findViewById(R.id.home_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//it should be Vertical only
        homeList.setLayoutManager(linearLayoutManager);
    }

    private void loadNewArticle(){
        articleitems = new ArrayList<>();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FetchArticleData.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

//         특정 key로 디비에서 데이터 가져올떄
//        call = articleApiService.getArticle_with_keyword("Arsenal","2019-11-17");
//
        // 나중에 이거로 바꾸기
        call = articleApiService.getArticle_recommend(SaveSharedPreference.getUserID(context));

        homeList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
            @Override
            public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    articleitems = response.body();
                    articleitems.add(new ArticleModel());
//                    Log.d("getData",Integer.toString(articleitems.get(0).getId()));
                    adapter = new ArticleAdapter(getActivity(),articleitems,R.layout.fragment_article);
                    homeList.setVisibility(View.VISIBLE);
                    homeList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                homeList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.d("getData2",t.getLocalizedMessage());
            }
        });

//        if (articleitems == null){
//            articleitems = new ArrayList<>();
//            ArticleModel[] items = new ArticleModel[15];
//            for (int i=0;i<15;i++){
//                ArticleModel temp_article = new ArticleModel();
//                temp_article.setSource("SkySports");
//                temp_article.setTitle("Carabao Cup quarter-finals: Oxford United vs Manchester City and Aston Villa vs Liverpool");
//                temp_article.setDate("2019-01-01");
//                temp_article.setImgSrc("https://e0.365dm.com/19/11/768x432/skysports-rhian-brewster-liverpool_4826389.jpg?20191104160948");
//                temp_article.setKeyword1("#Liverpool");
//                temp_article.setKeyword2("#Aston Villa");
//                temp_article.setKeyword3("#Man City");
//                items[i] = temp_article;
//                articleitems.add(items[i]);
//            }
//        }
//
//        adapter = new ArticleAdapter(getActivity(),articleitems,R.layout.fragment_article);
//        homeList.setAdapter(adapter);
    }

    private void setUpSwipeRefreshLayout(View view) {

        //find the id of swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.user_swipe_refresh_layout2);

        //implement refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //return if adapter is null
                if (adapter == null)
                    return;

                //make set refreshing true
                swipeRefreshLayout.setRefreshing(true);

                articleitems = new ArrayList<>();

                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(FetchArticleData.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
                final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

//                call = articleApiService.getArticle_with_keyword("Arsenal","2019-11-17");
                call = articleApiService.getArticle_recommend(SaveSharedPreference.getUserID(context));

                APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
                    @Override
                    public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                        if(response.isSuccessful()){
                            articleitems = response.body();
                            adapter.setItemList(articleitems);
                            articleitems.add(new ArticleModel());
//                            Toast.makeText(getContext(), "home refreshed.", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                        Log.d("getData2",t.getLocalizedMessage());
                    }
                });

//                ArticleModel temp_article = new ArticleModel();
//                temp_article.setSource("SkySports");
//                temp_article.setTitle("Carabao Cup quarter-finals: Oxford United vs Manchester City and Aston Villa vs Liverpool");
//                temp_article.setDate("2019-01-01");
//                temp_article.setImgSrc("https://e1.365dm.com/19/11/768x432/skysports-jack-grealish-england_4827027.jpg?20191105103217");
//                temp_article.setKeyword1("#Liverpool");
//                temp_article.setKeyword2("#Aston Villa");
//                temp_article.setKeyword3("#Man City");
//                articleitems.add(temp_article);
//                adapter.setItemList(articleitems);
//                Toast.makeText(getContext(), "tab1-1 refreshed.", Toast.LENGTH_SHORT).show();
//
//                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

}
