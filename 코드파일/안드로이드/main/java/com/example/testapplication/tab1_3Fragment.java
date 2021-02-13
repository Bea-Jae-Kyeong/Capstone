package com.example.testapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tab1_3Fragment extends Fragment {
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

    public static Retrofit retrofit = null;
    public Call<List<ArticleModel>> call;
    private String date;

    public int year;
    public int month;
    public int day;

    public tab1_3Fragment(String kwd){
        this.kwd = kwd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_frag_article,container,false);
        context = view.getContext();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        this.year = today.year;
        this.month = today.month;
        this.day = today.monthDay;

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        empty_tv = (TextView)view.findViewById(R.id.empty_tv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        setSpinnerView(view);
        setDateBtn(view);
        loadNewArticle();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewArticle();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("year",year);
//        outState.putInt("month",month);
//        outState.putInt("day",day);
//        outState.putInt("spinner_item",spinner.getSelectedItemPosition());
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(savedInstanceState != null){
//            year = savedInstanceState.getInt("year");
//            month = savedInstanceState.getInt("month");
//            day = savedInstanceState.getInt("day");
//            spinner.setSelection(savedInstanceState.getInt("spinner_item"));
//        }
//    }

    private void setUpRecyclerView(@NonNull View view) {
        articleList = view.findViewById(R.id.article_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);//it should be Vertical only
        articleList.setLayoutManager(linearLayoutManager);
    }

    private void loadNewArticle(){
        articleitems = new ArrayList<>();

//        date = Integer.toString(ArticleFragment.year) + '-' + Integer.toString(ArticleFragment.month) + '-' + Integer.toString(ArticleFragment.day);
//        date = "2019-11-15";
//        fetchArticleData = new FetchArticleData("Arsenal",date);
//        fetchArticleData.connectAndGetApiData();
//        articleitems = fetchArticleData.getArticleLists();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FetchArticleData.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

        date = Integer.toString(year) + '-' + (month+1) + '-' + day;

        call = articleApiService.getArticle_with_keyword(kwd,date);

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
                    adapter = new ArticleAdapterNoHeader(getActivity(),articleitems,R.layout.fragment_article);
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



//        if (articleitems == null){
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

//        adapter = new ArticleAdapterNoHeader(getActivity(),articleitems,R.layout.fragment_article);
//        articleList.setAdapter(adapter);


    }

    private void setUpSwipeRefreshLayout(View view) {
        //find the id of swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.user_swipe_refresh_layout2);
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
//                Log.d("getData",date);
            call = articleApiService.getArticle_with_keyword(kwd,date);

//            final ProgressDialog progressDialog;
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMax(100);
////            progressDialog.setMessage("Its loading....");
////            progressDialog.setTitle("ProgressDialog bar");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            // show it
//            progressDialog.show();

            APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
                @Override
                public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                    if(response.isSuccessful()){
//                        progressDialog.dismiss();

                        articleitems = response.body();
                        adapter.setItemList(articleitems);
//                        Toast.makeText(getContext(), "tab1-1 refreshed.", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
//                    progressDialog.dismiss();
                    Log.d("getData2",t.getLocalizedMessage());
                }
            });

//                date = Integer.toString(ArticleFragment.year) + '-' + Integer.toString(ArticleFragment.month) + '-' + Integer.toString(ArticleFragment.day);
//                fetchArticleData = new FetchArticleData("Arsenal",date);
//                articleitems = fetchArticleData.getArticleLists();

//                if(articleitems == null){
//                    ArticleModel temp_article = new ArticleModel();
//                    temp_article.setSource("SkySports");
//                    temp_article.setTitle("Carabao Cup quarter-finals: Oxford United vs Manchester City and Aston Villa vs Liverpool");
//                    temp_article.setDate("2019-01-01");
//                    temp_article.setImgSrc("https://e1.365dm.com/19/11/768x432/skysports-jack-grealish-england_4827027.jpg?20191105103217");
//                    temp_article.setKeyword1("#Liverpool");
//                    temp_article.setKeyword2("#Aston Villa");
//                    temp_article.setKeyword3("#Man City");
//                    articleitems.add(temp_article);
//                }
//                adapter.setItemList(articleitems);
//                Toast.makeText(getContext(), "tab1-1 refreshed.", Toast.LENGTH_SHORT).show();
//
//                swipeRefreshLayout.setRefreshing(false);

        }
    };

    private void setSpinnerView(View view){
        spinner = view.findViewById(R.id.spinner);

        final ArrayList<String> list = new ArrayList<>();
        list.add("최신순");
        list.add("관련도순");

        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                articleitems = new ArrayList<>();
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(FetchArticleData.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
                final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

                date = Integer.toString(year) + '-' + (month+1) + '-' + day;
                if(position == 0){
                    call = articleApiService.getArticle_with_keyword(kwd,date);
                }
                else{
                    call = articleApiService.getArticle_with_keyword2(kwd,date);
                }

                articleList.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                empty_tv.setVisibility(View.GONE);

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

                            articleitems = response.body();
                            adapter.setItemList(articleitems);
                            swipeRefreshLayout.setRefreshing(false);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDateBtn(View view){
        date_btn = view.findViewById(R.id.date_pick_btn);

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, DateSetListener, year, month, day);
                dialog.show();
            }
        });
    }
    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDate(year,monthOfYear,dayOfMonth);

            if(spinner.getSelectedItemPosition() == 0){
                articleitems = new ArrayList<>();

                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(FetchArticleData.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
                final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);
                date = Integer.toString(year) + '-' + (month+1) + '-' + day;

                call = articleApiService.getArticle_with_keyword(kwd,date);

                articleList.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                empty_tv.setVisibility(View.GONE);

                APIHelper.enqueueWithRetry(call, 3, new Callback<List<ArticleModel>>() {
                    @Override
                    public void onResponse(Call<List<ArticleModel>> call, Response<List<ArticleModel>> response) {
                        if (response.isSuccessful()) {
                            articleList.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            articleitems = response.body();
                            if (articleitems.size() == 0) {
                                empty_tv.setVisibility(View.VISIBLE);
                            }

                            adapter.setItemList(articleitems);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ArticleModel>> call, Throwable t) {
                        articleList.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Log.d("getData2", t.getLocalizedMessage());
                    }
                });

            }
            else if(spinner.getSelectedItemPosition() == 1){
                spinner.setSelection(0);
            }



//            Fragment frg = null;
//            frg = getFragmentManager().findFragmentByTag("fragment1");
//            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frg);
//            ft.attach(frg);
//            ft.commit();
            Toast.makeText(context, year + "년" + ++monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
        }
    };
}
