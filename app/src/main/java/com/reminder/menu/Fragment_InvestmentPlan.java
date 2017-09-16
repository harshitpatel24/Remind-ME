package com.reminder.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.reminder.R;
import java.util.ArrayList;

/**
 * Created by admin on 11-03-2017.
 */

public class Fragment_InvestmentPlan extends Fragment {

    int counter = 0;
    public RecyclerView mRecyclerView;
    //public RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    int image;
    private ArrayList<Model_Category> mCategoryModel;
    Model_Category model;
    ArrayAdapter adapter;
    SQLClass db;
    ArrayList<String> arr_list = new ArrayList<>();
    ListView list_cat;
    static int main_id;
    int chk = 0;
    ArrayList<String> arr_list_category = new ArrayList<>();
    FloatingActionButton fab_add_category;

    public void setId(int id) {
        main_id = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addinvestmentplan, null);

        fab_add_category = (FloatingActionButton) view.findViewById(R.id.listview_floating_button);
        fab_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Add category Avtivity", Toast.LENGTH_LONG).show();

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View dialoguecategory = layoutInflater.inflate(R.layout.fragment_addcategory, null);
                AlertDialog.Builder builder_category = new AlertDialog.Builder(getContext());

                builder_category.setView(dialoguecategory);
                final EditText dialog_categoryname = (EditText) dialoguecategory.findViewById(R.id.edittext_category_name);
                builder_category.setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor data = db.viewAllcategory(String.valueOf(main_id));
                                if (dialog_categoryname.getText().toString().equals("")) {
                                    Toast.makeText(getContext(), "Enter Plan Name", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (data.getCount() > 0) {
                                        data.moveToFirst();
                                        do {
                                            if (data.getString(1).toLowerCase().equals(dialog_categoryname.getText().toString().toLowerCase())) {
                                                chk = 1;
                                                break;
                                            }
                                        } while (data.moveToNext());
                                    }
                                    if (chk == 0) {
                                        data = db.viewDefaultCategory();
                                        if (data.getCount() > 0) {
                                            data.moveToFirst();
                                            do {
                                                if (data.getString(0).toLowerCase().equals(dialog_categoryname.getText().toString().toLowerCase())) {
                                                    chk = 1;
                                                    break;
                                                }
                                            } while (data.moveToNext());
                                        }
                                    }
                                    if (chk == 0) {
                                        boolean isInserted = db.insertcategory(main_id, dialog_categoryname.getText().toString());
                                        if (isInserted == true) {
                                            Toast.makeText(getContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                            Fragment_InvestmentPlan up = new Fragment_InvestmentPlan();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.containerView, up);
                                            fragmentTransaction.commit();
                                        } else {
                                            Toast.makeText(getContext(), "Data is not Added", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Already Exist", Toast.LENGTH_SHORT).show();
                                        chk = 0;
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = builder_category.create();

                // show it
                alertDialog.show();
            }
        });


        db = new SQLClass(getContext());
        Cursor data;
        data = db.viewDefaultCategory();
        if (data.getCount() > 0) {
            data.moveToFirst();
            do {
                counter++;
                arr_list.add(data.getString(0));
            } while (data.moveToNext());
        }
        data = db.viewAllcategory(String.valueOf(main_id));

        if (data.getCount() > 0) {
            data.moveToFirst();
            do {
                counter++;
                arr_list.add(data.getString(1));
            } while (data.moveToNext());
        }
       /* adapter=new ArrayAdapter<String>(getContext(),R.layout.listview_display,arr_list);
        list_cat=(ListView) view.findViewById(R.id.listview_category);
        list_cat.setAdapter(adapter);*/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        //  mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));


        mCategoryModel = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            if (arr_list.get(i).equalsIgnoreCase("Mediclaim")) {
                image = R.drawable.mediclaim;
            } else if (arr_list.get(i).equalsIgnoreCase("LIC")) {
                image = R.drawable.lic;
            } else if (arr_list.get(i).equalsIgnoreCase("pf")) {
                image = R.drawable.pf;
            } else {
                image = R.drawable.user;
            }

            model = new Model_Category(arr_list.get(i), image);
            mCategoryModel.add(model);

        }
        mAdapter = new Adapter_List(mCategoryModel);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
    /*private void spinnerdata_category() {
        db = new SQLClass(getContext());
        arr_list_category.clear();
        Cursor data;
        data = db.viewDefaultCategory();
        if (data.getCount() != 0) {
            data.moveToFirst();
            do {
                arr_list_category.add(data.getString(0));
            } while (data.moveToNext());
        }
        data = db.viewAllcategory(String.valueOf(main_id));
        if (data.getCount() != 0) {
            data.moveToFirst();
            do {
                arr_list_category.add(data.getString(1));
            } while (data.moveToNext());
        }
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr_list_category);
        spinner_category_names.setAdapter(adapter);
    }*/

}
