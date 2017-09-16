package com.reminder.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.reminder.R;

/**
 * Created by abc on 1/7/2017.
 */
public class Utils {

    public static void setCheckValue(Context context, int chk) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.check_value),chk).apply();
    }
    public static int getCheckValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.check_value), -1);
    }
    public static void setUpdateValue(Context context, int chk) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.check_value),chk).apply();
    }
    public static int getUpdateValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.check_value), -1);
    }
    public static void setExtraUpdate(Context context, int chk) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.check_value),chk).apply();
    }
    public static int getExtraUpdate(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.check_value),-1);
    }
}
