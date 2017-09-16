package com.reminder.menu;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reminder.R;

import java.util.ArrayList;

/**
 * Created by admin on 23-03-2017.
 */

public class Fragment_CompletedPlan extends Fragment {

    ArrayList<Model_Completed> arr_list = new ArrayList<>();
    Adapter_Completed mAdapter;
    Model_Completed cm;
    ArrayAdapter adapter;
    static int main_id;
    SQLClass db;
    RecyclerView mRes;

    public static void setId(int id) {
        main_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completedplan, null);
        mRes = (RecyclerView) view.findViewById(R.id.completed_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRes.setLayoutManager(mLayoutManager);
        db = new SQLClass(getContext());
        Cursor data;
        data = db.ViewCompletedPlan(String.valueOf(main_id));
        if (data.getCount() > 0) {
            data.moveToFirst();
            do {
                cm = new Model_Completed(data.getInt(1), data.getString(2), data.getString(3), data.getString(4));
                arr_list.add(cm);
            } while (data.moveToNext());
        } else {
            TextView text = (TextView) view.findViewById(R.id.textview_completed);
            text.setText("No Plan");
        }
        mAdapter = new Adapter_Completed(arr_list, 2);
        mRes.setAdapter(mAdapter);
        return view;
    }
}
