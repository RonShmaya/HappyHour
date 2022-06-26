package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Review;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private ArrayList<Review> reviews = new ArrayList<>();

    public ReviewAdapter(Activity activity, ArrayList<Review> reviews){
        this.activity = activity;
        this.reviews = reviews;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        ReviewHolder reviewHolder = new ReviewHolder(view);
        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ReviewHolder holder = (ReviewHolder) viewHolder;
        Review review = getItem(position);

        holder.listReview_LBL_name.setText(review.getReviewer_name());
        holder.listReview_LBL_review.setText(review.getDescription());
        holder.listReview_RAB_rating.setRating(review.getStars_rating());

        Glide.with(activity).load(review.getImg()).placeholder(R.drawable.ic_user).into(holder.listReview_IMG_reviewer);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public Review getItem(int position) {
        return reviews.get(position);
    }



    class ReviewHolder extends RecyclerView.ViewHolder {

        private CircleImageView listReview_IMG_reviewer;
        private MaterialTextView listReview_LBL_name;
        private MaterialTextView listReview_LBL_review;
        private RatingBar listReview_RAB_rating;

        public ReviewHolder(View itemView) {
            super(itemView);
             listReview_IMG_reviewer = itemView.findViewById(R.id.listReview_IMG_reviewer);
             listReview_LBL_name = itemView.findViewById(R.id.listReview_LBL_name);
            listReview_LBL_review = itemView.findViewById(R.id.listReview_LBL_review);
            listReview_RAB_rating = itemView.findViewById(R.id.listReview_RAB_rating);
        }

    }
}