package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_bar_details;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.CustomerBarsAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Follower;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Activity_customer_main_page extends AppCompatActivity {
    private RecyclerView customer_menu_LST_hot_today;
    private RecyclerView customer_menu_LST_fab1;
    private RecyclerView customer_menu_LST_fab2;
    private ArrayList<Bar> hot_today;
    private ArrayList<Bar> fav_1;
    private ArrayList<Bar> fav_2;
    private MaterialToolbar toolbar;
    private CustomerBarsAdapter hotTodayAdapter;
    private CustomerBarsAdapter fav_1_adapter;
    private CustomerBarsAdapter fav_2_adapter;
    private MaterialTextView customer_menu_LBL_fav1;
    private MaterialTextView customer_menu_LBL_fav2;
    private FloatingActionButton fab_search;
    private BottomNavigationView nav_view;
    private LottieAnimationView loading_animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_page);

        loading_animation_view = findViewById(R.id.loading_animation_view);

        init_toolbar();
        findViews();
        MyDB.getInstance().setCallback_get_bars(callback_get_bars);
        MyDB.getInstance().get_bars();

    }

    private void findViews() {
        customer_menu_LST_hot_today = findViewById(R.id.customer_menu_LST_hot_today);
        customer_menu_LST_fab1 = findViewById(R.id.customer_menu_LST_fab1);
        customer_menu_LST_fab2 = findViewById(R.id.customer_menu_LST_fab2);
        customer_menu_LBL_fav1 = findViewById(R.id.customer_menu_LBL_fav1);
        customer_menu_LBL_fav2 = findViewById(R.id.customer_menu_LBL_fav2);
    }


    private CustomerBarsAdapter.Barlistener barlistener = new CustomerBarsAdapter.Barlistener() {
        @Override
        public void clicked(Bar bar, int position) {
            Bundle bundle = new Bundle();
            bundle.putString(DataManager.EXTRA_BAR, bar.getId());
            Intent intent = new Intent(Activity_customer_main_page.this, Activity_bar_details.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        @Override
        public void follow(Bar bar, int position) {
            String userId = DataManager.getDataManager().getPrivateAccount().getId();
            String userName = DataManager.getDataManager().getPrivateAccount().getName();
            String img = DataManager.getDataManager().getPrivateAccount().getImgUri();
            Follower follower = new Follower(userName,img);
            if (bar.getFollowers().containsKey(userId)) { //unfollow
                bar.getFollowers().remove(userId);
                DataManager.getDataManager().remove_follower(bar, userId);
            } else {
                bar.setFollower(userId, follower);
                DataManager.getDataManager().add_follower(bar, userId, follower);
            }
            if_contain_change(fav_1, fav_1_adapter, bar);
            if_contain_change(hot_today, hotTodayAdapter, bar);
            if_contain_change(fav_2, fav_2_adapter, bar);
        }
    };

    private void if_contain_change(ArrayList<Bar> barList, CustomerBarsAdapter barAdapter, Bar bar) {
        if (barList.contains(bar)) {
            barAdapter.notifyItemChanged(barList.indexOf(bar));
        }
    }

    private Callback_get_bars callback_get_bars = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
            init_adapters(bars);
        }

        @Override
        public void get_bar(Bar bar) {
        }

        @Override
        public void failed() {
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

    private void init_adapters(HashMap<String, Bar> bars) {
        hot_today = get_bars_by_hot_today(new ArrayList<>(bars.values()));

        eBarType barType1 = DataManager.getDataManager().getPrivateAccount().getFavorite_1();
        eBarType barType2 = DataManager.getDataManager().getPrivateAccount().getFavorite_2();
        fav_1 = get_bars_by_bar_type(new ArrayList<>(bars.values()),barType1);
        fav_2 = get_bars_by_bar_type(new ArrayList<>(bars.values()), barType2);
        customer_menu_LBL_fav1.setText(barType1.toString().replace('_',' ')+":");
        customer_menu_LBL_fav2.setText(barType2.toString().replace('_',' ')+":");

        hotTodayAdapter = new CustomerBarsAdapter(this, hot_today).setBarlistener(barlistener);
        fav_1_adapter = new CustomerBarsAdapter(this, fav_1).setBarlistener(barlistener);
        fav_2_adapter = new CustomerBarsAdapter(this, fav_2).setBarlistener(barlistener);


        LinearLayoutManager layoutManager_hot_today = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_fab1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_fab2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        customer_menu_LST_hot_today.setLayoutManager(layoutManager_hot_today);
        customer_menu_LST_fab1.setLayoutManager(layoutManager_fab1);
        customer_menu_LST_fab2.setLayoutManager(layoutManager_fab2);

        customer_menu_LST_hot_today.setAdapter(hotTodayAdapter);
        customer_menu_LST_fab1.setAdapter(fav_1_adapter);
        customer_menu_LST_fab2.setAdapter(fav_2_adapter);
    }

    private ArrayList<Bar> get_bars_by_bar_type(ArrayList<Bar> bars, eBarType barType) {
        Collections.sort(bars, (bar1, bar2) -> {
            if (bar1.getBarType() == barType)
                return -1;
            if (bar2.getBarType() == barType)
                return 1;
            return 0;
        });
        ArrayList<Bar> ten_bars = new ArrayList<>();
        for (int i = 0; i < bars.size() && bars.get(i).getBarType() == barType && i < 10; i++) {
            ten_bars.add(bars.get(i));
        }
        return ten_bars;
    }

    private ArrayList<Bar> get_bars_by_hot_today(ArrayList<Bar> bars) {
        Collections.sort(bars, (bar1, bar2) -> {
            float result = bar2.starsAvg() - bar1.starsAvg();
            if (result == 0)
                return (int) result;
            return (int) result;
        });
        ArrayList<Bar> ten_best_bars = new ArrayList<>();
        for (int i = 0; i < bars.size() && i < 10; i++) {
            ten_best_bars.add(bars.get(i));
        }
        return ten_best_bars;
    }

    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("");

        nav_view = findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_home){
                    //stay here
                }
                else if(item.getItemId() == R.id.action_follow){
                    go_next(Activity_follow_bar.class);
                }
                else if(item.getItemId() == R.id.action_order){
                    go_next(Activity_my_reservations.class);
                }
                else if(item.getItemId() == R.id.action_profile){
                    go_next(Activity_private_account_profile.class);
                }
                return false;
            }
        });
        fab_search = findViewById(R.id.fab_search);
        fab_search.setOnClickListener(view -> {
            go_next(Activity_customer_main_page.class); //todo change it!
        });
    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        DataManager.getDataManager().logout();
        MyServices.getInstance().makeToast("logout...");
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }

}