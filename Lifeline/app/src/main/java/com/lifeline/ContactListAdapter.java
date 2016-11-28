package com.lifeline;

/**
 * Created by sanya on 10/11/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>  {
    String email;
    Context context;
    DBEmergency db;
    int layoutResourceId;
    ArrayList<EmerContact> data=new ArrayList<EmerContact>();
    public ContactListAdapter(ArrayList<EmerContact> data, int layoutResourceId, Context context,String email) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.email=email;
        db=new DBEmergency(context);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResourceId, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EmerContact contact=data.get(position);
        holder.textName.setText(contact._name);
        holder.textContact.setText(contact._phone);

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textName;
        public TextView textContact;
        public TextView btnEdit,btnDelete;

        public ViewHolder(View row) {
            super(row);
            textName=(TextView)row.findViewById(R.id.cont_name);
            textContact=(TextView)row.findViewById(R.id.cont_number);
            btnEdit=(TextView)row.findViewById(R.id.btn_edit);
            btnDelete=(TextView)row.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_edit)
            {
                Intent intent=new Intent(context,Edit_ContactActivity.class);
                Bundle b=new Bundle();
                b.putString("Name",textName.getText().toString());
                b.putString("Phone",textContact.getText().toString());
                b.putInt("ContactIndex", getPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(b);
                context.startActivity(intent);
            }
            else if(v.getId()==R.id.btn_delete)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = getPosition() + 1;
                                db.updatecontact("cont" + position, "NULL", email);
                                removeAt(getPosition());
                            }
                        }).setNegativeButton("No", null).show();
                builder.create();

            }


        }
        private void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(getPosition(), data.size());
        }
    }


    /*
     to store images efficiently in android
     retrieving is done in backgroud so as to avoid UI to slow down
     */


}



