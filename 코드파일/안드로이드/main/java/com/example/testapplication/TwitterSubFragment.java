package com.example.testapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.Locale;

public class TwitterSubFragment extends Fragment {
    private Context context;
    private RecyclerView userTimelineRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TweetTimelineRecyclerViewAdapter adapter;
    private String team_name;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TwitterSubFragment(String name) {
        this.team_name = name;
    }

    public static TwitterFragment newInstance() {

        Bundle args = new Bundle();

        TwitterFragment fragment = new TwitterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_frag_twitter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        loadUserTimeline();
    }

    /**
     * method to set up recycler view
     *
     * @param view of the fragment
     */
    private void setUpRecyclerView(@NonNull View view) {
        userTimelineRecyclerView = view.findViewById(R.id.user_timeline_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);//it should be Vertical only
        userTimelineRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * method to load user timeline over recycler view
     */
    private void loadUserTimeline() {

        //build UserTimeline
//        UserTimeline userTimeline = new UserTimeline.Builder()
//                .screenName(team_name)//screen name of the user to show tweets for
//                .includeReplies(true)//Whether to include replies. Defaults to false.
//                .includeRetweets(true)//Whether to include re-tweets. Defaults to true.
//                .maxItemsPerRequest(50)//Max number of items to return per request
//                .build();

        //build the Search TimeLine
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(team_name)//the search query for Tweets
                .languageCode(Locale.ENGLISH.getLanguage())//set the language code
//                .resultType(SearchTimeline.ResultType.POPULAR)
                .maxItemsPerRequest(50)//Max number of items to return per request
                .build();

        /*  =============   If you don't want to login the user and still you want to see the User Timeline
        then you can pass any SCREEN NAME and see the timeline. Enable the below code to do this.   ===============  */

       /* UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("sonusurender0")//any screen name
                .includeReplies(true)//Whether to include replies. Defaults to false.
                .includeRetweets(true)//Whether to include re-tweets. Defaults to true.
                .maxItemsPerRequest(50)//Max number of items to return per request
                .build();*/

        //now build adapter for recycler view
        adapter = new TweetTimelineRecyclerViewAdapter.Builder(context)
                .setTimeline(searchTimeline)//set the created timeline
                //action callback to listen when user like/unlike the tweet
                .setOnActionCallback(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        //do something on success response
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        //do something on failure response
                    }
                })
                //set tweet view style
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .build();

        //finally set the created adapter to recycler view
        userTimelineRecyclerView.setAdapter(adapter);
    }

    /**
     * set up swipe refresh layout
     *
     * @param view of the fragment
     */
    private void setUpSwipeRefreshLayout(View view) {

        //find the id of swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.user_swipe_refresh_layout3);

        //implement refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //return if adapter is null
                if (adapter == null)
                    return;

                //make set refreshing true
                swipeRefreshLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        //on success response make refreshing false
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, "Tweets refreshed.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                        Toast.makeText(context, "Failed to refresh tweets.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
