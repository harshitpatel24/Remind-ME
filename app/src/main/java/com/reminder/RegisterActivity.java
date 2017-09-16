package com.reminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.reminder.menu.SQLClass;
import com.reminder.utils.Session;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button reg;
    private TextView tvLogin;
    private EditText etEmail, etPass, etMobile;
    private SQLClass db;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session=new Session(this);
        db = new SQLClass(this);
        reg = (Button)findViewById(R.id.btnReg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email=etEmail.getText().toString();
                boolean chk=db.validateUser(email);
                if(chk==true)
                {
                 AlertDialog.Builder alert=new AlertDialog.Builder(RegisterActivity.this);
                    alert.setMessage("User Already Exists !!");
                    alert.setPositiveButton("Go for Login",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    finish();
                                }
                            });

                    alert.setNegativeButton("Register Other",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
                            finish();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();


                }
            }
        });
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    public void alertcheck()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

        alertDialogBuilder.setCancelable(false);

        //final EditText newpass=(EditText) v.findViewById(R.id.pass);

        alertDialogBuilder.setPositiveButton("Go to Login",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0,int arg1)
            {
                arg0.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        try {
            alertDialog.show();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnReg:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
                break;
            default:

        }
    }

    private void register() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String mobile = etMobile.getText().toString();
        String error = new String();
        //int flag = 0;

        if(email.equals("")|| mobile.equals("") || pass.equals(""))
        {
            error = "\n" + "Please fill up all details" + "\n";
            alert1(error);
        }
        else
        {
            if (!isValidEmail(email)) {
                error = "\n" + "Invalid Email" + "\n";
                alert1(error);

            } else {
                if (!isValidMobile(mobile)) {

                    error = "\n" + "Mobile Number :" + "\n" + "10 digits" + "\n";
                    alert1(error);
                } else {
                    if (!isValidPassword(pass)) {
                        error = "\n" + "Password :" + "\n" + "minimum 8 characters" + "\n" + " at least 1 Capital Alphabet" + "\n" + "atleast 1 Number" + "\n" + "atleast 1 Special Character" + "\n";
                        alert1(error);
                    } else {
                        db.addUser(email, pass, mobile, "", "OUR", null, "", "", "");
                        Cursor cr=db.getUserData(email);
                        cr.moveToFirst();
                        int id=cr.getInt(0);
                        session.setLoggedin(true);
                        Boolean chk=db.updatemode(String.valueOf(id),"OUR");
                        Intent intent = MainActivity.newIntent(RegisterActivity.this,id);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }



    void alert1(String error)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Error Log:"+"\n"+error);

        alertDialogBuilder.setNegativeButton("OK",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public final static boolean isValidMobile(CharSequence target) {
        if(target.length()!= 10)
            return false;
        else
            return true;

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
    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
