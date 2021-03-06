package com.example.happyhour.activities;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.happyhour.R;
import com.example.happyhour.activities.business_account.Activity_bar_account;
import com.example.happyhour.activities.private_account.Activity_customer_main_page;
import com.example.happyhour.activities.private_account.Activity_private_account_profile;
import com.example.happyhour.callbacks.Callback_find_account;
import com.example.happyhour.objects.Account;
import com.example.happyhour.objects.PrivateAccount;
import com.example.happyhour.tools.DataManager;
import com.example.happyhour.tools.MyDB;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_enter_app extends AppCompatActivity {
    private LottieAnimationView enter_animation_view;
    private DataManager.eUserTypes userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app);
        MyDB.getInstance().setCallback_find_user(callback_find_account);
        findViews();
        decide_page_to_open();
    }

    private void findViews() {
        enter_animation_view = findViewById(R.id.enter_animation_view);
    }

    private void decide_page_to_open() {
        enter_animation_view.playAnimation();
        enter_animation_view.addAnimatorListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                userType = DataManager.getDataManager().getUserType();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                verifyUser();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });

    }

    public void verifyUser() {
            if(userType == null) {
                go_next(Activity_user_connect.class); // no user connected
            }
            else {
                MyDB.getInstance().isAccountExists(userType , FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private Callback_find_account callback_find_account = new Callback_find_account() {
        @Override
        public void account_found(Account account) {
            DataManager.getDataManager().set_account(account);
            if(userType == DataManager.eUserTypes.Business) {
                go_next(Activity_bar_account.class);
            }
            else if(userType == DataManager.eUserTypes.Private){
                if(((PrivateAccount)account).getFavorite_1() == null)
                    go_next(Activity_private_account_profile.class);
                else
                    go_next(Activity_customer_main_page.class);

            }
        }

        @Override
        public void account_not_found() {
            FirebaseAuth.getInstance().signOut();
            go_next(Activity_user_connect.class);
        }

        @Override
        public void failed() {
            FirebaseAuth.getInstance().signOut();
            go_next(Activity_user_connect.class);
        }
    };
}