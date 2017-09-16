package com.reminder.menu;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.reminder.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Fragment_Upcoming extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_Upcoming_Pending mAdapter;
    SQLClass DB;
    ArrayList<Model_DataBase> arrayList_filter = new ArrayList<>();
    ArrayAdapter adapter;
    SQLClass db;
    ArrayList<String> filter_list = new ArrayList<>();
    ArrayList<Model_DataBase> arrayList=new ArrayList<>();
    ArrayList<Model_Date> arr=new ArrayList<>();
    static Model_DataBase ddd;
    long total_days=0;
    int len;
    Model_Date md;
    Date date;
    int entry=0,state=0;
    static int Id;
    ImageButton filter;
    FloatingActionButton fab;

    public void setId(int id)
    {
        Id=id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_upcoming,null);
        filter = (ImageButton) view.findViewById(R.id.imgbtn_filer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        arrayList=new ArrayList<>();
        mAdapter = new Adapter_Upcoming_Pending(arrayList,0);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnItemTouchListener(new TouchListener(getContext(), recyclerView, new TouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ddd = arrayList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        filter.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                //  LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                //  View filterview = layoutInflater.inflate(R.layout.filter_listview, null);
                arrayList.clear();
                db=new SQLClass(getContext());
                final AlertDialog.Builder filter_builder = new AlertDialog.Builder(getContext());
                //    filter_builder.setView(filterview);
                Cursor data;
                data= db.viewAllcategory(String.valueOf(Id));
                filter_list.clear();
                filter_list.add("All");
                if (data.getCount() > 0) {
                    data.moveToFirst();
                    do {
                        filter_list.add(data.getString(1));
                    } while (data.moveToNext());
                }
                data=db.viewDefaultCategory();
               // filter_list.clear();
                if (data.getCount() > 0) {
                    data.moveToFirst();
                    do {
                        filter_list.add(data.getString(0));
                    } while (data.moveToNext());
                }

                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice, filter_list);
                filter_builder.setTitle("Select category");
                filter_builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Cursor cursor;
                        Cursor cursor1;
                        cursor= DB.viewAllData(String.valueOf(Id));
                        cursor1=DB.ViewDate(String.valueOf(Id));
                        if(cursor1.getCount()!=0)
                        {
                            cursor1.moveToFirst();
                            do {
                                Calendar cal=Calendar.getInstance();
                                int id=cursor1.getInt(1);
                                int day=cursor1.getInt(2);
                                int month=cursor1.getInt(3);
                                int year=cursor1.getInt(4);
                                int hour=cursor1.getInt(5);
                                int minute=cursor1.getInt(6);

                                cal.set(Calendar.DAY_OF_MONTH,day);
                                cal.set(Calendar.MONTH,month-1);
                                cal.set(Calendar.YEAR,year);
                                cal.set(Calendar.HOUR_OF_DAY,hour);
                                cal.set(Calendar.MINUTE,minute);
                                cal.set(Calendar.SECOND,0);
                                // cal.set(Calendar.AM_PM,Calendar.PM);
                                date=cal.getTime();
                                md=new Model_Date(id,date,"P");
                                arr.add(md);
                            }while(cursor1.moveToNext());
                        }
                        if (cursor.getCount() != 0 && cursor1.getCount()!=0) {
                            cursor.moveToFirst();
                            do {
                                if(filter_list.get(i).equalsIgnoreCase("all"))
                                {
                                    entry = 0;
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                                    final Calendar c = Calendar.getInstance();
                                    int mYear = c.get(Calendar.YEAR);
                                    int mMonth = c.get(Calendar.MONTH) + 1;
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    Date installDate = new Date();
                                    Date currentDate = new Date();
                                    String currentDateTimeString;
                                    Calendar cal = Calendar.getInstance();
                                    Calendar cal1 = Calendar.getInstance();
                                    currentDateTimeString = mDay + "/" + mMonth + "/" + mYear;
                                    len = arr.size();
                                    while (len != 0) {
                                        md = arr.get(len - 1);
                                        if (cursor.getInt(1) == md.getId()) {
                                            state++;
                                            entry++;
                                            installDate = md.getDate();
                                            break;
                                        }
                                        len--;
                                    }
                                    if (entry == 0) {
                                        continue;
                                    }
                                    try {
                                        currentDate = simpleDateFormat.parse(currentDateTimeString);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(installDate);
                                    cal1.setTime(currentDate);
                                    Date date2;
                                    mDay = cal.get(Calendar.DAY_OF_MONTH);
                                    mMonth = cal.get(Calendar.MONTH) + 1;
                                    mYear = cal.get(Calendar.YEAR);
                                    String installDateString = mDay + "/" + mMonth + "/" + mYear;
                                    try {

                                        date2 = simpleDateFormat.parse(installDateString);
                                        currentDate = simpleDateFormat.parse(currentDateTimeString);

                                        total_days = printDifference(currentDate, date2);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Model_DataBase model = new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                            cursor.getString(5)
                                            , cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                                            , cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16), total_days);
                                    arrayList.add(model);
                                }
                                else if(cursor.getString(5).equals(filter_list.get(i))) {
                                    entry = 0;
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                                    final Calendar c = Calendar.getInstance();
                                    int mYear = c.get(Calendar.YEAR);
                                    int mMonth = c.get(Calendar.MONTH) + 1;
                                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                                    Date installDate = new Date();
                                    Date currentDate = new Date();
                                    String currentDateTimeString;
                                    Calendar cal = Calendar.getInstance();
                                    Calendar cal1 = Calendar.getInstance();
                                    currentDateTimeString = mDay + "/" + mMonth + "/" + mYear;
                                    len = arr.size();
                                    while (len != 0) {
                                        md = arr.get(len - 1);
                                        if (cursor.getInt(1) == md.getId()) {
                                            state++;
                                            entry++;
                                            installDate = md.getDate();
                                            break;
                                        }
                                        len--;
                                    }
                                    if (entry == 0) {
                                        continue;
                                    }
                                    try {
                                        currentDate = simpleDateFormat.parse(currentDateTimeString);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(installDate);
                                    cal1.setTime(currentDate);
                                    Date date2;
                                    mDay = cal.get(Calendar.DAY_OF_MONTH);
                                    mMonth = cal.get(Calendar.MONTH) + 1;
                                    mYear = cal.get(Calendar.YEAR);
                                    String installDateString = mDay + "/" + mMonth + "/" + mYear;
                                    try {

                                        date2 = simpleDateFormat.parse(installDateString);
                                        currentDate = simpleDateFormat.parse(currentDateTimeString);

                                        total_days = printDifference(currentDate, date2);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Model_DataBase model = new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                                            cursor.getString(5)
                                            , cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                                            , cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16), total_days);
                                    arrayList.add(model);
                                }
                            } while (cursor.moveToNext());
                        }
                        if(state==0)
                        {
                            TextView tt=(TextView) getView().findViewById(R.id.textview_nothing);
                            tt.setText("Nothing");
                        }
                        Collections.sort(arrayList , new Sort());
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                        Toast.makeText(getContext(), "Category:-" + filter_list.get(i), Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                    }
                });
                // create alert dialog
                AlertDialog alertDialog = filter_builder.create();

                // show it
                alertDialog.show();
            }
        });
       fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fab.setVisibility(View.GONE);
                Intent i = Activity_AddPlan.newIntent(getContext(),0);
                startActivity(i);
                // Snackbar.make(view, "Add investment reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //startActivity(new Intent(getContext(),Activity_AddPlan.class));

            }
        });


        DB = new SQLClass(getContext());
            Cursor cursor;
            Cursor cursor1;
            cursor= DB.viewAllData(String.valueOf(Id));
            cursor1=DB.ViewDate(String.valueOf(Id));
            if(cursor1.getCount()!=0)
            {
                cursor1.moveToFirst();
                do {
                    Calendar cal=Calendar.getInstance();
                    int id=cursor1.getInt(1);
                    int day=cursor1.getInt(2);
                    int month=cursor1.getInt(3);
                    int year=cursor1.getInt(4);
                    int hour=cursor1.getInt(5);
                    int minute=cursor1.getInt(6);

                    cal.set(Calendar.DAY_OF_MONTH,day);
                    cal.set(Calendar.MONTH,month-1);
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.HOUR_OF_DAY,hour);
                    cal.set(Calendar.MINUTE,minute);
                    cal.set(Calendar.SECOND,0);
                    // cal.set(Calendar.AM_PM,Calendar.PM);
                    date=cal.getTime();
                    md=new Model_Date(id,date,"P");
                    arr.add(md);
                }while(cursor1.moveToNext());
            }
            if (cursor.getCount() != 0 && cursor1.getCount()!=0) {
                cursor.moveToFirst();
                do {
                    entry=0;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH)+1;
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    Date installDate=new Date();
                    Date currentDate=new Date();
                    String currentDateTimeString;
                    Calendar cal=Calendar.getInstance();
                    //Calendar cal1=Calendar.getInstance();
                    currentDateTimeString=mDay + "/" + mMonth + "/" + mYear;
                    len=arr.size();
                    while(len!=0)
                    {
                        md=arr.get(len-1);
                        if(cursor.getInt(1)==md.getId())
                        {
                            state++;
                            entry++;
                            installDate=md.getDate();
                            break;
                        }
                        len--;
                    }
                    if(entry==0)
                    {
                        continue;
                    }
                    try
                    {
                        currentDate = simpleDateFormat.parse(currentDateTimeString);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cal.setTime(installDate);
                    //cal1.setTime(currentDate);
                    Date date2;
                    mDay=cal.get(Calendar.DAY_OF_MONTH);
                    mMonth=cal.get(Calendar.MONTH)+1;
                    mYear=cal.get(Calendar.YEAR);
                    String installDateString=mDay + "/" + mMonth + "/" + mYear;
                    try {

                        date2 = simpleDateFormat.parse(installDateString);
                        currentDate = simpleDateFormat.parse(currentDateTimeString);

                        total_days=printDifference(currentDate, date2);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Model_DataBase model = new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5)
                            , cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                            , cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16), total_days);
                    arrayList.add(model);
                } while (cursor.moveToNext());
            }
            if(state==0)
            {
                TextView tt=(TextView) view.findViewById(R.id.textview_nothing);
                tt.setText("Nothing");
            }
            Collections.sort(arrayList , new Sort());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        return view;
    }

    public long printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
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
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
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
       // return super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
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
               String q=query;
               Intent i=SearchResultActivity.newIntent1(getContext(),query,Id);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Sort implements Comparator<Model_DataBase>
    {

        @Override
        public int compare(Model_DataBase dataBaseModel, Model_DataBase t1) {
            return (int)dataBaseModel.getTotal_days()-(int)t1.getTotal_days();
            //return Month.valueOf(o1.getMonth().toUpperCase()).compare(Month.valueOf(o2.getMonth().toUpperCase()));
        }
    }
}