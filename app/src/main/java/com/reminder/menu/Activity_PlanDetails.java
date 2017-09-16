package com.reminder.menu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.reminder.MainActivity;
import com.reminder.R;

/**
 * Created by admin on 10-03-2017.
 */

public class Activity_PlanDetails extends AppCompatActivity {
    static int main_id,Flag;
    static Model_DataBase dx;
    TextView name,policy_no,investment_plan,holder_names,sum_assured,premium_amount,
            start_date,maturity_date,payment_mode,term,total_premium,premium_date,no_of_install,main_holder;
    static SQLClass sql;
    ImageView Photo1;
    Cursor data;
    Toolbar toolbar;

    public static void setId(int id)
    {
        main_id=id;
    }

    public static Intent newIntent(Context context, int id,int flag)
    {
        Intent i=new Intent(context,Activity_PlanDetails.class);
        Flag=flag;
        sql=new SQLClass(context);
        Cursor cursor=sql.viewSingleData(String.valueOf(main_id),String.valueOf(id));
        cursor.moveToFirst();
        dx=new Model_DataBase(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5)
                , cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)
                , cursor.getString(12), cursor.getInt(13),cursor.getInt(14),cursor.getString(15),cursor.getString(16),0);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_details);

        toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        main_holder=(TextView)findViewById(R.id.textViewMainHolderName1);
        Photo1=(ImageView) findViewById(R.id.imageView);
        name=(TextView) findViewById(R.id.textViewName1);
        policy_no=(TextView) findViewById(R.id.textViewPolicyNo1);
        investment_plan=(TextView) findViewById(R.id.textViewInvestmentPlan1);
        holder_names=(TextView) findViewById(R.id.textViewHolderName1);
        sum_assured=(TextView) findViewById(R.id.textViewSumAssured1);
        premium_amount=(TextView) findViewById(R.id.textViewPremiumAmount1);
        start_date=(TextView) findViewById(R.id.textViewStartDate1);
        maturity_date=(TextView) findViewById(R.id.textViewMaturityDate1);
        payment_mode=(TextView) findViewById(R.id.textViewpremiuminterval1);
        term=(TextView) findViewById(R.id.textViewbefore_notify1);
        total_premium=(TextView) findViewById(R.id.textViewTotalPremium1);
        premium_date=(TextView) findViewById(R.id.textViewPremiumDate1);
        no_of_install=(TextView) findViewById(R.id.textViewno_installment1);

        if(dx.getInvestMent_Plan().toLowerCase().equals("mediclaim"))
        {
            Photo1.setImageResource(R.drawable.mediclaim);
        }
        else if(dx.getInvestMent_Plan().toLowerCase().equals("lic"))
        {
            Photo1.setImageResource(R.drawable.lic);
        }
        else if(dx.getInvestMent_Plan().toLowerCase().equals("pf"))
        {
            Photo1.setImageResource(R.drawable.pf);
        }
        else
        {
            Photo1.setImageResource(R.drawable.user);
        }
        main_holder.setText(dx.getMain_Holder());
        name.setText(dx.getName());
        policy_no.setText(dx.getPolicy_No());
        investment_plan.setText(dx.getInvestMent_Plan());
        holder_names.setText(dx.getHolder_Names());
        sum_assured.setText(String.valueOf(dx.getSum_Assured()));
        premium_amount.setText(String.valueOf(dx.getPremium_Amount()));
        start_date.setText(dx.getStart_Date());
        maturity_date.setText(dx.getMaturity_Date());
        payment_mode.setText(dx.getPayment_Mode());
        term.setText(dx.getTerm());
        premium_date.setText(dx.getPremium_Date());
        total_premium.setText(String.valueOf(dx.getTotal_Premium_Amount()));
        no_of_install.setText(String.valueOf(dx.getNo_Installment()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.policy_graph:
                Intent i = Individual_Statistics.newIntent(Activity_PlanDetails.this,dx.getId(),dx.getName(),dx.getPolicy_No(),dx.getPremium_Amount());
                    startActivity(i);
                return true;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if(Flag==1)
        {
            //upcoming
            Intent i= MainActivity.newIntentx1(Activity_PlanDetails.this,2,0);
            startActivity(i);
        }
        else if(Flag==5)
        {
            //pending
            Intent i= MainActivity.newIntentx1(Activity_PlanDetails.this,2,1);
            startActivity(i);
        }
        else if(Flag==2)
        {
            //history
            Intent i= MainActivity.newIntentx1(Activity_PlanDetails.this,2,2);
            startActivity(i);
        }
        else if(Flag==3)
        {
            //todayevent
            Intent i= MainActivity.newIntentx(Activity_PlanDetails.this,3);
            startActivity(i);
        }
        else if(Flag==4)
        {
            //completedplan
            Intent i= MainActivity.newIntentx(Activity_PlanDetails.this,4);
            startActivity(i);
        }
    }
}
