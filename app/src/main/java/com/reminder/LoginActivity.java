package com.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.reminder.menu.MyAlarmService;
import com.reminder.menu.SQLClass;
import com.reminder.utils.Session;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.id;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener {
    private Button login;
    private TextView register;
    private EditText etEmail, etPass;
    private SQLClass db;
    private Session session;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private ProfileTracker mProfileTracker;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(LoginActivity.this, MyAlarmService.class));
        db = new SQLClass(this);
        session = new Session(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        if (session.loggedin()) {
            int id = session.getUserId(this);
            Cursor cc = db.ViewSingleUser(String.valueOf(id));
            String mode = "";
            if (cc.getCount() > 0) {
                cc.moveToFirst();
                mode = cc.getString(5);
            }
            if (mode.equals("GOOGLE")) {
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {

                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            //hideProgressDialog();
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
            } else if (mode.equals("OUR")) {
                Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                startActivity(intent);
                finish();
            } else if (mode.equals("FB")) {
                Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                startActivity(intent);
                finish();
            }

        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
    /* Google gso and api client */

    /* fb callback manager */
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        //mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        mCallbackManager = CallbackManager.Factory.create();


        login = (Button) findViewById(R.id.btnLogin);
        register = (TextView) findViewById(R.id.btnReg);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getApplicationContext(),"before",Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
           }
            @Override
            public void afterTextChanged(Editable s) {
                //   Toast.makeText(getApplicationContext(),"after",Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
                boolean chk = db.validateUser(etEmail.getText().toString());
                if (chk == true) {
                    //Toast.makeText(getApplicationContext(),"validate user",Toast.LENGTH_LONG).show();
                    Cursor cr = db.getUserData(etEmail.getText().toString());
                    cr.moveToFirst();
                    int id = cr.getInt(0);
                    Cursor cr1 = db.ViewSingleUser(String.valueOf(id));
                    cr1.moveToFirst();
                    String pass = cr1.getString(3);
                    if (pass.equals("") || pass == null) {
                        gotoalert1();
                    } else {

                    }
                } else {

                }
            }
        });
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // mLoginButton.setVisibility(View.GONE);
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            //  String id=profile2.getId().toString();
                            Cursor cc = db.getusermainid(profile2.getId().toString());
                            cc.moveToFirst();
                            if (cc.getCount() > 0) {
                                session.setLoggedin(true);
                                Session.setUserId(getApplicationContext(), cc.getInt(0));
                                db.updatemode(String.valueOf(id), "FB");
                                Intent FbIntent = MainActivity.newIntent(LoginActivity.this, cc.getInt(0));
                                startActivity(FbIntent);
                                //mProfileTracker.stopTracking();

                            } else {
                                alertfb(profile2.getName().toString(), profile2.getProfilePictureUri(100, 100).toString(), profile2.getId());
                                //mProfileTracker.stopTracking();
                            }
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Cursor cc = db.getusermainid(profile.getId().toString());
                    cc.moveToFirst();
                    if (cc.getCount() > 0) {
                        int id = cc.getInt(0);
                        session.setLoggedin(true);
                        Session.setUserId(getApplicationContext(), id);
                        db.updatemode(String.valueOf(id), "FB");
                        Intent FbIntent = MainActivity.newIntent(LoginActivity.this, cc.getInt(0));
                        startActivity(FbIntent);
                    } else {
                        alertfb(profile.getName().toString(), profile.getProfilePictureUri(100, 100).toString(), profile.getId());
                    }
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    void gotoalert1() {
        Cursor cr = db.getUserData(etEmail.getText().toString());
        cr.moveToFirst();
        int id = cr.getInt(0);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.get_password_layout, null);
        alertDialogBuilder.setView(v);
        final EditText newpass = (EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Let's Go !",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String newpassword = newpass.getText().toString();
                        if (!isValidPassword(newpassword)) {
                            if(newpassword.equals(""))
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertDialogBuilder.setMessage("Please Enter Password \n");
                                alertDialogBuilder.setPositiveButton("OK ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                gotoalert1();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            else
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertDialogBuilder.setMessage("\n" + "Password :" + "\n" + "Minimum 8 characters" + "\n" + "At least 1 Capital Alphabet" + "\n" + "Atleast 1 Number" + "\n" + "Atleast 1 Special Character" + "\n");
                                alertDialogBuilder.setPositiveButton("Set Again",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                gotoalert1();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                        } else {
                            Cursor cr = db.getUserData(etEmail.getText().toString());
                            cr.moveToFirst();
                            int id = cr.getInt(0);
                            boolean y;
                            boolean x = db.updatepass(String.valueOf(id), newpassword);
                            boolean z = db.updatemode(String.valueOf(id), "OUR");
                            //boolean y=db.updatemode(String.valueOf(id),)
                            y = x;
                            session.setLoggedin(true);
                            Session.setUserId(getApplicationContext(), id);
                            Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void gotoalert() {
        Cursor cr = db.getUserData(etEmail.getText().toString());
        cr.moveToFirst();
        int id = cr.getInt(0);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.get_password_layout, null);
        alertDialogBuilder.setView(v);
        final EditText newpass = (EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Let's Go !",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String newpassword = newpass.getText().toString();
                        if (!isValidPassword(newpassword)) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertDialogBuilder.setMessage("\n" + "Password :" + "\n" + "Minimum 8 characters" + "\n" + "At least 1 Capital Alphabet" + "\n" + "Atleast 1 Number" + "\n" + "Atleast 1 Special Character" + "\n");
                            alertDialogBuilder.setPositiveButton("Set Again",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            gotoalert();
                                        }
                                    });
                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    LoginManager.getInstance().logOut();
                                    arg0.dismiss();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            Cursor cr = db.getUserData(etEmail.getText().toString());
                            cr.moveToFirst();
                            int id = cr.getInt(0);
                            boolean y;
                            boolean x = db.updatepass(String.valueOf(id), newpassword);
                            boolean z = db.updatemode(String.valueOf(id), "OUR");
                            //boolean y=db.updatemode(String.valueOf(id),)
                            y = x;
                            session.setLoggedin(true);
                            Session.setUserId(getApplicationContext(), id);
                            Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginManager.getInstance().logOut();
                        arg0.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isValidPassword(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(target);
            return matcher.matches();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    void alertfb(final String username, final String userpro, final String fbid) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.get_email_layout, null);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCancelable(false);
        final EditText newemail = (EditText) v.findViewById(R.id.email);
        //final EditText newpass=(EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final int id;
                        //final String newpassword=newpass.getText().toString();
                        final String email = newemail.getText().toString();
                        if (!isValidEmail(email)) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            if(email.equals(""))
                            {
                                alertDialogBuilder.setMessage("Please Enter Email Address\n\n");
                            }
                            else
                            {
                                alertDialogBuilder.setMessage("Wrong Email Address\n\n");
                            }

                            alertDialogBuilder.setPositiveButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertfb(username, userpro, fbid);
                                        }
                                    });
                            /*alertDialogBuilder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            LoginManager.getInstance().logOut();
                                            arg0.dismiss();
                                        }
                                    }); */
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            if (db.validateUser(email)) {
                                Cursor cc1 = db.getUserData(email);
                                cc1.moveToFirst();
                                id = cc1.getInt(0);
                                Cursor cc = db.ViewSingleUser(String.valueOf(id));
                                cc.moveToFirst();
                                if (cc.getString(3).equals("")) {
                                    alertgoogleexist(id, email, username, userpro, fbid);
                                } else {
                                    alertpasswordcheckingfb(id, email, username, userpro, fbid);
                                }
                            } else {
                                alertemailpassget(email, username, userpro, fbid);
                            }
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginManager.getInstance().logOut();
                        arg0.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void alertgoogleexist(final int id, final String email, final String username, final String userpro, String fid) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String fbid = fid;
        View v = getLayoutInflater().inflate(R.layout.get_password_layout, null);
        alertDialogBuilder.setView(v);
        final EditText newpass = (EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Lets go !",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (!isValidPassword(newpass.getText().toString())) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            if(newpass.getText().toString().equals(""))
                            {
                                alertDialogBuilder.setMessage("Please Enter Password\n\n");
                            }
                            else
                            {
                                alertDialogBuilder.setMessage("\n" + "Password :" + "\n" + "Minimum 8 characters" + "\n" + "At least 1 Capital Alphabet" + "\n" + "Atleast 1 Number" + "\n" + "Atleast 1 Special Character" + "\n");
                            }

                            alertDialogBuilder.setPositiveButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertgoogleexist(id, email, username, userpro, fbid);
                                        }
                                    });
                            /*alertDialogBuilder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            LoginManager.getInstance().logOut();
                                            arg0.dismiss();
                                        }
                                    }); */
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        } else {
                            boolean a = db.updatepass(String.valueOf(id), newpass.getText().toString());
                            boolean b = db.updateusername(String.valueOf(id), username);
                            boolean c = db.updatepicfb(String.valueOf(id), userpro);
                            boolean d = db.updatefbid(String.valueOf(id), fbid);
                            boolean z = db.updatemode(String.valueOf(id), "FB");
                            session.setLoggedin(true);
                            Session.setUserId(getApplicationContext(), id);
                            Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginManager.getInstance().logOut();
                        arg0.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void alertpasswordcheckingfb(final int id, final String email, final String username, final String userpro, String fid) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String fbid = fid;
        View v = getLayoutInflater().inflate(R.layout.get_password_fb_layout, null);
        alertDialogBuilder.setView(v);
        final EditText newpass = (EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Let's go !",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (newpass.getText().toString().equals("")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertDialogBuilder.setMessage("Please Enter Password\n\n");
                            alertDialogBuilder.setNegativeButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertpasswordcheckingfb(id,email,username,userpro,fbid);
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            try {
                                alertDialog.show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                         else {
                            Cursor cc = db.ViewSingleUser(String.valueOf(id));
                            cc.moveToFirst();
                            if (cc.getString(3).equals(newpass.getText().toString())) {
                                boolean b = db.updateusername(String.valueOf(id), username);
                                boolean c = db.updatepicfb(String.valueOf(id), userpro);
                                boolean d = db.updatefbid(String.valueOf(id), fbid);
                                boolean z = db.updatemode(String.valueOf(id), "FB");
                                session.setLoggedin(true);
                                Session.setUserId(getApplicationContext(), id);
                                Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                alertDialogBuilder.setMessage("Wrong Password\n\n");
                                alertDialogBuilder.setPositiveButton("Try Again !",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                alertpasswordcheckingfb(id,email,username,userpro,fbid);
                                            }
                                        });
                                alertDialogBuilder.setNegativeButton("Back",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                LoginManager.getInstance().logOut();
                                                arg0.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                try {
                                    alertDialog.show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                                }
                            }
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginManager.getInstance().logOut();
                        arg0.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    void alertemailpassget(final String email, final String username, final String userpro, String fid) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String fbid = fid;
        View v = getLayoutInflater().inflate(R.layout.get_email_pass_fb_layout, null);
        alertDialogBuilder.setView(v);
        final EditText pass = (EditText) v.findViewById(R.id.pass);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Lets go !",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (!isValidPassword(pass.getText().toString())) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            if(pass.getText().toString().equals(""))
                            {
                                alertDialogBuilder.setMessage("Please Enter Password\n\n");
                            }
                            else
                            {
                                alertDialogBuilder.setMessage("\n" + "Password :" + "\n" + "Minimum 8 characters" + "\n" + "At least 1 Capital Alphabet" + "\n" + "Atleast 1 Number" + "\n" + "Atleast 1 Special Character" + "\n");
                            }

                            alertDialogBuilder.setPositiveButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertemailpassget(email, username, userpro, fbid);
                                        }
                                    });
                            /* alertDialogBuilder.setNegativeButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            LoginManager.getInstance().logOut();
                                            arg0.dismiss();
                                        }
                                    }); */
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            db.addUser(email, pass.getText().toString(), "", username, "FB", null, "", userpro, fbid);
                            Cursor cc = db.getUserData(email);
                            cc.moveToFirst();
                            int id = cc.getInt(0);
                            boolean z = db.updatemode(String.valueOf(id), "FB");
                            session.setLoggedin(true);
                            Session.setUserId(getApplicationContext(), id);
                            Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginManager.getInstance().logOut();
                        arg0.dismiss();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReg:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default:

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            db = new SQLClass(this);
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount userAccount = result.getSignInAccount();
            String userId = userAccount.getId();
            String displayedUsername = userAccount.getDisplayName();
            String userEmail = userAccount.getEmail();
            boolean chk = db.validateUser(userEmail);
            int id;
            if (chk == true) {
                Cursor cr = db.getUserData(userEmail);
                if (cr.getCount() > 0) {
                    cr.moveToFirst();
                    id = cr.getInt(0);
                    try {
                        String userProfilePhoto = userAccount.getPhotoUrl().toString();
                        boolean x = db.updatepicgoogle(String.valueOf(id), userProfilePhoto);
                        boolean z = db.updatemode(String.valueOf(id), "GOOGLE");
                        session.setLoggedin(true);
                        Session.setUserId(getApplicationContext(), id);
                        Intent googleSignInIntent = MainActivity.newIntent(LoginActivity.this, id);
                        startActivity(googleSignInIntent);
                    } catch (Exception e) {
                        boolean x = db.updatepicgoogle(String.valueOf(id), "");
                        boolean z = db.updatemode(String.valueOf(id), "GOOGLE");
                        session.setLoggedin(true);
                        Session.setUserId(getApplicationContext(), id);
                        Intent googleSignInIntent = MainActivity.newIntent(LoginActivity.this, id);
                        startActivity(googleSignInIntent);
                    }

                }
            } else {
                try {
                    String userProfilePhoto = userAccount.getPhotoUrl().toString();

                    byte[] bytes = null;
                    db.addUser(userEmail, "", "", displayedUsername, "GOOGLE", bytes, userProfilePhoto, "", "");
                    Cursor cc = db.ViewAllUser();
                    cc.moveToLast();
                    id = cc.getInt(0);
                    session.setLoggedin(true);
                    Session.setUserId(getApplicationContext(), id);
                    Intent googleSignInIntent = MainActivity.newIntent(LoginActivity.this, id);
                    startActivity(googleSignInIntent);
                    //Intent googleSignInIntent = new Intent(LoginActivity.this,MainActivity.class);
                    //startActivity(googleSignInIntent);
                } catch (Exception e) {
                /*icon= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                    ByteArrayOutputStream stream =new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] byteArray=stream.toByteArray();
                    byte[] image=byteArray;*/
                    byte[] bytes = null;
                    db.addUser(userEmail, "", "", displayedUsername, "GOOGLE", bytes, "", "", "");
                    Cursor cc = db.ViewAllUser();
                    cc.moveToLast();
                    id = cc.getInt(0);
                    session.setLoggedin(true);
                    Session.setUserId(getApplicationContext(), id);
                    // Session.setCheckValue(this,2,userEmail,"null");
                    Intent googleSignInIntent = MainActivity.newIntent(LoginActivity.this, id);
                    startActivity(googleSignInIntent);
                }
            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        LoginManager.getInstance().logOut();
        super.onStop();
    }


    private void login() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        if(email.equals("") || pass.equals(""))
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage(" Please enter Email or Passwrod ");
            alertDialogBuilder.setPositiveButton("OK ",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
        }
        else
        {
            if (db.getUser(email, pass)) {
                //Session.setCheckValue(this,1,email,"null");
                Cursor cr = db.getUserData(email);
                cr.moveToFirst();
                int id = cr.getInt(0);
                session.setLoggedin(true);
                Session.setUserId(getApplicationContext(), id);
                Boolean chk = db.updatemode(String.valueOf(id), "OUR");
                Intent intent = MainActivity.newIntent(LoginActivity.this, id);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Wrong email/password", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
