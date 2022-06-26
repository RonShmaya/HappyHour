package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Follower;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessFollowersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private ArrayList<Follower> followers = new ArrayList<>();

    public BusinessFollowersAdapter(Activity activity, ArrayList<Follower> followers){
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


        holder.listFollower_LBL_name.setText(follower.getName());
        Glide.with(activity).load(follower.getImg()).placeholder(R.drawable.ic_user).into(holder.listFollower_IMG_reviewer);

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