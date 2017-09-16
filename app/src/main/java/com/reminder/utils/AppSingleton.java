package com.reminder.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/*SprentSingleton : This is App level class to use global array list to manage data and MultiDex support */
public class AppSingleton extends MultiDexApplication {

    /*Global object of UserProfileBean */
    private AppSingleton instance;
    private Utils objUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        /*UPDATE LOCAL LANGUAGE PREFRANCE*/
        objUtils = new Utils(this);
    }

    // here you can handle all unexpected crashes
    public void handleUncaughtException(Thread thread, Throwable e) {
        // not all Android versions will print the stack trace automatically
        e.printStackTrace();
        System.exit(1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(AppSingleton.this);
    }

    /*Create instance of SprentSingleton */
    public void initInstance() {
        if (instance == null)
            instance = new AppSingleton();
    }

    /*Get instance of SprentSingleton */
    public AppSingleton getInstance() {
        return instance;
    }

}
