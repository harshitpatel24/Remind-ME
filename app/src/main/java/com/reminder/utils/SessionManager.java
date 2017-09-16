package com.reminder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SessionManager : store all primitives local data
 */
/*SessionManager : This is shared preference class to store local primitive data */
public class SessionManager {

    // Shared Preferences
    private SharedPreferences objPreferences;
    // Editor for Shared preferences
    private Editor objEditor;
    // Context
    private Context objContext;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this.objContext = context;
        objPreferences = objContext.getSharedPreferences(Constant.REMIND_ME_PREF, PRIVATE_MODE);
        objEditor = objPreferences.edit();

    }

    public void setBooleanDetail(String key, Boolean value) {
        objEditor.putBoolean(key, value);
        objEditor.commit();
    }

    public boolean getBooleanDetail(String key) {
        boolean status = objPreferences.getBoolean(key, false);
        return status;
    }

    public void setStringDetail(String key, String value) {
        objEditor.putString(key, value);
        objEditor.commit();
    }

    public String getStringDetail(String key) {
        String status = objPreferences.getString(key, "");
        return status;
    }

    public void setIntDetail(String key, int value) {
        objEditor.putInt(key, value);
        objEditor.commit();
    }

    public int getIntDetail(String key) {
        int status = objPreferences.getInt(key, 0);
        return status;
    }

    public void clearAllSP() {
        // Clearing all data from Shared Preferences
        objEditor.clear();
        objEditor.commit();
    }
}
