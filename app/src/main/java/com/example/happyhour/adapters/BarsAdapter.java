package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class BarsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Barlistener {
        void clicked(Bar bar, int position);
        void share(Bar bar, int position);
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
        holder.listBars_LBL_description.setText(bar.getDescription());
        holder.listBars_RAB_rating.setRating(3.5F);


       // int resourceId = activity.getResources().getIdentifier(bar.getImage(), "drawable", activity.getPackageName());
     //   holder.listBars_IMG.setImageResource(resourceId);
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
        private MaterialTextView listBars_LBL_description;
        private MaterialButton listBars_BTN_share_account;
        private MaterialButton listBars_BTN_remove_account;
        private RatingBar listBars_RAB_rating;


        public BarHolder(View itemView) {
            super(itemView);
            listBars_IMG = itemView.findViewById(R.id.listBars_IMG);
            listBars_LBL_name = itemView.findViewById(R.id.listBars_LBL_name);
            listBars_LBL_description = itemView.findViewById(R.id.listBars_LBL_description);
            listBars_BTN_share_account = itemView.findViewById(R.id.listBars_BTN_share_account);
            listBars_BTN_remove_account = itemView.findViewById(R.id.listBars_BTN_remove_account);
            listBars_RAB_rating = itemView.findViewById(R.id.listBars_RAB_rating);


            listBars_BTN_share_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (barlistener != null) {
                        barlistener.share(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

            listBars_BTN_remove_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (barlistener != null) {
                        barlistener.minus(getItem(getAdapterPosition()), getAdapterPosition());
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