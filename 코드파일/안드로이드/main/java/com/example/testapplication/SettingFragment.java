package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    RecyclerView settingList;
    ArticleAdapter adapter;

    private TextView tv_favorites;
    private Button btn_edit;
    private TextView tv_first;
    private TextView tv_second;
    private TextView tv_third;
    private ImageView iv_first;
    private ImageView iv_second;
    private ImageView iv_third;
    private Context context;

    public SettingFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        String kwd1 = SaveSharedPreference.getPrefKeyword1(context);
        String kwd2 = SaveSharedPreference.getPrefKeyword2(context);
        String kwd3 = SaveSharedPreference.getPrefKeyword3(context);
        tv_first.setText(kwd1);
        tv_second.setText(kwd2);
        tv_third.setText(kwd3);

        if(kwd1.equals("Arsenal") || kwd1.equals("Aston Villa") || kwd1.equals("Bournemouth") || kwd1.equals("Brighton & Hove Albion") || kwd1.equals("Burnley") || kwd1.equals("Chelsea")
                || kwd1.equals("Crystal Palace") || kwd1.equals("Everton") || kwd1.equals("Leicester City") || kwd1.equals("Liverpool") || kwd1.equals("Manchester City")
                || kwd1.equals("Manchester United") || kwd1.equals("Newcastle United") || kwd1.equals("Norwich City") || kwd1.equals("Sheffield United")
                || kwd1.equals("Southampton") || kwd1.equals("Tottenham Hotspur") || kwd1.equals("Watford") || kwd1.equals("West Ham United") || kwd1.equals("Wolverhampton Wanderers") ){
            if (kwd1.equals("Arsenal")){
                iv_first.setImageResource(R.drawable.arsenal);
            }
            else if(kwd1.equals("Aston Villa")){
                iv_first.setImageResource(R.drawable.villa);
            }
            else if(kwd1.equals("Bournemouth")){
                iv_first.setImageResource(R.drawable.bournmouth);
            }
            else if(kwd1.equals("Brighton & Hove Albion")){
                iv_first.setImageResource(R.drawable.brighton);
            }
            else if(kwd1.equals("Burnley")){
                iv_first.setImageResource(R.drawable.burnley);
            }
            else if(kwd1.equals("Chelsea")){
                iv_first.setImageResource(R.drawable.chelsea);
            }
            else if(kwd1.equals("Crystal Palace")){
                iv_first.setImageResource(R.drawable.crystal);
            }
            else if(kwd1.equals("Everton")){
                iv_first.setImageResource(R.drawable.everton);
            }
            else if(kwd1.equals("Leicester City")){
                iv_first.setImageResource(R.drawable.leicester);
            }
            else if(kwd1.equals("Liverpool")){
                iv_first.setImageResource(R.drawable.liverpool);
            }
            else if(kwd1.equals("Manchester City")){
                iv_first.setImageResource(R.drawable.mancity);
            }
            else if(kwd1.equals("Manchester United")){
                iv_first.setImageResource(R.drawable.manu);
            }
            else if(kwd1.equals("Newcastle United")){
                iv_first.setImageResource(R.drawable.newcastle);
            }
            else if(kwd1.equals("Norwich City")){
                iv_first.setImageResource(R.drawable.norwich);
            }
            else if(kwd1.equals("Sheffield United")){
                iv_first.setImageResource(R.drawable.sheffield);
            }
            else if(kwd1.equals("Southampton")){
                iv_first.setImageResource(R.drawable.southampton);
            }
            else if(kwd1.equals("Tottenham Hotspur")){
                iv_first.setImageResource(R.drawable.tottenham);
            }
            else if(kwd1.equals("Watford")){
                iv_first.setImageResource(R.drawable.watford);
            }
            else if(kwd1.equals("West Ham United")){
                iv_first.setImageResource(R.drawable.westham);
            }
            else if(kwd1.equals("Wolverhampton Wanderers")){
                iv_first.setImageResource(R.drawable.wolves);
            }
        }
        else if(kwd1.equals("Unai Emery") || kwd1.equals("Dean Smith") || kwd1.equals("Eddie Howe") || kwd1.equals("Graham Potter") || kwd1.equals("Sean Dyche") ||
                kwd1.equals("Frank Lampard") || kwd1.equals("Roy Hodgson") || kwd1.equals("Marco Silva") || kwd1.equals("Brendan Rodgers") || kwd1.equals("Jürgen Klopp") ||
                kwd1.equals("Josep Guardiola") || kwd1.equals("Ole Gunnar Solskjaer") || kwd1.equals("Steve Bruce") || kwd1.equals("Daniel Farke") ||
                kwd1.equals("Chris Wilder") || kwd1.equals("Ralph Hasenhüttl") || kwd1.equals("José Mourinho") || kwd1.equals("Enrique Sánchez Flores") ||
                kwd1.equals("Manuel Pellegrini") || kwd1.equals("Nuno Espírito Santo")){

            iv_first.setImageResource(R.drawable.ic_manager);
        }
        else{
            iv_first.setImageResource(R.drawable.soccer_jersey);
        }

        if(kwd2.equals("Arsenal") || kwd2.equals("Aston Villa") || kwd2.equals("Bournemouth") || kwd2.equals("Brighton & Hove Albion") || kwd2.equals("Burnley") || kwd2.equals("Chelsea")
                || kwd2.equals("Crystal Palace") || kwd2.equals("Everton") || kwd2.equals("Leicester City") || kwd2.equals("Liverpool") || kwd2.equals("Manchester City")
                || kwd2.equals("Manchester United") || kwd2.equals("Newcastle United") || kwd2.equals("Norwich City") || kwd2.equals("Sheffield United")
                || kwd2.equals("Southampton") || kwd2.equals("Tottenham Hotspur") || kwd2.equals("Watford") || kwd2.equals("West Ham United") || kwd2.equals("Wolverhampton Wanderers") ){
            if (kwd2.equals("Arsenal")){
                iv_second.setImageResource(R.drawable.arsenal);
            }
            else if(kwd2.equals("Aston Villa")){
                iv_second.setImageResource(R.drawable.villa);
            }
            else if(kwd2.equals("Bournemouth")){
                iv_second.setImageResource(R.drawable.bournmouth);
            }
            else if(kwd2.equals("Brighton & Hove Albion")){
                iv_second.setImageResource(R.drawable.brighton);
            }
            else if(kwd2.equals("Burnley")){
                iv_second.setImageResource(R.drawable.burnley);
            }
            else if(kwd2.equals("Chelsea")){
                iv_second.setImageResource(R.drawable.chelsea);
            }
            else if(kwd2.equals("Crystal Palace")){
                iv_second.setImageResource(R.drawable.crystal);
            }
            else if(kwd2.equals("Everton")){
                iv_second.setImageResource(R.drawable.everton);
            }
            else if(kwd2.equals("Leicester City")){
                iv_second.setImageResource(R.drawable.leicester);
            }
            else if(kwd2.equals("Liverpool")){
                iv_second.setImageResource(R.drawable.liverpool);
            }
            else if(kwd2.equals("Manchester City")){
                iv_second.setImageResource(R.drawable.mancity);
            }
            else if(kwd2.equals("Manchester United")){
                iv_second.setImageResource(R.drawable.manu);
            }
            else if(kwd2.equals("Newcastle United")){
                iv_second.setImageResource(R.drawable.newcastle);
            }
            else if(kwd2.equals("Norwich City")){
                iv_second.setImageResource(R.drawable.norwich);
            }
            else if(kwd2.equals("Sheffield United")){
                iv_second.setImageResource(R.drawable.sheffield);
            }
            else if(kwd2.equals("Southampton")){
                iv_second.setImageResource(R.drawable.southampton);
            }
            else if(kwd2.equals("Tottenham Hotspur")){
                iv_second.setImageResource(R.drawable.tottenham);
            }
            else if(kwd2.equals("Watford")){
                iv_second.setImageResource(R.drawable.watford);
            }
            else if(kwd2.equals("West Ham United")){
                iv_second.setImageResource(R.drawable.westham);
            }
            else if(kwd2.equals("Wolverhampton Wanderers")){
                iv_second.setImageResource(R.drawable.wolves);
            }
        }
        else if(kwd2.equals("Unai Emery") || kwd2.equals("Dean Smith") || kwd2.equals("Eddie Howe") || kwd2.equals("Graham Potter") || kwd2.equals("Sean Dyche") ||
                kwd2.equals("Frank Lampard") || kwd2.equals("Roy Hodgson") || kwd2.equals("Marco Silva") || kwd2.equals("Brendan Rodgers") || kwd2.equals("Jürgen Klopp") ||
                kwd2.equals("Josep Guardiola") || kwd2.equals("Ole Gunnar Solskjaer") || kwd2.equals("Steve Bruce") || kwd2.equals("Daniel Farke") ||
                kwd2.equals("Chris Wilder") || kwd2.equals("Ralph Hasenhüttl") || kwd2.equals("José Mourinho") || kwd2.equals("Enrique Sánchez Flores") ||
                kwd2.equals("Manuel Pellegrini") || kwd2.equals("Nuno Espírito Santo")){

            iv_second.setImageResource(R.drawable.ic_manager);
        }
        else{
            iv_second.setImageResource(R.drawable.soccer_jersey);
        }


        if(kwd3.equals("Arsenal") || kwd3.equals("Aston Villa") || kwd3.equals("Bournemouth") || kwd3.equals("Brighton & Hove Albion") || kwd3.equals("Burnley") || kwd3.equals("Chelsea")
                || kwd3.equals("Crystal Palace") || kwd3.equals("Everton") || kwd3.equals("Leicester City") || kwd3.equals("Liverpool") || kwd3.equals("Manchester City")
                || kwd3.equals("Manchester United") || kwd3.equals("Newcastle United") || kwd3.equals("Norwich City") || kwd3.equals("Sheffield United")
                || kwd3.equals("Southampton") || kwd3.equals("Tottenham Hotspur") || kwd3.equals("Watford") || kwd3.equals("West Ham United") || kwd3.equals("Wolverhampton Wanderers") ){
            if (kwd3.equals("Arsenal")){
                iv_third.setImageResource(R.drawable.arsenal);
            }
            else if(kwd3.equals("Aston Villa")){
                iv_third.setImageResource(R.drawable.villa);
            }
            else if(kwd3.equals("Bournemouth")){
                iv_third.setImageResource(R.drawable.bournmouth);
            }
            else if(kwd3.equals("Brighton & Hove Albion")){
                iv_third.setImageResource(R.drawable.brighton);
            }
            else if(kwd3.equals("Burnley")){
                iv_third.setImageResource(R.drawable.burnley);
            }
            else if(kwd3.equals("Chelsea")){
                iv_third.setImageResource(R.drawable.chelsea);
            }
            else if(kwd3.equals("Crystal Palace")){
                iv_third.setImageResource(R.drawable.crystal);
            }
            else if(kwd3.equals("Everton")){
                iv_third.setImageResource(R.drawable.everton);
            }
            else if(kwd3.equals("Leicester City")){
                iv_third.setImageResource(R.drawable.leicester);
            }
            else if(kwd3.equals("Liverpool")){
                iv_third.setImageResource(R.drawable.liverpool);
            }
            else if(kwd3.equals("Manchester City")){
                iv_third.setImageResource(R.drawable.mancity);
            }
            else if(kwd3.equals("Manchester United")){
                iv_third.setImageResource(R.drawable.manu);
            }
            else if(kwd3.equals("Newcastle United")){
                iv_third.setImageResource(R.drawable.newcastle);
            }
            else if(kwd3.equals("Norwich City")){
                iv_third.setImageResource(R.drawable.norwich);
            }
            else if(kwd3.equals("Sheffield United")){
                iv_third.setImageResource(R.drawable.sheffield);
            }
            else if(kwd3.equals("Southampton")){
                iv_third.setImageResource(R.drawable.southampton);
            }
            else if(kwd3.equals("Tottenham Hotspur")){
                iv_third.setImageResource(R.drawable.tottenham);
            }
            else if(kwd3.equals("Watford")){
                iv_third.setImageResource(R.drawable.watford);
            }
            else if(kwd3.equals("West Ham United")){
                iv_third.setImageResource(R.drawable.westham);
            }
            else if(kwd3.equals("Wolverhampton Wanderers")){
                iv_third.setImageResource(R.drawable.wolves);
            }
        }
        else if(kwd3.equals("Unai Emery") || kwd3.equals("Dean Smith") || kwd3.equals("Eddie Howe") || kwd3.equals("Graham Potter") || kwd3.equals("Sean Dyche") ||
                kwd3.equals("Frank Lampard") || kwd3.equals("Roy Hodgson") || kwd3.equals("Marco Silva") || kwd3.equals("Brendan Rodgers") || kwd3.equals("Jürgen Klopp") ||
                kwd3.equals("Josep Guardiola") || kwd3.equals("Ole Gunnar Solskjaer") || kwd3.equals("Steve Bruce") || kwd3.equals("Daniel Farke") ||
                kwd3.equals("Chris Wilder") || kwd3.equals("Ralph Hasenhüttl") || kwd3.equals("José Mourinho") || kwd3.equals("Enrique Sánchez Flores") ||
                kwd3.equals("Manuel Pellegrini") || kwd3.equals("Nuno Espírito Santo")){
            iv_third.setImageResource(R.drawable.ic_manager);
        }
        else{
            iv_third.setImageResource(R.drawable.soccer_jersey);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        context = view.getContext();

        tv_favorites = (TextView) view.findViewById(R.id.tv_favorites);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        tv_first = (TextView) view.findViewById(R.id.first_kwd);
        tv_second = (TextView) view.findViewById(R.id.second_kwd);
        tv_third = (TextView) view.findViewById(R.id.third_kwd);
        iv_first = (ImageView) view.findViewById(R.id.first_img);
        iv_second = (ImageView) view.findViewById(R.id.second_img);
        iv_third = (ImageView) view.findViewById(R.id.third_img);

        btn_edit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TeamSelect.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
