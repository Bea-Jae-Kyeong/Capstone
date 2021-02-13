package com.example.testapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager firstViewPager;
    private Context context;
    private TabViewPagerAdapter adapter;
    private LinearLayout fragment_container;

    private Retrofit retrofit = null;
    private Call<List<UserModel>> call;
    List<UserModel> user_data_list;

    public ArticleFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        context = getContext();

        firstViewPager = (ViewPager) rootView.findViewById(R.id.viewPager_article);

        ActionBar actionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

//        fragment_container =(LinearLayout) rootView.findViewById(R.id.fragment_container);
//        tabLayout.addTab(tabLayout.newTab().setText("Arsenal"));
//        tabLayout.addTab(tabLayout.newTab().setText("Ozil"));
//        tabLayout.addTab(tabLayout.newTab().setText("Aubameyang"));
//        replaceFragment(new tab1_1Fragment(),"fragment1");

        tabLayout.setupWithViewPager(firstViewPager);

        firstViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                firstViewPager.setCurrentItem(tab.getPosition());
//                if(tab.getPosition() == 0){
//                    replaceFragment(new tab1_1Fragment(),"fragment1");
//                }
//                else if(tab.getPosition() == 1){
//                    replaceFragment(new tab1_2Fragment(),"fragment2");
//                }
//                else{
//                    replaceFragment(new tab1_1Fragment(),"fragment3");
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupViewPager(firstViewPager);
        return rootView;
    }
//    private void replaceFragment(Fragment fragment,String fragment_tag) {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment,fragment_tag).addToBackStack(fragment_tag).commit();
//
//    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TabViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new tab1_1Fragment(SaveSharedPreference.getPrefKeyword1(context)), SaveSharedPreference.getPrefKeyword1(context));
        adapter.addFragment(new tab1_2Fragment(SaveSharedPreference.getPrefKeyword2(context)), SaveSharedPreference.getPrefKeyword2(context));
        adapter.addFragment(new tab1_3Fragment(SaveSharedPreference.getPrefKeyword3(context)), SaveSharedPreference.getPrefKeyword3(context));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.article_toolbar, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.date_icon){
//            DatePickerDialog dialog = new DatePickerDialog(context, listener, this.year, this.month, this.day);
//            dialog.show();
//            return true;
//        }
//        else{
//            return false;
//        }
//    }


//    public void setDate(int year, int month, int day){
//        this.year = year;
//        this.month = month;
//        this.day = day;
//    }
//
//    public DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            setDate(year,monthOfYear,dayOfMonth);
////            Fragment frg = null;
////            frg = getFragmentManager().findFragmentById(R.id.viewPager_article);
////            final FragmentTransaction ft = getFragmentManager().beginTransaction();
////            ft.detach(frg);
////            ft.attach(frg);
////            ft.commit();
//            Toast.makeText(context, year + "년" + ++monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
//        }
//    };
}
