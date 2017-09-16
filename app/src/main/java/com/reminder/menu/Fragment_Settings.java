package com.reminder.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.reminder.MainActivity;
import com.reminder.R;
import com.reminder.utils.Session;

public class Fragment_Settings extends Fragment {

    Switch aSwitch1,aSwitch2;
    Session sess;
    android.support.v7.app.AlertDialog.Builder a_builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.fragment_setting, null);
        sess=new Session(view.getContext());
        final boolean x=sess.snooze();
        final boolean y=sess.Notify();
        aSwitch1=(Switch)view.findViewById(R.id.switch1);
        aSwitch2=(Switch)view.findViewById(R.id.switch2);
        if(x==true)
        {
            aSwitch1.setChecked(true);
        }
        if(y==true)
        {
            aSwitch2.setChecked(true);
        }
        aSwitch1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(x==true)
                        {
                            a_builder=new AlertDialog.Builder(view.getContext());
                            a_builder.setMessage("If you off snooze time then you will not get snooze notification are you sure want to do that?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sess.setSnooze(false);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            aSwitch1.setChecked(true);
                                            dialog.cancel();
                                        }
                                    });
                            android.support.v7.app.AlertDialog alert=a_builder.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                        else
                        {
                            sess.setSnooze(true);
                            //view.getContext().startService(new Intent(view.getContext(),MyAlarmService.class));
                        }
                    }
                }
        );
        aSwitch2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if(y==true)
                        {
                            a_builder=new AlertDialog.Builder(view.getContext());
                            a_builder.setMessage("If you off Notification then you will not get notification are you sure want to do that?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sess.setnotify(false);
                                            //view.getContext().stopService(new Intent(view.getContext(),MyAlarmService.class));
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            aSwitch2.setChecked(true);
                                            dialog.cancel();
                                        }
                                    });
                            android.support.v7.app.AlertDialog alert=a_builder.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                        else
                        {
                            sess.setnotify(true);
                        }
                    }
                }
        );
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
    }
}