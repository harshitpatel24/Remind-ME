package com.reminder.menu;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.reminder.R;
import java.util.ArrayList;

public class Fragment_AddHolder extends Fragment {


    ArrayAdapter adapter;
    SQLClass db;
    ArrayList<Model_Holder> arr_list = new ArrayList<>();
    Model_Holder hm;
    FloatingActionButton FAB;
    static int main_id;
    RecyclerView holder_res;
    Adapter_Holder mAdapter;

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
        View view = inflater.inflate(R.layout.fragment_addholder, null);
        FAB = (FloatingActionButton) view.findViewById(R.id.holder_listview_floating_button);
        db = new SQLClass(getContext());
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View dialogueview = layoutInflater.inflate(R.layout.addholder_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setView(dialogueview);
                final EditText dialog_name = (EditText) dialogueview.findViewById(R.id.dialog_editText_name);
                final EditText dialog_age = (EditText) dialogueview.findViewById(R.id.dialog_editText_age);
                final Spinner relation = (Spinner) dialogueview.findViewById(R.id.dialog_spinner_relation);
                ArrayAdapter relation_adapter;
                ArrayList<String> relation_list = new ArrayList<>();
                relation_list.add("Son");
                relation_list.add("Daughter");
                relation_list.add("Mother");
                relation_list.add("Father");
                relation_list.add("Other");
                relation_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, relation_list);
                relation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                relation.setAdapter(relation_adapter);
                builder.setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialog_age.getText().toString().equals("") || dialog_name.getText().toString().equals("")) {
                                    Toast.makeText(getContext(), "Enter Name and Age", Toast.LENGTH_SHORT).show();
                                } else {
                                    boolean insert = db.insertHolder(main_id, dialog_name.getText().toString(), relation.getSelectedItem().toString(), Integer.parseInt(dialog_age.getText().toString()));
                                    if (insert == true) {
                                        //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Fragment_AddHolder add = new Fragment_AddHolder();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.containerView, add);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
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
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();

            }
        });

        db = new SQLClass(getContext());
        Cursor data = db.viewAllHolder(String.valueOf(main_id));

        if (data.getCount() != 0) {
            data.moveToFirst();
            do {
                hm = new Model_Holder(data.getInt(1), data.getString(2), data.getInt(4));
                arr_list.add(hm);
            } while (data.moveToNext());
        }
        holder_res = (RecyclerView) view.findViewById(R.id.holder_recyclerview);
        holder_res.setLayoutManager(new LinearLayoutManager(getContext()));
        holder_res.setHasFixedSize(true);
        mAdapter = new Adapter_Holder(arr_list);
        holder_res.setAdapter(mAdapter);
        /*
        adapter=new ArrayAdapter<String>(getContext(),R.layout.listview_display,arr_list);
        list_cat=(ListView) view.findViewById(R.id.listview_holder_name);
        list_cat.setAdapter(adapter);*/
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}