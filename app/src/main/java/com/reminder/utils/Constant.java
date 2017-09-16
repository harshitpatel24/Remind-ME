package com.reminder.utils;

import android.Manifest;

import java.io.File;

public class Constant {

    /*==================== ARRAY :: RUNTIME PERMISSION ====================*/
    public static final String[] RTPS = {Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /*==================== INTENT START ACTIVITY RESULT REQUEST CODE ====================*/
    public static final int INTENT_SAR_CAMERA_REQUEST = 111;
    public static final int INTENT_SAR_GALLERY_REQUEST = 222;
    public static final int INTENT_SAR_GPS_ON_OFF = 333;
    public static final int INTENT_SAR_NETWORK_CONNECTION = 444;
    public static final int INTENT_SAR_PLAY_SERVICES_RESOLUTION_REQUEST = 555;

    public static final String REMIND_ME_PREF = "remind_me_pref";
    public static final String LOG_TAG = "remind_me_log";



}
