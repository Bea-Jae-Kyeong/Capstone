package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.models.Search;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private BottomViewPagerAdapter pagerAdapter;
    private Toolbar up_toolbar;
    TextView toolbar_title;
    HomeFragment homeFragment;
    ArticleFragment articleFragment;
    TwitterFragment twitterFragment;
    SettingFragment settingFragment;
    TotalFragment totalFragment;
    MenuItem prevMenuItem;

    MenuItem mSearch;
    RecyclerView searchList;
    SearchAdapter searchAdapter;
    List<KeywordModel> searchitems;
    private static Retrofit retrofit = null;
    private Call<List<KeywordModel>> call;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        long startTime = System.currentTimeMillis();


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        up_toolbar = (Toolbar) findViewById(R.id.up_toolbar);
        setSupportActionBar(up_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.d("action_home","action home");
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.action_total:
                        Log.d("action_total","action total");
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.action_article:
                        Log.d("action_article","action article");
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.action_twitter:
                        Log.d("action_twitter","twitter good");
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.action_dots:
                        Log.d("action_dots","action dots");
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.CONSUMER_KEY), getResources().getString(R.string.CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
        Twitter.initialize(config);

//        searchList = (RecyclerView) findViewById(R.id.search_recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//it should be Vertical only
//        searchList.setLayoutManager(linearLayoutManager);

        long endTime = System.currentTimeMillis();
        Log.d("Time", "elapsedTime for MainActivity : " + (endTime - startTime));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.basic_toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.search_icon){
            Intent searchResultsIntent = new Intent(this, SearchResultsActivity.class);
            startActivity(searchResultsIntent);
//            mSearch.expandActionView();
            return true;
        }
        else{
//            mSearch.collapseActionView();
            return false;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new BottomViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        totalFragment = new TotalFragment();
        articleFragment = new ArticleFragment();
        twitterFragment = new TwitterFragment();
        settingFragment = new SettingFragment();
        pagerAdapter.addFragment(homeFragment);
        pagerAdapter.addFragment(totalFragment);
        pagerAdapter.addFragment(articleFragment);
        pagerAdapter.addFragment(twitterFragment);
        pagerAdapter.addFragment(settingFragment);

        viewPager.setAdapter(pagerAdapter);
    }

    private void loadKeyword(String full_name){
        searchitems = new ArrayList<>();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FetchArticleData.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

//         특정 key로 디비에서 데이터 가져올떄
        call = articleApiService.getKeyword(full_name);
//
//        homeList.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);

        APIHelper.enqueueWithRetry(call, 3, new Callback<List<KeywordModel>>() {
            @Override
            public void onResponse(Call<List<KeywordModel>> call, Response<List<KeywordModel>> response) {
                if(response.isSuccessful()){
//                    progressBar.setVisibility(View.GONE);
                    searchitems = response.body();
                    searchAdapter = new SearchAdapter(context,searchitems);
//                    searchList.setVisibility(View.VISIBLE);
                    searchList.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<KeywordModel>> call, Throwable t) {
//                homeList.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
                Log.d("getData2",t.getLocalizedMessage());
            }
        });
    }
    //    private void connectAndGetApiData() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }


//        ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

        // 특정 table데이터 모두 가져올때
//        Call<List<ArticleModel>> call = articleApiService.getData();

        // 특정 key로 디비에서 데이터 가져올떄
//        String title= "title2";
//        Call<List<ArticleModel>> call = articleApiService.getData_with_title(title);

        // Post 할때 쓰는 거
//        HashMap<String,Object> input = new HashMap<>();
//        input.put("title","title3");
//        input.put("subtitle","subtitle3");
//        input.put("content","content3");
//        input.put("date","2019-06-06");
//        Call<List<ArticleModel>> call = articleApiService.postData(input);


//        call.enqueue(new Callback<List<ArticleModel>>() {
//            @Override
//            public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
//                if(response.isSuccessful()){
//                    List<ArticleModel> articles = response.body();
//
//                    if(articles != null){
//                        result = articles.get(0).getTitle();
//                        textView1.setText("Title: "+ articles.get(0).getTitle() + "\n" + "SubTitle: " + articles.get(0).getSource() + "\n" + "Content: "+ articles.get(0).getImgSrc());
//                        Log.d("getData",articles.get(0).getTitle());
//                        Log.d("getData",articles.get(0).getSource());
//                        Log.d("getData",articles.get(0).getImgSrc());
//                        Log.d("getData",articles.get(0).getDate());
//                    }
//                    else{
//                        System.out.println("fail");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
//                Log.e("Test",t.getLocalizedMessage());
//            }
//        });
//    }
}
