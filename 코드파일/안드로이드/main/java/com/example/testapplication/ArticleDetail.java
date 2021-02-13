package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleDetail extends AppCompatActivity {
    private Context context;
    private int news_id;
    private Toolbar detail_toolbar;
    private TextView tv_title;
    private ImageView imageView;
    private TextView tv_source;
    private TextView tv_date;
    private TextView tv_content;
    private Intent intent;
    private Retrofit retrofit = null;
    private Call<List<ArticleModel2>> call;
    private ProgressBar progressBar;
    private ConstraintLayout detail_container;
    List<ArticleModel2> articlecontents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_view);

        long startTime = System.currentTimeMillis();

        context = getApplicationContext();
        intent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.progressbar_detail);
        detail_container = (ConstraintLayout) findViewById(R.id.detail_container);
        detail_toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        tv_title = (TextView) findViewById(R.id.detail_title);
        imageView = (ImageView) findViewById(R.id.detail_img);
        tv_source = (TextView) findViewById(R.id.detail_source);
        tv_date = (TextView) findViewById(R.id.detail_date);
        tv_content = (TextView) findViewById(R.id.detail_content) ;

        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ↓툴바의 홈버튼의 이미지를 변경(기본 이미지는 뒤로가기 화살표)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        detail_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        news_id = intent.getExtras().getInt("news_id");
        Log.d("id_output",Integer.toString(news_id));

        articlecontents = new ArrayList<>();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FetchArticleData.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

        call = articleApiService.getArticle_with_id(news_id,SaveSharedPreference.getUserID(context));
        progressBar.setVisibility(View.VISIBLE);
        detail_container.setVisibility(View.GONE);

        //기사내용 및 기존클릭수 가져오기
        APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel2>>() {
            @Override
            public void onResponse(Call<List<ArticleModel2>> call, Response<List<ArticleModel2>> response) {
                if(response.isSuccessful()){
                    articlecontents = response.body();
                    progressBar.setVisibility(View.GONE);
                    detail_container.setVisibility(View.VISIBLE);

                    // 기사내용들 세팅
                    tv_title.setText(articlecontents.get(0).getTitle());
                    Glide.with(context).load(articlecontents.get(0).getImgSrc()).into(imageView);
                    tv_source.setText(articlecontents.get(0).getSource());
                    String temp_date[] = articlecontents.get(0).getDate().split("T");
                    String temp_time[] = temp_date[1].split(":");
                    String format_date = temp_date[0] + " " + temp_time[0] + ":" + temp_time[1];
                    tv_date.setText(format_date);
                    tv_content.setText(articlecontents.get(0).getContents());
                }
            }

            @Override
            public void onFailure(Call<List<ArticleModel2>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                detail_container.setVisibility(View.VISIBLE);
                Log.d("getData2",t.getLocalizedMessage());
            }
        });

        long endTime = System.currentTimeMillis();
        Log.d("Time", "elapsedTime for ArticleDetail : " + (endTime - startTime));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.read_article_btn){
            Intent intent2 = new Intent(context, ArticleWebView.class);
            intent2.putExtra("source",articlecontents.get(0).getSource());
            intent2.putExtra("url",articlecontents.get(0).getUrl());
            startActivity(intent2);
            return true;
        }
        else{
            return false;
        }
    }

}
