package com.reminder.menu;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.reminder.MainActivity;
import com.reminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchResultActivity1 extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    Toolbar toolbar;
    ArrayList<Model_DataBase> arrayList=new ArrayList<>();
    SQLClass DB;
    static int main_id;
    static String Query;
    Model_DataBase dx;
    int count=0;
    Model_Date md;
    Date date;
    android.support.v7.app.AlertDialog.Builder a_builder;
    ArrayList<Model_Date> arr=new ArrayList<>();

    public static void setId(int main_id1){
        main_id=main_id1;
    }

    public static Intent newIntent1(Context context, String query,int id)
    {
        Intent i=new Intent(context,SearchResultActivity1.class);
        Query=query;
        main_id=id;
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview_result_activity);
        //Toast.makeText()
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int iddx=0;

        toolbar = (Toolbar) findViewById(R.id.toolbar_search_result);
        setSupportActionBar(toolbar);
        String s=Query;
        Date currentDate=new Date();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        long total_days=0;
        String currentDateTimeString=mDay + "/" + mMonth + "/" + mYear;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        try
        {
            currentDate = simpleDateFormat.parse(currentDateTimeString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        DB=new SQLClass(this);
        Cursor cursor1;
        cursor1=DB.ViewDate(String.valueOf(main_id));
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
        Cursor cursor=DB.viewAllData(String.valueOf(main_id));
        if(cursor.getCount()!=0)
        {
            Calendar cal=Calendar.getInstance();
            cursor.moveToFirst();
            do {
                String name=cursor.getString(2).toString().toLowerCase();
                String no=cursor.getString(6).toString().toLowerCase();
                String plan=cursor.getString(5).toString().toLowerCase();
                String holder=cursor.getString(3).toString().toLowerCase();
                String main_holder=cursor.getString(4).toString().toLowerCase();
                String final_query=Query.toLowerCase();
                Date installDate=new Date();
                if(name.contains(final_query) ||  no.contains(final_query) || plan.contains(final_query) || holder.contains(final_query) || main_holder.contains(final_query)) {

                    Cursor czz=DB.ViewCompletedPlan(String.valueOf(main_id));
                    if(czz.getCount()>0)
                    {
                        czz.moveToFirst();
                        int len=czz.getCount();
                        while(len>0)
                        {
                            if(cursor.getInt(1)==czz.getInt(1))
                            {
                                total_days=-9999;
                                iddx=czz.getInt(1);
                                dx = new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                                        , cursor.getString(5),cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                                        , cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16), total_days);
                                arrayList.add(dx);
                                break;
                            }
                            czz.moveToNext();
                            len--;
                        }
                    }
                    if(iddx!=cursor.getInt(1))
                    {
                        int len=arr.size();
                        while(len>0)
                        {
                            md=arr.get(len-1);
                            if(cursor.getInt(1)==md.getId())
                            {
                                installDate=md.getDate();
                                break;
                            }
                            len--;
                        }
                        cal.setTime(installDate);
                        Date date2;
                        mDay=cal.get(Calendar.DAY_OF_MONTH);
                        mMonth=cal.get(Calendar.MONTH)+1;
                        mYear=cal.get(Calendar.YEAR);
                        String installDateString=mDay + "/" + mMonth + "/" + mYear;
                        try {

                            date2 = simpleDateFormat.parse(installDateString);

                            total_days=printDifference(currentDate, date2);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dx = new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                                , cursor.getString(5),cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                                , cursor.getString(12), cursor.getInt(13), cursor.getInt(14), cursor.getString(15), cursor.getString(16), total_days);
                        arrayList.add(dx);
                    }
                    count++;
                    }
                    else
                    {

                    }

            }while (cursor.moveToNext());
            cursor.close();

        }
        if(count==0){
            a_builder=new AlertDialog.Builder(SearchResultActivity1.this);
            a_builder.setMessage("No Record Found")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=MainActivity.newIntentxid(SearchResultActivity1.this,2,main_id);
                            startActivity(i);
                        }
                    });
            android.support.v7.app.AlertDialog alert=a_builder.create();
            alert.setTitle("Alert");
            alert.show();
        }
        mAdapter=new Adapter_Upcoming_Pending(arrayList,5);
        mRecyclerView.setAdapter(mAdapter);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) SearchResultActivity1.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchResultActivity1.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i= SearchResultActivity1.newIntent1(SearchResultActivity1.this,query,main_id);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=MainActivity.newIntentxid(SearchResultActivity1.this,2,main_id);
        startActivity(i);
    }
}
