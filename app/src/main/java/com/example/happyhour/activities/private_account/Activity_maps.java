
package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_maps extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_BARS = "EXTRA_BARS";
    private GoogleMap mMap;
    private MaterialToolbar toolbar;
    private FloatingActionButton fab_search;
    private BottomNavigationView nav_view;
    private HashMap<String, Bar> myBars = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        String bars_gson = bundle.getString(EXTRA_BARS, null);
        if (bars_gson != null) {
            myBars = new Gson().fromJson(bars_gson, new TypeToken<HashMap<String, Bar>>() {
            }.getType());

        } else {
            MyDB.getInstance().setCallback_get_bars(callback_get_bars);
            MyDB.getInstance().get_bars();
        }
        init_toolbar();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        myBars.values().forEach(bar -> {
            LatLng address = MyServices.getInstance().getLocationFromAddress(this, bar.getAddressMaps().toString());
            if (address != null) {
                MarkerOptions marker = new MarkerOptions().position(address).title(bar.getName());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bar_google_map));
                mMap.addMarker(marker);
                markerOptions.add(marker);
            }
        });
        PrivateAccount privateAccount = DataManager.getDataManager().getPrivateAccount();
        LatLng userAddress = MyServices.getInstance().getLocationFromAddress(this, privateAccount.getAddressMaps().toString());
         MarkerOptions marker = new MarkerOptions().position(userAddress).title(privateAccount.getName());
         marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_map));
        mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(userAddress));
        mMap.setMinZoomPreference(14);
    }

    private Callback_get_bars callback_get_bars = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
            myBars = bars;
        }

        @Override
        public void get_bar(Bar bar) {
        }

        @Override
        public void failed() {
            MyServices.getInstance().makeToast("something went wrong");
        }
    };

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
                if (item.getItemId() == R.id.action_home) {
                    go_next(Activity_customer_main_page.class);
                } else if (item.getItemId() == R.id.action_follow) {
                    go_next(Activity_follow_bar.class);
                } else if (item.getItemId() == R.id.action_order) {
                    go_next(Activity_my_reservations.class);
                } else if (item.getItemId() == R.id.action_profile) {
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

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
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