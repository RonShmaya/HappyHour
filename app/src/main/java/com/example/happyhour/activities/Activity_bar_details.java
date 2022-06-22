package com.example.happyhour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.adapters.ReviewAdapter;
import com.example.happyhour.dialogs.DialogChangeBarDetails;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.objects.Review;
import com.example.happyhour.objects.eBarType;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

// TODO: 20/06/2022 upload post
// TODO: 20/06/2022 photoes list
// TODO: 20/06/2022 add photo
// TODO: 20/06/2022 add rating
// TODO: 20/06/2022 add menu
// TODO: 20/06/2022 add reviews
public class Activity_bar_details extends AppCompatActivity {

    private FloatingActionButton barDetails_FAB_changeBarName;
    private FloatingActionButton barDetails_FAB_changeBarPhoto;
    private FloatingActionButton barDetails_FAB_changeBarType;
    private FloatingActionButton barDetails_FAB_changeDescription;
    private FloatingActionButton barDetails_FAB_changeHappyHour;
    private FloatingActionButton barDetails_FAB_changeMenu;

    private MaterialButton barDetails_BTN_writeReview;
    private MaterialButton barDetails_BTN_uploadPost;
    private MaterialButton barDetails_BTN_follow;
    private MaterialButton barDetails_BTN_makeReservation;
    private MaterialButton barDetails_BTN_myTables;

    private MaterialTextView barDetails_LBL_description;
    private MaterialTextView barDetails_LBL_name;
    private MaterialTextView barDetails_LBL_barType;
    private MaterialTextView barDetails_LBL_musicType;
    private MaterialTextView barDetails_LBL_happyHour;
    private MaterialTextView barDetails_LBL_menu;

    private FloatingActionButton action_logout;
    private FloatingActionButton action_return_back;

    private RecyclerView barDetails_LST_Reviews;
    private RatingBar barDetails_RAB_rating;
    private CircleImageView barDetails_IMG_barPhoto;

    private HashMap<View.OnClickListener, FloatingActionButton> business_account_actions = new HashMap<>();
    private ReviewAdapter reviewsAdapter;
    private ArrayList<Review> reviews;
    private Bar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_details);

        Bundle bundle = getIntent().getExtras();
        String barId = bundle.getString(DataManager.EXTRA_BAR);
        bar = DataManager.getDataManager().getBar(barId);

        findViews();
        update_ui_by_account_type();
        reviews = new ArrayList<>(get()); // TODO: 19/06/2022 get reviews  DataManager.getDataManager().getBusinessAccount().getMyBars().values()
        reviewsAdapter = new ReviewAdapter(this, reviews);
        barDetails_LST_Reviews.setAdapter(reviewsAdapter);
    }

    private ArrayList<Review> get() { // TODO: 19/06/2022  delete this!
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
                        .setReviewer_name("stiv nash"),
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
        action_logout.setOnClickListener(view -> logout());

        Boolean account_visibility_business = null;
        if (DataManager.getDataManager().getUserType() == DataManager.eUserTypes.Business) {
            account_visibility_business = true;
        } else if (DataManager.getDataManager().getUserType() == DataManager.eUserTypes.Private) {
            account_visibility_business = false;
        }

        if (account_visibility_business == null) {
            MyServices.getInstance().makeToast("There is a problem with your account");
            return;
        }
        int business_visible_value = account_visibility_business == true ? View.VISIBLE : View.INVISIBLE;
        int private_visible_value = account_visibility_business == true ? View.INVISIBLE : View.VISIBLE;

        business_views_visability(business_visible_value);
        private_views_visability(private_visible_value);

        views_get_data();
    }

    private void views_get_data() {
        if (bar == null) {
            MyServices.getInstance().makeToast("something went wrong! signOut");
            return;
        }
        barDetails_LBL_description.setText(bar.getDescription());
        barDetails_LBL_name.setText(bar.getName());
        barDetails_LBL_barType.setText(bar.barTypeToString());
        barDetails_LBL_musicType.setText(bar.barMusicToString());
        barDetails_LBL_happyHour.setText(bar.getHappy_hour());


        // TODO: 20/06/2022 add photo        barDetails_LBL_menu
        // TODO: 20/06/2022 add               barDetails_RAB_rating
        // TODO: 20/06/2022 photo     barDetails_IMG_barPhoto
        // TODO: 20/06/2022   add                 reviews


    }

    private void private_views_visability(int visibility_value) {
        barDetails_BTN_makeReservation.setVisibility(visibility_value);
        barDetails_BTN_follow.setVisibility(visibility_value);
        barDetails_BTN_writeReview.setVisibility(visibility_value);
    }

    private void business_views_visability(int visibility_value) {
        business_account_actions.forEach((action, fab) -> fab.setVisibility(visibility_value));

        barDetails_BTN_uploadPost.setVisibility(visibility_value);
        barDetails_BTN_myTables.setVisibility(visibility_value);

        if (visibility_value == View.VISIBLE) {
            business_account_actions.forEach((action, fab) -> fab.setOnClickListener(action));
            barDetails_BTN_myTables.setOnClickListener(go_to_my_table);
            action_return_back.setOnClickListener(view -> go_next(Activity_bar_account.class));
        }

    }


    private void findViews() {
        business_account_actions.put(changeBarName, findViewById(R.id.barDetails_FAB_changeBarName));
        business_account_actions.put(changeBarPhoto, findViewById(R.id.barDetails_FAB_changeBarPhoto));
        business_account_actions.put(changeBarType, findViewById(R.id.barDetails_FAB_changeBarType));
        business_account_actions.put(changeDescription, findViewById(R.id.barDetails_FAB_changeDescription));
        business_account_actions.put(changeHappyHour, findViewById(R.id.barDetails_FAB_changeHappyHour));
        business_account_actions.put(changeMenu, findViewById(R.id.barDetails_FAB_changeMenu));

        barDetails_BTN_writeReview = findViewById(R.id.barDetails_BTN_writeReview);

        barDetails_BTN_uploadPost = findViewById(R.id.barDetails_BTN_uploadPost);
        barDetails_BTN_follow = findViewById(R.id.barDetails_BTN_follow);
        barDetails_BTN_makeReservation = findViewById(R.id.barDetails_BTN_makeReservation);
        barDetails_BTN_myTables = findViewById(R.id.barDetails_BTN_myTables);

        barDetails_LBL_description = findViewById(R.id.barDetails_LBL_description);
        barDetails_LBL_name = findViewById(R.id.barDetails_LBL_name);
        barDetails_LBL_barType = findViewById(R.id.barDetails_LBL_barType);
        barDetails_LBL_musicType = findViewById(R.id.barDetails_LBL_musicType);
        barDetails_LBL_happyHour = findViewById(R.id.barDetails_LBL_happyHour);
        barDetails_LBL_menu = findViewById(R.id.barDetails_LBL_menu);

        barDetails_IMG_barPhoto = findViewById(R.id.barDetails_IMG_barPhoto);
        barDetails_LST_Reviews = findViewById(R.id.barDetails_LST_Reviews);
        barDetails_RAB_rating = findViewById(R.id.barDetails_RAB_rating);

        action_logout = findViewById(R.id.action_logout);
        action_return_back = findViewById(R.id.action_return_back);
    }

    private View.OnClickListener changeBarName = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DialogChangeBarDetails()
                    .setCallback_change_bar(callback_change_bar)
                    .addEditText_show(R.id.dialogChangeBar_TIL_edit_last_detail,
                            R.id.dialogChangeBar_TIETL_edit_last_detail,
                            20,
                            "bar name",
                            barDetails_LBL_name.getText().toString(),
                            Activity_bar_details.this,
                            barDetails_LBL_name.getId());
        }
    };


    private View.OnClickListener changeBarPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener changeBarType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DialogChangeBarDetails()
                    .setCallback_change_bar(callback_change_bar)
                    .AutoCompleteTextView_show(Activity_bar_details.this , barDetails_LBL_barType.getId());
        }
    };

    private View.OnClickListener changeDescription = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DialogChangeBarDetails()
                    .setCallback_change_bar(callback_change_bar)
                    .addEditText_show(R.id.dialogChangeBar_TIL_edit_last_detail,
                            R.id.dialogChangeBar_TIETL_edit_last_detail,
                            150,
                            "description",
                            barDetails_LBL_description.getText().toString(),
                            Activity_bar_details.this,
                            barDetails_LBL_description.getId());
        }
    };

    private View.OnClickListener changeHappyHour = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new DialogChangeBarDetails()
                    .setCallback_change_bar(callback_change_bar)
                    .addEditText_show(R.id.dialogChangeBar_TIL_edit_last_detail,
                            R.id.dialogChangeBar_TIETL_edit_last_detail,
                            125,
                            "Happy Hour",
                            barDetails_LBL_happyHour.getText().toString(),
                            Activity_bar_details.this,
                            barDetails_LBL_happyHour.getId());
        }
    };
    private View.OnClickListener changeMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
    private View.OnClickListener go_to_my_table = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(DataManager.EXTRA_BAR, bar.getId());
            Intent intent = new Intent(Activity_bar_details.this, Activity_bar_tables.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    };

    private void logout() {
        DataManager.getDataManager().logout();
        MyServices.getInstance().makeToast("logout...");
        Intent intent = new Intent(this, Activity_user_connect.class);
        startActivity(intent);
        finish();
    }
    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
    private DialogChangeBarDetails.Callback_change_bar callback_change_bar = new DialogChangeBarDetails.Callback_change_bar() {
        @Override
        public void change_text(String newTxt, int objResId) {
            if (barDetails_LBL_description.getId() == objResId) {
                barDetails_LBL_description.setText(newTxt);
                DataManager.getDataManager().change_bar_description(bar.getId(),newTxt);

            } else if (barDetails_LBL_name.getId() == objResId) {
                barDetails_LBL_name.setText(newTxt);
                DataManager.getDataManager().change_bar_name(bar.getId(),newTxt);

            } else if (barDetails_LBL_barType.getId() == objResId) {
                barDetails_LBL_barType.setText(newTxt);
                DataManager.getDataManager().change_bar_type(bar.getId(), eBarType.valueOf(newTxt.replace(' ','_')));

            } else if (barDetails_LBL_happyHour.getId() == objResId) {
                barDetails_LBL_happyHour.setText(newTxt);
                DataManager.getDataManager().change_bar_happy_hour(bar.getId(),newTxt);
            }

        }
    };
}