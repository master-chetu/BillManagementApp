package chetu.felixpat.letsply.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import chetu.felixpat.letsply.R;
import chetu.felixpat.letsply.model.BillingDetails;

/**
 * Created by Chetu on 09-02-2018.
 */

public class AddAdapter extends ArrayAdapter<BillingDetails> {

    Context context;
    List<BillingDetails> list;
    BillingDetails billingDetails;

    public AddAdapter(@NonNull Context context, int resource, @NonNull List<BillingDetails> object) {
        super(context, resource);
        this.context=context;
        this.list = object;
    }
    public static class ViewHolder{

        TextView bill;
        TextView date;
        TextView amount;
        TextView paid;
        TextView tobePaid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        View rowView = convertView;

        if(rowView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.add_card,parent,false);

            holder = new ViewHolder();

            holder.bill = rowView.findViewById(R.id.add_card_bill);
            holder.date = rowView.findViewById(R.id.add_card_date);
            holder.amount = rowView.findViewById(R.id.add_card_amount);
            holder.tobePaid = rowView.findViewById(R.id.add_card_tobePaid);
            holder.paid = rowView.findViewById(R.id.add_card_paid);

            rowView.setTag(holder);

        }
        else {
            holder =(ViewHolder) rowView.getTag();
        }

        holder.bill.setTag(position);
        holder.date.setTag(position);
        holder.amount.setTag(position);
        holder.tobePaid.setTag(position);
        holder.paid.setTag(position);


        billingDetails = list.get(position);

        holder.bill.setText(billingDetails.getBillNo());
        holder.date.setText(billingDetails.getDate());
        holder.amount.setText(billingDetails.getAmount());
        holder.tobePaid.setText(billingDetails.getToBePaid());
        holder.paid.setText(billingDetails.getAmountPaid());

        return rowView;
    }

    @Override
    public BillingDetails getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
