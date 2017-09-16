package com.reminder.menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.reminder.MainActivity;
import com.reminder.R;
import com.reminder.multispinner.MultiSpinnerSearch;

import java.util.ArrayList;
import java.util.List;


public class Activity_AddPlan extends AppCompatActivity {

    static int id, update=0;
    static int main_id;
    EditText name, policy_no,main_holder;
    Spinner spinner_category_names;
    MultiSpinnerSearch spinner_holdernames;
    FloatingActionButton fab_addholder, fab_add_category;
    ArrayAdapter adapter;
    SQLClass db;
    ArrayList<String> arr_list_category = new ArrayList<>();
    ArrayList<String> arr_list_holder = new ArrayList<>();
    static Model_DataBase dx;
    int chk = 0;
    Button next;

    public static void newbackIntent(int up)
    {
        update=up;
    }

    public static Intent updatemode(Context context, int up, Model_DataBase d) {
        Intent i = new Intent(context, Activity_AddPlan.class);
        update = up;
        dx = d;
        return i;
    }

    public static Intent newIntent(Context context, int up) {
        Intent i = new Intent(context, Activity_AddPlan.class);
        update = up;
        return i;
    }

    public void setId(int id) {
        main_id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addplan);
        Initialize();
        if (update == 1) {
            UpdateData();
        }
        AddData();
    }

    public void Initialize() {
        main_holder=(EditText) findViewById(R.id.edittext_form_main_holder_name);
        name = (EditText) findViewById(R.id.edittext_form_name);
        policy_no = (EditText) findViewById(R.id.edittext_form_policy_no);
        spinner_category_names = (Spinner) findViewById(R.id.spinner_category_names);
        spinnerdata_category();
        spinner_holdernames = (MultiSpinnerSearch) findViewById(R.id.searchMultiSpinner);
        next = (Button) findViewById(R.id.button_form_submit);
        fab_addholder = (FloatingActionButton) findViewById(R.id.fab_add_holder_names);
        fab_add_category = (FloatingActionButton) findViewById(R.id.fab_add_category_names);
    }

    public void UpdateData() {
        name.setText(dx.getName());
        policy_no.setText(dx.getPolicy_No());
        String install = dx.getInvestMent_Plan();
        spinner_category_names.setSelection(adapter.getPosition(install));
        main_holder.setText(dx.getMain_Holder());
        fab_add_category.setEnabled(false);
        spinner_category_names.setEnabled(false);
    }

    public void AddData() {
        showHolderData();
        fab_addholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Activity_AddPlan.this, "Add holder Dialog", Toast.LENGTH_LONG).show();

                LayoutInflater layoutInflater = LayoutInflater.from(Activity_AddPlan.this);
                View dialogueview = layoutInflater.inflate(R.layout.addholder_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_AddPlan.this);

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
                relation_adapter = new ArrayAdapter(Activity_AddPlan.this, android.R.layout.simple_spinner_item, relation_list);
                relation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                relation.setAdapter(relation_adapter);

                builder.setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialog_age.getText().toString().equals("") || dialog_name.getText().toString().equals("")) {
                                    Toast.makeText(Activity_AddPlan.this, "Enter Name and Age", Toast.LENGTH_SHORT).show();
                                } else {
                                    boolean insert = db.insertHolder(main_id, dialog_name.getText().toString(), relation.getSelectedItem().toString(), Integer.parseInt(dialog_age.getText().toString()));
                                    if (insert == true) {
                                        showHolderData();
                                        Toast.makeText(Activity_AddPlan.this, "Success", Toast.LENGTH_SHORT).show();
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

        fab_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(), "Add category Avtivity", Toast.LENGTH_LONG).show();

                LayoutInflater layoutInflater = LayoutInflater.from(Activity_AddPlan.this);
                View dialoguecategory = layoutInflater.inflate(R.layout.fragment_addcategory, null);
                AlertDialog.Builder builder_category = new AlertDialog.Builder(Activity_AddPlan.this);

                builder_category.setView(dialoguecategory);
                final EditText dialog_categoryname = (EditText) dialoguecategory.findViewById(R.id.edittext_category_name);
                builder_category.setCancelable(true)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (dialog_categoryname.getText().toString().equals("")) {
                                    Toast.makeText(Activity_AddPlan.this, "Enter Plan Name", Toast.LENGTH_SHORT).show();
                                } else {
                                    Cursor data = db.viewAllcategory(String.valueOf(main_id));
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
                                            Toast.makeText(Activity_AddPlan.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                            spinnerdata_category();
                                        } else {
                                            Toast.makeText(Activity_AddPlan.this, "Data is not Added", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Activity_AddPlan.this, "Already Exist", Toast.LENGTH_SHORT).show();
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
        next.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkValidation()) {

                            if (update == 1) {
                                Intent i = Activity_AddPlan2.newIntent1(Activity_AddPlan.this, name.getText().toString(), policy_no.getText().toString()
                                        , spinner_category_names.getSelectedItem().toString(), spinner_holdernames.getSelectedItem().toString(),main_holder.getText()
                                        .toString(), 1, dx);
                                update = 0;
                                startActivity(i);
                            } else {
                                Intent i = Activity_AddPlan2.newIntent(Activity_AddPlan.this, name.getText().toString(), policy_no.getText().toString()
                                        , spinner_category_names.getSelectedItem().toString(), spinner_holdernames.getSelectedItem().toString(),
                                        main_holder.getText().toString());
                                startActivity(i);

                            }
                        } else {
                            Toast.makeText(Activity_AddPlan.this, "Please fill up all the details", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
    }

    private void showHolderData() {
        List<String> list = new ArrayList<>();
        db = new SQLClass(Activity_AddPlan.this);
        Cursor data = db.viewAllHolder(String.valueOf(main_id));
        list.clear();
        if (data.getCount() != 0) {
            data.moveToFirst();
            do {
                list.add(data.getString(2));
            } while (data.moveToNext());
        }
        final List<KeyPairBoolData> listArray = new ArrayList<>();
        listArray.clear();
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray.add(h);

        }
        /***
         * -1 is no by default selection
         * 0 to length will select corresponding values
         */
        spinner_holdernames.setItems(listArray, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });
        spinner_holdernames.resetLimit();
        spinner_holdernames.setLimit(5, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(Activity_AddPlan.this,
                        "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void spinnerdata_category() {
        db = new SQLClass(Activity_AddPlan.this);
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
        adapter = new ArrayAdapter<String>(Activity_AddPlan.this, android.R.layout.simple_spinner_dropdown_item, arr_list_category);
        spinner_category_names.setAdapter(adapter);
    }

    private boolean checkValidation() {

        if (name.getText().toString().equals("") || policy_no.getText().toString().equals("") || main_holder.getText().toString().equals("") ) {
            return false;
        } else {
            return true;
        }
    }

    private void getHoldernames() {
        db = new SQLClass(Activity_AddPlan.this);
        Cursor data = db.viewAllHolder(String.valueOf(main_id));

        if (data.getCount() != 0) {
            data.moveToFirst();
            do {
                arr_list_holder.add(data.getString(2));
            } while (data.moveToNext());
        }
        adapter = new ArrayAdapter<String>(Activity_AddPlan.this, R.layout.listview_display, arr_list_holder);
        spinner_holdernames.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent i = MainActivity.newIntentx(Activity_AddPlan.this, 2);
        startActivity(i);
    }

}
