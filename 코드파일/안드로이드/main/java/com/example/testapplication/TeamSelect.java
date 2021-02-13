package com.example.testapplication;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.Image;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamSelect extends AppCompatActivity{
    public static Context context;
    private Button[] buttons = new Button[22];
    private int[] id_arr = new int[22];
    private Button Tab0,Tab1,Tab2,Tab3,Tab4,Tab5,Tab6,Tab7,Tab8,Tab9,Tab10,Tab11,Tab12,Tab13,Tab14,Tab15,Tab16,Tab17,Tab18,Tab19,Tab20,Tab21;
    private TabHost tabHost;
    private Toolbar select_toolbar;

    private ImageButton team0,team1,team2,team3,team4,team5,team6,team7,team8,team9,team10,team11,team12,team13,team14,team15,team16,team17,team18,team19,team20;

    private ImageButton arsenal0,arsenal1,arsenal2,arsenal3,arsenal4,arsenal5,arsenal6,arsenal7,arsenal8,arsenal9,arsenal10,arsenal11,arsenal12,arsenal13,arsenal14,arsenal15,arsenal16,arsenal17,arsenal18,arsenal19,arsenal20,arsenal21,arsenal22,arsenal23,arsenal24;
    private ImageButton villa0,villa1,villa2,villa3,villa4,villa5,villa6,villa7,villa8,villa9,villa10,villa11,villa12,villa13,villa14,villa15,villa16,villa17,villa18,villa19,villa20,villa21,villa22,villa23,villa24;
    private ImageButton born0,born1,born2,born3,born4,born5,born6,born7,born8,born9,born10,born11,born12,born13,born14,born15,born16,born17,born18,born19,born20,born21,born22,born23,born24,born25,born26;
    private ImageButton brighton0,brighton1,brighton2,brighton3,brighton4,brighton5,brighton6,brighton7,brighton8,brighton9,brighton10,brighton11,brighton12,brighton13,brighton14,brighton15,brighton16,brighton17,brighton18,brighton19,brighton20,brighton21,brighton22,brighton23,brighton24,brighton25,brighton26;
    private ImageButton burnley0,burnley1,burnley2,burnley3,burnley4,burnley5,burnley6,burnley7,burnley8,burnley9,burnley10,burnley11,burnley12,burnley13,burnley14,burnley15,burnley16,burnley17,burnley18,burnley19,burnley20,burnley21,burnley22,burnley23;
    private ImageButton chelsea0,chelsea1,chelsea2,chelsea3,chelsea4,chelsea5,chelsea6,chelsea7,chelsea8,chelsea9,chelsea10,chelsea11,chelsea12,chelsea13,chelsea14,chelsea15,chelsea16,chelsea17,chelsea18,chelsea19,chelsea20,chelsea21,chelsea22,chelsea23,chelsea24;
    private ImageButton crystal0,crystal1,crystal2,crystal3,crystal4,crystal5,crystal6,crystal7,crystal8,crystal9,crystal10,crystal11,crystal12,crystal13,crystal14,crystal15,crystal16,crystal17,crystal18,crystal19,crystal20,crystal21,crystal22,crystal23;
    private ImageButton everton0,everton1,everton2,everton3,everton4,everton5,everton6,everton7,everton8,everton9,everton10,everton11,everton12,everton13,everton14,everton15,everton16,everton17,everton18,everton19,everton20,everton21,everton22,everton23,everton24,everton25;
    private ImageButton leicester0,leicester1,leicester2,leicester3,leicester4,leicester5,leicester6,leicester7,leicester8,leicester9,leicester10,leicester11,leicester12,leicester13,leicester14,leicester15,leicester16,leicester17,leicester18,leicester19,leicester20,leicester21,leicester22,leicester23,leicester24,leicester25,leicester26;
    private ImageButton liverpool0,liverpool1,liverpool2,liverpool3,liverpool4,liverpool5,liverpool6,liverpool7,liverpool8,liverpool9,liverpool10,liverpool11,liverpool12,liverpool13,liverpool14,liverpool15,liverpool16,liverpool17,liverpool18,liverpool19,liverpool20,liverpool21,liverpool22;
    private ImageButton mancity0,mancity1,mancity2,mancity3,mancity4,mancity5,mancity6,mancity7,mancity8,mancity9,mancity10,mancity11,mancity12,mancity13,mancity14,mancity15,mancity16,mancity17,mancity18,mancity19,mancity20,mancity21,mancity22;
    private ImageButton manu0,manu1,manu2,manu3,manu4,manu5,manu6,manu7,manu8,manu9,manu10,manu11,manu12,manu13,manu14,manu15,manu16,manu17,manu18,manu19,manu20,manu21,manu22,manu23,manu24;
    private ImageButton new0,new1,new2,new3,new4,new5,new6,new7,new8,new9,new10,new11,new12,new13,new14,new15,new16,new17,new18,new19,new20,new21,new22,new23,new24,new25,new26,new27;
    private ImageButton norwich0,norwich1,norwich2,norwich3,norwich4,norwich5,norwich6,norwich7,norwich8,norwich9,norwich10,norwich11,norwich12,norwich13,norwich14,norwich15,norwich16,norwich17,norwich18,norwich19,norwich20,norwich21,norwich22,norwich23,norwich24;
    private ImageButton shef0,shef1,shef2,shef3,shef4,shef5,shef6,shef7,shef8,shef9,shef10,shef11,shef12,shef13,shef14,shef15,shef16,shef17,shef18,shef19,shef20,shef21,shef22,shef23,shef24,shef25;
    private ImageButton south0,south1,south2,south3,south4,south5,south6,south7,south8,south9,south10,south11,south12,south13,south14,south15,south16,south17,south18,south19,south20,south21;
    private ImageButton totten0,totten1,totten2,totten3,totten4,totten5,totten6,totten7,totten8,totten9,totten10,totten11,totten12,totten13,totten14,totten15,totten16,totten17,totten18,totten19,totten20,totten21,totten22,totten23,totten24;
    private ImageButton watford0,watford1,watford2,watford3,watford4,watford5,watford6,watford7,watford8,watford9,watford10,watford11,watford12,watford13,watford14,watford15,watford16,watford17,watford18,watford19,watford20,watford21,watford22,watford23,watford24,watford25,watford26,watford27,watford28;
    private ImageButton west0,west1,west2,west3,west4,west5,west6,west7,west8,west9,west10,west11,west12,west13,west14,west15,west16,west17,west18,west19,west20,west21,west22,west23;
    private ImageButton wolves0,wolves1,wolves2,wolves3,wolves4,wolves5,wolves6,wolves7,wolves8,wolves9,wolves10,wolves11,wolves12,wolves13,wolves14,wolves15,wolves16,wolves17,wolves18,wolves19,wolves20,wolves21,wolves22,wolves23,wolves24;
    private ImageButton manager0,manager1,manager2,manager3,manager4,manager5,manager6,manager7,manager8,manager9,manager10,manager11,manager12,manager13,manager14,manager15,manager16,manager17,manager18,manager19;

    public ImageButton[] kwd_btns = new ImageButton[545];
    public int[] kwd_id = new int[545];
    public String[] keywords = new String[545];
    public ArrayList<String> user_keywords = new ArrayList<>();

    public static int Tab = -1;

    private Retrofit retrofit = null;
    private Call<List<UserModel>> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_select);

        context = this.getApplicationContext();
        select_toolbar = (Toolbar) findViewById(R.id.select_toolbar);
        setSupportActionBar(select_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ↓툴바의 홈버튼의 이미지를 변경(기본 이미지는 뒤로가기 화살표)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        select_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tabHost = (TabHost) findViewById(R.id.tabHost1);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);

        setTabBtn();

        setTabSpec();

        setKwdBtn();
        setTeamBtn();


        getUserKeyword();

        setOnClickBtn();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.team_select_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.finish_btn){
            Intent intent;
            if(SaveSharedPreference.getUserID(context).length() == 0){
                String idByANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("getData",idByANDROID_ID);
                if (user_keywords.size() == 3){
                    SaveSharedPreference.setUserInfo(context,idByANDROID_ID,user_keywords.get(0),user_keywords.get(1),user_keywords.get(2));

                    if (retrofit == null) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(FetchArticleData.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                    }
                    final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

                    call = articleApiService.saveUserData(idByANDROID_ID,user_keywords.get(0),user_keywords.get(1),user_keywords.get(2));

                    //유저 데이터 저장
                    APIHelper.enqueueWithRetry(call, 3, new Callback<List<UserModel>>() {
                        @Override
                        public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                            if(response.isSuccessful()){
                                Log.d("getData2","userdata Success");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserModel>> call, Throwable t) {
                            Log.d("getData2",t.getLocalizedMessage());
                        }
                    });


                    intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You must enter three keywords" , Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if (user_keywords.size() == 3){
                    Log.d("getData",SaveSharedPreference.getUserID(context));
                    SaveSharedPreference.setUserKeyword(context,user_keywords.get(0),user_keywords.get(1),user_keywords.get(2));

                    if (retrofit == null) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(FetchArticleData.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                    }
                    final ArticleApiService articleApiService = retrofit.create(ArticleApiService.class);

                    call = articleApiService.saveUserData(SaveSharedPreference.getUserID(context),user_keywords.get(0),user_keywords.get(1),user_keywords.get(2));

                    //유저 데이터 저장
                    APIHelper.enqueueWithRetry(call, 3, new Callback<List<UserModel>>() {
                        @Override
                        public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                            if(response.isSuccessful()){
                                Log.d("getData2","userdata Success");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<UserModel>> call, Throwable t) {
                            Log.d("getData2",t.getLocalizedMessage());
                        }
                    });

                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You must enter three keywords" , Toast.LENGTH_SHORT).show();
                }
            }

            return true;
        }
        else{
            return false;
        }
    }


    private void getUserKeyword(){
        if(user_keywords.size() == 0){
            if(SaveSharedPreference.getPrefKeyword1(context).length() != 0){
                String kwd1 = SaveSharedPreference.getPrefKeyword1(context);
                user_keywords.add(kwd1);
                Log.d("keyword",kwd1);
                for (int i = 0; i < keywords.length; i++){
                    if(keywords[i].equals(kwd1)){
                        kwd_btns[i].setSelected(true);
                    }
                }
            }
            if(SaveSharedPreference.getPrefKeyword2(context).length() != 0){
                String kwd2 = SaveSharedPreference.getPrefKeyword2(context);
                user_keywords.add(kwd2);
                Log.d("keyword",kwd2);
                for (int i = 0; i < keywords.length; i++){
                    if(keywords[i].equals(kwd2)){
                        kwd_btns[i].setSelected(true);
                    }
                }
            }
            if(SaveSharedPreference.getPrefKeyword3(context).length() != 0){
                String kwd3 = SaveSharedPreference.getPrefKeyword3(context);
                user_keywords.add(kwd3);
                Log.d("keyword",kwd3);
                for (int i = 0; i < keywords.length; i++){
                    if(keywords[i].equals(kwd3)){
                        kwd_btns[i].setSelected(true);
                    }
                }
            }
        }
    }

    public void tabHandler(View target) {
        if (target.getId() == R.id.Tab0) {
            Tab=0;
            tabHost.setCurrentTab(0);
        } else if (target.getId() == R.id.Tab1) {
            Tab=1;
            tabHost.setCurrentTab(1);
        } else if (target.getId() == R.id.Tab2) {
            Tab=2;
            tabHost.setCurrentTab(2);
        }
        else if (target.getId() == R.id.Tab3) {
            Tab=3;
            tabHost.setCurrentTab(3);
        }
        else if (target.getId() == R.id.Tab4) {
            Tab=4;
            tabHost.setCurrentTab(4);
        }
        else if (target.getId() == R.id.Tab5) {
            Tab=5;
            tabHost.setCurrentTab(5);
        }
        else if (target.getId() == R.id.Tab6) {
            Tab=6;
            tabHost.setCurrentTab(6);
        }
        else if (target.getId() == R.id.Tab7) {
            Tab=7;
            tabHost.setCurrentTab(7);
        }
        else if (target.getId() == R.id.Tab8) {
            Tab=8;
            tabHost.setCurrentTab(8);
        }
        else if (target.getId() == R.id.Tab9) {
            Tab=9;
            tabHost.setCurrentTab(9);
        }
        else if (target.getId() == R.id.Tab10) {
            Tab=9;
            tabHost.setCurrentTab(10);
        }
        else if (target.getId() == R.id.Tab11) {
            Tab=9;
            tabHost.setCurrentTab(11);
        }
        else if (target.getId() == R.id.Tab12) {
            Tab=9;
            tabHost.setCurrentTab(12);
        }
        else if (target.getId() == R.id.Tab13) {
            Tab=9;
            tabHost.setCurrentTab(13);
        }
        else if (target.getId() == R.id.Tab14) {
            Tab=9;
            tabHost.setCurrentTab(14);
        }
        else if (target.getId() == R.id.Tab15) {
            Tab=9;
            tabHost.setCurrentTab(15);
        }
        else if (target.getId() == R.id.Tab16) {
            Tab=9;
            tabHost.setCurrentTab(16);
        }
        else if (target.getId() == R.id.Tab17) {
            Tab=9;
            tabHost.setCurrentTab(17);
        }
        else if (target.getId() == R.id.Tab18) {
            Tab=9;
            tabHost.setCurrentTab(18);
        }
        else if (target.getId() == R.id.Tab19) {
            Tab=9;
            tabHost.setCurrentTab(19);
        }
        else if (target.getId() == R.id.Tab20) {
            Tab=9;
            tabHost.setCurrentTab(20);
        }
        else if (target.getId() == R.id.Tab21) {
            Tab=9;
            tabHost.setCurrentTab(21);
        }

    }

    private void setTabBtn(){
        Tab0=(Button)findViewById(R.id.Tab0);
        buttons[0] = Tab0;
        id_arr[0] = R.id.Tab0;
        setOnClickTab(0);
        Tab0.setSelected(true);

        Tab1=(Button)findViewById(R.id.Tab1);
        buttons[1] = Tab1;
        id_arr[1] = R.id.Tab1;
        setOnClickTab(1);
//        Tab1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Tab1.setPressed(true);
//                Tab2.setPressed(false);
//                return true;
//            }
//        });

        Tab2=(Button)findViewById(R.id.Tab2);
        buttons[2] = Tab2;
        id_arr[2] = R.id.Tab2;
        setOnClickTab(2);

        Tab3=(Button)findViewById(R.id.Tab3);
        buttons[3] = Tab3;
        id_arr[3] = R.id.Tab3;
        setOnClickTab(3);

        Tab4=(Button)findViewById(R.id.Tab4);
        buttons[4] = Tab4;
        id_arr[4] = R.id.Tab4;
        setOnClickTab(4);

        Tab5=(Button)findViewById(R.id.Tab5);
        buttons[5] = Tab5;
        id_arr[5] = R.id.Tab5;
        setOnClickTab(5);

        Tab6=(Button)findViewById(R.id.Tab6);
        buttons[6] = Tab6;
        id_arr[6] = R.id.Tab6;
        setOnClickTab(6);

        Tab7=(Button)findViewById(R.id.Tab7);
        buttons[7] = Tab7;
        id_arr[7] = R.id.Tab7;
        setOnClickTab(7);

        Tab8=(Button)findViewById(R.id.Tab8);
        buttons[8] = Tab8;
        id_arr[8] = R.id.Tab8;
        setOnClickTab(8);

        Tab9=(Button)findViewById(R.id.Tab9);
        buttons[9] = Tab9;
        id_arr[9] = R.id.Tab9;
        setOnClickTab(9);

        Tab10=(Button)findViewById(R.id.Tab10);
        buttons[10] = Tab10;
        id_arr[10] = R.id.Tab10;
        setOnClickTab(10);

        Tab11=(Button)findViewById(R.id.Tab11);
        buttons[11] = Tab11;
        id_arr[11] = R.id.Tab11;
        setOnClickTab(11);

        Tab12=(Button)findViewById(R.id.Tab12);
        buttons[12] = Tab12;
        id_arr[12] = R.id.Tab12;
        setOnClickTab(12);

        Tab13=(Button)findViewById(R.id.Tab13);
        buttons[13] = Tab13;
        id_arr[13] = R.id.Tab13;
        setOnClickTab(13);

        Tab14=(Button)findViewById(R.id.Tab14);
        buttons[14] = Tab14;
        id_arr[14] = R.id.Tab14;
        setOnClickTab(14);

        Tab15=(Button)findViewById(R.id.Tab15);
        buttons[15] = Tab15;
        id_arr[15] = R.id.Tab15;
        setOnClickTab(15);

        Tab16=(Button)findViewById(R.id.Tab16);
        buttons[16] = Tab16;
        id_arr[16] = R.id.Tab16;
        setOnClickTab(16);

        Tab17=(Button)findViewById(R.id.Tab17);
        buttons[17] = Tab17;
        id_arr[17] = R.id.Tab17;
        setOnClickTab(17);

        Tab18=(Button)findViewById(R.id.Tab18);
        buttons[18] = Tab18;
        id_arr[18] = R.id.Tab18;
        setOnClickTab(18);

        Tab19=(Button)findViewById(R.id.Tab19);
        buttons[19] = Tab19;
        id_arr[19] = R.id.Tab19;
        setOnClickTab(19);

        Tab20=(Button)findViewById(R.id.Tab20);
        buttons[20] = Tab20;
        id_arr[20] = R.id.Tab20;
        setOnClickTab(20);

        Tab21=(Button)findViewById(R.id.Tab21);
        buttons[21] = Tab21;
        id_arr[21] = R.id.Tab21;
        setOnClickTab(21);
    }

    private void setTabSpec(){
        // Tab for tab1
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab0");
        // setting Title and Icon for the Tab
        spec1.setIndicator("Tab0");
        spec1.setContent(R.id.content_team);

        // Tab for tab2
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab1");
        // setting Title and Icon for the Tab
        spec2.setIndicator("Tab1");
        spec2.setContent(R.id.content_arsenal);

        // Tab for tab3
        TabHost.TabSpec spec3 = tabHost.newTabSpec("Tab2");
        // setting Title and Icon for the Tab
        spec3.setIndicator("Tab2");
        spec3.setContent(R.id.content_villa);

        // Tab for tab4

        TabHost.TabSpec spec4 = tabHost.newTabSpec("Tab3");
        // setting Title and Icon for the Tab
        spec4.setIndicator("Tab3");
        spec4.setContent(R.id.content_bourn);

        // Tab for tab5
        TabHost.TabSpec spec5 = tabHost.newTabSpec("Tab4");
        // setting Title and Icon for the Tab
        spec5.setIndicator("Tab4");
        spec5.setContent(R.id.content_brighton);

        // Tab for tab5
        TabHost.TabSpec spec6 = tabHost.newTabSpec("Tab5");
        // setting Title and Icon for the Tab
        spec6.setIndicator("Tab5");
        spec6.setContent(R.id.content_burnley);

        TabHost.TabSpec spec7 = tabHost.newTabSpec("Tab6");
        spec7.setIndicator("Tab6");
        spec7.setContent(R.id.content_chelsea);

        TabHost.TabSpec spec8 = tabHost.newTabSpec("Tab7");
        spec8.setIndicator("Tab7");
        spec8.setContent(R.id.content_crystal);

        TabHost.TabSpec spec9 = tabHost.newTabSpec("Tab8");
        spec9.setIndicator("Tab8");
        spec9.setContent(R.id.content_everton);

        TabHost.TabSpec spec10 = tabHost.newTabSpec("Tab9");
        spec10.setIndicator("Tab9");
        spec10.setContent(R.id.content_leicester);

        TabHost.TabSpec spec11 = tabHost.newTabSpec("Tab10");
        spec11.setIndicator("Tab10");
        spec11.setContent(R.id.content_liverpool);

        TabHost.TabSpec spec12 = tabHost.newTabSpec("Tab11");
        spec12.setIndicator("Tab11");
        spec12.setContent(R.id.content_mancity);

        TabHost.TabSpec spec13 = tabHost.newTabSpec("Tab12");
        spec13.setIndicator("Tab12");
        spec13.setContent(R.id.content_manu);

        TabHost.TabSpec spec14 = tabHost.newTabSpec("Tab13");
        spec14.setIndicator("Tab13");
        spec14.setContent(R.id.content_newcastle);

        TabHost.TabSpec spec15 = tabHost.newTabSpec("Tab14");
        spec15.setIndicator("Tab14");
        spec15.setContent(R.id.content_norwich);

        TabHost.TabSpec spec16 = tabHost.newTabSpec("Tab15");
        spec16.setIndicator("Tab15");
        spec16.setContent(R.id.content_sheffield);

        TabHost.TabSpec spec17 = tabHost.newTabSpec("Tab16");
        spec17.setIndicator("Tab16");
        spec17.setContent(R.id.content_southampton);

        TabHost.TabSpec spec18 = tabHost.newTabSpec("Tab17");
        spec18.setIndicator("Tab17");
        spec18.setContent(R.id.content_tottenham);

        TabHost.TabSpec spec19 = tabHost.newTabSpec("Tab18");
        spec19.setIndicator("Tab18");
        spec19.setContent(R.id.content_watford);

        TabHost.TabSpec spec20 = tabHost.newTabSpec("Tab19");
        spec20.setIndicator("Tab19");
        spec20.setContent(R.id.content_westham);

        TabHost.TabSpec spec21 = tabHost.newTabSpec("Tab20");
        spec21.setIndicator("Tab20");
        spec21.setContent(R.id.content_wolverhampton);

        TabHost.TabSpec spec22 = tabHost.newTabSpec("Tab21");
        spec22.setIndicator("Tab21");
        spec22.setContent(R.id.content_manager);


        // Adding all TabSpec to TabHost
        tabHost.addTab(spec1); // Adding tab1
        tabHost.addTab(spec2); // Adding tab2
        tabHost.addTab(spec3); // Adding tab3
        tabHost.addTab(spec4); // Adding tab4
        tabHost.addTab(spec5); // Adding tab5
        tabHost.addTab(spec6); // Adding tab5
        tabHost.addTab(spec7); // Adding tab5
        tabHost.addTab(spec8); // Adding tab5
        tabHost.addTab(spec9); // Adding tab5
        tabHost.addTab(spec10); // Adding tab5
        tabHost.addTab(spec11); // Adding tab5
        tabHost.addTab(spec12); // Adding tab5
        tabHost.addTab(spec13); // Adding tab5
        tabHost.addTab(spec14); // Adding tab5
        tabHost.addTab(spec15); // Adding tab5
        tabHost.addTab(spec16); // Adding tab5
        tabHost.addTab(spec17); // Adding tab5
        tabHost.addTab(spec18); // Adding tab5
        tabHost.addTab(spec19); // Adding tab5
        tabHost.addTab(spec20); // Adding tab5
        tabHost.addTab(spec21); // Adding tab5
        tabHost.addTab(spec22);
    }

    private void setOnClickTab(int i){
        buttons[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                v.setSelected(true);
                for (int k=0; k<buttons.length; k++){
                    if (id == id_arr[k]){
                        Toast.makeText(getApplicationContext(), "Tab " + k + " Click", Toast.LENGTH_SHORT).show();
                        tabHost.setCurrentTab(k);
                    }
                    else{
                        buttons[k].setSelected(false);
                    }
                }
            }
        });
//        buttons[i].setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                int id = v.getId();
//                if (action == MotionEvent.ACTION_DOWN){
//                    v.setPressed(true);
//
//                    for (int k=0;k<10;k++){
//                        if (id == id_arr[k]){
//                            Toast.makeText(getApplicationContext(), "Tab " + k + " Click", Toast.LENGTH_SHORT).show();
//                            tabHost.setCurrentTab(k);
//                        }
//                        else{
//                            buttons[k].setPressed(false);
//                        }
//                    }
//                }
//
//                return true;
//            }
//        });
    }

    private void setKwdBtn(){
        team0 = (ImageButton) findViewById(R.id.arsenal_btn);
        team1 = (ImageButton) findViewById(R.id.villa_btn);
        team2 = (ImageButton) findViewById(R.id.bournemouth_btn);
        team3 = (ImageButton) findViewById(R.id.brighton_btn);
        team4 = (ImageButton) findViewById(R.id.burnley_btn);
        team5 = (ImageButton) findViewById(R.id.chelsea_btn);
        team6 = (ImageButton) findViewById(R.id.crystal_btn);
        team7 = (ImageButton) findViewById(R.id.everton_btn);
        team8 = (ImageButton) findViewById(R.id.leicester_btn);
        team9 = (ImageButton) findViewById(R.id.liverpool_btn);
        team10 = (ImageButton) findViewById(R.id.mancity_btn);
        team11 = (ImageButton) findViewById(R.id.manu_btn);
        team12 = (ImageButton) findViewById(R.id.newcastle_btn);
        team13 = (ImageButton) findViewById(R.id.norwich_btn);
        team14 = (ImageButton) findViewById(R.id.sheffield_btn);
        team15 = (ImageButton) findViewById(R.id.southampton_btn);
        team16 = (ImageButton) findViewById(R.id.tottenham_btn);
        team17 = (ImageButton) findViewById(R.id.watford_btn);
        team18 = (ImageButton) findViewById(R.id.westham_btn);
        team19 = (ImageButton) findViewById(R.id.wolves_btn);

        kwd_btns[0] = team0;
        kwd_btns[1] = team1;
        kwd_btns[2] = team2;
        kwd_btns[3] = team3;
        kwd_btns[4] = team4;
        kwd_btns[5] = team5;
        kwd_btns[6] = team6;
        kwd_btns[7] = team7;
        kwd_btns[8] = team8;
        kwd_btns[9] = team9;
        kwd_btns[10] = team10;
        kwd_btns[11] = team11;
        kwd_btns[12] = team12;
        kwd_btns[13] = team13;
        kwd_btns[14] = team14;
        kwd_btns[15] = team15;
        kwd_btns[16] = team16;
        kwd_btns[17] = team17;
        kwd_btns[18] = team18;
        kwd_btns[19] = team19;
        kwd_btns[20] = arsenal0;
        kwd_btns[21] = arsenal1;
        kwd_btns[22] = arsenal2;
        kwd_btns[23] = arsenal3;
        kwd_btns[24] = arsenal4;
        kwd_btns[25] = arsenal5;
        kwd_btns[26] = arsenal6;
        kwd_btns[27] = arsenal7;
        kwd_btns[28] = arsenal8;
        kwd_btns[29] = arsenal9;
        kwd_btns[30] = arsenal10;
        kwd_btns[31] = arsenal11;
        kwd_btns[32] = arsenal12;
        kwd_btns[33] = arsenal13;
        kwd_btns[34] = arsenal14;
        kwd_btns[35] = arsenal15;
        kwd_btns[36] = arsenal16;
        kwd_btns[37] = arsenal17;
        kwd_btns[38] = arsenal18;
        kwd_btns[39] = arsenal19;
        kwd_btns[40] = arsenal20;
        kwd_btns[41] = arsenal21;
        kwd_btns[42] = arsenal22;
        kwd_btns[43] = arsenal23;
        kwd_btns[44] = arsenal24;
        kwd_btns[45] = villa0;
        kwd_btns[46] = villa1;
        kwd_btns[47] = villa2;
        kwd_btns[48] = villa3;
        kwd_btns[49] = villa4;
        kwd_btns[50] = villa5;
        kwd_btns[51] = villa6;
        kwd_btns[52] = villa7;
        kwd_btns[53] = villa8;
        kwd_btns[54] = villa9;
        kwd_btns[55] = villa10;
        kwd_btns[56] = villa11;
        kwd_btns[57] = villa12;
        kwd_btns[58] = villa13;
        kwd_btns[59] = villa14;
        kwd_btns[60] = villa15;
        kwd_btns[61] = villa16;
        kwd_btns[62] = villa17;
        kwd_btns[63] = villa18;
        kwd_btns[64] = villa19;
        kwd_btns[65] = villa20;
        kwd_btns[66] = villa21;
        kwd_btns[67] = villa22;
        kwd_btns[68] = villa23;
        kwd_btns[69] = villa24;
        kwd_btns[70] = born0;
        kwd_btns[71] = born1;
        kwd_btns[72] = born2;
        kwd_btns[73] = born3;
        kwd_btns[74] = born4;
        kwd_btns[75] = born5;
        kwd_btns[76] = born6;
        kwd_btns[77] = born7;
        kwd_btns[78] = born8;
        kwd_btns[79] = born9;
        kwd_btns[80] = born10;
        kwd_btns[81] = born11;
        kwd_btns[82] = born12;
        kwd_btns[83] = born13;
        kwd_btns[84] = born14;
        kwd_btns[85] = born15;
        kwd_btns[86] = born16;
        kwd_btns[87] = born17;
        kwd_btns[88] = born18;
        kwd_btns[89] = born19;
        kwd_btns[90] = born20;
        kwd_btns[91] = born21;
        kwd_btns[92] = born22;
        kwd_btns[93] = born23;
        kwd_btns[94] = born24;
        kwd_btns[95] = born25;
        kwd_btns[96] = born26;
        kwd_btns[97] = brighton0;
        kwd_btns[98] = brighton1;
        kwd_btns[99] = brighton2;
        kwd_btns[100] = brighton3;
        kwd_btns[101] = brighton4;
        kwd_btns[102] = brighton5;
        kwd_btns[103] = brighton6;
        kwd_btns[104] = brighton7;
        kwd_btns[105] = brighton8;
        kwd_btns[106] = brighton9;
        kwd_btns[107] = brighton10;
        kwd_btns[108] = brighton11;
        kwd_btns[109] = brighton12;
        kwd_btns[110] = brighton13;
        kwd_btns[111] = brighton14;
        kwd_btns[112] = brighton15;
        kwd_btns[113] = brighton16;
        kwd_btns[114] = brighton17;
        kwd_btns[115] = brighton18;
        kwd_btns[116] = brighton19;
        kwd_btns[117] = brighton20;
        kwd_btns[118] = brighton21;
        kwd_btns[119] = brighton22;
        kwd_btns[120] = brighton23;
        kwd_btns[121] = brighton24;
        kwd_btns[122] = brighton25;
        kwd_btns[123] = brighton26;
        kwd_btns[124] = burnley0;
        kwd_btns[125] = burnley1;
        kwd_btns[126] = burnley2;
        kwd_btns[127] = burnley3;
        kwd_btns[128] = burnley4;
        kwd_btns[129] = burnley5;
        kwd_btns[130] = burnley6;
        kwd_btns[131] = burnley7;
        kwd_btns[132] = burnley8;
        kwd_btns[133] = burnley9;
        kwd_btns[134] = burnley10;
        kwd_btns[135] = burnley11;
        kwd_btns[136] = burnley12;
        kwd_btns[137] = burnley13;
        kwd_btns[138] = burnley14;
        kwd_btns[139] = burnley15;
        kwd_btns[140] = burnley16;
        kwd_btns[141] = burnley17;
        kwd_btns[142] = burnley18;
        kwd_btns[143] = burnley19;
        kwd_btns[144] = burnley20;
        kwd_btns[145] = burnley21;
        kwd_btns[146] = burnley22;
        kwd_btns[147] = burnley23;
        kwd_btns[148] = chelsea0;
        kwd_btns[149] = chelsea1;
        kwd_btns[150] = chelsea2;
        kwd_btns[151] = chelsea3;
        kwd_btns[152] = chelsea4;
        kwd_btns[153] = chelsea5;
        kwd_btns[154] = chelsea6;
        kwd_btns[155] = chelsea7;
        kwd_btns[156] = chelsea8;
        kwd_btns[157] = chelsea9;
        kwd_btns[158] = chelsea10;
        kwd_btns[159] = chelsea11;
        kwd_btns[160] = chelsea12;
        kwd_btns[161] = chelsea13;
        kwd_btns[162] = chelsea14;
        kwd_btns[163] = chelsea15;
        kwd_btns[164] = chelsea16;
        kwd_btns[165] = chelsea17;
        kwd_btns[166] = chelsea18;
        kwd_btns[167] = chelsea19;
        kwd_btns[168] = chelsea20;
        kwd_btns[169] = chelsea21;
        kwd_btns[170] = chelsea22;
        kwd_btns[171] = chelsea23;
        kwd_btns[172] = chelsea24;
        kwd_btns[173] = crystal0;
        kwd_btns[174] = crystal1;
        kwd_btns[175] = crystal2;
        kwd_btns[176] = crystal3;
        kwd_btns[177] = crystal4;
        kwd_btns[178] = crystal5;
        kwd_btns[179] = crystal6;
        kwd_btns[180] = crystal7;
        kwd_btns[181] = crystal8;
        kwd_btns[182] = crystal9;
        kwd_btns[183] = crystal10;
        kwd_btns[184] = crystal11;
        kwd_btns[185] = crystal12;
        kwd_btns[186] = crystal13;
        kwd_btns[187] = crystal14;
        kwd_btns[188] = crystal15;
        kwd_btns[189] = crystal16;
        kwd_btns[190] = crystal17;
        kwd_btns[191] = crystal18;
        kwd_btns[192] = crystal19;
        kwd_btns[193] = crystal20;
        kwd_btns[194] = crystal21;
        kwd_btns[195] = crystal22;
        kwd_btns[196] = crystal23;
        kwd_btns[197] = everton0;
        kwd_btns[198] = everton1;
        kwd_btns[199] = everton2;
        kwd_btns[200] = everton3;
        kwd_btns[201] = everton4;
        kwd_btns[202] = everton5;
        kwd_btns[203] = everton6;
        kwd_btns[204] = everton7;
        kwd_btns[205] = everton8;
        kwd_btns[206] = everton9;
        kwd_btns[207] = everton10;
        kwd_btns[208] = everton11;
        kwd_btns[209] = everton12;
        kwd_btns[210] = everton13;
        kwd_btns[211] = everton14;
        kwd_btns[212] = everton15;
        kwd_btns[213] = everton16;
        kwd_btns[214] = everton17;
        kwd_btns[215] = everton18;
        kwd_btns[216] = everton19;
        kwd_btns[217] = everton20;
        kwd_btns[218] = everton21;
        kwd_btns[219] = everton22;
        kwd_btns[220] = everton23;
        kwd_btns[221] = everton24;
        kwd_btns[222] = everton25;
        kwd_btns[223] = leicester0;
        kwd_btns[224] = leicester1;
        kwd_btns[225] = leicester2;
        kwd_btns[226] = leicester3;
        kwd_btns[227] = leicester4;
        kwd_btns[228] = leicester5;
        kwd_btns[229] = leicester6;
        kwd_btns[230] = leicester7;
        kwd_btns[231] = leicester8;
        kwd_btns[232] = leicester9;
        kwd_btns[233] = leicester10;
        kwd_btns[234] = leicester11;
        kwd_btns[235] = leicester12;
        kwd_btns[236] = leicester13;
        kwd_btns[237] = leicester14;
        kwd_btns[238] = leicester15;
        kwd_btns[239] = leicester16;
        kwd_btns[240] = leicester17;
        kwd_btns[241] = leicester18;
        kwd_btns[242] = leicester19;
        kwd_btns[243] = leicester20;
        kwd_btns[244] = leicester21;
        kwd_btns[245] = leicester22;
        kwd_btns[246] = leicester23;
        kwd_btns[247] = leicester24;
        kwd_btns[248] = leicester25;
        kwd_btns[249] = leicester26;
        kwd_btns[250] = liverpool0;
        kwd_btns[251] = liverpool1;
        kwd_btns[252] = liverpool2;
        kwd_btns[253] = liverpool3;
        kwd_btns[254] = liverpool4;
        kwd_btns[255] = liverpool5;
        kwd_btns[256] = liverpool6;
        kwd_btns[257] = liverpool7;
        kwd_btns[258] = liverpool8;
        kwd_btns[259] = liverpool9;
        kwd_btns[260] = liverpool10;
        kwd_btns[261] = liverpool11;
        kwd_btns[262] = liverpool12;
        kwd_btns[263] = liverpool13;
        kwd_btns[264] = liverpool14;
        kwd_btns[265] = liverpool15;
        kwd_btns[266] = liverpool16;
        kwd_btns[267] = liverpool17;
        kwd_btns[268] = liverpool18;
        kwd_btns[269] = liverpool19;
        kwd_btns[270] = liverpool20;
        kwd_btns[271] = liverpool21;
        kwd_btns[272] = liverpool22;
        kwd_btns[273] = mancity0;
        kwd_btns[274] = mancity1;
        kwd_btns[275] = mancity2;
        kwd_btns[276] = mancity3;
        kwd_btns[277] = mancity4;
        kwd_btns[278] = mancity5;
        kwd_btns[279] = mancity6;
        kwd_btns[280] = mancity7;
        kwd_btns[281] = mancity8;
        kwd_btns[282] = mancity9;
        kwd_btns[283] = mancity10;
        kwd_btns[284] = mancity11;
        kwd_btns[285] = mancity12;
        kwd_btns[286] = mancity13;
        kwd_btns[287] = mancity14;
        kwd_btns[288] = mancity15;
        kwd_btns[289] = mancity16;
        kwd_btns[290] = mancity17;
        kwd_btns[291] = mancity18;
        kwd_btns[292] = mancity19;
        kwd_btns[293] = mancity20;
        kwd_btns[294] = mancity21;
        kwd_btns[295] = mancity22;
        kwd_btns[296] = manu0;
        kwd_btns[297] = manu1;
        kwd_btns[298] = manu2;
        kwd_btns[299] = manu3;
        kwd_btns[300] = manu4;
        kwd_btns[301] = manu5;
        kwd_btns[302] = manu6;
        kwd_btns[303] = manu7;
        kwd_btns[304] = manu8;
        kwd_btns[305] = manu9;
        kwd_btns[306] = manu10;
        kwd_btns[307] = manu11;
        kwd_btns[308] = manu12;
        kwd_btns[309] = manu13;
        kwd_btns[310] = manu14;
        kwd_btns[311] = manu15;
        kwd_btns[312] = manu16;
        kwd_btns[313] = manu17;
        kwd_btns[314] = manu18;
        kwd_btns[315] = manu19;
        kwd_btns[316] = manu20;
        kwd_btns[317] = manu21;
        kwd_btns[318] = manu22;
        kwd_btns[319] = manu23;
        kwd_btns[320] = manu24;
        kwd_btns[321] = new0;
        kwd_btns[322] = new1;
        kwd_btns[323] = new2;
        kwd_btns[324] = new3;
        kwd_btns[325] = new4;
        kwd_btns[326] = new5;
        kwd_btns[327] = new6;
        kwd_btns[328] = new7;
        kwd_btns[329] = new8;
        kwd_btns[330] = new9;
        kwd_btns[331] = new10;
        kwd_btns[332] = new11;
        kwd_btns[333] = new12;
        kwd_btns[334] = new13;
        kwd_btns[335] = new14;
        kwd_btns[336] = new15;
        kwd_btns[337] = new16;
        kwd_btns[338] = new17;
        kwd_btns[339] = new18;
        kwd_btns[340] = new19;
        kwd_btns[341] = new20;
        kwd_btns[342] = new21;
        kwd_btns[343] = new22;
        kwd_btns[344] = new23;
        kwd_btns[345] = new24;
        kwd_btns[346] = new25;
        kwd_btns[347] = new26;
        kwd_btns[348] = new27;
        kwd_btns[349] = norwich0;
        kwd_btns[350] = norwich1;
        kwd_btns[351] = norwich2;
        kwd_btns[352] = norwich3;
        kwd_btns[353] = norwich4;
        kwd_btns[354] = norwich5;
        kwd_btns[355] = norwich6;
        kwd_btns[356] = norwich7;
        kwd_btns[357] = norwich8;
        kwd_btns[358] = norwich9;
        kwd_btns[359] = norwich10;
        kwd_btns[360] = norwich11;
        kwd_btns[361] = norwich12;
        kwd_btns[362] = norwich13;
        kwd_btns[363] = norwich14;
        kwd_btns[364] = norwich15;
        kwd_btns[365] = norwich16;
        kwd_btns[366] = norwich17;
        kwd_btns[367] = norwich18;
        kwd_btns[368] = norwich19;
        kwd_btns[369] = norwich20;
        kwd_btns[370] = norwich21;
        kwd_btns[371] = norwich22;
        kwd_btns[372] = norwich23;
        kwd_btns[373] = norwich24;
        kwd_btns[374] = shef0;
        kwd_btns[375] = shef1;
        kwd_btns[376] = shef2;
        kwd_btns[377] = shef3;
        kwd_btns[378] = shef4;
        kwd_btns[379] = shef5;
        kwd_btns[380] = shef6;
        kwd_btns[381] = shef7;
        kwd_btns[382] = shef8;
        kwd_btns[383] = shef9;
        kwd_btns[384] = shef10;
        kwd_btns[385] = shef11;
        kwd_btns[386] = shef12;
        kwd_btns[387] = shef13;
        kwd_btns[388] = shef14;
        kwd_btns[389] = shef15;
        kwd_btns[390] = shef16;
        kwd_btns[391] = shef17;
        kwd_btns[392] = shef18;
        kwd_btns[393] = shef19;
        kwd_btns[394] = shef20;
        kwd_btns[395] = shef21;
        kwd_btns[396] = shef22;
        kwd_btns[397] = shef23;
        kwd_btns[398] = shef24;
        kwd_btns[399] = shef25;
        kwd_btns[400] = south0;
        kwd_btns[401] = south1;
        kwd_btns[402] = south2;
        kwd_btns[403] = south3;
        kwd_btns[404] = south4;
        kwd_btns[405] = south5;
        kwd_btns[406] = south6;
        kwd_btns[407] = south7;
        kwd_btns[408] = south8;
        kwd_btns[409] = south9;
        kwd_btns[410] = south10;
        kwd_btns[411] = south11;
        kwd_btns[412] = south12;
        kwd_btns[413] = south13;
        kwd_btns[414] = south14;
        kwd_btns[415] = south15;
        kwd_btns[416] = south16;
        kwd_btns[417] = south17;
        kwd_btns[418] = south18;
        kwd_btns[419] = south19;
        kwd_btns[420] = south20;
        kwd_btns[421] = south21;
        kwd_btns[422] = totten0;
        kwd_btns[423] = totten1;
        kwd_btns[424] = totten2;
        kwd_btns[425] = totten3;
        kwd_btns[426] = totten4;
        kwd_btns[427] = totten5;
        kwd_btns[428] = totten6;
        kwd_btns[429] = totten7;
        kwd_btns[430] = totten8;
        kwd_btns[431] = totten9;
        kwd_btns[432] = totten10;
        kwd_btns[433] = totten11;
        kwd_btns[434] = totten12;
        kwd_btns[435] = totten13;
        kwd_btns[436] = totten14;
        kwd_btns[437] = totten15;
        kwd_btns[438] = totten16;
        kwd_btns[439] = totten17;
        kwd_btns[440] = totten18;
        kwd_btns[441] = totten19;
        kwd_btns[442] = totten20;
        kwd_btns[443] = totten21;
        kwd_btns[444] = totten22;
        kwd_btns[445] = totten23;
        kwd_btns[446] = totten24;
        kwd_btns[447] = watford0;
        kwd_btns[448] = watford1;
        kwd_btns[449] = watford2;
        kwd_btns[450] = watford3;
        kwd_btns[451] = watford4;
        kwd_btns[452] = watford5;
        kwd_btns[453] = watford6;
        kwd_btns[454] = watford7;
        kwd_btns[455] = watford8;
        kwd_btns[456] = watford9;
        kwd_btns[457] = watford10;
        kwd_btns[458] = watford11;
        kwd_btns[459] = watford12;
        kwd_btns[460] = watford13;
        kwd_btns[461] = watford14;
        kwd_btns[462] = watford15;
        kwd_btns[463] = watford16;
        kwd_btns[464] = watford17;
        kwd_btns[465] = watford18;
        kwd_btns[466] = watford19;
        kwd_btns[467] = watford20;
        kwd_btns[468] = watford21;
        kwd_btns[469] = watford22;
        kwd_btns[470] = watford23;
        kwd_btns[471] = watford24;
        kwd_btns[472] = watford25;
        kwd_btns[473] = watford26;
        kwd_btns[474] = watford27;
        kwd_btns[475] = watford28;
        kwd_btns[476] = west0;
        kwd_btns[477] = west1;
        kwd_btns[478] = west2;
        kwd_btns[479] = west3;
        kwd_btns[480] = west4;
        kwd_btns[481] = west5;
        kwd_btns[482] = west6;
        kwd_btns[483] = west7;
        kwd_btns[484] = west8;
        kwd_btns[485] = west9;
        kwd_btns[486] = west10;
        kwd_btns[487] = west11;
        kwd_btns[488] = west12;
        kwd_btns[489] = west13;
        kwd_btns[490] = west14;
        kwd_btns[491] = west15;
        kwd_btns[492] = west16;
        kwd_btns[493] = west17;
        kwd_btns[494] = west18;
        kwd_btns[495] = west19;
        kwd_btns[496] = west20;
        kwd_btns[497] = west21;
        kwd_btns[498] = west22;
        kwd_btns[499] = west23;
        kwd_btns[500] = wolves0;
        kwd_btns[501] = wolves1;
        kwd_btns[502] = wolves2;
        kwd_btns[503] = wolves3;
        kwd_btns[504] = wolves4;
        kwd_btns[505] = wolves5;
        kwd_btns[506] = wolves6;
        kwd_btns[507] = wolves7;
        kwd_btns[508] = wolves8;
        kwd_btns[509] = wolves9;
        kwd_btns[510] = wolves10;
        kwd_btns[511] = wolves11;
        kwd_btns[512] = wolves12;
        kwd_btns[513] = wolves13;
        kwd_btns[514] = wolves14;
        kwd_btns[515] = wolves15;
        kwd_btns[516] = wolves16;
        kwd_btns[517] = wolves17;
        kwd_btns[518] = wolves18;
        kwd_btns[519] = wolves19;
        kwd_btns[520] = wolves20;
        kwd_btns[521] = wolves21;
        kwd_btns[522] = wolves22;
        kwd_btns[523] = wolves23;
        kwd_btns[524] = wolves24;
        kwd_btns[525] = manager0;
        kwd_btns[526] = manager1;
        kwd_btns[527] = manager2;
        kwd_btns[528] = manager3;
        kwd_btns[529] = manager4;
        kwd_btns[530] = manager5;
        kwd_btns[531] = manager6;
        kwd_btns[532] = manager7;
        kwd_btns[533] = manager8;
        kwd_btns[534] = manager9;
        kwd_btns[535] = manager10;
        kwd_btns[536] = manager11;
        kwd_btns[537] = manager12;
        kwd_btns[538] = manager13;
        kwd_btns[539] = manager14;
        kwd_btns[540] = manager15;
        kwd_btns[541] = manager16;
        kwd_btns[542] = manager17;
        kwd_btns[543] = manager18;
        kwd_btns[544] = manager19;


    }

    private void setTeamBtn(){
        kwd_id[0] = R.id.arsenal_btn;
        kwd_id[1] = R.id.villa_btn;
        kwd_id[2] = R.id.bournemouth_btn;
        kwd_id[3] = R.id.brighton_btn;
        kwd_id[4] = R.id.burnley_btn;
        kwd_id[5] = R.id.chelsea_btn;
        kwd_id[6] = R.id.crystal_btn;
        kwd_id[7] = R.id.everton_btn;
        kwd_id[8] = R.id.leicester_btn;
        kwd_id[9] = R.id.liverpool_btn;
        kwd_id[10] = R.id.mancity_btn;
        kwd_id[11] = R.id.manu_btn;
        kwd_id[12] = R.id.newcastle_btn;
        kwd_id[13] = R.id.norwich_btn;
        kwd_id[14] = R.id.sheffield_btn;
        kwd_id[15] = R.id.southampton_btn;
        kwd_id[16] = R.id.tottenham_btn;
        kwd_id[17] = R.id.watford_btn;
        kwd_id[18] = R.id.westham_btn;
        kwd_id[19] = R.id.wolves_btn;

        keywords[0] = "Arsenal";
        keywords[1] = "Aston Villa";
        keywords[2] = "Bournemouth";
        keywords[3] = "Brighton & Hove Albion";
        keywords[4] = "Burnley";
        keywords[5] = "Chelsea";
        keywords[6] = "Crystal Palace";
        keywords[7] = "Everton";
        keywords[8] = "Leicester City";
        keywords[9] = "Liverpool";
        keywords[10] = "Manchester City";
        keywords[11] = "Manchester United";
        keywords[12] = "Newcastle United";
        keywords[13] = "Norwich City";
        keywords[14] = "Sheffield United";
        keywords[15] = "Southampton";
        keywords[16] = "Tottenham Hotspur";
        keywords[17] = "Watford";
        keywords[18] = "West Ham United";
        keywords[19] = "Wolverhampton Wanderers";
        // Arsenal 추가
        int t = 0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "arsenal" + t + "_btn";
            String id_name2 = "arsenal" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+20;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "villa" + t + "_btn";
            String id_name2 = "villa" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+45;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<27;i++){
            t++;
            String id_name = "born" + t + "_btn";
            String id_name2 = "born" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+70;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<27;i++){
            t++;
            String id_name = "brighton" + t + "_btn";
            String id_name2 = "brighton" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+97;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<24;i++){
            t++;
            String id_name = "burnley" + t + "_btn";
            String id_name2 = "burnley" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+124;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "chelsea" + t + "_btn";
            String id_name2 = "chelsea" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+148;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<24;i++){
            t++;
            String id_name = "crystal" + t + "_btn";
            String id_name2 = "crystal" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+173;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<26;i++){
            t++;
            String id_name = "everton" + t + "_btn";
            String id_name2 = "everton" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+197;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<27;i++){
            t++;
            String id_name = "leicester" + t + "_btn";
            String id_name2 = "leicester" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+223;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<23;i++){
            t++;
            String id_name = "liverpool" + t + "_btn";
            String id_name2 = "liverpool" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+250;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<23;i++){
            t++;
            String id_name = "mancity" + t + "_btn";
            String id_name2 = "mancity" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+273;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "manu" + t + "_btn";
            String id_name2 = "manu" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+296;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<28;i++){
            t++;
            String id_name = "new" + t + "_btn";
            String id_name2 = "new" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+321;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "norwich" + t + "_btn";
            String id_name2 = "norwich" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+349;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<26;i++){
            t++;
            String id_name = "shef" + t + "_btn";
            String id_name2 = "shef" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+374;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<22;i++){
            t++;
            String id_name = "south" + t + "_btn";
            String id_name2 = "south" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+400;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "totten" + t + "_btn";
            String id_name2 = "totten" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+422;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<29;i++){
            t++;
            String id_name = "watford" + t + "_btn";
            String id_name2 = "watford" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+447;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<24;i++){
            t++;
            String id_name = "west" + t + "_btn";
            String id_name2 = "west" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+476;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<25;i++){
            t++;
            String id_name = "wolves" + t + "_btn";
            String id_name2 = "wolves" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+500;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
        t=0;
        for (int i=0;i<20;i++){
            t++;
            String id_name = "manager" + t + "_btn";
            String id_name2 = "manager" + t + "_tv";
            int resId = getResources().getIdentifier(id_name,"id",getPackageName());
            int resId2 = getResources().getIdentifier(id_name2,"id",getPackageName());
            int index = i+525;
            kwd_btns[index] = (ImageButton)findViewById(resId);
            kwd_id[index] = resId;
            TextView temp = (TextView)findViewById(resId2);
            keywords[index] = (String)temp.getText();
        }
    }

    private void setOnClickBtn(){
        for(int i=0; i<kwd_btns.length; i++){
            kwd_btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if (v.isSelected()){
                        for(int k=0; k<kwd_btns.length; k++){
                            if(id == kwd_id[k]){
                                deleteKeyword(k);
                            }
                        }
                        v.setSelected(false);
                    }
                    else{
                        for(int k=0; k<kwd_btns.length; k++){
                            if(id == kwd_id[k]){
                                addKeyword(k);
                            }
                        }
                    }
                }
            });
        }
    }

    private void addKeyword(int i){
        if(user_keywords.size() < 3){
            user_keywords.add(keywords[i]);
            kwd_btns[i].setSelected(true);
            Toast.makeText(getApplicationContext(), "keyword add " + keywords[i] , Toast.LENGTH_SHORT).show();
        }
        else if(user_keywords.size() >= 3){
            Toast.makeText(getApplicationContext(), "keyword limit: 3", Toast.LENGTH_SHORT).show();
            return;
        }
        return;
    }
    private void deleteKeyword(int i){
        String target = keywords[i];
        for(int k=0; k< user_keywords.size(); k++){
            if(user_keywords.get(k).equals(target)){
                user_keywords.remove(k);
                Toast.makeText(getApplicationContext(), "keyword delete " + target , Toast.LENGTH_SHORT).show();
            }
        }
    }


}
