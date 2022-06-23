package com.example.happyhour.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.adapters.BarsAdapter;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
// TODO: 20/06/2022 add rating
// TODO: 20/06/2022 where to add bar reservations?
public class Activity_bar_account extends AppCompatActivity {
    private RecyclerView main_LST_bars;
    private ArrayList<Bar> bars;
    private FloatingActionButton fab_add_bar;
    private MaterialToolbar toolbar;
    private BarsAdapter barsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_account);
        findViews();
        init_toolbar();

        bars = new ArrayList<>(DataManager.getDataManager().getBusinessAccount().getMyBars().values());
        barsAdapter = new BarsAdapter(this, bars);
        barsAdapter.setBarlistener(barlistener);
        main_LST_bars.setAdapter(barsAdapter);

        fab_add_bar.setOnClickListener(onClickAddBar);


    }

    private void findViews() {
        main_LST_bars = findViewById(R.id.main_LST_bars);
        fab_add_bar = findViewById(R.id.fab_add_bar);
    }


   BarsAdapter.Barlistener barlistener = new BarsAdapter.Barlistener() {
       @Override
       public void clicked(Bar bar, int position) {
           Bundle bundle = new Bundle();
           bundle.putString(DataManager.EXTRA_BAR , bar.getId());
           Intent intent = new Intent(Activity_bar_account.this, Activity_bar_details.class);
           intent.putExtras(bundle);
           startActivity(intent);
           finish();
       }

       @Override
       public void minus(Bar bar, int position) {
            are_you_sure_dialog(bar , position);
       }
   };

    private void are_you_sure_dialog(Bar bar, int position) {
        new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle("Warning!")
                .setMessage("Are you sure you want to delete this bar?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bar.getFollowers().forEach( (f_id , f_name) -> DataManager.getDataManager().remove_follower_private(bar , f_id));
                        bars.remove(position);
                        barsAdapter.notifyItemRemoved(position);
                        DataManager.getDataManager().delete_bar(bar , position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    View.OnClickListener onClickAddBar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            go_next(Activity_create_bar.class);
        }
    };

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
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
        MyServices.getInstance().makeToast("logout...");
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }
}