package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_bar_details;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.TableAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.MyTime;
import com.example.happyhour.objects.Order;
import com.example.happyhour.objects.Table;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class Activity_reservation extends AppCompatActivity {
    private MaterialTextView reservation_LBL_open_time;
    private MaterialTextView reservation_LBL_close_time;
    private MaterialTextView reservation_LBL_date;
    private MaterialTextView reservation_LBL_time;
    private FloatingActionButton reservation_FAB_addDate;
    private FloatingActionButton reservation_FAB_addTime;
    private MaterialToolbar toolbar;
    private RecyclerView bar_tables_LST_tables;
    private TableAdapter tablesAdapter;
    private ArrayList<Table> tables = new ArrayList<>();
    private Date date = null;
    private MyTime time = null;
    private Bar bar;
    private String barId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Bundle bundle = getIntent().getExtras();
        barId = bundle.getString(DataManager.EXTRA_BAR);
        MyDB.getInstance().setCallback_get_bars(callback_get_bar);
        MyDB.getInstance().get_bar(barId);
        init_toolbar();
        findViews();

        tablesAdapter = new TableAdapter(this, tables, DataManager.getDataManager().getUserType());
        tablesAdapter.setCallback_table(callback_table);
        bar_tables_LST_tables.setAdapter(tablesAdapter);
    }

    private void findViews() {
        reservation_LBL_open_time = findViewById(R.id.reservation_LBL_open_time);
        reservation_LBL_close_time = findViewById(R.id.reservation_LBL_close_time);
        reservation_LBL_date = findViewById(R.id.reservation_LBL_date);
        reservation_FAB_addDate = findViewById(R.id.reservation_FAB_addDate);
        reservation_LBL_time = findViewById(R.id.reservation_LBL_time);
        reservation_FAB_addTime = findViewById(R.id.reservation_FAB_addTime);
        bar_tables_LST_tables = findViewById(R.id.bar_tables_LST_tables);
    }
    private MyServices.DatePickerFragment.Callback_date callback_date = new MyServices.DatePickerFragment.Callback_date() {
        @Override
        public void get_input_date(Date date) {
            Date date_now = new Date(System.currentTimeMillis());
            String date_now_str =  new SimpleDateFormat("dd / MM / yy", Locale.US).format(System.currentTimeMillis());
            String date_str =  new SimpleDateFormat("dd / MM / yy", Locale.US).format(date);

            if(!(date_now_str.equals(date_str)) && date.compareTo(date_now) < 0){
                MyServices.getInstance().makeToast("Date Must Be From\n" + date_now_str + " forward");
                return;
            }
            reservation_LBL_date.setText(date_str);
            Activity_reservation.this.date = date;
            if(time != null)
                search_for_tabels();
        }
    };

    private MyServices.TimePickerFragment.Callback_time callback_time_open = new MyServices.TimePickerFragment.Callback_time() {
        @Override
        public void get_input_time(MyTime time) {
          if(!(time.isBetweenByHours(bar.getOpenTime() , bar.getClosingTime()))){
              MyServices.getInstance().makeToast("the hour are not really between\n the business hours");
              return;
          }
            reservation_LBL_time.setText(time.toString());
            Activity_reservation.this.time = time;
            if(date != null)
                search_for_tabels();

        }
    };
    private Callback_get_bars callback_get_bar = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
        }
        @Override
        public void get_bar(Bar bar) {
            init_Views(bar);
        }
        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

    private void init_Views(Bar bar) {
        this.bar = bar;
        reservation_LBL_open_time.setText(this.bar.getOpenTime().toString());
        reservation_LBL_close_time.setText(this.bar.getClosingTime().toString());

        reservation_FAB_addDate.setOnClickListener(view -> {
            DialogFragment newFragment = new MyServices.DatePickerFragment();
            ((MyServices.DatePickerFragment)newFragment).setCallback_date(callback_date);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
        reservation_FAB_addTime.setOnClickListener(view -> {
            DialogFragment newFragment = new MyServices.TimePickerFragment();
            ((MyServices.TimePickerFragment) newFragment).setCallback_time(callback_time_open);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });


    }
    private void search_for_tabels() {
        if(bar.getClosingTime().compareTo(bar.getOpenTime()) == 0){
            MyServices.getInstance().makeToast("Sorry!,\nit's look the bar didn't defined reservation option yet");
            this.tables.clear();
            this.tablesAdapter.notifyDataSetChanged();
            return; // place didn't  defined time
        }
        if(bar.getTables().size() == 0){
            MyServices.getInstance().makeToast("Sorry!,\nit's look that the bar didn't defined reservation option yet ");
            return;
        }
        ArrayList<Table> tmp_tables = new ArrayList<>(bar.getTables().values());
        ArrayList<Table> tables_to_show = new ArrayList<>(bar.getTables().values());

        tmp_tables.forEach(table -> table.getOrders().forEach((k ,order) -> {
            if(order.getDate().toString().equals(this.date.toString())){
                MyTime end_time_to_order =  new MyTime()
                                .setHour(this.time.getHour()+2)
                                .setMinutes(this.time.getMinutes());
                MyTime start_time_to_order =  new MyTime()
                                .setHour(this.time.getHour())
                                .setMinutes(this.time.getMinutes());
                if(order.getMyTime().isBetweenByHours(start_time_to_order , end_time_to_order) && end_time_to_order.getHour()!=order.getMyTime().getHour()) {
                    tables_to_show.remove(table);
                }
            }
        }));

        if(tables_to_show.size() == 0){
            if(tables.size()!=0){
                tables.clear();
                this.tablesAdapter.notifyDataSetChanged();
            }
            MyServices.getInstance().makeToast("no result for your searching");
            return;
        }
        if(time.getHour() == bar.getOpenTime().getHour() && time.getMinutes() < bar.getOpenTime().getMinutes()){
            time.setMinutes(bar.getOpenTime().getMinutes());
        }
        if(time.getHour() == bar.getClosingTime().getHour()){
            MyServices.getInstance().makeToast("it's to close to closing time");
            this.tables.clear();
            this.tablesAdapter.notifyDataSetChanged();
            return;
        }
        this.tables.clear();
        this.tables.addAll(tables_to_show);
        this.tablesAdapter.setTimeOrder(this.time);
        this.tablesAdapter.notifyDataSetChanged();

    }
    private TableAdapter.Callback_table callback_table = new TableAdapter.Callback_table() {
        @Override
        public void action_button_clicked(Table table, int position) {
            Order order = new Order()
                    .setMyTime(time)
                    .setDate(date)
                    .setBarId(bar.getId())
                    .setUser_id(DataManager.getDataManager().getPrivateAccount().getId())
                    .setOrder_id(UUID.randomUUID().toString());


            MyServices.getInstance().makeToast("order saved...");
            bar.getTables().get(table.getId()).getOrders().put(order.getOrder_id() , order);
            tables.remove(table);
            tablesAdapter.notifyItemRemoved(position);
            MyDB.getInstance().add_reservation(bar , table.getId(), order);
        }

        @Override
        public void table_clicked(Table table, int position) {
        }
    };
    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(DataManager.EXTRA_BAR, barId);
            Intent intent = new Intent(this, Activity_bar_details.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
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