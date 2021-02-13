package com.example.testapplication;

import android.content.Context;
import android.os.Bundle;
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

public class TotalFragment extends Fragment {
    RecyclerView totalList;
    ArticleAdapterTotal adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<ArticleModel> articleitems;
    Context context;
    ProgressBar progressBar;

    public static Retrofit retrofit = null;
    public Call<List<ArticleModel>> call;


    public TotalFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_article,container,false);
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
        totalList = view.findViewById(R.id.total_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//it should be Vertical only
        totalList.setLayoutManager(linearLayoutManager);
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
        call = articleApiService.getArticle_latest();

        totalList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
            @Override
            public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    articleitems = response.body();
                    articleitems.add(new ArticleModel());
                    adapter = new ArticleAdapterTotal(getActivity(),articleitems,R.layout.fragment_article);
                    totalList.setVisibility(View.VISIBLE);
                    totalList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                totalList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.d("getData2",t.getLocalizedMessage());
            }
        });

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

                call = articleApiService.getArticle_latest();

                APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
                    @Override
                    public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                        if(response.isSuccessful()){
                            articleitems = response.body();
                            adapter.setItemList(articleitems);
                            articleitems.add(new ArticleModel());
                            Toast.makeText(getContext(), "total refreshed.", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                        Log.d("getData2",t.getLocalizedMessage());
                    }
                });


            }
        });
    }

}
