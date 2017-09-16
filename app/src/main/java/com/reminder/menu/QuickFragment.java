package com.reminder.menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.reminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class QuickFragment extends Fragment {

    ArrayAdapter adapter;
    SQLClass db;
    static QuickReminderModel ddd;
    private Adapter_QuickReminder quick_adapter;
    ArrayList<QuickReminderModel> arr_dbmodel = new ArrayList<>();
    RecyclerView recyclerView;
    FloatingActionButton FAB;
    static int main_id;
    Toolbar xyz;

    public void setId(int id) {
        main_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_reminders, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_quick_reminder);
        arr_dbmodel = new ArrayList<>();
        //xyz=(Toolbar)view.findViewById(R.id.toolbar);
        //xyz.setTitle("Hello");
        quick_adapter = new Adapter_QuickReminder(arr_dbmodel, 0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnItemTouchListener(new TouchListener(getContext(), recyclerView, new TouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ddd = arr_dbmodel.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        FAB = (FloatingActionButton) view.findViewById(R.id.quick_reminder_floating_button);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = Activity_AddQuickReminder.newIntent1(getContext(), 0);
                startActivity(i);

            }
        });

        int mDay, mMonth, mYear;
        long total_days = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        db = new SQLClass(getContext());
        Cursor data;
        data = db.viewAllQuickReminder(String.valueOf(main_id));
        if (data.getCount() > 0) {
            data.moveToFirst();
            do {
                Calendar cal = Calendar.getInstance();
                Date Current, date2;
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                mMonth = cal.get(Calendar.MONTH) + 1;
                mYear = cal.get(Calendar.YEAR);
                String currentDate = mDay + "/" + mMonth + "/" + mYear;
                mDay = data.getInt(3);
                mMonth = data.getInt(4);
                mYear = data.getInt(5);
                String getDate = mDay + "/" + mMonth + "/" + mYear;
                try {

                    date2 = simpleDateFormat.parse(getDate);
                    Current = simpleDateFormat.parse(currentDate);

                    total_days = printDifference(Current, date2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String date = data.getInt(3) + "/" + data.getInt(4) + "/" + data.getInt(5);
                String time = data.getInt(6) + ":" + data.getInt(7);
                QuickReminderModel model = new QuickReminderModel(data.getInt(1), data.getString(2), date, time, data.getString(8), total_days);
                arr_dbmodel.add(model);
            } while (data.moveToNext());
        }
        //  adapter=new ArrayAdapter<Model_DataBase>(getContext(),R.layout.listview_display,arr_dbmodel);
        Collections.sort(arr_dbmodel, new Sort());
        recyclerView.setAdapter(quick_adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    public long printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedDays;

    }

    public class Sort implements Comparator<QuickReminderModel> {

        @Override
        public int compare(QuickReminderModel dataBaseModel, QuickReminderModel t1) {
            return (int) dataBaseModel.getDays() - (int) t1.getDays();
            //return Month.valueOf(o1.getMonth().toUpperCase()).compare(Month.valueOf(o2.getMonth().toUpperCase()));
        }
    }
}