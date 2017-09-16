package com.reminder.menu;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.reminder.R;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abc on 3/9/2017.
 */
public class TotalStatistics extends Fragment {
    int total[]=new int[20];
    static int size=0;
    Cursor cursor,cursorCategory,cursorCopy;
    Boolean flag=false,basic=true;
    ArrayList<String> categories=new ArrayList<String>();
    SQLClass db;
    int year,month,day;
    Date d;
    Date dddd,dx;
    Date dbStart,dbEnd,StartDt,EndDt;
    int mYear,mMonth,mDay;
    Button startPicker,endPicker;
    EditText startDate,endDate;
    BarChart chart ;
    ArrayList<BarEntry> barGroup1;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset1;
    BarData BARDATA ;
    String dbStartDate="02/1/2017";
    String dbEndDate="01/11/2018";
    int dbInstallMonth=3;
    int dbInstallAmount=1000;
    static int main_id;

    public void setId(int id)
    {
        main_id=id;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_statistics, container, false);
        db=new SQLClass(getActivity());
        chart = (BarChart)v.findViewById(R.id.chart1);
        chart.setDescription("");

        barGroup1 = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        startDate =(EditText)v.findViewById(R.id.start_date_edit_text);
        endDate =(EditText)v.findViewById(R.id.end_date_edit_text);
        StartDt=convertToDate(dbStartDate);
        EndDt=convertToDate(dbEndDate);

        startPicker=(Button)v.findViewById(R.id.graph_start_date_button);
        startPicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        StartDt=convertToDate(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
        );

        endPicker=(Button)v.findViewById(R.id.graph_end_date_button);
        endPicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        endDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                        EndDt=convertToDate(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                        if(startDate.getText().toString()!=null){
                                            RemovePillars(size);
                                            basic=false;
                                            for(int i=0;i<size;i++){
                                                total[i]=0;
                                                flag=false;
                                                cursor=db.viewAllData(String.valueOf(main_id));
                                                while(cursor.moveToNext()){            //TOTAL FOR ALL POLICIES,CATEGORIES WISE
                                                    int amt=0;
                                                    String cat=cursor.getString(5);
                                                    if(cat.equals(categories.get(i))){
                                                        flag=true;
                                                        dbStart=convertToDate(cursor.getString(12));
                                                        dbEnd=convertToDate(cursor.getString(16));
                                                        String x=cursor.getString(10);
                                                        String sep[]=x.split(" ");
                                                        dbInstallMonth=Integer.valueOf(sep[0]);
                                                             dbInstallAmount=cursor.getInt(11);
                                                        //dbInstallAmount=1000;
                                                        amt=calculateTotal(dbStart,dbEnd,dbInstallMonth,dbInstallAmount);
                                                        total[i]+=amt;
                                                    }

                                                }
                                            }
                                            for(int i=0;i<size;i++){
                                                RefreshPillar(total[i], i);
                                            }
                                        }
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
        );


         cursorCategory=db.viewDefaultCategory();     //TO GET ALL CATEGORIES
         cursor=db.viewAllData(String.valueOf(main_id));         //TO GET ALL DATA

        while(cursorCategory.moveToNext()){       //TO GET ALL CATEGORY NAMES IN ARRAY
            categories.add(cursorCategory.getString(0));
            AddValuesToBarEntryLabels(cursorCategory.getString(0));
        }
        cursorCategory=db.viewAllcategory(String.valueOf(main_id));
        while(cursorCategory.moveToNext()){       //TO GET ALL CATEGORY NAMES IN ARRAY
            categories.add(cursorCategory.getString(1));
            AddValuesToBarEntryLabels(cursorCategory.getString(1));
        }
        if(categories.size()>4){
            chart.getXAxis().setLabelRotationAngle(270);
        }
        size=categories.size();
        for(int i=0;i<size;i++){
            total[i]=0;
            flag=false;
            basic=true;
            cursor=db.viewAllData(String.valueOf(main_id));
            while(cursor.moveToNext()){            //TOTAL FOR ALL POLICIES,CATEGORIES WISE
                int amt;
                String cat=cursor.getString(5);
                if(cat.equals(categories.get(i))){
                    flag=true;
                    dbStart=convertToDate(cursor.getString(12));
                    StartDt=dbStart;
                    dbEnd=convertToDate(cursor.getString(16));
                    EndDt=dbEnd;
                    String x=cursor.getString(10);
                    String sep[]=x.split(" ");
                    dbInstallMonth=Integer.valueOf(sep[0]);
                   dbInstallAmount=cursor.getInt(11);
                    //dbInstallAmount=1000;
                    amt=calculateTotal(dbStart,dbEnd,dbInstallMonth,dbInstallAmount);
                    total[i]+=amt;
                }
        }
        }
        for(int i=0;i<size;i++){
            AddPillar(total[i], i);
        }


        Bardataset1 = new BarDataSet(barGroup1,"Categories");
        Bardataset1.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(Bardataset1);

        BARDATA = new BarData(BarEntryLabels,dataSets);

        chart.setData(BARDATA);
      //  chart.animateX(3000);
        chart.animateY(3000);
        return v;
    }
    public void AddPillar(int val,int index) {
        barGroup1.add(new BarEntry(val, index));
    }
    public void RefreshPillar(int val,int index){
        barGroup1.add(new BarEntry(val,index));
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
    public void RemovePillars(int size){
        for(int i=size-1;i>=0;i--){
            barGroup1.remove(i);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }
    public void AddValuesToBarEntryLabels(String label){
        BarEntryLabels.add(label);
    }
    public Date convertToDate(String dt){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        try {
            d = sdf.parse(dt);
            dx=d;
        } catch (Exception ex) {
              ex.printStackTrace();
        }
        return d;
    }
    public String convertToString(Date dt){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        sdf.applyPattern("dd/M/yyyy");
        return sdf.format(dt);
    }
    public Calendar convertToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    public Date convertToDate1(Calendar c){
        return c.getTime();
    }
    public Date ConvertAndAddMonth(Calendar c,int val){
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+val;
        day   = c.get(Calendar.DAY_OF_MONTH);
        String td=day + "/" + month + "/" + year;
        return convertToDate(td);
    }
    public int calculateTotal(Date sdate,Date edate,int install,int amt){
        int count=0;
        Date checkDate;
        if(edate.compareTo(EndDt)>0){
            checkDate=EndDt;
        }else{
            checkDate=edate;
        }
        if(StartDt.compareTo(sdate)>0 ){
            dddd=sdate;
            while(StartDt.compareTo(dddd)>0){
                Calendar c=convertToCalendar(dddd);
                c.add(Calendar.MONTH,install);
                dddd=convertToDate1(c);
            }
        }
        if(StartDt.compareTo(sdate)<=0 )
        dddd=sdate;

        while(checkDate.compareTo(dddd)>=0){
            Calendar c=convertToCalendar(dddd);
              c.add(Calendar.MONTH,install);
              dddd=convertToDate1(c);
            count++;
        }
        //count--;
        Calendar c=convertToCalendar(dddd);
        c.add(Calendar.MONTH,-install);
        dddd=convertToDate1(c);
        if(checkDate.compareTo(dddd)>0 && checkDate==edate)
            count++;
       return  amt*count;
    }
}
