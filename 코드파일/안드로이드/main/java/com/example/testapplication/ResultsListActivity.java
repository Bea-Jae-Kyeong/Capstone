package com.example.testapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class ResultsListActivity extends AppCompatActivity {

    RecyclerView articleList;
    Spinner spinner;
    ImageButton date_btn;
    ArticleAdapterNoHeader adapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    List<ArticleModel> articleitems;
    Context context;
    ProgressBar progressBar;
    TextView empty_tv;
    private String kwd;
    TextView toolbar_title;
    Toolbar search_toolbar;

    private static Retrofit retrofit = null;
    private Call<List<ArticleModel>> call;
    private String date;

    public int year;
    public int month;
    public int day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        context = getApplicationContext();
        Intent intent = getIntent();
        kwd = intent.getExtras().getString("keyword");

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        this.year = today.year;
        this.month = today.month;
        this.day = today.monthDay;

        progressBar = (ProgressBar) findViewById(R.id.progressbar2);
        empty_tv = (TextView) findViewById(R.id.empty_tv2);

        search_toolbar = (Toolbar) findViewById(R.id.up_toolbar2);
        setSupportActionBar(search_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ↓툴바의 홈버튼의 이미지를 변경(기본 이미지는 뒤로가기 화살표)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        search_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = (TextView) findViewById(R.id.toolbar_title2);
        toolbar_title.setText(kwd);

        setUpSwipeRefreshLayout();
        setUpRecyclerView();
//        setSpinnerView();
//        setDateBtn();
        loadNewArticle();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewArticle();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("year",year);
        outState.putInt("month",month);
        outState.putInt("day",day);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            year = savedInstanceState.getInt("year");
            month = savedInstanceState.getInt("month");
            day = savedInstanceState.getInt("day");
        }
    }

    private void setUpRecyclerView() {
        articleList = findViewById(R.id.search_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//it should be Vertical only
        articleList.setLayoutManager(linearLayoutManager);
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

        date = Integer.toString(year) + '-' + (month+1) + '-' + day;

        call = articleApiService.getArticle_with_keyword_no_date(kwd);

        articleList.setVisibility(View.GONE);
        empty_tv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
            @Override
            public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                if(response.isSuccessful()){
                    articleList.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    articleitems = response.body();
                    if (articleitems.size() == 0){
                        empty_tv.setVisibility(View.VISIBLE);
                    }
                    adapter = new ArticleAdapterNoHeader(context,articleitems,R.layout.fragment_article);
                    articleList.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                articleList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.d("getData2",t.getLocalizedMessage());
            }
        });

    }

    private void setUpSwipeRefreshLayout() {
        //find the id of swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.user_swipe_refresh_layout2);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

    }

    //implement refresh listener
    public SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
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

            date = Integer.toString(year) + '-' + (month+1) + '-' + day;

            call = articleApiService.getArticle_with_keyword_no_date(kwd);

            APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
                @Override
                public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                    if(response.isSuccessful()){
                        articleitems = response.body();
                        adapter.setItemList(articleitems);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                    Log.d("getData2",t.getLocalizedMessage());
                }
            });
        }
    };

}
