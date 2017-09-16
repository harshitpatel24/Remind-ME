package com.reminder.menu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reminder.MainActivity;
import com.reminder.R;
import com.reminder.utils.Session;

import java.util.Calendar;

/**
 * Created by admin on 21-03-2017.
 */

public class Activity_AddQuickReminder extends AppCompatActivity {
    Calendar c;
    EditText title_edit;
    EditText time_edit;
    EditText date_edit, edit_desc;
    Button ok_button;
    ImageView time_img;
    ImageView date_img;
    static int Update = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    static int main_id;
    static QuickReminderModel dx;

    public void setId(int id) {
        main_id = id;
    }

    public static Intent newIntent(Context content, int up, QuickReminderModel d) {
        Intent i = new Intent(content, Activity_AddQuickReminder.class);
        Update = up;
        dx = d;
        return i;
    }

    public static Intent newIntent1(Context content, int up) {
        Intent i = new Intent(content, Activity_AddQuickReminder.class);
        Update = up;
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_quick_reminder);
        title_edit = (EditText) findViewById(R.id.edittext_title);
        time_edit = (EditText) findViewById(R.id.edittext_time);
        date_edit = (EditText) findViewById(R.id.edittext_date);
        ok_button = (Button) findViewById(R.id.button_submit);
        time_img = (ImageView) findViewById(R.id.imageview_time);
        date_img = (ImageView) findViewById(R.id.imageview_date);
        edit_desc = (EditText) findViewById(R.id.edittext_description);
        time_edit.setEnabled(false);
        date_edit.setEnabled(false);
        if (Update == 1) {
            update();
        }
        add();


    }

    public void add() {
        c = Calendar.getInstance();


        time_img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_AddQuickReminder.this,
                                new TimePickerDialog.OnTimeSetListener() {


                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        mHour = hourOfDay;
                                        mMinute = minute;
                                        time_edit.setText(mHour + ":" + mMinute);

                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                }
        );

        date_img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH) + 1;
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_AddQuickReminder.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                date_edit.setText(mDay + "-" + mMonth + "-" + mYear);
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
        );

        ok_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLClass db = new SQLClass(getApplicationContext());
                        if (chkvalidation()) {
                            if (Update == 1) {
                                Update = 0;
                                boolean update = db.updateQuickReminder(String.valueOf(main_id), String.valueOf(dx.getId()), title_edit.getText().toString(), mDay, mMonth, mYear, mHour, mMinute,
                                        edit_desc.getText().toString());
                                if (update == true) {
                                    Cursor cc = db.ViewSingleSnoozeDate(String.valueOf(main_id), String.valueOf(dx.getId()));
                                    if (cc.getCount() > 0) {
                                        boolean update1 = db.updateSnoozeDate(String.valueOf(main_id), String.valueOf(dx.getId()), mDay, mMonth, mYear, mHour, mMinute, "T");
                                        if (update1 == true) {
                                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                                                Intent myIntent = new Intent(Activity_AddQuickReminder.this, MyAlarmService.class);
                                                startService(myIntent);
                                            Intent i = MainActivity.newIntentx(Activity_AddQuickReminder.this, 1);
                                            startActivity(i);
                                        }
                                    } else {
                                        boolean insert1 = db.insertSnoozeDate(main_id, dx.getId(), mDay, mMonth, mYear, mHour, mMinute, "T");
                                        if (insert1 == true) {
                                            Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                                                Intent myIntent = new Intent(Activity_AddQuickReminder.this, MyAlarmService.class);
                                                startService(myIntent);
                                            Intent i = MainActivity.newIntentx(Activity_AddQuickReminder.this, 1);
                                            startActivity(i);
                                        }
                                    }
                                }
                            } else {
                                boolean insert = db.insertQuickReminder(main_id, title_edit.getText().toString(), mDay, mMonth, mYear, mHour, mMinute,
                                        edit_desc.getText().toString());
                                if (insert == true) {
                                    Cursor cc = db.viewAllQuickReminder(String.valueOf(main_id));
                                    cc.moveToLast();
                                    int id = cc.getInt(1);
                                    boolean insert1 = db.insertSnoozeDate(main_id, id, mDay, mMonth, mYear, mHour, mMinute, "T");
                                    if (insert1 == true) {
                                        Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_LONG).show();
                                            Intent myIntent = new Intent(Activity_AddQuickReminder.this, MyAlarmService.class);
                                            startService(myIntent);
                                        Intent i = MainActivity.newIntentx(Activity_AddQuickReminder.this, 1);
                                        startActivity(i);
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }

    public boolean chkvalidation() {
        if (title_edit.getText().toString().equals("") || date_edit.getText().toString().equals("") || time_edit.getText().toString()
                .equals("")) {
            Toast.makeText(Activity_AddQuickReminder.this, "Please fill up all Details", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void update() {
        time_edit.setText(dx.getQuickTime());
        String date = dx.getQuickTime();
        String sep[] = date.split(":");
        mHour = Integer.valueOf(sep[0]);
        mMinute = Integer.valueOf(sep[1]);
        date_edit.setText(dx.getQuickDate());
        date = dx.getQuickDate();
        String sep1[] = date.split("/");
        mDay = Integer.valueOf(sep1[0]);
        mMonth = Integer.valueOf(sep1[1]);
        mYear = Integer.valueOf(sep1[2]);
        title_edit.setText(dx.getTitle());
        edit_desc.setText(dx.getDescription());

        title_edit.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Intent i = MainActivity.newIntentx(Activity_AddQuickReminder.this, 1);
        startActivity(i);
    }
}
