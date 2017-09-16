package com.reminder.menu;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.reminder.R;
import java.util.ArrayList;

public class Fragment_History extends Fragment {

    RecyclerView res;
    ArrayList<History_Model> arrayList = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    SQLClass DB;
    static int main_id;
    FloatingActionButton fab;

    public void setId(int id) {
        main_id = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_history, container, false);
        res = (RecyclerView) view.findViewById(R.id.history_recycler_view);
        arrayList = new ArrayList<>();
        res.setLayoutManager(new LinearLayoutManager(getActivity()));
        res.setHasFixedSize(true);
        res.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.VERTICAL));

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab.setVisibility(View.GONE);
                // Snackbar.make(view, "Add investment reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(getContext(), Activity_AddPlan.class));

            }
        });

        DB = new SQLClass(getActivity());
        Cursor cursor = DB.viewHistory(String.valueOf(main_id));
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                History_Model model = new History_Model(cursor.getInt(1), cursor.getString(2), cursor.getString(3));
                arrayList.add(model);
            } while (cursor.moveToNext());
        }

        if (cursor.getCount() == 0) {
            TextView tt = (TextView) view.findViewById(R.id.textview_history_nothing);
            tt.setText("Nothing");
        }
        mAdapter = new Adapter_History(arrayList);
        res.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String q = query;
                Intent i = SearchResultActivity.newIntent1(getContext(), query, main_id);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}