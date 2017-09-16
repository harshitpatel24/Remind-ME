package com.reminder.menu;

/**
 * Created by admin on 02-02-2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.reminder.R;
import com.reminder.utils.Session;

import java.util.Calendar;
import java.util.Date;


public class NotificationBar extends BroadcastReceiver {

    static int id=0,main_id=0;
    static String Type;
    SQLClass db;
    Session sess;
    public static void setId(int tmp,int idd,String type)
    {
        id=tmp;
        main_id=idd;
        Type=type;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Cursor cc;
        Date now = new Date();
        long uniqueId = now.getTime();
        int chk=0;
        boolean xy=false;
        sess=new Session(context);
        db=new SQLClass(context);
        if(Type.equals("P"))
        {
            cc=db.viewSingleData(String.valueOf(main_id),String.valueOf(id));
            cc.moveToFirst();

            Cursor cx=db.viewPendingDate(String.valueOf(main_id));
            if(cx.getCount()>0)
            {
                int len=cx.getCount();
                cx.moveToFirst();
                while(len!=0)
                {
                    if(cc.getInt(1)==cx.getInt(1))
                    {
                        chk=1;
                        db.deleteSnoozeDate(String.valueOf(main_id),String.valueOf(cc.getInt(1)),"P");
                        break;
                    }
                    cx.moveToNext();
                    len--;
                }
            }
            if(chk==0)
            {
                String md=cc.getString(13);
                String sepe[]=md.split(" ");
                int mdd=Integer.valueOf(sepe[0]);
                Cursor cz=db.ViewSingleDate(String.valueOf(main_id),String.valueOf(cc.getInt(1)));
                cz.moveToFirst();
                db.deleteDate(String.valueOf(main_id),String.valueOf(cz.getInt(1)));
                db.insertPendingDate(main_id,cc.getInt(1),cz.getInt(2),cz.getInt(3),cz.getInt(4),cz.getInt(5),cz.getInt(6));
                if(sess.snooze())
                {
                    Calendar cal=Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH,cz.getInt(2));
                    cal.set(Calendar.MONTH,cz.getInt(3)-1);
                    cal.set(Calendar.YEAR,cz.getInt(4));
                    cal.set(Calendar.HOUR_OF_DAY,cz.getInt(5));
                    cal.set(Calendar.MINUTE,cz.getInt(6));
                    cal.add(Calendar.DAY_OF_MONTH,-mdd);
                    cal.add(Calendar.HOUR_OF_DAY,1);
                    int day=cal.get(Calendar.DAY_OF_MONTH);
                    int month=cal.get(Calendar.MONTH)+1;
                    int year=cal.get(Calendar.YEAR);
                    int hour=cal.get(Calendar.HOUR_OF_DAY);
                    int minute=cal.get(Calendar.MINUTE);
                    xy=db.updateSnoozeDate(String.valueOf(main_id),String.valueOf(cz.getInt(1)),day,month,year,hour,minute,"P");
                }
                else
                {
                    db.deleteSnoozeDate(String.valueOf(main_id),String.valueOf(cc.getInt(1)),"P");
                }
            }
        }
        else
        {
            cc=db.viewSingleQuickReminer(String.valueOf(main_id),String.valueOf(id));
            cc.moveToFirst();
            db.deleteSnoozeDate(String.valueOf(main_id),String.valueOf(id),"T");
        }


        Intent intent1=new Intent(context,NotificationBar.class);
        PendingIntent pintent=PendingIntent.getActivity(context,0, intent1,PendingIntent.FLAG_ONE_SHOT);//update_current
        /*Notification noti=new Notification.Builder(context)
                .setTicker("TickerTitle")
                .setContentTitle(cc.getString(1)+" "+cc.getString(2))
                .setContentText("Content hello vatsal soni")
                .setSmallIcon(R.drawable.calender)
                .setContentIntent(pintent).getNotification();
        noti.flags=Notification.FLAG_AUTO_CANCEL;
         NotificationManager nm=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(0,noti);
        */
        if(sess.Notify())
        {
            NotificationCompat.Builder noti = new NotificationCompat.Builder(context)
                    .setTicker("TickerTitle")
                    .setContentTitle(cc.getString(2))
                    .setContentText("Content hello vatsal soni")
                    .setSmallIcon(R.drawable.calender)
                    .setContentIntent(pintent);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            noti.setSound(alarmSound);
            NotificationManager nm=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify((int)uniqueId,noti.build());
        }

        /*String x=cc.getString(9);
        String seperated[]=x.split(" ");
        int im=Integer.valueOf(seperated[0]);
        Cursor cr=db.ViewSingleDate(String.valueOf(id));
        cr.moveToFirst();
        int day=cr.getInt(1);
        int month=cr.getInt(2);
        int year=cr.getInt(3);
        month=month+im;
        boolean xy=db.updateDate(String.valueOf(id),day,month,year);*/
        if(xy==true)
        {
            //Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
            cc.close();
            context.stopService(new Intent(context,MyAlarmService.class));
        }
    }
}
