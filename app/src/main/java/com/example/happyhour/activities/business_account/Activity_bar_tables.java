package com.example.happyhour.activities.business_account;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_bar_details;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.OrdersAdapter;
import com.example.happyhour.adapters.TableAdapter;
import com.example.happyhour.dialogs.DialogAddTable;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Table;
import com.example.happyhour.objects.MyTime;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity_bar_tables extends AppCompatActivity {
    private RecyclerView bar_tables_LST_tables;
    private TableAdapter tablesAdapter;
    private OrdersAdapter ordersAdapter;
    private FloatingActionButton fab_add_table;
    private ArrayList<Table> tables;
    private ArrayList<Bar> barsForAdapter = new ArrayList<>();
    private MaterialToolbar toolbar;
    private MaterialTextView bar_tables_TXT_open_time;
    private MaterialButton barDetails_BTN_open_time;
    private MaterialTextView bar_tables_TXT_close_time;
    private MaterialButton barDetails_BTN_close_time;
    private MaterialButton bar_tables_BTN_show_all;
    private MaterialTextView bar_tables_LBL_time;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
    private FloatingActionButton bar_tables_FAB_addTime;

    private Bar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_tables);

        Bundle bundle = getIntent().getExtras();
        String barId = bundle.getString(DataManager.EXTRA_BAR);
        bar = DataManager.getDataManager().getBar(barId);

        init_toolbar();
        findViews();

        tables = new ArrayList<>(bar.getTables().values());
        tablesAdapter = new TableAdapter(this, tables, DataManager.getDataManager().getUserType());
        tablesAdapter.setCallback_table(callback_table);
        bar_tables_LST_tables.setAdapter(tablesAdapter);

    }

    private void findViews() {
        bar_tables_LST_tables = findViewById(R.id.bar_tables_LST_tables);
        fab_add_table = findViewById(R.id.fab_add_table);
        bar_tables_TXT_open_time = findViewById(R.id.bar_tables_TXT_open_time);
        barDetails_BTN_open_time = findViewById(R.id.barDetails_BTN_open_time);
        bar_tables_TXT_close_time = findViewById(R.id.bar_tables_TXT_close_time);
        barDetails_BTN_close_time = findViewById(R.id.barDetails_BTN_close_time);

        bar_tables_BTN_show_all = findViewById(R.id.bar_tables_BTN_show_all);
        bar_tables_LBL_time = findViewById(R.id.bar_tables_LBL_time);
        bar_tables_FAB_addTime = findViewById(R.id.bar_tables_FAB_addTime);

        bar_tables_TXT_open_time.setText(bar.getOpenTime().toString());
        bar_tables_TXT_close_time.setText(bar.getClosingTime().toString());


        fab_add_table.setOnClickListener(add_table_listener);

        barDetails_BTN_open_time.setOnClickListener(view -> {
            DialogFragment newFragment = new MyServices.TimePickerFragment();
            ((MyServices.TimePickerFragment) newFragment).setCallback_time(callback_time_open);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });
        barDetails_BTN_close_time.setOnClickListener(view -> {
            DialogFragment newFragment = new MyServices.TimePickerFragment();
            ((MyServices.TimePickerFragment) newFragment).setCallback_time(callback_time_closing);
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });

        bar_tables_BTN_show_all.setOnClickListener(view -> bar_tables_LST_tables.setAdapter(tablesAdapter));
        bar_tables_FAB_addTime.setOnClickListener(view -> {
            DialogFragment newFragment = new MyServices.DatePickerFragment();
            ((MyServices.DatePickerFragment)newFragment).setCallback_date(callback_date);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    private MyServices.DatePickerFragment.Callback_date callback_date = new MyServices.DatePickerFragment.Callback_date() {
        @Override
        public void get_input_date(Date date) {
            Date date_now = new Date(System.currentTimeMillis());
            String date_now_str =  simpleDateFormat.format(System.currentTimeMillis());
            String date_str =  simpleDateFormat.format(date);

            if(!(date_now_str.equals(date_str)) && date.compareTo(date_now) < 0){
                MyServices.getInstance().makeToast("Date Must Be From\n" + date_now_str + " forward");
                return;
            }
            bar_tables_LBL_time.setText(date_str);
            show_orders(date_str);

        }
    };
    private void show_orders(String date_to_search) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR , 0);
        cal.set(Calendar.MINUTE , 0);
        String date_str = simpleDateFormat.format(cal.getTime());
        Date date = cal.getTime();
        barsForAdapter.clear();
        bar.getTables().values().forEach(table -> {
            table.getOrders().values().forEach(order -> {
                if (order.getDate().compareTo(date) < 0
                        &&  !(simpleDateFormat.format(order.getDate()).equals(date_str))) {
                    MyDB.getInstance().remove_order(bar, table.getId(), order);
                    return;
                }
                if(!(date_to_search.equals(simpleDateFormat.format(order.getDate())))){
                    return;
                }
                Table table_for_adapter = new Table()
                        .setNumOfPlaces(table.getNumOfPlaces())
                        .setDescription(table.getDescription())
                        .setName(table.getName())
                        .addOrder(OrdersAdapter.ORDER_KEY, order);
                barsForAdapter.add(new Bar()
                        .setName(bar.getName())
                        .setBar_photo(bar.getBar_photo())
                        .setHappy_hour(bar.getHappy_hour())
                        .addTable(OrdersAdapter.TABLE_KEY, table_for_adapter));
            });
        });
        if(barsForAdapter.size() == 0){
            MyServices.getInstance().makeToast("There is no Reservation in this day");
        }
        ordersAdapter = new OrdersAdapter(this , barsForAdapter);
        bar_tables_LST_tables.setAdapter(ordersAdapter);
    }

    private TableAdapter.Callback_table callback_table = new TableAdapter.Callback_table() {
        @Override
        public void action_button_clicked(Table table, int position) {
            tables.remove(position);
            tablesAdapter.notifyItemRemoved(position);
            DataManager.getDataManager().delete_table(bar, table);
        }

        @Override
        public void table_clicked(Table table, int position) {

        }
    };
    private View.OnClickListener add_table_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            make_dialog();
        }
    };

    private void make_dialog() {
        DialogAddTable add_table_dialog = new DialogAddTable();
        add_table_dialog.setCallback_add_table(new DialogAddTable.Callback_add_table() {
            @Override
            public void table_to_add(Table table) {
                MyServices.getInstance().makeToast("table added");
                tables.add(table);
                tablesAdapter.notifyItemInserted((tables.size() - 1));
                DataManager.getDataManager().add_table(bar, table);
            }
        });
        add_table_dialog.show(this);
    }

    private void init_toolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(DataManager.EXTRA_BAR, bar.getId());
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

    private MyServices.TimePickerFragment.Callback_time callback_time_open = new MyServices.TimePickerFragment.Callback_time() {
        @Override
        public void get_input_time(MyTime time) {
            bar_tables_TXT_open_time.setText(time.toString());
            DataManager.getDataManager().change_bar_time_open(bar.getId(), time);

        }
    };
    private MyServices.TimePickerFragment.Callback_time callback_time_closing = new MyServices.TimePickerFragment.Callback_time() {
        @Override
        public void get_input_time(MyTime time) {
            bar_tables_TXT_close_time.setText(time.toString());
            DataManager.getDataManager().change_bar_time_close(bar.getId(), time);

        }
    };
}