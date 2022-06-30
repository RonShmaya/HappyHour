package com.example.happyhour.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.example.happyhour.tools.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CustomerBarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Barlistener {
        void clicked(Bar bar, int position);
        void follow(Bar bar, int position);
    }

    private Activity activity;
    private Barlistener barlistener;
    private ArrayList<Bar> bars = new ArrayList<>();

    public CustomerBarsAdapter(Activity activity, ArrayList<Bar> bars){
        this.activity = activity;
        this.bars = bars;
    }

    public CustomerBarsAdapter setBarlistener(Barlistener barlistener) {
        this.barlistener = barlistener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bars_customer_side, parent, false);
        BarHolder barHolder = new BarHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final BarHolder holder = (BarHolder) viewHolder;
        Bar bar = getItem(position);


        holder.list_bar_customer_LBL_name.setText(bar.getName());
        holder.list_bar_customer_LBL_barType.setText(bar.barTypeToString());
        if(bar.getFollowers().containsKey(DataManager.getDataManager().getPrivateAccount().getId())){
            holder.list_bar_customer_BTN_follow.setBackgroundColor(Color.parseColor("#555B6E"));
        }
        else{
            holder.list_bar_customer_BTN_follow.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.list_bar_customer_RAB_rating.setRating(bar.starsAvg());
        holder.list_bar_customer_LBL_followers.setText(bar.getFollowers().size() + " Followers");

        Glide.with(activity).load(bar.getBar_photo()).placeholder(R.drawable.img_placeholder).into(holder.list_bar_customer_IMG);

    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public Bar getItem(int position) {
        return bars.get(position);
    }



    class BarHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView list_bar_customer_IMG;
        private MaterialTextView list_bar_customer_LBL_name;
        private MaterialTextView list_bar_customer_LBL_barType;
        private MaterialTextView list_bar_customer_LBL_followers;
        private MaterialButton list_bar_customer_BTN_follow;
        private RatingBar list_bar_customer_RAB_rating;


        public BarHolder(View itemView) {
            super(itemView);
            list_bar_customer_IMG = itemView.findViewById(R.id.list_bar_customer_IMG);
            list_bar_customer_LBL_name = itemView.findViewById(R.id.list_bar_customer_LBL_name);
            list_bar_customer_LBL_followers = itemView.findViewById(R.id.list_bar_customer_LBL_followers);
            list_bar_customer_LBL_barType = itemView.findViewById(R.id.list_bar_customer_LBL_barType);
            list_bar_customer_BTN_follow = itemView.findViewById(R.id.list_bar_customer_BTN_follow);
            list_bar_customer_RAB_rating = itemView.findViewById(R.id.list_bar_customer_RAB_rating);


            list_bar_customer_BTN_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (barlistener != null) {
                        barlistener.follow(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (barlistener != null) {
                        barlistener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }

    }
}