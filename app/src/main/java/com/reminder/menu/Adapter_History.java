package com.reminder.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminder.R;

import java.util.ArrayList;

/**
 * Created by admin on 02-02-2017.
 */

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.RecyclerViewHolder> {

    ArrayList<History_Model> arrayList = new ArrayList<>();
    History_Model ddd;
    SQLClass db;
    static int main_id;

    public static void setId(int id) {
        main_id = id;
    }

    public Adapter_History(ArrayList<History_Model> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        History_Model dbm = arrayList.get(position);
        ddd = dbm;
        holder.name.setText(dbm.getName());
        holder.date.setText(dbm.getDate());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, name;
        ImageView delete;
        android.support.v7.app.AlertDialog.Builder a_builder;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.textview_history_date);
            name = (TextView) itemView.findViewById(R.id.textview_history_name);
            delete = (ImageView) itemView.findViewById(R.id.quick_checkbox_paid);
            delete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            db = new SQLClass(view.getContext());
                            a_builder = new AlertDialog.Builder(view.getContext());
                            a_builder.setMessage("Are you sure you want to Delete All History Related to this Plan will be Deleted")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int pos = getAdapterPosition();
                                            History_Model tx = arrayList.get(pos);
                                            db.deleteHistory(String.valueOf(main_id), String.valueOf(tx.getId()));
                                            Context context = view.getContext();
                                            Fragment_Investment up = new Fragment_Investment();
                                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.containerView, up);
                                            fragmentTransaction.commit();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            android.support.v7.app.AlertDialog alert = a_builder.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                    }
            );
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ddd = arrayList.get(pos);

            Intent i = Activity_PlanDetails.newIntent(v.getContext(), ddd.getId(), 2);
            v.getContext().startActivity(i);
        }
    }
}
