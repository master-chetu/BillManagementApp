package chetu.felixpat.letsply.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import chetu.felixpat.letsply.HolderActivity;
import chetu.felixpat.letsply.PlyActivity;
import chetu.felixpat.letsply.R;
import chetu.felixpat.letsply.constants.Constants;
import chetu.felixpat.letsply.model.BillingDetails;
import chetu.felixpat.letsply.model.BillingDetailsStorage;
import chetu.felixpat.letsply.model.PlyDetails;
import chetu.felixpat.letsply.model.PlyDetailsStorage;

import static android.content.ContentValues.TAG;

/**
 * Created by Chetu on 07-02-2018.
 */

public class plyAdapter extends ArrayAdapter<PlyDetails> {

    Context context;
    List<PlyDetails> list;
    PlyDetails plyDetails;
    DatabaseReference databaseReference,databaseReference2;

    public plyAdapter(Context context, int resource, List<PlyDetails> object) {
        super(context, resource);
        this.context = context;
        this.list = object;
    }

    public static class ViewHolder {

        TextView name;
        TextView billCount;
        TextView amount;
        TextView tobePaid;
        TextView delete;
        TextView edit;

    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_card, parent, false);

            holder = new ViewHolder();


            holder.name = rowView.findViewById(R.id.list_view_card_name);
            holder.billCount = rowView.findViewById(R.id.list_view_card_bill_count);
            holder.amount = rowView.findViewById(R.id.list_view_card_amount);
            holder.tobePaid = rowView.findViewById(R.id.list_view_card_amount_pending);
            holder.delete = rowView.findViewById(R.id.list_view_card_delete);
            holder.edit = rowView.findViewById(R.id.list_view_card_edit);

            rowView.setTag(holder);

        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.name.setTag(position);
        holder.billCount.setTag(position);
        holder.amount.setTag(position);
        holder.tobePaid.setTag(position);
        holder.delete.setTag(position);
        holder.edit.setTag(position);


        plyDetails = list.get(position);

        holder.name.setText(plyDetails.getName());
        holder.billCount.setText(plyDetails.getBillCount());
        holder.amount.setText(plyDetails.getAmount());
        holder.tobePaid.setText(plyDetails.getToBePaid());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
                builder.setMessage("Do You want to Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                PlyDetails details = list.get(position);
                                final String name = list.get(position).getName();




                                List<BillingDetails> billList = BillingDetails.findWithQuery(BillingDetails.class,"SELECT * FROM Billing_Details");
                                for(BillingDetails item:billList){
                                    if(item.getName().equals(list.get(position).getName()))
                                    {
                                        item.delete();
                                    }


                                }


                                databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Ply");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for(DataSnapshot item:dataSnapshot.getChildren()){

                                            PlyDetailsStorage details = item.getValue(PlyDetailsStorage.class);
                                            if(details.getName().equals(name)){
                                                databaseReference.child(item.getKey()).removeValue();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference2 = FirebaseDatabase.getInstance().getReference("Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bills");
                                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for(DataSnapshot item:dataSnapshot.getChildren()){

                                            BillingDetailsStorage details = item.getValue(BillingDetailsStorage.class);
                                            if(details.getName().equals(name)){
                                                databaseReference2.child(item.getKey()).removeValue();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });








                                details.delete();
                                list.remove(position);

                                notifyDataSetChanged();



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();


            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,HolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME,Constants.FRAGMENT_VIEW);
                bundle.putString("name",list.get(position).getName().toString());
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return rowView;
    }


    @Override
    public PlyDetails getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
