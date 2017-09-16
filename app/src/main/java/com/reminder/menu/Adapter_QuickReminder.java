package com.reminder.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.reminder.R;
import java.util.ArrayList;

/**
 * Created by vishvraj on 22-03-2017.
 */

public class Adapter_QuickReminder extends RecyclerView.Adapter<Adapter_QuickReminder.MyViewHolder> {
    static QuickReminderModel ddd;
    ArrayList<QuickReminderModel> arrayList = new ArrayList<>();
    int y;
    static int main_id;

    public static void setId(int id) {
        main_id = id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quick_reminder_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ddd = arrayList.get(position);

        QuickReminderModel dbm = arrayList.get(position);
        ddd = dbm;

        holder.title.setText(ddd.getTitle());
        holder.days_left.setText(String.valueOf(ddd.getDays()) + " Day left");

        int progress = 365 - (int) ddd.getDays();
        if (progress <= 0) {
            progress = 0;
        }
        if (progress > 350) {
            holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (progress > 300 && progress <= 350) {
            holder.progressBar.getProgressDrawable().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);
        } else if (progress > 200 && progress <= 300) {
            holder.progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        } else if (progress > 100 && progress <= 200) {
            holder.progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        } else {
            holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
        holder.progressBar.setProgress(progress);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, days_left;
        public ProgressBar progressBar;
        ImageView imageView, imageView1;
        public CheckBox chk;
        public SQLClass db;
        String date;

        public MyViewHolder(View itemView) {
            super(itemView);
            db = new SQLClass(itemView.getContext());
            progressBar = (ProgressBar) itemView.findViewById(R.id.quick_progressBar);
            title = (TextView) itemView.findViewById(R.id.quick_textView_name);
            days_left = (TextView) itemView.findViewById(R.id.quick_days_left);
            imageView = (ImageView) itemView.findViewById(R.id.quick_checkbox_paid);
            imageView1 = (ImageView) itemView.findViewById(R.id.quick_update);
            db = new SQLClass(itemView.getContext());
            imageView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            AlertDialog.Builder a_builder = new AlertDialog.Builder(view.getContext());
                            a_builder.setMessage("Are You Sure want to delete?")
                                    .setCancelable(true)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int x = getAdapterPosition();
                                            ddd = arrayList.get(x);
                                            db.deleteQuickReminder(String.valueOf(main_id), String.valueOf(ddd.getId()));
                                            Toast.makeText(view.getContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                                            Context context = view.getContext();
                                            QuickFragment up = new QuickFragment();
                                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.containerView, up);
                                            fragmentTransaction.commit();
                                        }

                                    });
                            a_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                    });
            imageView1.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int x = getAdapterPosition();
                            ddd = arrayList.get(x);
                            Intent i = Activity_AddQuickReminder.newIntent(view.getContext(), 1, ddd);
                            view.getContext().startActivity(i);
                        }
                    }
            );
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ddd = arrayList.get(pos);

            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
            View dialoguecategory = layoutInflater.inflate(R.layout.fragment_show_quick_reminders, null);
            android.app.AlertDialog.Builder builder_category = new android.app.AlertDialog.Builder(v.getContext());

            builder_category.setView(dialoguecategory);
            final TextView title = (TextView) dialoguecategory.findViewById(R.id.textview_title_quick);
            final TextView date = (TextView) dialoguecategory.findViewById(R.id.textview_date_quick);
            final TextView time = (TextView) dialoguecategory.findViewById(R.id.textview_time_quick);
            final TextView description = (TextView) dialoguecategory.findViewById(R.id.textview_description_quick);
            Cursor data = db.viewSingleQuickReminer(String.valueOf(main_id), String.valueOf(ddd.getId()));
            if (data.getCount() > 0) {
                data.moveToFirst();
                title.setText(data.getString(2));
                int day = data.getInt(3);
                int month = data.getInt(4);
                int year = data.getInt(5);
                int hour = data.getInt(6);
                int minute = data.getInt(7);
                String dd = day + "/" + month + "/" + year;
                String tm = hour + ":" + minute;
                time.setText(tm);
                date.setText(dd);
                description.setText(data.getString(8));
            }
            builder_category.setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            // create alert dialog
            android.app.AlertDialog alertDialog = builder_category.create();

            // show it
            alertDialog.show();
            /**/
        }
    }

    public Adapter_QuickReminder(ArrayList<QuickReminderModel> arrayList, int x) {
        super();
        this.arrayList = arrayList;
        y = x;
    }

    public Adapter_QuickReminder() {

    }


}

