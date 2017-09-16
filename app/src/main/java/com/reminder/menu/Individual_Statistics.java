package com.reminder.menu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.reminder.R;
import java.util.ArrayList;

/**
 * Created by admin on 23-03-2017.
 */

public class Individual_Statistics extends AppCompatActivity {
    Cursor data;
    SQLClass db;
    BarChart chart ;
    ArrayList<BarEntry> barGroup1;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset1;
    BarData BARDATA ;
    static int main_id,Id,Amount;
    static String Name,Policy_no;
    TextView policy_name,policy_no,paid_text,unpaid_text;
    int pending,paid;

    public static Intent newIntent(Context context, int id, String name, String policy_no,int amount)
    {
        Intent i = new Intent(context,Individual_Statistics.class);
        Id=id;
        Amount=amount;
        Name=name;
        Policy_no=policy_no;
        return i;
    }
    public static void setId(int id)
    {
        main_id=id;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policy_graph);
        db = new SQLClass(this);
        chart = (BarChart) findViewById(R.id.chart2);
        paid_text=(TextView) findViewById(R.id.policygraph_paid);
        unpaid_text=(TextView) findViewById(R.id.policygraph_unpaid);
        policy_name=(TextView)findViewById(R.id.policygraph_policy_name);
        policy_no=(TextView)findViewById(R.id.policygraph_policy_no);
        policy_name.setText(Name);
        policy_no.setText(Policy_no);
        chart.setDescription("");
        data=db.ViewSingleInstallMonth(String.valueOf(main_id),String.valueOf(Id));
        data.moveToFirst();
        int x=data.getCount();

        paid=data.getInt(2)*Amount;
        pending=(data.getInt(3)-data.getInt(2))*Amount;

        paid_text.setText("Paid :"+String.valueOf(data.getInt(2)));
        unpaid_text.setText("Unpaid :"+String.valueOf(data.getInt(3)-data.getInt(2)));
        barGroup1 = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddPillar(paid,0);
        AddPillar(pending,1);
        AddValuesToBarEntryLabels("Paid");
        AddValuesToBarEntryLabels("Unpaid");
        Bardataset1 = new BarDataSet(barGroup1,"Policy");
        Bardataset1.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(Bardataset1);

        BARDATA = new BarData(BarEntryLabels,dataSets);
        chart.setData(BARDATA);
        //  chart.animateX(3000);
        chart.animateY(3000);

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
}