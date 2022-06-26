package com.example.happyhour.dialogs;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.adapters.BusinessFollowersAdapter;
import com.example.happyhour.objects.Follower;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class DialogShowFollowers {
    private RecyclerView showFollowers_LST_followers;
    private MaterialTextView showFollowers_LBL_numOfFollowers;
    private BusinessFollowersAdapter followersAdapter;


    public void show(AppCompatActivity activity, ArrayList<Follower> followers){
        final Dialog dialog = new Dialog(activity);
        followersAdapter = new BusinessFollowersAdapter(activity,followers);
        dialog.setContentView(R.layout.dialog_show_followers);
        findViews(dialog , followers.size());
        dialog.show();
    }


    private void findViews(Dialog dialog , int numOfFollowers) {
        showFollowers_LST_followers = dialog.findViewById(R.id.showFollowers_LST_followers);
        showFollowers_LBL_numOfFollowers = dialog.findViewById(R.id.showFollowers_LBL_numOfFollowers);
        showFollowers_LBL_numOfFollowers.setText("There is "+ numOfFollowers + " Followers");
        showFollowers_LST_followers.setAdapter(followersAdapter);

    }
}
