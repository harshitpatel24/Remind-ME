package com.reminder.menu;

/**
 * Created by admin on 24-01-2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.reminder.MainActivity;
import com.reminder.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by vishvraj on 20-01-2017.
 */

public class Adapter_Upcoming_Pending extends RecyclerView.Adapter<Adapter_Upcoming_Pending.MyViewHolder> {
    static Model_DataBase ddd;
    ArrayList<Model_DataBase> arrayList=new ArrayList<>();
    int y;
    public SQLClass db;
    static int main_id;

    public void setId(int id)
    {
        main_id=id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name,no,days_left1,type,premium_amount;
        public ProgressBar progressBar;
        android.support.v7.app.AlertDialog.Builder a_builder;
        int pay_id;
        int pay_day,pay_month,pay_year,pay_hour,pay_minute;
        String date;
        ImageView paid,update,delete;
        Date current,start,end;
        CircleImageView Photo1;



        public MyViewHolder(final View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
            name=(TextView)view.findViewById(R.id.textView_name);
            no=(TextView)view.findViewById(R.id.textView_no);
            days_left1=(TextView)view.findViewById(R.id.days_left);
            db=new SQLClass(view.getContext());
            paid=(ImageView)view.findViewById(R.id.imageView4);
            update=(ImageView)view.findViewById(R.id.imageView2);
            delete=(ImageView)view.findViewById(R.id.imageView3);
            Photo1 = (CircleImageView)view.findViewById(R.id.PolicyPic);
            type=(TextView)view.findViewById(R.id.textView_type);
            premium_amount=(TextView)view.findViewById(R.id.textView_premium_amount);
            if(y==5)
            {
                paid.setVisibility(View.GONE);
            }
            update.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos=getAdapterPosition();
                            Model_DataBase tx=arrayList.get(pos);
                            Intent i= Activity_AddPlan.updatemode(view.getContext(),1,tx);
                            view.getContext().startActivity(i);
                        }
                    }
            );
                delete.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                a_builder=new AlertDialog.Builder(view.getContext());
                                a_builder.setMessage("Are you sure you want to Delete")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int pos=getAdapterPosition();
                                                Model_DataBase tx=arrayList.get(pos);
                                                db.DeleteData(String.valueOf(main_id),String.valueOf(tx.getId()));
                                                if(y==5)
                                                {
                                                    Intent i = MainActivity.newIntentx(view.getContext(),2);
                                                    view.getContext().startActivity(i);
                                                }
                                                else
                                                {
                                                    Context context=view.getContext();
                                                    Fragment_Investment up=new Fragment_Investment();
                                                    FragmentManager fragmentManager =((AppCompatActivity)context).getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                    fragmentTransaction.replace(R.id.containerView, up);
                                                    fragmentTransaction.commit();
                                                }
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
                            }
                        }
                );
            if(y!=5)
            {
                paid.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                AlertDialog.Builder a_builder=new AlertDialog.Builder(view.getContext());
                                a_builder.setMessage("Have you paid?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Calendar cal=Calendar.getInstance();

                                                int mMonth=cal.get(Calendar.MONTH)+1;
                                                int mYear=cal.get(Calendar.YEAR);
                                                int mDay=cal.get(Calendar.DAY_OF_MONTH);
                                                date=mDay+"/"+mMonth+"/"+mYear;
                                                int pos=getAdapterPosition();
                                                Model_DataBase ttmp=arrayList.get(pos);
                                                boolean isInserted=db.insertHistory(main_id,ttmp.getId(),ttmp.getName(),date);
                                                Cursor cr=db.viewSinglePremiumAmount(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                if(cr.getCount()>0)
                                                {
                                                    Cursor czz=db.ViewSingleInstallMonth(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                    czz.moveToFirst();
                                                    cr.moveToFirst();
                                                    db.updateInstallMonth(String.valueOf(main_id),String.valueOf(ttmp.getId()),cr.getInt(3)+czz.getInt(2));
                                                    db.deletePremiumAmount(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                }
                                                else
                                                {
                                                    Cursor czz=db.ViewSingleInstallMonth(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                    czz.moveToFirst();
                                                    db.updateInstallMonth(String.valueOf(main_id),String.valueOf(ttmp.getId()),czz.getInt(2)+1);
                                                }
                                                String startDate=ttmp.getPremium_Date();
                                                String endDate=ttmp.getLast_Premium_Date();
                                                if(isInserted==true)
                                                {
                                                    Cursor cursor;
                                                    if(y==0)
                                                    {
                                                        cursor=db.ViewDate(String.valueOf(main_id));
                                                    }
                                                    else
                                                    {
                                                        cursor=db.viewPendingDate(String.valueOf(main_id));
                                                    }
                                                    String x=ttmp.getPayment_Mode();
                                                    String sep[]=x.split(" ");
                                                    int im=Integer.valueOf(sep[0]);
                                                    cursor.moveToFirst();
                                                    do {
                                                        if(cursor.getInt(1)==ttmp.getId())
                                                        {
                                                            pay_id=cursor.getInt(1);
                                                            pay_day=cursor.getInt(2);
                                                            pay_month=cursor.getInt(3);
                                                            pay_year=cursor.getInt(4);
                                                            pay_hour=cursor.getInt(5);
                                                            pay_minute=cursor.getInt(6);
                                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
                                                            try
                                                            {
                                                                end=simpleDateFormat.parse(endDate);
                                                                start=simpleDateFormat.parse(startDate);
                                                                //install=simpleDateFormat.parse(pay_install);
                                                                current = simpleDateFormat.parse(date);
                                                            }
                                                            catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            Calendar cal4=Calendar.getInstance();
                                                            Calendar cal3=Calendar.getInstance();
                                                            Calendar cal2=Calendar.getInstance();
                                                            Calendar cal1=Calendar.getInstance();
                                                            cal2.set(Calendar.DAY_OF_MONTH,pay_day);
                                                            cal2.set(Calendar.MONTH,pay_month-1);
                                                            cal2.set(Calendar.YEAR,pay_year);
                                                            cal2.set(Calendar.HOUR_OF_DAY,pay_hour);
                                                            cal2.set(Calendar.MINUTE,pay_minute);
                                                            cal1.setTime(current);
                                                            cal3.setTime(start);
                                                            cal4.setTime(end);

                                                            if(cal4.get(Calendar.DAY_OF_MONTH)==pay_day&&(cal4.get(Calendar.MONTH)+1)==pay_month&&cal4.get(Calendar.YEAR)==pay_year)
                                                            {
                                                                if(y==0)
                                                                {
                                                                    db.deleteDate(String.valueOf(main_id),String.valueOf(pay_id));
                                                                    db.deleteSnoozeDate(String.valueOf(main_id),String.valueOf(pay_id),"P");
                                                                    //Toast.makeText(view.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                                                }
                                                                else
                                                                {
                                                                    db.deletePendingDate(String.valueOf(main_id),String.valueOf(pay_id));
                                                                    Cursor cd=db.ViewSnoozeDate(String.valueOf(main_id));
                                                                    cd.moveToFirst();
                                                                    do {
                                                                        if(cd.getInt(1)==pay_id)
                                                                        {
                                                                            db.deleteSnoozeDate(String.valueOf(main_id),String.valueOf(pay_id),"P");
                                                                            break;
                                                                        }
                                                                    }while(cd.moveToNext());
                                                                    //Toast.makeText(view.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();

                                                                }
                                                                db.deleteInstallMonth(String.valueOf(main_id),String.valueOf(pay_id));
                                                                db.insertCompletedPlan(main_id,pay_id,ttmp.getName(),ttmp.getPolicy_No(),ttmp.getInvestMent_Plan());
                                                            }
                                                            else
                                                            {
                                                                cal2.add(Calendar.MONTH,im);
                                                                int mDay1,mMonth1,mYear1;
                                                                if(cal4.after(cal2))
                                                                {
                                                                    String xy=ttmp.getTerm();
                                                                    String sepe[]=xy.split(" ");
                                                                    int days=Integer.valueOf(sepe[0]);
                                                                    mDay=cal2.get(Calendar.DAY_OF_MONTH);
                                                                    mMonth=cal2.get(Calendar.MONTH)+1;
                                                                    mYear=cal2.get(Calendar.YEAR);
                                                                    cal2.add(Calendar.DAY_OF_MONTH,-days);
                                                                    mDay1=cal2.get(Calendar.DAY_OF_MONTH);
                                                                    mMonth1=cal2.get(Calendar.MONTH)+1;
                                                                    mYear1=cal2.get(Calendar.YEAR);
                                                                }
                                                                else
                                                                {
                                                                    String xy=ttmp.getTerm();
                                                                    String sepe[]=xy.split(" ");
                                                                    int days=Integer.valueOf(sepe[0]);
                                                                    mDay=cal4.get(Calendar.DAY_OF_MONTH);
                                                                    mMonth=cal4.get(Calendar.MONTH)+1;
                                                                    mYear=cal4.get(Calendar.YEAR);
                                                                    cal4.add(Calendar.DAY_OF_MONTH,-days);
                                                                    mDay1=cal4.get(Calendar.DAY_OF_MONTH);
                                                                    mMonth1=cal4.get(Calendar.MONTH)+1;
                                                                    mYear1=cal4.get(Calendar.YEAR);
                                                                }
                                                                boolean chk;
                                                                if(y==0)
                                                                {
                                                                    db.updateSnoozeDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay1,mMonth1,mYear1,pay_hour,pay_minute,"P");
                                                                    chk=db.updateDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay,mMonth,mYear,pay_hour,pay_minute);
                                                                }
                                                                else
                                                                {
                                                                    if(cal4.after(cal2))
                                                                    {
                                                                        if(cal2.after(cal1))
                                                                        {
                                                                       /* String xy=ttmp.getRemind_day();
                                                                        String sepe[]=xy.split(" ");
                                                                        int days=Integer.valueOf(sepe[0]);
                                                                        cal2.add(Calendar.DAY_OF_MONTH,-days);
                                                                        int mDay2=cal2.get(Calendar.DAY_OF_MONTH);*/
                                                                            int chkk=0;
                                                                            Cursor cd=db.ViewSnoozeDate(String.valueOf(main_id));
                                                                            if(cd.getCount()>0)
                                                                            {
                                                                                cd.moveToFirst();
                                                                                do {
                                                                                    if(cd.getInt(0)==ttmp.getId())
                                                                                    {
                                                                                        chkk=1;
                                                                                        db.updateSnoozeDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay1,mMonth1,mYear1,pay_hour,pay_minute,"P");
                                                                                        break;
                                                                                    }
                                                                                }while(cd.moveToNext());
                                                                            }
                                                                            if(chkk==0)
                                                                            {
                                                                                db.insertSnoozeDate(main_id,ttmp.getId(),mDay1,mMonth1,mYear1,pay_hour,pay_minute,"P");
                                                                            }
                                                                            chk=db.insertDate(main_id,ttmp.getId(),mDay,mMonth,mYear,pay_hour,pay_minute);
                                                                            db.deletePendingDate(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                                        }
                                                                        else
                                                                        {
                                                                            chk=db.updatePendingDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay,mMonth,mYear,pay_hour,pay_minute);
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        if(cal4.after(cal1))
                                                                        {
                                                                            String xy=ttmp.getTerm();
                                                                            String sepe[]=xy.split(" ");
                                                                            int days=Integer.valueOf(sepe[0]);
                                                                            cal4.add(Calendar.DAY_OF_MONTH,-days);
                                                                            int mDay2=cal4.get(Calendar.DAY_OF_MONTH);
                                                                            int mMonth2=cal4.get(Calendar.MONTH)+1;
                                                                            int mYear2=cal4.get(Calendar.YEAR);

                                                                            int chkk=0;
                                                                            Cursor cd=db.ViewSnoozeDate(String.valueOf(main_id));
                                                                            if(cd.getCount()>0)
                                                                            {
                                                                                cd.moveToFirst();
                                                                                do {
                                                                                    if(cd.getInt(0)==ttmp.getId())
                                                                                    {
                                                                                        chkk++;
                                                                                        db.updateSnoozeDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay2,mMonth2,mYear2,pay_hour,pay_minute,"P");
                                                                                        break;
                                                                                    }
                                                                                }while(cd.moveToNext());
                                                                            }
                                                                            if(chkk==0)
                                                                            {
                                                                                db.insertSnoozeDate(main_id,ttmp.getId(),mDay2,mMonth2,mYear2,pay_hour,pay_minute,"P");
                                                                            }
                                                                            chk=db.insertDate(main_id,ttmp.getId(),mDay,mMonth,mYear,pay_hour,pay_minute);
                                                                            db.deletePendingDate(String.valueOf(main_id),String.valueOf(ttmp.getId()));
                                                                        }
                                                                        else
                                                                        {
                                                                            chk=db.updatePendingDate(String.valueOf(main_id),String.valueOf(ttmp.getId()),mDay,mMonth,mYear,pay_hour,pay_minute);
                                                                        }
                                                                    }
                                                                }
                                                                if(chk==true)
                                                                {
                                                                    view.getContext().stopService(new Intent(view.getContext(),MyAlarmService.class));
                                                                    //Toast.makeText(view.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                                                }

                                                                break;
                                                            }


                                                        }
                                                    }while(cursor.moveToNext());
                                                        Context context=view.getContext();
                                                       // Fragment_History up=new Fragment_History();
                                                        Fragment_Investment up=new Fragment_Investment();
                                                    Bundle bd=new Bundle();
                                                    bd.putInt("Pos",2);
                                                    up.setArguments(bd);
                                                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.containerView, up);
                                                        fragmentTransaction.commit();

                                                /* set callback interface to invoke while click on list item or view stub
                                                *
                                                * or try broadcast reciver to get notify list click event from */
                                                    // view.getContext().startActivity(new Intent(view.getContext(),Intermediate.class));
                                                }
                                                else
                                                {
                                                    Toast.makeText(view.getContext(),"not deleted",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert=a_builder.create();
                                alert.setTitle("Alert");
                                alert.show();
                            }
                        }
                );
            }

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            ddd =arrayList.get(pos);
            Intent i;
            if(y==1)
            {
                i= Activity_PlanDetails.newIntent(v.getContext(),ddd.getId(),5);
            }
            else
            {
                i= Activity_PlanDetails.newIntent(v.getContext(),ddd.getId(),1);
            }
            v.getContext().startActivity(i);
        }
    }

    public Adapter_Upcoming_Pending(ArrayList<Model_DataBase> arrayList, int x) {
        this.arrayList = arrayList;
        y=x;
    }
    public Adapter_Upcoming_Pending() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        setAnimation(holder.itemView, position);
        ddd= arrayList.get(position);

        Model_DataBase dbm=arrayList.get(position);
        ddd=dbm;

        holder.no.setText(ddd.getPolicy_No());
        if(ddd.getInvestMent_Plan().toLowerCase().equals("mediclaim"))
        {
            holder.Photo1.setImageResource(R.drawable.mediclaim);
        }
        else if(ddd.getInvestMent_Plan().toLowerCase().equals("lic"))
        {
            holder.Photo1.setImageResource(R.drawable.lic);
        }
        else if(ddd.getInvestMent_Plan().toLowerCase().equals("pf"))
        {
            holder.Photo1.setImageResource(R.drawable.pf);
        }
        else
        {
            holder.Photo1.setImageResource(R.drawable.user);
        }
        holder.no.setText(ddd.getPolicy_No());
        holder.type.setText(ddd.getInvestMent_Plan());
        holder.name.setText(ddd.getName());
        Cursor cr=db.viewSinglePremiumAmount(String.valueOf(main_id),String.valueOf(ddd.getId()));
        if(cr.getCount()>0)
        {
            cr.moveToFirst();
            holder.premium_amount.setText("Premium Amount: "+String.valueOf(cr.getInt(2)));
        }
        else
        {
            holder.premium_amount.setText("Premium Amount: "+String.valueOf(ddd.getPremium_Amount()));
        }
        if(ddd.getTotal_days()==-9999)
        {
            holder.progressBar.setVisibility(View.GONE);
            holder.days_left1.setText("Completed");
            holder.days_left1.setTextColor(Color.rgb(46,125,32));
        }
        else
        {
            holder.days_left1.setText(String.valueOf(ddd.getTotal_days())+" Day left");
            int progress=365-(int)ddd.getTotal_days();
            if(progress<=0){
                progress=0;
            }
            if(progress>350) {
                holder.progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }else if(progress>300 && progress<=350) {
                holder.progressBar.getProgressDrawable().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);
            }else if(progress>200 && progress<=300) {
                holder.progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
            }else if(progress>100 && progress<=200) {
                holder.progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            }else{
                holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            }
            holder.progressBar.setProgress(progress);
        }
    }
    private int lastPosition = -1;
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}