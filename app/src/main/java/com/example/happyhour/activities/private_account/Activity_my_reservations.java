package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.OrdersAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Order;
import com.example.happyhour.objects.Table;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Activity_my_reservations extends AppCompatActivity {

    private RecyclerView order_LST_tables;
    private OrdersAdapter ordersAdapter;
    private MaterialToolbar toolbar;
    private ArrayList<Bar> bars_from_db = new ArrayList<>();
    private ArrayList<Bar> barsToAdapter = new ArrayList<>();
    private HashMap<String , Order> bars_I_order = new HashMap<>();
    private int num_of_bars_from_DB;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private FloatingActionButton fab_search;
    private BottomNavigationView nav_view;
    private LottieAnimationView loading_animation_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);
        loading_animation_view = findViewById(R.id.loading_animation_view);
        init_toolbar();
        findViews();
        MyDB.getInstance().setCallback_get_bars(callback_get_bars);
        bars_I_order = DataManager.getDataManager().getPrivateAccount().getOrders();
        bars_I_order.forEach( (order_id , order) ->  MyDB.getInstance().get_bar(order.getBarId()));

    }
    private Callback_get_bars callback_get_bars = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
        }
        @Override
        public void get_bar(Bar bar) {
            if(!(bars_from_db.contains(bar)))
                bars_from_db.add(bar);
            num_of_bars_from_DB++;
            if(num_of_bars_from_DB == bars_I_order.size()){
                loading_animation_view.cancelAnimation();
                loading_animation_view.setVisibility(View.GONE);
                init_page();
            }

        }
        @Override
        public void failed() {
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

    private void init_page() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR , 0);
        cal.set(Calendar.MINUTE , 0);
        String date_str = simpleDateFormat.format(cal.getTime());
        Date date = cal.getTime();
        bars_from_db.forEach(bar -> {
            bar.getTables().values().forEach
                    (table -> table.getOrders().forEach((order_id, order) -> {
                        if (order.getDate().compareTo(date) < 0
                                &&  !(simpleDateFormat.format(order.getDate()).equals(date_str))) {
                            MyDB.getInstance().remove_order(bar, table.getId(), order);
                            return;
                        }
                        if (order.getUser_id().equals(DataManager.getDataManager().getPrivateAccount().getId())) {
                            Table table_for_adapter = new Table()
                                    .setNumOfPlaces(table.getNumOfPlaces())
                                    .setDescription(table.getDescription())
                                    .setName(table.getName())
                                    .addOrder(OrdersAdapter.ORDER_KEY, order);
                            //todo add photo
                            barsToAdapter.add(new Bar()
                                    .setName(bar.getName())
                                    .setHappy_hour(bar.getHappy_hour())
                                    .addTable(OrdersAdapter.TABLE_KEY, table_for_adapter));

                        }

                    }));
        });

        ordersAdapter = new OrdersAdapter(this , barsToAdapter);
        order_LST_tables.setAdapter(ordersAdapter);

    }

    private void findViews() {
        order_LST_tables = findViewById(R.id.order_LST_tables);
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
                    go_next(Activity_customer_main_page.class);
                }
                else if(item.getItemId() == R.id.action_follow){
                    go_next(Activity_follow_bar.class);
                }
                else if(item.getItemId() == R.id.action_order){
                    //stay here
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
        MyServices.getInstance().makeToast("logout...");
        DataManager.getDataManager().logout();
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }
}