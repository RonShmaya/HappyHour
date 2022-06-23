package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.objects.Follower;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private ArrayList<Follower> followers = new ArrayList<>();

    public FollowersAdapter(Activity activity, ArrayList<Follower> followers){
        this.activity = activity;
        this.followers = followers;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_followers, parent, false);
        FollowerHolder followerHolder = new FollowerHolder(view);
        return followerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final FollowerHolder holder = (FollowerHolder) viewHolder;
        Follower follower = getItem(position);

        //holder.listFollower_IMG_reviewer.setText(review.getReviewer_name()); // TODO: 23/06/2022 image
        holder.listFollower_LBL_name.setText(follower.getName());


       // int resourceId = activity.getResources().getIdentifier(bar.getImage(), "drawable", activity.getPackageName());
     //   holder.listReview_IMG_reviewer.setImageResource(resourceId);
        // TODO: 19/06/2022 set photo
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public Follower getItem(int position) {
        return followers.get(position);
    }



    class FollowerHolder extends RecyclerView.ViewHolder {

        private CircleImageView listFollower_IMG_reviewer;
        private MaterialTextView listFollower_LBL_name;


        public FollowerHolder(View itemView) {
            super(itemView);
             listFollower_IMG_reviewer = itemView.findViewById(R.id.listFollower_IMG_reviewer);
             listFollower_LBL_name = itemView.findViewById(R.id.listFollower_LBL_name);
        }

    }
}