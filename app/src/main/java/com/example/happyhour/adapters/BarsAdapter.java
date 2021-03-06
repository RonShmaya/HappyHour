package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class BarsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Barlistener {
        void clicked(Bar bar, int position);
        void minus(Bar bar, int position);
    }

    private Activity activity;
    private Barlistener barlistener;
    private ArrayList<Bar> bars = new ArrayList<>();

    public BarsAdapter(Activity activity, ArrayList<Bar> bars){
        this.activity = activity;
        this.bars = bars;
    }

    public void setBarlistener(Barlistener barlistener) {
        this.barlistener = barlistener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bars, parent, false);
        BarHolder barHolder = new BarHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final BarHolder holder = (BarHolder) viewHolder;
        Bar bar = getItem(position);

        holder.listBars_LBL_name.setText(bar.getName());
        holder.listBars_LBL_barType.setText(bar.barTypeToString());
        holder.listBars_RAB_rating.setRating(bar.starsAvg());

        Glide.with(activity).load(bar.getBar_photo()).placeholder(R.drawable.img_placeholder).into(holder.listBars_IMG);

    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public Bar getItem(int position) {
        return bars.get(position);
    }



    class BarHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView listBars_IMG;
        private MaterialTextView listBars_LBL_name;
        private MaterialTextView listBars_LBL_barType;
        private MaterialButton listBars_BTN_remove_account;
        private RatingBar listBars_RAB_rating;


        public BarHolder(View itemView) {
            super(itemView);
            listBars_IMG = itemView.findViewById(R.id.listBars_IMG);
            listBars_LBL_name = itemView.findViewById(R.id.listBars_LBL_name);
            listBars_LBL_barType = itemView.findViewById(R.id.listBars_LBL_barType);
            listBars_BTN_remove_account = itemView.findViewById(R.id.listBars_BTN_remove_account);
            listBars_RAB_rating = itemView.findViewById(R.id.listBars_RAB_rating);


            listBars_BTN_remove_account.setOnClickListener(view -> {
                if (barlistener != null) {
                    barlistener.minus(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

            itemView.setOnClickListener(view -> {
                if (barlistener != null) {
                    barlistener.clicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

    }
}