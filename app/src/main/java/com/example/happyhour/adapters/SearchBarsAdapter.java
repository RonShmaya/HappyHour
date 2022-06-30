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

public class SearchBarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Barlistener {
        void clicked(Bar bar, int position);
    }

    private Activity activity;
    private Barlistener barlistener;
    private ArrayList<Bar> bars = new ArrayList<>();

    public SearchBarsAdapter(Activity activity, ArrayList<Bar> bars){
        this.activity = activity;
        this.bars = bars;
    }

    public void setBarlistener(Barlistener barlistener) {
        this.barlistener = barlistener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bars_search, parent, false);
        BarHolder barHolder = new BarHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final BarHolder holder = (BarHolder) viewHolder;
        Bar bar = getItem(position);

        holder.listBarSearch_LBL_name.setText(bar.getName());
        holder.listBarSearch_LBL_musicType.setText(bar.barMusicToString());
        holder.listBarSearch_LBL_happyHour.setText("Happy Hour - "+bar.getHappy_hour());
        Glide.with(activity).load(bar.getBar_photo()).placeholder(R.drawable.img_placeholder).into(holder.listBarSearch_IMG);
    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public Bar getItem(int position) {
        return bars.get(position);
    }



    class BarHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView listBarSearch_IMG;
        private MaterialTextView listBarSearch_LBL_name;
        private MaterialTextView listBarSearch_LBL_happyHour;
        private MaterialTextView listBarSearch_LBL_musicType;

        public BarHolder(View itemView) {
            super(itemView);
            listBarSearch_IMG = itemView.findViewById(R.id.listBarSearch_IMG);
            listBarSearch_LBL_name = itemView.findViewById(R.id.listBarSearch_LBL_name);
            listBarSearch_LBL_happyHour = itemView.findViewById(R.id.listBarSearch_LBL_happyHour);
            listBarSearch_LBL_musicType = itemView.findViewById(R.id.listBarSearch_LBL_musicType);


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