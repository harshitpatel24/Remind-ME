package com.reminder.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminder.R;

import java.util.ArrayList;

/**
 * Created by Hitarth on 24-03-2017.
 */

public class Adapter_List extends RecyclerView.Adapter<Adapter_List.ListViewHolder> {
    Model_Category model;
    ArrayList<Model_Category> model_list;

    public Adapter_List(ArrayList<Model_Category> model_list1) {
        this.model_list = model_list1;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);
        ListViewHolder recyclerViewHolder = new ListViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Model_Category dbm = model_list.get(position);
        model = dbm;
        holder.names.setText(dbm.getName());
        holder.imageView.setImageResource(dbm.getImage());
    }

    @Override
    public int getItemCount() {
        return model_list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView names;
        ImageView imageView;

        public ListViewHolder(View itemView) {
            super(itemView);

            names = (TextView) itemView.findViewById(R.id.cardview_name);
            imageView = (ImageView) itemView.findViewById(R.id.cardview_image);

            // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}