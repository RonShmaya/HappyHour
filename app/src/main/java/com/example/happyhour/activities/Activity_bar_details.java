package com.example.happyhour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import com.example.happyhour.R;
import com.example.happyhour.adapters.BarsAdapter;
import com.example.happyhour.adapters.ReviewAdapter;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Review;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_bar_details extends AppCompatActivity {

    private FloatingActionButton barDetails_FAB_changeBarName;
    private FloatingActionButton barDetails_FAB_changeBarPhoto;
    private FloatingActionButton barDetails_FAB_changeBarType;
    private FloatingActionButton barDetails_FAB_changeDescription;
    private FloatingActionButton barDetails_FAB_changeMusicType;
    private FloatingActionButton barDetails_FAB_changeHappyHour;
    private FloatingActionButton barDetails_FAB_changeMenu;

    private MaterialButton barDetails_BTN_writeReview;
    private MaterialButton barDetails_BTN_uploadPost;
    private MaterialButton barDetails_BTN_follow;
    private MaterialButton barDetails_BTN_makeReservation;

    private MaterialTextView barDetails_LBL_description;
    private MaterialTextView barDetails_LBL_name;
    private MaterialTextView barDetails_LBL_barType;
    private MaterialTextView barDetails_LBL_musicType;
    private MaterialTextView barDetails_LBL_happyHour;
    private MaterialTextView barDetails_LBL_menu;

    private RecyclerView barDetails_LST_Reviews;
    private RatingBar barDetails_RAB_rating;
    private CircleImageView barDetails_IMG_barPhoto;

    private HashMap<View.OnClickListener , FloatingActionButton> business_account_actions = new HashMap<>();
    private ReviewAdapter reviewsAdapter;
    private ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_details);

        findViews();
        update_ui_by_account_type();
        reviews = new ArrayList<>(get()); // TODO: 19/06/2022 get reviews  DataManager.getDataManager().getBusinessAccount().getMyBars().values()
        reviewsAdapter = new ReviewAdapter(this, reviews);
        barDetails_LST_Reviews.setAdapter(reviewsAdapter);
    }

    private  ArrayList<Review> get() { // TODO: 19/06/2022  delte this!
        return new ArrayList<>(Arrays.asList(
                new Review()
                .setDescription("it was nice")
                .setStars_rating(3.5F)
                .setReviewer_name("yao ming"),
                new Review()
                        .setDescription("it was great , i will come again for sure")
                        .setStars_rating(4F)
                        .setReviewer_name("stiv nash"),
                new Review()
                        .setDescription("it was great , i will come again for sure")
                        .setStars_rating(4F)
                        .setReviewer_name("stiv nash"),
                new Review()
                .setDescription("it was great , i will come again for sure")
                .setStars_rating(4F)
                .setReviewer_name("stiv nash")

        ));
    }

    private void update_ui_by_account_type() {
        Boolean account_visibility_business = null;
        if(DataManager.getDataManager().getUserType() == DataManager.eUserTypes.Business){
            account_visibility_business = true;
        }
        else if(DataManager.getDataManager().getUserType() == DataManager.eUserTypes.Private){
            account_visibility_business = false;
        }

        if(account_visibility_business == null){
            MyServices.getInstance().makeToast("There is a problem with your account");
            return;
        }
        int business_visible_value = account_visibility_business == true ? View.VISIBLE : View.INVISIBLE;
        int private_visible_value = account_visibility_business == true ? View.INVISIBLE : View.VISIBLE;

        business_views_visability(business_visible_value);
        private_views_visability(business_visible_value);


    }

    private void private_views_visability(int visibility_value) {
        barDetails_BTN_makeReservation.setVisibility(visibility_value);
        barDetails_BTN_follow.setVisibility(visibility_value);
        barDetails_BTN_writeReview.setVisibility(visibility_value);
    }

    private void business_views_visability(int visibility_value) {
        business_account_actions.forEach( (action , fab) -> fab.setVisibility(visibility_value));

        barDetails_BTN_uploadPost.setVisibility(visibility_value);

        if(visibility_value == View.VISIBLE){
            business_account_actions.forEach( (action , fab) -> fab.setOnClickListener(action) );
        }

    }


    private void findViews() {
        business_account_actions.put(changeBarName, findViewById(R.id.barDetails_FAB_changeBarName));
        business_account_actions.put(changeBarPhoto, findViewById(R.id.barDetails_FAB_changeBarPhoto));
        business_account_actions.put(changeBarType,findViewById(R.id.barDetails_FAB_changeBarType));
        business_account_actions.put(changeDescription, findViewById(R.id.barDetails_FAB_changeDescription));
        business_account_actions.put(changeMusicType, findViewById(R.id.barDetails_FAB_changeMusicType));
        business_account_actions.put(changeHappyHour, findViewById(R.id.barDetails_FAB_changeHappyHour));
        business_account_actions.put(changeMenu, findViewById(R.id.barDetails_FAB_changeMenu));

        barDetails_BTN_writeReview = findViewById(R.id.barDetails_BTN_writeReview);

        barDetails_BTN_uploadPost = findViewById(R.id.barDetails_BTN_uploadPost);
        barDetails_BTN_follow = findViewById(R.id.barDetails_BTN_follow);
        barDetails_BTN_makeReservation = findViewById(R.id.barDetails_BTN_makeReservation);

        barDetails_LBL_description = findViewById(R.id.barDetails_LBL_description);
        barDetails_LBL_name = findViewById(R.id.barDetails_LBL_name);
        barDetails_LBL_barType = findViewById(R.id.barDetails_LBL_barType);
        barDetails_LBL_musicType = findViewById(R.id.barDetails_LBL_musicType);
        barDetails_LBL_happyHour = findViewById(R.id.barDetails_LBL_happyHour);
        barDetails_LBL_menu = findViewById(R.id.barDetails_LBL_menu);

        barDetails_IMG_barPhoto = findViewById(R.id.barDetails_IMG_barPhoto);
        barDetails_LST_Reviews = findViewById(R.id.barDetails_LST_Reviews);
        barDetails_RAB_rating = findViewById(R.id.barDetails_RAB_rating);
    }

    private View.OnClickListener changeBarName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private View.OnClickListener changeBarPhoto  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeBarType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeMusicType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeHappyHour = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}