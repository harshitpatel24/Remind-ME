package com.reminder.menu;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.reminder.R;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 14-03-2017.
 */

public class Fragment_TodayEvent extends Fragment {

    SQLClass db;
    ArrayList<Model_Completed> arr_list=new ArrayList<>();
    RecyclerView mRes;
    Model_Completed cm;
    Adapter_Completed mAdapter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_event, null);
        mRes=(RecyclerView) view.findViewById(R.id.today_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRes.setLayoutManager(mLayoutManager);
        db=new SQLClass(getContext());
        Cursor data=db.ViewSnoozeDate(String.valueOf(main_id));
        Cursor cc=db.viewAllData(String.valueOf(main_id));
        cc.moveToFirst();
        int chk=0,day,month,year;
        Calendar cal;

        if(data.getCount()!=0) {
            cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);

            data.moveToFirst();

            do {
                if (data.getInt(2) == day && data.getInt(3) == (month + 1) && data.getInt(4) == year) {
                    chk++;
                    int ln = cc.getCount();
                    while (ln > 0) {
                        if (cc.getInt(1) == data.getInt(1)) {
                            cm=new Model_Completed(cc.getInt(1),cc.getString(2),cc.getString(6),cc.getString(5));
                            arr_list.add(cm);
                        }
                        ln--;
                        cc.moveToNext();
                    }
                }
            } while (data.moveToNext());

        }
        data = db.viewPendingDate(String.valueOf(main_id));
            if(data.getCount()!=0) {
                cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);

                data.moveToFirst();

                do {
                    cc.moveToFirst();
                    int id=data.getInt(1);
                    Cursor cz=db.viewSingleData(String.valueOf(main_id),String.valueOf(id));
                    cz.moveToFirst();
                    String x=cz.getString(15);
                    String sep[]=x.split(" ");
                    int xz=Integer.valueOf(sep[0]);
                    if (data.getInt(2)-xz == day && data.getInt(3) == (month + 1) && data.getInt(4) == year) {
                        chk++;
                        int ln = cc.getCount();
                        while (ln > 0) {
                            if (cc.getInt(1) == id) {
                                cm=new Model_Completed(cc.getInt(1),cc.getString(2),cc.getString(6),cc.getString(5));
                                arr_list.add(cm);
                            }
                            ln--;
                            cc.moveToNext();
                        }
                    }
                } while (data.moveToNext());
            }



        if(chk==0)
        {
            TextView text=(TextView) view.findViewById(R.id.today_completed);
            text.setText("Today, There is nothing that you have to do");
        }
        mAdapter=new Adapter_Completed(arr_list,1);
        mRes.setAdapter(mAdapter);
        return view;
    }
}
