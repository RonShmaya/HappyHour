package com.example.happyhour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.happyhour.R;
import com.example.happyhour.adapters.BarsAdapter;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.BusinessAccount;
import com.example.happyhour.tools.DataManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_bar_account extends AppCompatActivity {
    private RecyclerView main_LST_bars;
    private ArrayList<Bar> bars;
    private FloatingActionButton fab_add_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_account);
        findViews();
        bars = new ArrayList<>(DataManager.getDataManager().getBusinessAccount().getMyBars().values());
        BarsAdapter barsAdapter = new BarsAdapter(this, get());
        barsAdapter.setBarlistener(barlistener);
        main_LST_bars.setAdapter(barsAdapter);

        fab_add_bar.setOnClickListener(onClickAddBar);


    }

    private void findViews() {
        main_LST_bars = findViewById(R.id.main_LST_bars);
        fab_add_bar = findViewById(R.id.fab_add_bar);
    }

    private ArrayList<Bar> get() {
        return new ArrayList<>(Arrays.asList(
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg "),
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg "),
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg "),
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg "),
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg "),
           new Bar().setDescription("fdgdfgdfgdfg fdg").setName("fgdfg ")
        ));
    }
   BarsAdapter.Barlistener barlistener = new BarsAdapter.Barlistener() {
       @Override
       public void clicked(Bar bar, int position) {
       }

       @Override
       public void share(Bar bar, int position) {
       }

       @Override
       public void minus(Bar bar, int position) {
       }
   };
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
}