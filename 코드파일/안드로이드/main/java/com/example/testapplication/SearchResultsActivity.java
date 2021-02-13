package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {

    MenuItem mSearch;
    RecyclerView searchList;
    SearchAdapter searchAdapter;
    List<KeywordModel> searchitems;
    private static Retrofit retrofit = null;
    private Call<List<KeywordModel>> call;
    Context context;

    private MaterialSearchView mSearchView;
    private ImageView mBackButton;
    private ImageView mVoiceSearchButton;
    private ImageView mEmptyButton;
    private EditText mSearchTextView;
    private RelativeLayout result_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);

        mSearchView.showSearch(false);
        mBackButton = (ImageView) mSearchView.findViewById(R.id.action_up_btn);

        mEmptyButton = (ImageView) mSearchView.findViewById(R.id.action_empty_btn);

        mSearchTextView = (EditText) mSearchView.findViewById(R.id.searchTextView);

        result_container = (RelativeLayout) findViewById(R.id.result_container);
        result_container.setVisibility(View.GONE);

        mEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                result_container.setVisibility(View.GONE);
                mSearchTextView.setText("");
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchList = (RecyclerView) findViewById(R.id.search_recycler_view);
        searchList.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        searchList.setLayoutManager(linearLayoutManager);
        searchitems = new ArrayList<>();
        searchAdapter = new SearchAdapter(context,searchitems);
        searchList.setAdapter(searchAdapter);

        mSearchView.setHint("EPL Teams, players, managers");

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadKeyword(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
//                    searchList.setVisibility(View.VISIBLE);
                    result_container.setVisibility(View.VISIBLE);
                    loadKeyword(newText);
                } else {
//                    searchList.setVisibility(View.GONE);
                    result_container.setVisibility(View.GONE);
                }

                return true;
            }
        });
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
        result_container.setVisibility(View.GONE);

        APIHelper.enqueueWithRetry(call, 3, new Callback<List<KeywordModel>>() {
            @Override
            public void onResponse(Call<List<KeywordModel>> call, Response<List<KeywordModel>> response) {
                if(response.isSuccessful()){
                    result_container.setVisibility(View.VISIBLE);
                    searchitems = response.body();
                    searchAdapter.setItemList(searchitems);
//                    searchAdapter = new SearchAdapter(context,searchitems);
//                    searchList.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<KeywordModel>> call, Throwable t) {
                Log.d("getData2",t.getLocalizedMessage());
            }
        });
    }


    private class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

}
