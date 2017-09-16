package com.reminder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.reminder.menu.Activity_AddPlan;
import com.reminder.menu.Activity_AddPlan2;
import com.reminder.menu.Activity_AddQuickReminder;
import com.reminder.menu.Activity_PlanDetails;
import com.reminder.menu.Adapter_Holder;
import com.reminder.menu.Fragment_CompletedPlan;
import com.reminder.menu.Fragment_Upcoming;
import com.reminder.menu.MyAlarmService;
import com.reminder.menu.Individual_Statistics;
import com.reminder.menu.SQLClass;
import com.reminder.menu.TotalStatistics;
import com.reminder.menu.Adapter_Completed;
import com.reminder.menu.Adapter_History;
import com.reminder.menu.Fragment_Pending;
import com.reminder.menu.Adapter_QuickReminder;
import com.reminder.menu.Fragment_History;
import com.reminder.menu.Adapter_Upcoming_Pending;
import com.reminder.utils.Session;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.reminder.menu.Fragment_AddHolder;
import com.reminder.menu.Fragment_InvestmentPlan;
import com.reminder.menu.Fragment_ContactUs;
import com.reminder.menu.Fragment_FAQ;
import com.reminder.menu.Fragment_Feedback;
import com.reminder.menu.Fragment_Help;
import com.reminder.menu.Fragment_Investment;
import com.reminder.menu.QuickFragment;
import com.reminder.menu.Fragment_Settings;
import com.reminder.menu.Fragment_TodayEvent;
import com.reminder.utils.Utils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener{

    /* =====  View controls ===== */
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    static String Mode="";
    static int Id;
    static int Index=2,Index1=0;
    static String User_Name,Profile;
    static android.support.v7.app.AlertDialog.Builder a_builder;

    /* ===== Fragments =====  */

    /*Add investment plan reminder*/
    private Fragment_Investment frgInvestmentFragment;

    /*Add quick reminders */
    private QuickFragment frgQuickFragment;

    /* Add Holder or Nominee details  */
    private Fragment_AddHolder frgAddHolderFragment;

    /* Add Investment Plan */
    private Fragment_InvestmentPlan frgAddInvestmentPlanFragment;

    /* Display Today Event */
    private Fragment_TodayEvent frgtodayevent;

    /*Edit application settings  */
    private Fragment_Settings frgSettingsFragment;

    /* Feedback */
    private Fragment_Feedback frgFeedbackFragment;

    /* contact us */
    private Fragment_ContactUs frgContactUsFragment;

    /* FAQ */
    private Fragment_FAQ frgFaqFragment;

    /* Help */
    private Fragment_Help frgHelpFragment;

    /*Custom classes*/
    private Utils objUtils;

    /*
    TotalStatistics
     */
    private TotalStatistics frgGraph;

    private Fragment_CompletedPlan frgCompleted;

    private static Fragment_Upcoming frgUpcoming;
    private static Fragment_Pending frgPending;
    private static Fragment_History frgHistory;

    /*  =====  Variables =====*/

    /*To identify the back press event duration */
    private long mBackPressed;

    /*set the exit limit while back press */
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.

    Session session;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    public static Intent newIntentx(Context context,int index)
    {
        Intent i=new Intent(context,MainActivity.class);
       Index=index;
        return i;
    }
    public static Intent newIntentx1(Context context,int index,int index1)
    {
        Intent i=new Intent(context,MainActivity.class);
        Index=index;
        Index1=index1;
        return i;
    }

    public static Intent newIntent(Context context,int id)
    {
        Intent i=new Intent(context,MainActivity.class);
        Index=2;
        User_Name="";
        Profile="";
        Id=id;
        return i;
    }
    public static Intent newIntentxid(Context context,int index,int id)
    {
        Intent i=new Intent(context,MainActivity.class);
        Index=index;
        Id=id;
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment_CompletedPlan.setId(Id);
        Adapter_Completed.setId(Id);
        Adapter_QuickReminder.setId(Id);
        //show_quick_reminders.setId(Id);
        Activity_PlanDetails.setId(Id);
        Adapter_Holder.setId(Id);
        Adapter_History.setId(Id);
            frgQuickFragment = new QuickFragment();
        frgQuickFragment.setId(Id);
        Activity_AddQuickReminder frgAddQuick=new Activity_AddQuickReminder();
        frgAddQuick.setId(Id);
        Individual_Statistics.setId(Id);
            MyAlarmService.setId(Id);
            SQLClass.setId(Id);
            frgUpcoming=new Fragment_Upcoming();
            frgUpcoming.setId(Id);
            frgHistory=new Fragment_History();
            frgHistory.setId(Id);
            frgPending=new Fragment_Pending();
            frgPending.setId(Id);
            frgAddHolderFragment=new Fragment_AddHolder();
            frgAddHolderFragment.setId(Id);
            frgAddInvestmentPlanFragment =new Fragment_InvestmentPlan();
            frgAddInvestmentPlanFragment.setId(Id);
            frgGraph = new TotalStatistics();
            frgGraph.setId(Id);
            Activity_AddPlan2 plan2=new Activity_AddPlan2();
            plan2.setId(Id);
            Activity_AddPlan plan=new Activity_AddPlan();
            plan.setId(Id);
            Adapter_Upcoming_Pending uap=new Adapter_Upcoming_Pending();
            uap.setId(Id);
            frgtodayevent = new Fragment_TodayEvent();
            frgtodayevent.setId(Id);
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            objUtils = new Utils(this);

            session = new Session(this);
            if(!session.loggedin()){
                logout();
            }

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            /*fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFragmentExist(getString(R.string.frg_quick_reminders)))
                        Snackbar.make(view, "Add quick reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    else if (isFragmentExist(getString(R.string.frg_investment_reminder)))
                    {
                        fab.setVisibility(View.GONE);
                        // Snackbar.make(view, "Add investment reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        startActivity(new Intent(MainActivity.this,Activity_AddPlan.class));
                    }
                }
            });*/

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            navigationView=(NavigationView) findViewById(R.id.nav_view);
            View hView = navigationView.getHeaderView(0);
            TextView nav_useremail=(TextView) hView.findViewById(R.id.userEmail);
            SQLClass db=new SQLClass(getApplicationContext());
        CircleImageView profilePhoto = (CircleImageView)hView.findViewById(R.id.userProfileImage);
            Cursor cc=db.ViewSingleUser(String.valueOf(Id));
        if(cc.getCount()>0)
        {
            cc.moveToFirst();
            if((cc.getString(5).equals("FB")))
            {
                if(cc.getString(8).equals(""))
                {
                    Picasso.with(MainActivity.this).load(R.mipmap.ic_launcher).into(profilePhoto);
                    nav_useremail.setText(cc.getString(4));
                }
                else
                {
                    Picasso.with(MainActivity.this).load(cc.getString(8)).into(profilePhoto);
                    nav_useremail.setText(cc.getString(4));
                }
            }
            else if((cc.getString(5).equals("GOOGLE")))
            {
                if(cc.getString(7).equals(""))
                {
                    Picasso.with(MainActivity.this).load(R.mipmap.ic_launcher).into(profilePhoto);
                    nav_useremail.setText(cc.getString(1));
                }
                else
                {
                    Picasso.with(MainActivity.this).load(cc.getString(7)).into(profilePhoto);
                    nav_useremail.setText(cc.getString(1));
                }
            }
            else if((cc.getString(5).equals("OUR")))
            {
                byte[] bb=cc.getBlob(6);
                if(bb==null)
                {
                    //  CircleImageView profilePhoto = (CircleImageView)hView.findViewById(R.id.userProfileImage);
                    Picasso.with(MainActivity.this).load(R.mipmap.ic_launcher).into(profilePhoto);
                    nav_useremail.setText(cc.getString(1));
                }
                else
                {
                    Bitmap bitmap;
                    BitmapFactory.Options opt=new BitmapFactory.Options();
                    bitmap=BitmapFactory.decodeByteArray(bb,0,bb.length,opt);
                    //CircleImageView profilePhoto = (CircleImageView)hView.findViewById(R.id.userProfileImage);
                    profilePhoto.setImageBitmap(bitmap);
                    nav_useremail.setText(cc.getString(1));
                }
            }

        }
        SQLClass db1=new SQLClass(getApplicationContext());
        final Cursor cc1=db1.ViewSingleUser(String.valueOf(Id));
        cc1.moveToFirst();

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state=cc1.getString(5);
                if(cc1.getString(5).equals("OUR"))
                {
                    Intent i=ProfilePicActivity.newIntent2(MainActivity.this,Id);
                    startActivity(i);
                }
                else if(cc1.getString(5).equals("GOOGLE") || cc1.getString(5).equals("FB"))
                {
                    //Log.d("hello","hello");
                }
            }
        });
        if(Index==2)
        {
            onClickInvestmentReminders();
        }
        else if(Index==1)
        {
            onClickQuickReminders();
        }
        else if(Index==4)
        {
            onClickCompletedPlan();
        }
        else if(Index==3)
        {
            onClickTodayEvent();
        }

    }



    private void onClickQuickReminders() {
        Index=1;
//        fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_quick_reminders))) {
            frgQuickFragment = new QuickFragment();
            replaceFragmentBy(frgQuickFragment, getString(R.string.frg_quick_reminders));
        }
    }

    private void onClickInvestmentReminders() {
        Index=2;
        //fab.setVisibility(View.VISIBLE);
        if (!isFragmentExist(getString(R.string.frg_investment_reminder))) {
            frgInvestmentFragment = new Fragment_Investment();
            Bundle bd=new Bundle();
            bd.putInt("Pos",Index1);
            frgInvestmentFragment.setArguments(bd);
            replaceFragmentBy(frgInvestmentFragment, getString(R.string.frg_investment_reminder));
        }
    }

    private void onClickTodayEvent() {
        Index=3;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_today_event))) {
            frgtodayevent = new Fragment_TodayEvent();
            replaceFragmentBy(frgtodayevent, getString(R.string.frg_today_event));
        }
    }

    private void onClickGraph() {
        Index=4;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_statistics))) {
            frgGraph = new TotalStatistics();
            replaceFragmentBy(frgGraph, getString(R.string.frg_statistics));
        }
    }

    private void onClickCompletedPlan() {
        Index=5;
       // fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_CompletedPlan))) {
            frgCompleted = new Fragment_CompletedPlan();
            replaceFragmentBy(frgCompleted, getString(R.string.frg_CompletedPlan));
        }
    }

    private void onClickAddHolder() {
        Index=6;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_add_holder))) {
            frgAddHolderFragment = new Fragment_AddHolder();
            replaceFragmentBy(frgAddHolderFragment, getString(R.string.frg_add_holder));
        }
    }

    private void onClickAddInvestmentPlan() {
        Index=7;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_add_investment))) {
            frgAddInvestmentPlanFragment = new Fragment_InvestmentPlan();
            replaceFragmentBy(frgAddInvestmentPlanFragment, getString(R.string.frg_add_investment));
        }
    }

    private void onClickSettings() {
        Index=8;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_settings))) {
            frgSettingsFragment = new Fragment_Settings();
            replaceFragmentBy(frgSettingsFragment, getString(R.string.frg_settings));
        }
    }

    private void onClickFeedback() {
        Index=9;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_feedback))) {
            frgFeedbackFragment = new Fragment_Feedback();
            replaceFragmentBy(frgFeedbackFragment, getString(R.string.frg_feedback));
        }
    }

    private void onClickContactUs() {
        Index=10;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_contact_us))) {
            frgContactUsFragment = new Fragment_ContactUs();
            replaceFragmentBy(frgContactUsFragment, getString(R.string.frg_contact_us));
        }
    }

    private void onClickFAQ() {
        Index=11;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_faq))) {
            frgFaqFragment = new Fragment_FAQ();
            replaceFragmentBy(frgFaqFragment, getString(R.string.frg_faq));
        }
    }

    private void onClickHelp() {
        Index=12;
        //fab.setVisibility(View.GONE);
        if (!isFragmentExist(getString(R.string.frg_help))) {
            frgHelpFragment = new Fragment_Help();
            replaceFragmentBy(frgHelpFragment, getString(R.string.frg_help));
        }
    }

    private void onClickShareTheApp() {
        Index=13;
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        objUtils.shareTheApp("Remind Me - A solution to avoid obliterate your stuff", "Hey guys!!!\n\nTry this amazing app which always keep me remind for all my stuff download it from " + "http://play.google.com/store/apps/details?id=" + appPackageName);
    }

    private void onClickRateTheApp() {
        Index=14;
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        Uri uri = Uri.parse("market://details?id=" + appPackageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * isFragmentExist : return true if fragment exist
     */
    private boolean isFragmentExist(String frgIndex) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentById(R.id.containerView);

        if (frgIndex.equalsIgnoreCase(getString(R.string.frg_quick_reminders))) {
            if (f != null && f instanceof QuickFragment) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_investment_reminder))) {
            if (f != null && f instanceof Fragment_Investment) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_add_investment))) {
            if (f != null && f instanceof Fragment_InvestmentPlan) {
                return true;
            } else {
                return false;
            }
        }
        else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_add_holder))) {
            if (f != null && f instanceof Fragment_AddHolder) {
                return true;
            } else {
                return false;
            }
        }
        else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_today_event))) {
            if (f != null && f instanceof Fragment_TodayEvent) {
                return true;
            } else {
                return false;
            }
        }
        else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_statistics))) {
            if (f != null && f instanceof TotalStatistics) {
                return true;
            } else {
                return false;
            }
        }
        else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_CompletedPlan))) {
            if (f != null && f instanceof Fragment_CompletedPlan) {
                return true;
            } else {
                return false;
            }
        }
        else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_settings))) {
            if (f != null && f instanceof Fragment_Settings) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_feedback))) {
            if (f != null && f instanceof Fragment_Feedback) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_contact_us))) {
            if (f != null && f instanceof Fragment_ContactUs) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_faq))) {
            if (f != null && f instanceof Fragment_FAQ) {
                return true;
            } else {
                return false;
            }
        } else if (frgIndex.equalsIgnoreCase(getString(R.string.frg_help))) {
            if (f != null && f instanceof Fragment_Help) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * replaceFragmentBy : return new fragment if not exist
     */
    private void replaceFragmentBy(Fragment fragment, String mBackStakTag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, fragment, mBackStakTag);
        if (mBackStakTag != null) {
            //fragmentTransaction.addToBackStack(mBackStakTag);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        Index1=0;
    }

    /**
     * Remove all backstack entry before replace new fragment
     */
    private void removeAllBackEntries() {
        // Clear all back stack.
        FragmentManager manager = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount > 0) {
            for (int i = 0; i < backStackCount; i++) {
                // Get the back stack fragment id.
                int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
                manager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_quick_reminders) {
            onClickQuickReminders();
        } else if (id == R.id.nav_item_inv_reminders) {
            onClickInvestmentReminders();
        } else if (id == R.id.nav_item_type) {
            onClickAddInvestmentPlan();
        } else if (id == R.id.nav_item_holder) {
            onClickAddHolder();
        }
        else if(id==R.id.nav_item_today_event)
        {
            onClickTodayEvent();
        }
        else if(id==R.id.nav_item_inv_graph)
        {
            onClickGraph();
        }
        else if(id==R.id.nav_item_inv_completed_plan)
        {
            onClickCompletedPlan();
        }
        else if (id == R.id.nav_item_setting) {
            onClickSettings();
        } else if (id == R.id.nav_item_feedback) {
            onClickFeedback();
        } else if (id == R.id.nav_item_contact_us) {
            onClickContactUs();
        } else if (id == R.id.nav_item_faq) {
            onClickFAQ();
        } else if (id == R.id.nav_item_help) {
            onClickHelp();
        } else if (id == R.id.nav_item_share) {
            onClickShareTheApp();
        } else if (id == R.id.nav_item_rta) {
            onClickRateTheApp();
        }
        else if(id==R.id.nav_item_signout){
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        /*if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            if (!this.isFinishing()) {
                finish();
            }
            return;
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }*/
        if(Index==2) {
            a_builder=new AlertDialog.Builder(MainActivity.this);
            a_builder.setMessage("Are you sure you want to Exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a=new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            android.support.v7.app.AlertDialog alert=a_builder.create();
            alert.setTitle("Alert");
            alert.show();
            /*Intent a=new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);*/
            //Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Index=2;
            Fragment_Investment up=new Fragment_Investment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView, up);
            fragmentTransaction.commit();
        }
       // mBackPressed = System.currentTimeMillis();
    }
    private void logout(){
        SQLClass db=new SQLClass(MainActivity.this);
        Cursor cc=db.ViewSingleUser(String.valueOf(Id));
        if(cc.getCount()>0)
        {
            cc.moveToFirst();
            String mode=cc.getString(5);
            if(mode.equals("OUR")) {
                session.setLoggedin(false);
                Session.setUserId(getApplicationContext(),-1);
                //Session.setCheckValue(this, 0, "#", "#");
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            else if(mode.equals("GOOGLE"))
            {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                session.setLoggedin(false);
                                Session.setUserId(getApplicationContext(),-1);
                                Session.setCheckValue(getApplicationContext(), 0, "#", "#");
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
            }
            else if(mode.equals("FB"))
            {
                LoginManager.getInstance().logOut();
                session.setLoggedin(false);
                Session.setUserId(getApplicationContext(),-1);
                Session.setCheckValue(getApplicationContext(), 0, "#", "#");
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        }
        else
        {
            LoginManager.getInstance().logOut();
            session.setLoggedin(false);
            Session.setUserId(getApplicationContext(),-1);
            Session.setCheckValue(getApplicationContext(), 0, "#", "#");
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
