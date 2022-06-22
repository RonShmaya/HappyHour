package com.example.happyhour.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.happyhour.R;
import com.example.happyhour.objects.Table;
import com.example.happyhour.tools.DataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Callback_table {
        void action_button_clicked(Table table, int position);
        void table_clicked(Table table, int position);
    }
    private Activity activity;
    private DataManager.eUserTypes userType;
    private Callback_table callback_table;
    private ArrayList<Table> tables = new ArrayList<>();

    public TableAdapter(Activity activity, ArrayList<Table> tables, DataManager.eUserTypes userType) {
        this.activity = activity;
        this.tables = tables;
        this.userType = userType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table, parent, false);
        TableHolder tableHolder = new TableHolder(view);
        return tableHolder;
    }

    public TableAdapter setCallback_table(Callback_table callback_table) {
        this.callback_table = callback_table;
        return this;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final TableHolder holder = (TableHolder) viewHolder;
        Table table = getItem(position);

        holder.listTable_LBL_name.setText(table.getName());
        holder.listTable_LBL_description.setText(table.getDescription()+"\nPlaces: "+table.getNumOfPlaces());
        if (this.userType == DataManager.eUserTypes.Business)
            holder.listTable_BTN_Order_or_remove.setText("Remove");
        else if (this.userType == DataManager.eUserTypes.Private)
            holder.listTable_BTN_Order_or_remove.setText("Order");
        else
            holder.listTable_BTN_Order_or_remove.setText("---");
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public Table getItem(int position) {
        return tables.get(position);
    }


    class TableHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listTable_LBL_name;
        private MaterialTextView listTable_LBL_description;
        private MaterialButton listTable_BTN_Order_or_remove;


        public TableHolder(View itemView) {
            super(itemView);
            listTable_LBL_name = itemView.findViewById(R.id.listTable_LBL_name);
            listTable_LBL_description = itemView.findViewById(R.id.listTable_LBL_description);
            listTable_BTN_Order_or_remove = itemView.findViewById(R.id.listTable_BTN_Order_or_remove);

            listTable_BTN_Order_or_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(callback_table != null){
                        callback_table.action_button_clicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(callback_table != null) {
                        callback_table.table_clicked(getItem(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }

    }
}