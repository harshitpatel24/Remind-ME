package com.reminder.menu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reminder.MainActivity;
import com.reminder.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_AddPlan2 extends AppCompatActivity {

    private OnFragmentInteractionListener mListener;
    static int id;
    static android.support.v7.app.AlertDialog.Builder a_builder;
    static String Name, Holder_Names, Policy_No, Investment_Plan, reminder,Main_Holder;
    Spinner install_duration, payment_mode, term;
    ArrayAdapter dataAdapter1, dataAdapter3;
    ArrayList<String> installment = new ArrayList<>();
    ArrayList<String> remind_day = new ArrayList<>();
    int mYear, mMonth, mDay, cal = 0, count = 0, total = 0, paid = 0;
    static int main_id;
    Button submit;
    TextView No_Of_Installment, Total_premium_amount;
    SQLClass sql;
    EditText sum_assured, premium_amount, maturity_date, start_date, premium_date;
    ImageView maturity_datepicker, start_datepicker, premiumdatepicker;
    static Model_DataBase dx;
    static int update = 0;
    int sum;

    public void setId(int id) {
        main_id = id;
    }

    public static Intent newIntent(Context context, String name, String policy_no, String plan, String holder,String main_holder) {
        Intent i = new Intent(context, Activity_AddPlan2.class);
        Name = name;
        update = 0;
        Holder_Names = holder;
        Main_Holder=main_holder;
        Policy_No = policy_no;
        Investment_Plan = plan;
        return i;
    }

    public static Intent newIntent1(Context context, String name, String policy_no, String plan, String holder,String main_holder, int up, Model_DataBase d) {
        Intent i = new Intent(context, Activity_AddPlan2.class);
        Name = name;
        Holder_Names = holder;
        Main_Holder=main_holder;
        Policy_No = policy_no;
        Investment_Plan = plan;
        update = up;
        dx = d;
        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addplan2);

        installment.add("1 Month");
        installment.add("2 Month");

        remind_day.add("1 Days");
        remind_day.add("2 Days");
        remind_day.add("4 Days");
        remind_day.add("7 Days");
        remind_day.add("10 Days");

        dataAdapter1 = new ArrayAdapter(Activity_AddPlan2.this, android.R.layout.simple_spinner_item, installment);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataAdapter3 = new ArrayAdapter(Activity_AddPlan2.this, android.R.layout.simple_spinner_item, remind_day);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sql = new SQLClass(getApplicationContext());
        Initialize();
        if (update == 1) {
            UpdataData();
        }
        AddData();

    }

    public void Initialize() {
        installment.add("3 Month");
        installment.add("4 Month");
        installment.add("5 Month");
        installment.add("6 Month");
        installment.add("7 Month");
        installment.add("8 Month");
        installment.add("9 Month");
        installment.add("10 Month");
        installment.add("11 Month");
        installment.add("12 Month");

        premium_date = (EditText) findViewById(R.id.edittext_form_premium_date);
        Total_premium_amount = (TextView) findViewById(R.id.textview_form_total_premium_amount_dynamic);
        No_Of_Installment = (TextView) findViewById(R.id.no_of_installment_dynamic);
        premiumdatepicker = (ImageView) findViewById(R.id.imageview_form_premium_date);
        sum_assured = (EditText) findViewById(R.id.edittext_form_sum_asssured);
        premium_amount = (EditText) findViewById(R.id.edittext_form_interval_amount);
        start_date = (EditText) findViewById(R.id.edittext_form_startdate);
        maturity_date = (EditText) findViewById(R.id.edittext_form_maturity_date);
        payment_mode = (Spinner) findViewById(R.id.spinner_form_premium_interval);
        payment_mode.setAdapter(dataAdapter1);
        term = (Spinner) findViewById(R.id.spinner_form_before_notify);
        term.setAdapter(dataAdapter3);
        start_datepicker = (ImageView) findViewById(R.id.imageview_form_start_date);
        maturity_datepicker = (ImageView) findViewById(R.id.imageview_form_maturity_date);
        submit = (Button) findViewById(R.id.button_submit);
    }

    public void UpdataData() {
        premium_amount.setText(String.valueOf(dx.getPremium_Amount()));
        premium_date.setText(dx.getPremium_Date());
        Total_premium_amount.setText(String.valueOf(dx.getTotal_Premium_Amount()));
        No_Of_Installment.setText(String.valueOf(dx.getNo_Installment()));
        start_date.setText(dx.getStart_Date());
        maturity_date.setText(dx.getMaturity_Date());
        String interval = dx.getPayment_Mode();
        payment_mode.setSelection(dataAdapter1.getPosition(interval));
        String before = dx.getBefore_notify();
        term.setSelection(dataAdapter3.getPosition(before));
        sum_assured.setText(dx.getSum_Assured());

        premium_amount.setEnabled(false);
        premium_date.setEnabled(false);
        start_date.setEnabled(false);
        maturity_date.setEnabled(false);
        payment_mode.setEnabled(false);
        sum_assured.setEnabled(false);

    }

    public void AddData() {
        sum_assured.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(Activity_AddPlan2.this,sum_assured.getText(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sum_assured.removeTextChangedListener(this);

                try {
                    String givenstring = editable.toString();
                    Long longval;
                    if (givenstring.contains(",")) {
                        givenstring = givenstring.replaceAll(",", "");
                        sum = Integer.parseInt(givenstring);
                        //Toast.makeText(Activity_AddPlan2.this,givenstring.toString(),Toast.LENGTH_SHORT).show();
                    }
                    longval = Long.parseLong(givenstring);
                    DecimalFormat formatter = new DecimalFormat("##,##,###");
                    String formattedString = formatter.format(longval);
                    sum_assured.setText(formattedString);
                    sum_assured.setSelection(sum_assured.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sum_assured.addTextChangedListener(this);

            }
        });


        start_datepicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        maturity_date.setText("");
                        premium_date.setText("");
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_AddPlan2.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        start_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
        );
        maturity_datepicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        premium_date.setText("");
                        if (start_date.getText().toString().equals("")) {
                            Toast.makeText(Activity_AddPlan2.this, "Please select Start Date First", Toast.LENGTH_LONG).show();
                        } else {

                            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_AddPlan2.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            maturity_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                            Date start;
                                            Date end;
                                            long total_days = 0;
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                                            try {

                                                start = simpleDateFormat.parse(start_date.getText().toString());
                                                end = simpleDateFormat.parse(maturity_date.getText().toString());
                                                total_days = printDifference(start, end);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            installment.clear();
                                            int i = 1;
                                            cal++;
                                            int x = (int) total_days / 30;
                                            while (i <= x && i <= 12) {
                                                installment.add(i + " Month");
                                                i++;
                                            }


                                        }
                                    }, mYear, mMonth, mDay);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                            Date StartDate = new Date();
                            try {
                                StartDate = simpleDateFormat.parse(start_date.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(StartDate);
                            cal1.add(Calendar.MONTH, 2);
                            datePickerDialog.getDatePicker().setMinDate(cal1.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    }
                }

        );
        if (cal != 0) {

            dataAdapter1 = new ArrayAdapter(Activity_AddPlan2.this, android.R.layout.simple_spinner_item, installment);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            install_duration.setAdapter(dataAdapter1);
        }
        premium_amount.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        premium_date.setText("");
                    }
                }
        );

        premiumdatepicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (start_date.getText().toString().equals("") || maturity_date.getText().toString().equals("") || premium_amount.getText().toString().equals("")) {
                            Toast.makeText(Activity_AddPlan2.this, "Please select Start Date and End Date and Interval Amount First", Toast.LENGTH_LONG).show();
                        } else {
                            final Calendar c = Calendar.getInstance();
                            final Calendar cal44 = Calendar.getInstance();
                            final Calendar cal43 = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_AddPlan2.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            premium_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                                            String xy = payment_mode.getSelectedItem().toString();
                                            Date endDate1 = new Date();
                                            Date premiumDate1 = new Date();
                                            String seperate[] = xy.split(" ");

                                            int imm = Integer.valueOf(seperate[0]);
                                            Calendar cal22 = Calendar.getInstance();
                                            try {
                                                endDate1 = simpleDateFormat.parse(maturity_date.getText().toString());
                                                premiumDate1 = simpleDateFormat.parse(premium_date.getText().toString());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            count = 0;
                                            cal22.setTime(premiumDate1);
                                            cal44.setTime(endDate1);
                                            //cal43.setTime(startDate);
                                            while (cal44.after(cal22)) {
                                                count++;
                                                cal22.add(Calendar.MONTH, imm);
                                            }
                                            total = count * Integer.valueOf(premium_amount.getText().toString());

                                            No_Of_Installment.setText(String.valueOf(count));
                                            Total_premium_amount.setText(String.valueOf(total));
                                        }
                                    }, mYear, mMonth, mDay);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                            Date startDate = new Date();
                            Date endDate = new Date();
                            try {
                                endDate = simpleDateFormat.parse(maturity_date.getText().toString());
                                startDate = simpleDateFormat.parse(start_date.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cal44.setTime(endDate);
                            cal43.setTime(startDate);
                            datePickerDialog.getDatePicker().setMinDate(cal43.getTimeInMillis());
                            datePickerDialog.getDatePicker().setMaxDate(cal44.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    }
                }
        );


        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (update == 1) {
                            boolean isupdated = sql.updatedata(String.valueOf(main_id), String.valueOf(dx.getId()), Name, Holder_Names, Main_Holder,Policy_No, term.getSelectedItem().toString());
                            Toast.makeText(Activity_AddPlan2.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                            update = 0;
                            Cursor cr = sql.ViewSingleSnoozeDate(String.valueOf(main_id), String.valueOf(id));
                            if (cr.getCount() > 0) {
                                String d = term.getSelectedItem().toString();
                                String seperated[] = d.split(" ");
                                int f = Integer.valueOf(seperated[0]);
                                Cursor cz = sql.ViewSingleDate(String.valueOf(main_id), String.valueOf(id));
                                cz.moveToFirst();
                                int day = cz.getInt(2);
                                int month = cz.getInt(3);
                                int year = cz.getInt(4);
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.DAY_OF_MONTH, day);
                                cal.set(Calendar.MONTH, month);
                                cal.set(Calendar.YEAR, year);
                                cal.add(Calendar.DAY_OF_MONTH, -f);
                                int day1 = cal.get(Calendar.DAY_OF_MONTH);
                                int month1 = cal.get(Calendar.MONTH) + 1;
                                int year1 = cal.get(Calendar.YEAR);
                                sql.updateSnoozeDate(String.valueOf(main_id), String.valueOf(id), day1, month1, year1, 8, 00, "P");

                            }
                                Intent myIntent = new Intent(Activity_AddPlan2.this, MyAlarmService.class);
                                startService(myIntent);
                            Intent i = MainActivity.newIntentx(Activity_AddPlan2.this, 2);
                            startActivity(i);
                        } else if (checkValidation()) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                            String xy = payment_mode.getSelectedItem().toString();
                            Date endDate1 = new Date();
                            Date premiumDate1 = new Date();
                            String seperate[] = xy.split(" ");
                            int imm = Integer.valueOf(seperate[0]);
                            Calendar cal22 = Calendar.getInstance();
                            Calendar cal44 = Calendar.getInstance();
                            try {
                                endDate1 = simpleDateFormat.parse(maturity_date.getText().toString());
                                premiumDate1 = simpleDateFormat.parse(premium_date.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cal22.setTime(premiumDate1);
                            cal44.setTime(endDate1);
                            while (cal44.after(cal22)) {
                                cal22.add(Calendar.MONTH, imm);
                            }
                            cal22.add(Calendar.MONTH, -imm);
                            int last_day, last_month, last_year;
                            last_day = cal22.get(Calendar.DAY_OF_MONTH);
                            last_month = cal22.get(Calendar.MONTH) + 1;
                            last_year = cal22.get(Calendar.YEAR);
                            String last_premium_date = last_day + "/" + last_month + "/" + last_year;
                            if (Holder_Names.equalsIgnoreCase("select holder")) {
                                Holder_Names = "No Sub Holder";
                            }
                            boolean isInserted = sql.insertdata(main_id, Name, Holder_Names, Main_Holder,Investment_Plan, Policy_No, sum_assured.getText().toString(),
                                    start_date.getText().toString(), maturity_date.getText().toString(), payment_mode.getSelectedItem().toString(),
                                    Integer.parseInt(premium_amount.getText().toString()), premium_date.getText().toString(),
                                    Integer.parseInt(No_Of_Installment.getText().toString()), Integer.parseInt(Total_premium_amount
                                            .getText().toString()), term.getSelectedItem().toString(), last_premium_date);

                            if (isInserted == true) {
                                Cursor cr = sql.viewAllData(String.valueOf(main_id));
                                int id = 0;
                                if (cr.getCount() > 0) {
                                    cr.moveToLast();
                                    id = cr.getInt(1);
                                }

                                Date endDate = new Date();
                                Date premiumDate = new Date();
                                Calendar cal2 = Calendar.getInstance();
                                Calendar cal3 = Calendar.getInstance();
                                Calendar cal4 = Calendar.getInstance();
                                String x = payment_mode.getSelectedItem().toString();
                                String seperat[] = x.split(" ");
                                int day, month, year;
                                int im = Integer.valueOf(seperat[0]);
                                try {
                                    endDate = simpleDateFormat.parse(last_premium_date);
                                    premiumDate = simpleDateFormat.parse(premium_date.getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                cal3.setTime(endDate);
                                cal4.setTime(premiumDate);
                                String d = term.getSelectedItem().toString();
                                String seperated[] = d.split(" ");
                                int f = Integer.valueOf(seperated[0]);
                                int day1, month1, year1;
                                if (cal3.after(cal2)) {

                                    while (cal4.before(cal2)) {
                                        paid++;
                                        cal4.add(Calendar.MONTH, im);
                                    }
                                    if (cal4.before(cal3)) {
                                        day = cal4.get(Calendar.DAY_OF_MONTH);
                                        month = cal4.get(Calendar.MONTH) + 1;
                                        year = cal4.get(Calendar.YEAR);
                                        cal4.add(Calendar.DAY_OF_MONTH, -f);
                                        day1 = cal4.get(Calendar.DAY_OF_MONTH);
                                        month1 = cal4.get(Calendar.MONTH) + 1;
                                        year1 = cal4.get(Calendar.YEAR);
                                    } else {
                                        day = cal3.get(Calendar.DAY_OF_MONTH);
                                        month = cal3.get(Calendar.MONTH) + 1;
                                        year = cal3.get(Calendar.YEAR);
                                        cal3.add(Calendar.DAY_OF_MONTH, -f);
                                        day1 = cal3.get(Calendar.DAY_OF_MONTH);
                                        month1 = cal3.get(Calendar.MONTH) + 1;
                                        year1 = cal3.get(Calendar.YEAR);
                                    }


                                    boolean insdate = sql.insertDate(main_id, id, day, month, year, 8, 00);
                                    boolean insdate1 = sql.insertSnoozeDate(main_id, id, day1, month1, year1, 8, 00, "P");
                                    if (insdate == true && insdate1 == true) {
                                        if (paid == 0) {
                                            boolean ins = sql.insertInstallMonth(main_id, id, paid, count);
                                            Toast.makeText(Activity_AddPlan2.this, "Data Successfully Added", Toast.LENGTH_LONG).show();
                                                Intent myIntent = new Intent(Activity_AddPlan2.this, MyAlarmService.class);
                                                startService(myIntent);
                                            Intent i = MainActivity.newIntentx(Activity_AddPlan2.this, 2);
                                            startActivity(i);
                                        } else {
                                            installdialogue(id);
                                        }

                                    }
                                } else {
                                    final int xz = id;
                                    a_builder = new AlertDialog.Builder(Activity_AddPlan2.this);
                                    a_builder.setMessage("Your Plan is already Completed and You can see it in Completed Plan Field")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    sql.insertCompletedPlan(main_id, xz, Name, Investment_Plan, Policy_No);
                                                        Intent myIntent = new Intent(Activity_AddPlan2.this, MyAlarmService.class);
                                                        startService(myIntent);
                                                    Intent i = MainActivity.newIntentx(Activity_AddPlan2.this, 2);
                                                    startActivity(i);
                                                }
                                            });
                                    android.support.v7.app.AlertDialog alert = a_builder.create();
                                    alert.setTitle("Alert");
                                    alert.show();
                                }
                            } else {
                                Toast.makeText(Activity_AddPlan2.this, "Data is not Added", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Activity_AddPlan2.this, "Please fill all details", Toast.LENGTH_LONG).show();
                        }

                    }

                }
        );
    }

    private boolean checkValidation() {
        int x=0;
        if(!premium_amount.getText().toString().equals(""))
        {
            x = Integer.valueOf(premium_amount.getText().toString()) * Integer.valueOf(No_Of_Installment.getText().toString());
        }
        if (x > sum) {
            Toast.makeText(getApplicationContext(), "Sum Assured Should be greater than or equal to total amount", Toast.LENGTH_LONG).show();
            return false;
        }
        if (start_date.getText().toString().equals("") || maturity_date.getText().toString().equals("") ||
                sum_assured.getText().toString().equals("") || premium_date.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public void installdialogue(final int i)
    {
        final int xz = i;
        a_builder = new AlertDialog.Builder(Activity_AddPlan2.this);
        LayoutInflater layoutInflater = LayoutInflater.from(Activity_AddPlan2.this);
        View dialoguecategory = layoutInflater.inflate(R.layout.alert_paid_installment, null);
        a_builder.setView(dialoguecategory);
        final EditText no = (EditText) dialoguecategory.findViewById(R.id.paid_installment);
        a_builder.setMessage("Total " + paid + " Premium that you missed")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(no.getText().toString().equals("")||Integer.valueOf(no.getText().toString())<0||Integer.valueOf(no.getText().toString())>paid)
                        {
                            Toast.makeText(Activity_AddPlan2.this,"Please enter valid installment month",Toast.LENGTH_LONG).show();
                            installdialogue(i);
                        }
                        else
                        {
                            boolean ins = sql.insertInstallMonth(main_id, xz, Integer.valueOf(no.getText().toString())
                                    , count);
                            if (paid == Integer.valueOf(no.getText().toString())) {

                            } else {
                                int pd = paid - Integer.valueOf(no.getText().toString());
                                pd++;
                                int pending = pd * Integer.valueOf(premium_amount.getText().toString());
                                boolean pm=sql.insertPendingPremium(main_id, xz, pending, pd);
                            }
                            Toast.makeText(Activity_AddPlan2.this, "Data Successfully Added", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(Activity_AddPlan2.this, MyAlarmService.class);
                            startService(myIntent);
                            Intent i = MainActivity.newIntentx(Activity_AddPlan2.this, 2);
                            startActivity(i);
                        }

                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Alert");
        alert.show();

    }

    public long printDifference(Date startDate, Date endDate) {

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onBackPressed()
    {
        if(update==1)
        {
            Activity_AddPlan.newbackIntent(1);
        }
        super.onBackPressed();
    }
}
