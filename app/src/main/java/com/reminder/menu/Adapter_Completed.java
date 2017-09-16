package com.reminder.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.reminder.R;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 26-03-2017.
 */

public class Adapter_Completed extends RecyclerView.Adapter<Adapter_Completed.RecyclerViewHolder> {

    ArrayList<Model_Completed> arrayList = new ArrayList<>();
    Model_Completed ddd;
    SQLClass db;
    static int main_id,Mode;
    public static void setId(int id)
    {
        main_id=id;
    }

    public Adapter_Completed(ArrayList<Model_Completed> arrayList, int mode) {
        this.arrayList = arrayList;
        Mode=mode;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_completed_plan, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Model_Completed dbm=arrayList.get(position);
        ddd=dbm;
        if(ddd.getInvestment_Plan().toLowerCase().equals("mediclaim"))
        {
            holder.Photo1.setImageResource(R.drawable.mediclaim);
        }
        else if(ddd.getInvestment_Plan().toLowerCase().equals("lic"))
        {
            holder.Photo1.setImageResource(R.drawable.lic);
        }
        else if(ddd.getInvestment_Plan().toLowerCase().equals("pf"))
        {
            holder.Photo1.setImageResource(R.drawable.pf);
        }
        else
        {
            holder.Photo1.setImageResource(R.drawable.user);
        }

        holder.name.setText(dbm.getName());
        holder.policy.setText(dbm.getPolicy_No());
        holder.investment.setText(dbm.getInvestment_Plan());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView policy,name,investment;
        ImageView delete;
        CircleImageView Photo1;
        android.support.v7.app.AlertDialog.Builder a_builder;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            Photo1 = (CircleImageView)itemView.findViewById(R.id.completed_PolicyPic);
            policy = (TextView) itemView.findViewById(R.id.completed_textView_no);
            investment=(TextView) itemView.findViewById(R.id.completed_textView_type);
            name = (TextView) itemView.findViewById(R.id.completed_textView_name);
            delete=(ImageView)itemView.findViewById(R.id.completed_imageView3);
            if(Mode==2)
            {
                delete.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                db=new SQLClass(view.getContext());
                                a_builder=new AlertDialog.Builder(view.getContext());
                                a_builder.setMessage("Are you sure you want to Delete")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int pos=getAdapterPosition();
                                                Model_Completed tx=arrayList.get(pos);
                                                db.deleteCompletedPlan(String.valueOf(main_id),String.valueOf(tx.getId()));
                                                db.DeleteData(String.valueOf(main_id),String.valueOf(tx.getId()));
                                                Context context=view.getContext();
                                                Fragment_CompletedPlan up=new Fragment_CompletedPlan();
                                                FragmentManager fragmentManager =((AppCompatActivity)context).getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.containerView, up);
                                                fragmentTransaction.commit();
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
            }
            else
            {
                delete.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ddd = arrayList.get(pos);
            if(Mode==1)
            {
                //todayevent
                Intent i= Activity_PlanDetails.newIntent(v.getContext(),ddd.getId(),3);
                v.getContext().startActivity(i);
            }
            else
            {
                Intent i= Activity_PlanDetails.newIntent(v.getContext(),ddd.getId(),4);
                v.getContext().startActivity(i);
            }
        }
    }
}
