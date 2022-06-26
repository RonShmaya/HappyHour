package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class PrivateFollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Barlistener {
        void clicked(Bar bar, int position);
    }

    private Activity activity;
    private Barlistener barlistener;
    private ArrayList<Bar> bars = new ArrayList<>();

    public PrivateFollowingAdapter(Activity activity, ArrayList<Bar> bars){
        this.activity = activity;
        this.bars = bars;
    }

    public void setBarlistener(Barlistener barlistener) {
        this.barlistener = barlistener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bars_following, parent, false);
        BarHolder barHolder = new BarHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final BarHolder holder = (BarHolder) viewHolder;
        Bar bar = getItem(position);


        holder.listBarFollowing_LBL_name.setText(bar.getName());
        holder.listBarFollowing_LBL_barType.setText(bar.barTypeToString());
        holder.listBarFollowing_LBL_business_hours.setText("Open: "+bar.getOpenTime().toString()+ " - " + bar.getClosingTime().toString());
        holder.listBarFollowing_LBL_happy_hour.setText(bar.getHappy_hour());
        Glide.with(activity).load(bar.getBar_photo()).placeholder(R.drawable.img_bar_basic).into(holder.listBarFollowing_IMG);
    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public Bar getItem(int position) {
        return bars.get(position);
    }



    class BarHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView listBarFollowing_IMG;
        private MaterialTextView listBarFollowing_LBL_name;
        private MaterialTextView listBarFollowing_LBL_barType;
        private MaterialTextView listBarFollowing_LBL_business_hours;
        private MaterialTextView listBarFollowing_LBL_happy_hour;

        public BarHolder(View itemView) {
            super(itemView);
            listBarFollowing_IMG = itemView.findViewById(R.id.listBarFollowing_IMG);
            listBarFollowing_LBL_name = itemView.findViewById(R.id.listBarFollowing_LBL_name);
            listBarFollowing_LBL_barType = itemView.findViewById(R.id.listBarFollowing_LBL_barType);
            listBarFollowing_LBL_business_hours = itemView.findViewById(R.id.listBarFollowing_LBL_business_hours);
            listBarFollowing_LBL_happy_hour = itemView.findViewById(R.id.listBarFollowing_LBL_happy_hour);

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