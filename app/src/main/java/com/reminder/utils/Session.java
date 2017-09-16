package com.reminder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.reminder.R;

/**
 * Created by Harshit on 2/9/2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin) {
        editor.putBoolean("loggedInmode", logggedin);
       editor.commit();
    }
    public void setSnooze(boolean snooze)
    {
        editor.putBoolean("SnoozeMode", snooze);
        editor.commit();
    }
    public boolean snooze() {
        return prefs.getBoolean("SnoozeMode", true);
    }
    public void setnotify(boolean noty)
    {
        editor.putBoolean("Notify", noty);
        editor.commit();
    }
    public boolean Notify() {
        return prefs.getBoolean("Notify", true);
    }

    public static void setUserId(Context context,int id)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt("USER_ID", id).apply();
    }


    public boolean loggedin() {
        return prefs.getBoolean("loggedInmode", false);
    }

    public static void setCheckValue(Context context, int chk , String email , String prolink) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.check_value), chk).apply();
        prefs.edit().putString("check value1", email).apply();
        prefs.edit().putString("check value2" , prolink).apply();
    }

    public static int getUserId(Context context)
    {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("USER_ID",-1);
    }


    public static int getCheckValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.check_value), -1);
    }

    public static String getEmail(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getString("check value1", "#");
    }
    public static String getProLink(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getString("check value2", "#");
    }

}
