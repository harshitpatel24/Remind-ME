package com.reminder.menu;

/**
 * Created by admin on 02-02-2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MyAlarmService extends Service
{
    static Calendar cal=Calendar.getInstance();
    Model_Date dm;
    SQLClass sq;
    Date date;
    ArrayList<Model_Date> arr;
    static int main_id;

    public static void setId(int id)
    {
        main_id=id;
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        sq=new SQLClass(getApplicationContext());
        super.onStart(intent, startId);
        arr=new ArrayList<>();
        arr.clear();
        Cursor cr=sq.ViewSnoozeDate(String.valueOf(main_id));
        if(cr.getCount()>0)
        {
            cr.moveToFirst();
            do {
                int id=cr.getInt(1);
                int day=cr.getInt(2);
                int month=cr.getInt(3);
                int year=cr.getInt(4);
                int hour=cr.getInt(5);
                int minute=cr.getInt(6);
                String type=cr.getString(7);

                cal.set(Calendar.DAY_OF_MONTH,day);
                cal.set(Calendar.MONTH,month-1);
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.HOUR_OF_DAY,hour);
                cal.set(Calendar.MINUTE,minute);
                cal.set(Calendar.SECOND,0);
               // cal.set(Calendar.AM_PM,Calendar.PM);
                date=cal.getTime();
                dm=new Model_Date(id,date,type);
                arr.add(dm);
            }while(cr.moveToNext());
            Collections.sort(arr,new DateSort());
            dm=arr.get(0);
            date=dm.getDate();
            cal.setTime(date);
            Intent i=new Intent(this, NotificationBar.class);
            NotificationBar.setId(dm.getId(),main_id,dm.getType());
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }
    }



    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        Intent myIntent = new Intent(MyAlarmService.this, MyAlarmService.class);
        startService(myIntent);
        //Toast.makeText(this,"Started",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
    public class DateSort implements Comparator<Model_Date>
    {

        @Override
        public int compare(Model_Date dateTime, Model_Date t1) {
            return dateTime.getDate().compareTo(t1.getDate());
        }
    }
}
