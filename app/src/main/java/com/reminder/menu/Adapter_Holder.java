package com.reminder.menu;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import com.reminder.R;
import java.util.ArrayList;

/**
 * Created by admin on 25-03-2017.
 */

public class Adapter_Holder extends RecyclerView.Adapter<Adapter_Holder.RecyclerViewHolder> {
    ArrayList<Model_Holder> arrayList = new ArrayList<>();
    Model_Holder ddd;
    SQLClass db;
    static int main_id;

    public static void setId(int id) {
        main_id = id;
    }

    public Adapter_Holder(ArrayList<Model_Holder> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public Adapter_Holder.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_list, parent, false);
        Adapter_Holder.RecyclerViewHolder recyclerViewHolder = new Adapter_Holder.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_Holder.RecyclerViewHolder holder, int position) {
        Model_Holder dbm = arrayList.get(position);
        ddd = dbm;
        holder.name.setText(dbm.getHolder());
        holder.age.setText(String.valueOf(dbm.getAge()));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView age, name;
        ImageView delete;
        android.support.v7.app.AlertDialog.Builder a_builder;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            age = (TextView) itemView.findViewById(R.id.textview_holder_age);
            name = (TextView) itemView.findViewById(R.id.textview_holder_name);
            delete = (ImageView) itemView.findViewById(R.id.holder_delete);
            delete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            db = new SQLClass(view.getContext());
                            a_builder = new AlertDialog.Builder(view.getContext());
                            a_builder.setMessage("This holder may associated with some Investment Plan Are you sure you want to Delete")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int pos = getAdapterPosition();
                                            Model_Holder tx = arrayList.get(pos);
                                            db.DeleteHolder(String.valueOf(main_id), String.valueOf(tx.getId()));
                                            Context context = view.getContext();
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            Fragment_AddHolder up = new Fragment_AddHolder();
                                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
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
                            android.support.v7.app.AlertDialog alert = a_builder.create();
                            alert.setTitle("Alert");
                            alert.show();
                        }
                    }
            );
            //itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ddd = arrayList.get(pos);

            Intent i=Activity_PlanDetails.newIntent(v.getContext(),ddd.getId());
            v.getContext().startActivity(i);
        }*/
    }
}
