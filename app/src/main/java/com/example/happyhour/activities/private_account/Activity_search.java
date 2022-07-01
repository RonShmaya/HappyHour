package com.example.happyhour.activities.private_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.Activity_bar_details;
import com.example.happyhour.activities.Activity_user_connect;
import com.example.happyhour.adapters.PostsAdapter;
import com.example.happyhour.adapters.SearchBarsAdapter;
import com.example.happyhour.callbacks.Callback_get_bars;
import com.example.happyhour.dialogs.DialogPost;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Post;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Activity_search extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ArrayList<Post> posts;
    private PostsAdapter postsAdapter;
    private RecyclerView search_LST_posts;
    private BottomNavigationView nav_view;
    private LottieAnimationView loading_animation_view;
    private HashMap<String, Bar> bars;
    private MaterialButton search_BTN_back;
    private SearchView search_SV;
    private LinearLayout search_LLY_search_engine;
    private LinearLayout search_LLY_posts;
    private RecyclerView search_LST_results;
    private ArrayList<Bar> bars_in_search = new ArrayList<>();
    private LinearLayout search_LLT_happy_hour;
    private View search_V_happy_hour;
    private LinearLayout search_LLT_bar_name;
    private View search_V_bar_name;
    private LinearLayout search_LLT_music;
    private View search_V_music;
    private HashMap<LinearLayout, SearchBy> searchOptions;
    private SearchBy searchBy;
    private HashMap<String, Bar> myBars = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init_toolbar();
        findViews();
        MyDB.getInstance().setCallback_get_bars(callback_get_bars);
        MyDB.getInstance().get_bars();
    }

    private void findViews() {
        loading_animation_view = findViewById(R.id.loading_animation_view);
        search_LST_posts = findViewById(R.id.search_LST_posts);
        search_BTN_back = findViewById(R.id.search_BTN_back);
        search_SV = findViewById(R.id.search_SV);
        search_LLY_search_engine = findViewById(R.id.search_LLY_search_engine);
        search_LLY_posts = findViewById(R.id.search_LLY_posts);
        search_LST_results = findViewById(R.id.search_LST_results);
        search_LLT_happy_hour = findViewById(R.id.search_LLT_happy_hour);
        search_V_happy_hour = findViewById(R.id.search_V_happy_hour);
        search_LLT_bar_name = findViewById(R.id.search_LLT_bar_name);
        search_V_bar_name = findViewById(R.id.search_V_bar_name);
        search_LLT_music = findViewById(R.id.search_LLT_music);
        search_V_music = findViewById(R.id.search_V_music);
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
            String bars_gson = new Gson().toJson(myBars);
            Bundle bundle = new Bundle();
            bundle.putString(Activity_maps.EXTRA_BARS, bars_gson);
            Intent intent = new Intent(this, Activity_maps.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
        nav_view = findViewById(R.id.nav_view);

        nav_view.setOnItemSelectedListener(item -> {
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
        MyServices.getInstance().makeToast("logout...");
        DataManager.getDataManager().logout();
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }

    private Callback_get_bars callback_get_bars = new Callback_get_bars() {
        @Override
        public void get_bars(HashMap<String, Bar> bars) {
            loading_animation_view.cancelAnimation();
            loading_animation_view.setVisibility(View.GONE);
            myBars = bars;
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
        this.bars = bars;
        posts = new ArrayList<>();
        HashMap<Post, Bar> bar_and_posts = new HashMap<>();
        bars.values().forEach(bar -> {
            bar.getPosts().values().forEach(post -> {
                posts.add(post);
                bar_and_posts.put(post, bar);
            });
        });
        Collections.sort(posts);
        postsAdapter = new PostsAdapter(this, posts, PostsAdapter.eList.list_search);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false);
        search_LST_posts.setHasFixedSize(true);
        search_LST_posts.setLayoutManager(layoutManager);
        search_LST_posts.setAdapter(postsAdapter);
        postsAdapter.setPostListener((post, position) -> new DialogPost()
                .setCallback_clicked_post(callback_clicked_post)
                .show(Activity_search.this, bar_and_posts.get(post), post, null, null));

        init_search_engine();
    }


    private void init_search_engine() {
        searchOptions = new HashMap<>();
        searchOptions.put(search_LLT_happy_hour, new SearchByHappyHour(search_V_happy_hour, search_LLT_happy_hour, "Search Happy Hour..."));
        searchOptions.put(search_LLT_bar_name, new SearchByBarName(search_V_bar_name, search_LLT_bar_name, "Search Bar Name..."));
        searchOptions.put(search_LLT_music, new SearchByMusicType(search_V_music, search_LLT_music, "Search Bar Music Type..."));
        searchBy = searchOptions.get(search_LLT_happy_hour);

        SearchBarsAdapter searchBarsAdapter = new SearchBarsAdapter(this, bars_in_search);
        searchBarsAdapter.setBarlistener((bar, position) -> {
            go_next_bundle(Activity_bar_details.class , bar);
        });
        search_LST_results.setAdapter(searchBarsAdapter);

        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        Activity_search.this.visibility_change(View.VISIBLE, View.GONE);
                        search_SV.setQueryHint(searchBy.hint_search);
                    }
                });


        search_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bars_in_search.clear();
                if (s.isEmpty()) {
                    searchBarsAdapter.notifyDataSetChanged();
                    return true;
                }
                bars.values().forEach(bar -> {

                    if (searchBy.isSearchContain(bar , s)) {
                        bars_in_search.add(bar);
                    } else {
                        bars_in_search.remove(bar);
                    }

                });
                searchBarsAdapter.notifyDataSetChanged();
                return true;
            }
        });
        search_BTN_back.setOnClickListener(view -> {
            visibility_change(View.GONE, View.VISIBLE);
            search_SV.clearFocus();
            search_SV.setQuery("", false);
            search_SV.setQueryHint("Search...");
        });
    }

    private <T extends AppCompatActivity> void go_next_bundle(Class<T> nextActivity, Bar bar) {
        Bundle bundle = new Bundle();
        bundle.putString(DataManager.EXTRA_BAR, bar.getId());
        Intent intent = new Intent(this, nextActivity);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void visibility_change(int searchVisibility, int postVisibility) {
        search_BTN_back.setVisibility(searchVisibility);
        search_LLY_search_engine.setVisibility(searchVisibility);

        search_LLY_posts.setVisibility(postVisibility);

    }

    public abstract class SearchBy {
        protected View view;
        protected LinearLayout linearLayout;
        protected String hint_search;

        public SearchBy(View view, LinearLayout linearLayout, String hint_search) {
            this.view = view;
            this.linearLayout = linearLayout;
            this.hint_search = hint_search;
            this.linearLayout.setOnClickListener(view1 -> {
                searchBy = this;
                search_SV.setQuery("", false);
                searchOptions.forEach( (s_linear , option_obj) -> {
                    if(s_linear.equals(this.linearLayout)){
                        option_obj.view.setVisibility(View.VISIBLE);
                    }
                    else{
                        option_obj.view.setVisibility(View.INVISIBLE);
                    }

                });
                search_SV.setQueryHint(hint_search);
            });

        }

        public abstract boolean isSearchContain(Bar bar, String input);
    }

    public class SearchByHappyHour extends SearchBy {
        public SearchByHappyHour(View view, LinearLayout linearLayout, String hint_search) {
            super(view, linearLayout, hint_search);
        }

        @Override
        public boolean isSearchContain(Bar bar, String input) {
            return bar.getHappy_hour().toLowerCase().contains(input.toLowerCase());
        }
    }

    public class SearchByBarName extends SearchBy {
        public SearchByBarName(View view, LinearLayout linearLayout, String hint_search) {
            super(view, linearLayout, hint_search);
        }

        @Override
        public boolean isSearchContain(Bar bar, String input) {
            return bar.getName().toLowerCase().contains(input.toLowerCase());
        }
    }

    public class SearchByMusicType extends SearchBy {
        public SearchByMusicType(View view, LinearLayout linearLayout, String hint_search) {
            super(view, linearLayout, hint_search);
        }

        @Override
        public boolean isSearchContain(Bar bar, String input) {
            return bar.barMusicToString().toLowerCase().contains(input.toLowerCase());
        }
    }
    private DialogPost.Callback_clicked_post_enter_bar callback_clicked_post = new DialogPost.Callback_clicked_post_enter_bar() {
        @Override
        public void post_clicked(Bar bar) {
            go_next_bundle(Activity_bar_details.class , bar);
        }

    };
}