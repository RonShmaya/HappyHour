package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_bar_details;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.PrivateFollowingAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_follow_bar extends AppCompatActivity {
    private RecyclerView followers_LST_tables;
    private PrivateFollowingAdapter privateFollowingAdapter;
    private ArrayList<Bar> bars = new ArrayList<>();
    private HashMap<String , String> follow_bars;
    private MaterialToolbar toolbar;
    private FloatingActionButton fab_search;
    private BottomNavigationView nav_view;
    private LottieAnimationView loading_animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_bar);
        loading_animation_view = findViewById(R.id.loading_animation_view);
        init_toolbar();
        findViews();

        MyDB.getInstance().setCallback_get_bars(callback_get_bars);
        follow_bars = DataManager.getDataManager().getPrivateAccount().getFollow_bars();
        follow_bars.forEach( (f_id , f_name) ->  MyDB.getInstance().get_bar(f_id));
        if(follow_bars.size() == 0){
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        followers_LST_tables = findViewById(R.id.followers_LST_tables);
    }

    private Callback_get_bars callback_get_bars = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
        }
        @Override
        public void get_bar(Bar bar) {
            bars.add(bar);
            if(bars.size() == follow_bars.size()){
                loading_animation_view.cancelAnimation();
                loading_animation_view.setVisibility(View.GONE);
                init_adapter();
            }
        }
        @Override
        public void failed() {
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

    private void init_adapter() {
        privateFollowingAdapter = new PrivateFollowingAdapter(this , bars);
        privateFollowingAdapter.setBarlistener(barlistener);
        followers_LST_tables.setAdapter(privateFollowingAdapter);
    }
    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_google_maps);
        toolbar.setNavigationIconTint(Color.BLACK);
        toolbar.setNavigationOnClickListener(v -> {
            go_next(Activity_maps.class);
        });
        nav_view = findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_home){
                    go_next(Activity_customer_main_page.class);
                }
                else if(item.getItemId() == R.id.action_follow){
                    //stay here
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
            go_next(Activity_search.class);
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
        MyServices.getInstance().makeToast("logout...");
        DataManager.getDataManager().logout();
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }
    private PrivateFollowingAdapter.Barlistener barlistener = new PrivateFollowingAdapter.Barlistener() {
        @Override
        public void clicked(Bar bar, int position) {
            Bundle bundle = new Bundle();
            bundle.putString(DataManager.EXTRA_BAR, bar.getId());
            Intent intent = new Intent(Activity_follow_bar.this, Activity_bar_details.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };
}