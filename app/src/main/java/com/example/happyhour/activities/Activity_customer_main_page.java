package com.example.happyhour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.adapters.CustomerBarsAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// TODO: 23/06/2022 add toolbar features
// TODO: 23/06/2022 look all prob
// TODO: 23/06/2022 upload photo
// TODO: 23/06/2022 add favorite bar ytpe?
public class Activity_customer_main_page extends AppCompatActivity {
    private RecyclerView customer_menu_LST_hot_today;
    private RecyclerView customer_menu_LST_wine_bar;
    private RecyclerView customer_menu_LST_dance_bar;
    private ArrayList<Bar> hot_today;
    private ArrayList<Bar> wine_bar;
    private ArrayList<Bar> dance_bar;
    private MaterialToolbar toolbar;
    private CustomerBarsAdapter hotTodayAdapter;
    private CustomerBarsAdapter wineBarAdapter;
    private CustomerBarsAdapter danceBarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_page);

        init_toolbar();
        findViews();
        MyDB.getInstance().setCallback_get_bars(callback_get_bars);
        MyDB.getInstance().get_bars();

    }

    private void findViews() {
        customer_menu_LST_hot_today = findViewById(R.id.customer_menu_LST_hot_today);
        customer_menu_LST_wine_bar = findViewById(R.id.customer_menu_LST_wine_bar);
        customer_menu_LST_dance_bar = findViewById(R.id.customer_menu_LST_dance_bar);
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
            if (bar.getFollowers().containsKey(userId)) { //unfollow
                bar.getFollowers().remove(userId);
                DataManager.getDataManager().remove_follower(bar, userId);
            } else {
                bar.setFollower(userId, userName);
                DataManager.getDataManager().add_follower(bar, userId, userName);
            }
            if_contain_change(wine_bar, wineBarAdapter, bar);
            if_contain_change(hot_today, hotTodayAdapter, bar);
            if_contain_change(dance_bar, danceBarAdapter, bar);
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
            init_adapters(bars);
        }

        @Override
        public void get_bar(Bar bar) {
        }
        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

    private void init_adapters(HashMap<String, Bar> bars) {
        hot_today = get_bars_by_hot_today(new ArrayList<>(bars.values()));
        wine_bar = get_bars_by_bar_type(new ArrayList<>(bars.values()), eBarType.Wine_Bar);
        dance_bar = get_bars_by_bar_type(new ArrayList<>(bars.values()), eBarType.Dance_Bar);


        hotTodayAdapter = new CustomerBarsAdapter(this, hot_today).setBarlistener(barlistener);
        wineBarAdapter = new CustomerBarsAdapter(this, wine_bar).setBarlistener(barlistener);
        danceBarAdapter = new CustomerBarsAdapter(this, dance_bar).setBarlistener(barlistener);


        LinearLayoutManager layoutManager_hot_today = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_wine_bar = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_dance_bar = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        customer_menu_LST_hot_today.setLayoutManager(layoutManager_hot_today);
        customer_menu_LST_wine_bar.setLayoutManager(layoutManager_wine_bar);
        customer_menu_LST_dance_bar.setLayoutManager(layoutManager_dance_bar);

        customer_menu_LST_hot_today.setAdapter(hotTodayAdapter);
        customer_menu_LST_wine_bar.setAdapter(wineBarAdapter);
        customer_menu_LST_dance_bar.setAdapter(danceBarAdapter);
    }

    private ArrayList<Bar> get_bars_by_bar_type(ArrayList<Bar> bars, eBarType barType) {
        Collections.sort(bars,(bar1, bar2) -> {
                    if(bar1.getBarType() == barType)
                        return -1;
                    if(bar2.getBarType() == barType)
                        return 1;
                    return 0;
        });
        ArrayList<Bar> ten_bars = new ArrayList<>();
        for (int i = 0; i <  bars.size() && bars.get(i).getBarType() == barType && i<10; i++) {
            ten_bars.add(bars.get(i));
        }
        return ten_bars;
    }

    private ArrayList<Bar> get_bars_by_hot_today(ArrayList<Bar> bars) {
        Collections.sort(bars, (bar1, bar2) -> {
            float result =  bar2.starsAvg() - bar1.starsAvg();
            if(result == 0)
                return (int)result;
            return (int)result;
        });
        ArrayList<Bar> ten_best_bars = new ArrayList<>();
        for (int i = 0; i < bars.size() && i<10; i++) {
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
            DataManager.getDataManager().logout();
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