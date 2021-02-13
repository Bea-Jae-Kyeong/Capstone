package com.example.testapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TwitterFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager twitterViewPager;
    private int[] tabIcons = {
            R.drawable.arsenal_circle,
            R.drawable.aston_villa_circle,
            R.drawable.bourn_circle,
            R.drawable.burnley_circle,
            R.drawable.brighton_circle,
            R.drawable.palace_circle,
            R.drawable.chelsea_circle,
            R.drawable.everton_circle,
            R.drawable.leicester_circle,
            R.drawable.liverpool_circle,
            R.drawable.mancity_circle,
            R.drawable.manu_circle,
            R.drawable.newcastle_circle,
            R.drawable.norwich_circle,
            R.drawable.sheffield_circle,
            R.drawable.southam_circle,
            R.drawable.totten_circle,
            R.drawable.watford_circle,
            R.drawable.west_circle,
            R.drawable.wolves_circle
    };
    public TwitterFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twitter, container, false);

        twitterViewPager = (ViewPager) rootView.findViewById(R.id.viewPager_twitter);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_tw);
//        tabItem1 = (TabItem) rootView.findViewById(R.id.tabItem1);
//        tabItem2 = (TabItem) rootView.findViewById(R.id.tabItem2);

        tabLayout.setupWithViewPager(twitterViewPager);

        twitterViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                twitterViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupViewPager(twitterViewPager);
        setupTabIcons();
        return rootView;
    }
    private void setupTabIcons() {
        for(int i=0;i<20;i++){
            View view = getLayoutInflater().inflate(R.layout.custom_team_tab,null);
            ImageView circle_img = view.findViewById(R.id.img_tab);
            circle_img.setImageResource(tabIcons[i]);
            tabLayout.getTabAt(i).setCustomView(view);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TwitterSubFragment("Arsenal"),null);
        adapter.addFragment(new TwitterSubFragment("Aston Villa"),null);
        adapter.addFragment(new TwitterSubFragment("Bournemouth"),null);
        adapter.addFragment(new TwitterSubFragment("Burnley"),null);
        adapter.addFragment(new TwitterSubFragment("Brighton"),null);
        adapter.addFragment(new TwitterSubFragment("Crystal Palace"),null);
        adapter.addFragment(new TwitterSubFragment("Chelsea"),null);
        adapter.addFragment(new TwitterSubFragment("Everton"),null);
        adapter.addFragment(new TwitterSubFragment("Leicester City"),null);
        adapter.addFragment(new TwitterSubFragment("Liverpool"),null);
        adapter.addFragment(new TwitterSubFragment("Manchester City"),null);
        adapter.addFragment(new TwitterSubFragment("Manchester United"),null);
        adapter.addFragment(new TwitterSubFragment("Newcastle United"),null);
        adapter.addFragment(new TwitterSubFragment("Norwich City"),null);
        adapter.addFragment(new TwitterSubFragment("Sheffield United"),null);
        adapter.addFragment(new TwitterSubFragment("Southampton"),null);
        adapter.addFragment(new TwitterSubFragment("Tottenham Hotspur"),null);
        adapter.addFragment(new TwitterSubFragment("Watford"),null);
        adapter.addFragment(new TwitterSubFragment("WestHam United"),null);
        adapter.addFragment(new TwitterSubFragment("Wolverhampton"),null);
        viewPager.setAdapter(adapter);
    }
}
