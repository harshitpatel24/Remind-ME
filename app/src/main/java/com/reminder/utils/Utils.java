package com.reminder.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.reminder.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Utils : This is App level utility class for code reusability   */
public class Utils {

    private static final double EQUATOR_LENGTH = 6378140;
    private static final double EARTHRADIUS = 6366198;
    /*Use to show different kind of alert dialog */
    public static AlertDialog showOkAlert, gpsAlert, callConfirmationAlert, RTPAlert, alertUpdateAppStore;
    /*Show & hide progress dialog */
    public ProgressDialog progressDialog;
    /*Local SharedPreferences class to save primitive data */
    private SessionManager objSession;
    private Context mContext;
    /*Access your global array and variables allover in app */
    private AppSingleton objAppSingleton;

    /*Return Image File, Image Bitmap , Image Path while capture or choose from gallery */
    private File iImageFile = null;
    private Bitmap iImageBitmap = null;
    private String iImagePath = null;

    public Utils(Context context) {
        this.mContext = context;
        this.progressDialog = new ProgressDialog(mContext);
        this.objSession = new SessionManager(mContext);
        objAppSingleton = ((AppSingleton) mContext.getApplicationContext());
    }


    private static double diffMeterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    private static double diffMeterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
    }

    /*addJpgSignatureToGallery : save picture in /sprent/ dir */
    public static File addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            String iconsStoragePath = Environment.getExternalStorageDirectory() + "/Sprent/";
            File sdIconStorageDir = new File(iconsStoragePath);
            sdIconStorageDir.mkdirs();
            File photo = new File(sdIconStorageDir, String.format("Sprent_IMG_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
//            scanMediaFile(photo);
            result = true;
            return photo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*saveBitmapToJPG : save bitmap to JPG */
    public static void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    /*cropCenter: return cropCenter bitmap*/
    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }

    /*isNetworkAvailableNoDialog : return boolean flag to check network connection */
    public boolean isNetworkAvailableNoDialog() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        } else {
            return activeNetworkInfo != null;
        }
    }

    /*isNetworkAvailable : return boolean flag to check network connection & Show popup to enable network first */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            showOk(getNetworkIsuue(), "No Internet");
            return false;
        } else {
            return activeNetworkInfo != null;
        }
    }

    public String getNetworkIsuue() {
        return "Sorry, there is problem with internet connection. Please try again later.";
    }

    /* =================================================================== */

    /*isGPSEnable : return boolean flag to check GPS enable or not & show popup to enable GPS*/
    public boolean isGPSEnable() {
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            return false;
        } else {
            return true;
        }
    }

    /* =================================================================== */

    /*showGPSDisabledAlertToUser : show popup to enable GPS if not showing already */
    private void showGPSDisabledAlertToUser() {
        if (gpsAlert == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(((Activity) mContext));

            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (gpsAlert != null)
                        gpsAlert.dismiss();
                    gpsAlert = null;
                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ((Activity) mContext).startActivityForResult(callGPSSettingIntent, Constant.INTENT_SAR_GPS_ON_OFF);
                }
            });
            gpsAlert = alertDialogBuilder.create();
            if (gpsAlert != null && !gpsAlert.isShowing())
                gpsAlert.show();
        }
    }

    /*hideKeyboard : Hide keyboard if open */
    public void hideKeyboard() {
        // Check if no view has focus:
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (((Activity) mContext).getCurrentFocus().getWindowToken() != null)
            inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
    }

    	/* =================================================================== */

    /*userCurrentLat : return Location Latitude in String */
    public String userCurrentLat(Location location) {
        if (location != null) {
            return String.valueOf(location.getLatitude());
        } else {
            return String.valueOf("");
        }
    }

    /*userCurrentLat : return Location Longitude in String */
    public String userCurrentLong(Location location) {
        if (location != null) {
            return String.valueOf(location.getLongitude());
        } else {
            return String.valueOf("");
        }
    }

    /*dpToPx : return int value of pixel */
    public int dpToPx(double dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = (int) Math.round(dp * (displayMetrics.densityDpi * 100 / DisplayMetrics.DENSITY_DEFAULT) / 100);
        return px;
    }

    /*IsValidEmail : return boolean flag to validate email address*/
    public boolean IsValidEmail(String mEmail) {
        if (mEmail != null && mEmail.length() > 0) {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
            Matcher matcher = pattern.matcher(mEmail.toString().trim());
            if (matcher.matches()) {
                return true;
            } else {

                return false;
            }
        } else {

            return false;
        }
    }

    /**
     * isValidPassword : return String if password is valid or null if not
     */
    /* =================================================================== */
    public String isValidChangePassword(EditText edtNewPass, EditText edtRtypePassword, int minLength, int maxLength) {
        String mIsValidPassword = null;
        String mCurrentPass = edtNewPass.getText().toString().trim();
        String mRetpyePass = edtRtypePassword.getText().toString().trim();
        if (mCurrentPass.length() < minLength || mCurrentPass.length() > maxLength) {
            mCurrentPass = "";
            mRetpyePass = "";
            mIsValidPassword = null;
        }
        if (mRetpyePass.length() < minLength || mRetpyePass.length() > maxLength) {
            mCurrentPass = "";
            mRetpyePass = "";
            mIsValidPassword = null;
        } else {
            if (mCurrentPass.equalsIgnoreCase(mRetpyePass)) {
                mIsValidPassword = edtNewPass.getText().toString().trim();
            } else {
                mIsValidPassword = null;
            }
        }
        return mIsValidPassword;
    }

    /**
     * isValidPassword : return String if password is valid or null if not
     */
    /* =================================================================== */
    public String isValidPassword(EditText edtCurrentPassword, int minLength, int maxLength) {
        String mIsValidPassword = null;
        String mCurrentPass = edtCurrentPassword.getText().toString().trim();
        if (mCurrentPass.length() < minLength || mCurrentPass.length() > maxLength) {
            mCurrentPass = "";
            mIsValidPassword = null;
        } else {
            mIsValidPassword = edtCurrentPassword.getText().toString().trim();
        }
        return mIsValidPassword;
    }

    /**
     * isValidNumber : return long if Number 0-9 or 0 if not
     * Saudi
     */
    /* =================================================================== */
    public String[] isValidNumber(EditText edtNumber) {
        String mIsValidNumber[] = new String[2];
        if (edtNumber.getText().toString().trim().length() != 10) {
            mIsValidNumber[0] = null;
            mIsValidNumber[1] = "invalide_phone_number";
        } else if (edtNumber.getText().toString().trim().length() == 10 && edtNumber.getText().toString().charAt(0) != '0') {
            mIsValidNumber[0] = null;
            mIsValidNumber[1] = "start_number_with_zero";
        } else if (edtNumber.getText().toString().trim().length() == 10) {
            mIsValidNumber[0] = edtNumber.getText().toString().trim();
            mIsValidNumber[1] = null;
        }
        return mIsValidNumber;
    }

    /**
     * isValidText : return String if text is valid or null if not
     */
    /* =================================================================== */
    public String isValidText(EditText edtText) throws NumberFormatException {
        String mIsValidText = null;
        if (edtText.getText().toString().length() <= 0) {
            mIsValidText = null;
        } /*else if (!edtText.getText().toString().matches("[a-zA-Z ]+")) {
            mIsValidText = null;
        }*/ else {
            mIsValidText = edtText.getText().toString().trim();
        }
        return mIsValidText;
    }

    /*showOk : Show informative popup dialog */
    public void showOk(String msg, String title) {
        if (showOkAlert == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String dialogTitle = mContext.getString(R.string.app_name);
            if (isNotNullNEmpty(title) != null) {
                dialogTitle = title;
            }
            builder.setTitle(dialogTitle).setMessage(Html.fromHtml(msg)).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (showOkAlert != null)
                        showOkAlert.dismiss();
                    showOkAlert = null;
                }
            });
            setLog(msg);
            showOkAlert = builder.create();
            if (showOkAlert != null && !showOkAlert.isShowing())
                showOkAlert.show();
        }
    }

    /*Add 90 Min in existing Time , For order something while select schedule order */
    public String getOrderSomethingTimeStamp(String mFormat) {
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime() + 90 * 60 * 1000);
        /*SimpleDateFormat fmt = new SimpleDateFormat(mFormat);
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmt.format(currentDate);*/
        return new SimpleDateFormat(mFormat).format(currentDate);
    }

    /*getDateFromUTCTimestamp : return date in given date format & Convert UTC time to Local time */
    public String getDateFromUTCTimestamp(long mTimestamp, String mDateFormate) {
        String date = null;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(mTimestamp * 1000L);
            date = DateFormat.format(mDateFormate, cal.getTimeInMillis()).toString();

            SimpleDateFormat formatter = new SimpleDateFormat(mDateFormate);
            Date value = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            date = dateFormatter.format(value);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getCurrentTimeStampInFormat(String mformat) {
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime());
        return new SimpleDateFormat(mformat).format(currentDate);
    }

    /*getTimeStampBaseOnDate : return timestamp base on date */
    public String getTimeStampBaseOnDate(String mDate, String mFormate) {
        SimpleDateFormat fmt = new SimpleDateFormat(mFormate);
        Date currentDate = new Date();
        try {
            currentDate = fmt.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentDate != null)
            return String.valueOf(currentDate.getTime() / 1000);
        else
            return null;
    }
/* =================================================================== */

    /*getUniqueDeviceID : return sting type of Unique device Id*/
    public String getUniqueDeviceID() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /* =================================================================== */

    /*setLog : to print log Note : comment log before upload to store */
    public void setLog(String mLog) {
        Log.v(Constant.LOG_TAG, "<< AppLoger >> " + mLog);
    }

    /* =================================================================== */

    /*hideProgressDialog : Hide progress dialog  */
    public void hideProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    /* =================================================================== */

    /*getSessionManagerInstance : set session object  */
    public SessionManager getSessionManagerInstance() {
        return objSession;
    }

    /*getSessionManagerInstance : get session object  */
    public AppSingleton getSingleTonInstance() {
        return objAppSingleton;
    }

    /**
     * Method to verify google play services on the device
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, ((Activity) mContext), Constant.INTENT_SAR_PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mContext, "This device is not supported.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    /**
     * getScreenHeight : return height in px
     */
    /* =================================================================== */
    public int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    /**
     * getScreenHeight : return width in px
     */
    /* =================================================================== */
    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    /*getDistanceInMeter : return distance in float*/
    public float getDistanceInMeter(double startLat, double startLong, double endLat, double endLong) {
        Location loc1 = new Location("");
        loc1.setLatitude(startLat);
        loc1.setLongitude(startLong);

        Location loc2 = new Location("");
        loc2.setLatitude(endLat);
        loc2.setLongitude(endLong);

        float distanceInMeters = loc1.distanceTo(loc2);

        return distanceInMeters;
    }

    /**
     * shareTheApp : launch default intent to share app
     */
    /* =================================================================== */
    public void shareTheApp(String subject, String content) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share it Using"));
    }

    /* isValidDateSelection to validate Pr  evious date should not selecte*/
    public boolean isValidDateSelection(int year, int month, int day) {
        Calendar objCal = Calendar.getInstance();
        int mYear = objCal.get(Calendar.YEAR);
        int mMonth = objCal.get(Calendar.MONTH);
        int mDay = objCal.get(Calendar.DAY_OF_MONTH);
        if (mYear >= year && mMonth >= month && mDay >= day) {
            return true;
        } else {
            return false;
        }
    }

    /*getMonthName : return month name in string */
    public String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }


    /*getStorageDirectory : return storage path of app */
    public String getStorageDirectory(String path) {
        String rootDir = "";
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            rootDir = mContext.getFilesDir().getAbsolutePath();
        }
        return rootDir + path;
    }

    /*GetCurrentTimeStamp : return current time stamp*/
    public String GetCurrentTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentTimeStamp = dateFormat.format(new Date()); // Find todays
        return currentTimeStamp;
    }

    /*getBitmapFromFile : return bitmap converted from File*/
    public Bitmap getBitmapFromFile(File mFile) {
        Bitmap mBMPonActivityResult = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            mBMPonActivityResult = BitmapFactory.decodeStream(new FileInputStream(mFile), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return mBMPonActivityResult;
    }

    public Bitmap getBitmapFromDrawble(int res) {
        return BitmapFactory.decodeResource(mContext.getResources(), res);
    }

    /*getBitmapFromFile : return resized bitmap */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void resizeImageScale(int Height) {
        setiImageBitmap(getResizedBitmap(getiImageBitmap(), Height));
        setiImageFile(Utils.addJpgSignatureToGallery(getiImageBitmap()));
    }

    /*callToUser : will call to user and will show pop up before call */
    public void callToUser(final String mNumber) {
        if (callConfirmationAlert == null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(((Activity) mContext));
            alertDialogBuilder.setTitle("Call");

            alertDialogBuilder.setMessage(mNumber).setCancelable(true).setPositiveButton("Call", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (callConfirmationAlert != null)
                        callConfirmationAlert.dismiss();
                    callConfirmationAlert = null;
                    if (mNumber != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mNumber));
                        ((Activity) mContext).startActivity(intent);
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (callConfirmationAlert != null)
                        callConfirmationAlert.dismiss();
                    callConfirmationAlert = null;

                }
            });
            callConfirmationAlert = alertDialogBuilder.create();
            if (callConfirmationAlert != null && !callConfirmationAlert.isShowing())
                callConfirmationAlert.show();
        }
    }

    /*Image Selection intent methods*/
    public File getiImageFile() {
        return iImageFile;
    }

    public void setiImageFile(File iImageFile) {
        this.iImageFile = iImageFile;
    }

    public Bitmap getiImageBitmap() {
        return iImageBitmap;
    }

    public void setiImageBitmap(Bitmap iImageBitmap) {
        this.iImageBitmap = iImageBitmap;
    }

    public String getiImagePath() {
        return iImagePath;
    }

    public void setiImagePath(String iImagePath) {
        this.iImagePath = iImagePath;
    }

    /* getMarkerBitmapFromView : return bitmap of markre , This function is using but commented as per clients requirement */
    public Bitmap getMarkerBitmapFromView(ImageView mMarkerImageView, View view, int res) {
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), res);
        mMarkerImageView.setImageBitmap(largeIcon);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(mContext, id);
        int h = ((int) dpToPx(28));
        int w = ((int) dpToPx(28));
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    /* getMarkerBitmapFromView : return bitmap of markre , This function is using but commented as per clients requirement */
    public Bitmap getMarkerBitmapFromView(ImageView mMarkerImageView, View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }
    /*========================================================================================================== */

    /* getMarkerBitmapFromView : return bitmap of markre , This function is using but commented as per clients requirement */
    public Bitmap getMarkerBitmapFromView(ImageView mMarkerImageView, View view, RoundedBitmapDrawable roundedBitmapDrawable) {
        mMarkerImageView.setImageDrawable(roundedBitmapDrawable);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    /* getCurrentTime :return current local time  */
    public String getCurrentTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

    }

    /* isNotNullNEmpty :return String if argument string is not NULL & Not EMPTY & Length > 0 */
    public String isNotNullNEmpty(String mString) {
        return mString != null && mString.length() > 0 ? mString : null;
    }

    /* isValidDouble :return Double if argument string is not NULL & Not EMPTY & Length > 0 */
    public Double isValidDouble(String mString) {
        return isNotNullNEmpty(mString) != null ? Double.valueOf(mString) : 0;
    }

    /*getRadiusInMeters : return radius base on meter */
    public float getRadiusInMeters(float km) {
        return (float) (km * 1000);
    }

    /*getZoomLevelByCircle : return Zoom level of map base on radius */
    public float getZoomLevelByCircle(GoogleMap googleMap, LatLng latLng, float totlaRadius) {
        setLog("getZoomLevelByCircle RADIUS TO METER " + getRadiusInMeters(totlaRadius));
        Circle circle = googleMap.addCircle(new CircleOptions().center(latLng).radius(getRadiusInMeters(totlaRadius)).strokeColor(Color.TRANSPARENT));
        circle.setVisible(false);
        circle.setCenter(latLng);
        int zoomLevel = 12; // Default
        if (circle != null) {
            double circleRadius = circle.getRadius();
            double scale = circleRadius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return (float) (zoomLevel - 0.5);
    }

    public int calculateZoomLevelByScreenNRadius(float radius) {
        double equatorLength = 40075004; // in meters
        double widthInPixels = getScreenWidth();
        double metersPerPixel = equatorLength / 256;
        int zoomLevel = 1;
        while ((metersPerPixel * widthInPixels) > 2000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        return zoomLevel;
    }


    public double getZoomForMetersWide(double desiredMeters, double latitude) {
        final double latitudinalAdjustment = Math.cos(Math.PI * latitude / 180);
        final double arg = EQUATOR_LENGTH * getScreenWidth() * latitudinalAdjustment / (desiredMeters * 256);
        return Math.log(arg) / Math.log(2);
    }

    public String getConvertedTimeToUTC(String ourDate, String mDateFormat) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(mDateFormat);
            fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = fmt.parse(ourDate);
            if (value != null)
                return String.valueOf(value.getTime() / 1000);
            else
                return null;
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }


    /*getVersionCode : get application versino code to aompare with server version code and notify if user have older app */
    public int getVersionCode() {
        int verCode = 0;
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }
}