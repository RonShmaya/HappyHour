package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.objects.Bar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String TABLE_KEY = "TABLE_KEY";
    public static String ORDER_KEY = "ORDER_KEY";
    private Activity activity;
    private ArrayList<Bar> bars = new ArrayList<>();


    public OrdersAdapter(Activity activity, ArrayList<Bar> bars){
        this.activity = activity;
        this.bars = bars;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_orders, parent, false);
        BarHolder barHolder = new BarHolder(view);
        return barHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final BarHolder holder = (BarHolder) viewHolder;
        Bar bar = getItem(position);

      //  holder.listOrder_IMG.setText(bar.getName()); // TODO: 19/06/2022 set photo
        holder.listOrder_LBL_bar_name.setText(bar.getName());
        holder.listOrder_LBL_table_name.setText("table: "+bar.getTables().get(TABLE_KEY).getName());
        holder.listOrder_LBL_table_description.setText(bar.getTables().get(TABLE_KEY).getDescription());
        holder.listOrder_LBL_table_num_of_places.setText("num of places: "+bar.getTables().get(TABLE_KEY).getNumOfPlaces());
        holder.listOrder_LBL_table_time.setText(bar.getTables().get(TABLE_KEY).getOrders().get(ORDER_KEY).getMyTime().toString()+" - "+
        new SimpleDateFormat("dd/MM/yy", Locale.US).format(bar.getTables().get(TABLE_KEY).getOrders().get(ORDER_KEY).getDate()));
        holder.listOrder_LBL_happy_hour.setText(bar.getHappy_hour());


       // int resourceId = activity.getResources().getIdentifier(bar.getImage(), "drawable", activity.getPackageName());
     //   holder.listBars_IMG.setImageResource(resourceId);
        // TODO: 19/06/2022 set photo
    }

    @Override
    public int getItemCount() {
        return bars.size();
    }

    public Bar getItem(int position) {
        return bars.get(position);
    }



    class BarHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView listOrder_IMG;
        private MaterialTextView listOrder_LBL_bar_name;
        private MaterialTextView listOrder_LBL_table_name;
        private MaterialTextView listOrder_LBL_table_description;
        private MaterialTextView listOrder_LBL_table_num_of_places;
        private MaterialTextView listOrder_LBL_table_time;
        private MaterialTextView listOrder_LBL_happy_hour;


        public BarHolder(View itemView) {
            super(itemView);
            listOrder_IMG = itemView.findViewById(R.id.listOrder_IMG);
            listOrder_LBL_bar_name = itemView.findViewById(R.id.listOrder_LBL_bar_name);
            listOrder_LBL_table_name = itemView.findViewById(R.id.listOrder_LBL_table_name);
            listOrder_LBL_table_description = itemView.findViewById(R.id.listOrder_LBL_table_description);
            listOrder_LBL_table_num_of_places = itemView.findViewById(R.id.listOrder_LBL_table_num_of_places);
            listOrder_LBL_table_time = itemView.findViewById(R.id.listOrder_LBL_table_time);
            listOrder_LBL_happy_hour = itemView.findViewById(R.id.listOrder_LBL_happy_hour);

        }

    }
}