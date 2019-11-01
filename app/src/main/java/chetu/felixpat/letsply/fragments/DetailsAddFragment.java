package chetu.felixpat.letsply.fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import chetu.felixpat.letsply.PlyActivity;
import chetu.felixpat.letsply.R;
import chetu.felixpat.letsply.adapter.AddAdapter;
import chetu.felixpat.letsply.constants.Constants;
import chetu.felixpat.letsply.model.BillingDetails;
import chetu.felixpat.letsply.model.BillingDetailsStorage;
import chetu.felixpat.letsply.model.PlyDetails;
import chetu.felixpat.letsply.model.PlyDetailsStorage;

import static android.content.Context.MODE_PRIVATE;

public class DetailsAddFragment extends Fragment {


    View view;
    EditText setDate,name,bill,amount,paid;
    Button add,done;
    ListView listView;
    List<BillingDetails> list;
    Boolean status;
    DatabaseReference databaseReference;
    BillingDetailsStorage billingDetailsStorage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_details_add, container, false);


        databaseReference = FirebaseDatabase.getInstance().getReference("Details");
        billingDetailsStorage = new BillingDetailsStorage();

        final String nameToSearch = getArguments().getString("name");
        int position = getArguments().getInt("position");
        list = new ArrayList<>();
        setDate = view.findViewById(R.id.fragment_details_add_date);
        name = view.findViewById(R.id.fragment_details_add_name);
        bill = view.findViewById(R.id.fragment_details_add_bill);
        amount = view.findViewById(R.id.fragment_details_add_amount);
        paid = view.findViewById(R.id.fragment_details_add_paid);
        add = view.findViewById(R.id.fragment_details_add_add);
        done = view.findViewById(R.id.fragment_details_add_done);
        listView = view.findViewById(R.id.fragment_details_add_listview);


        if(nameToSearch!=null){
              name.setText(nameToSearch);
              name.setEnabled(false);
              bill.requestFocus();
              status = true;

              List<BillingDetails> billList = BillingDetails.findWithQuery(BillingDetails.class,"SELECT * FROM Billing_Details");
              for(BillingDetails item:billList){
                  if(item.getName().equals(nameToSearch))
                      list.add(item);
              }
              addList();
        }
        else
            status = false;



        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        setDate.setText(sdf.format(calendar.getTime()));


                    }

                };
                new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();



            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               setCard();

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCard();
                getActivity().finish();
                int amountCalc = 0, toBePaidCalc = 0;
                for (BillingDetails item : list) {
                    amountCalc += Integer.parseInt(item.getAmount());
                    toBePaidCalc += Integer.parseInt(item.getToBePaid());
                }

                if(!status) {

                    PlyDetails plyDetails = new PlyDetails();
                    plyDetails.setName(list.get(0).getName());
                    plyDetails.setBillCount(Integer.toString(list.size()));
                    plyDetails.setAmount(Integer.toString(amountCalc));
                    plyDetails.setToBePaid(Integer.toString(toBePaidCalc));

                    plyDetails.save();

                    final PlyDetailsStorage plyDetailsStorage = new PlyDetailsStorage();
                    plyDetailsStorage.setName(list.get(0).getName());
                    plyDetailsStorage.setBillCount(Integer.toString(list.size()));
                    plyDetailsStorage.setAmount(Integer.toString(amountCalc));
                    plyDetailsStorage.setToBePaid(Integer.toString(toBePaidCalc));

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference user = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Ply");
                            user.push().setValue(plyDetailsStorage);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                else {
                    List<PlyDetails> listPly = PlyDetails.findWithQuery(PlyDetails.class,"SELECT * FROM Ply_Details");
                    for(PlyDetails item:listPly){
                        if(item.getName().equals(nameToSearch)){
                            item.setBillCount(Integer.toString(list.size()));
                            item.setAmount(Integer.toString(amountCalc));
                            item.setToBePaid(Integer.toString(toBePaidCalc));
                            item.save();

                        }
                    }
                    final DatabaseReference databaseReference;
                    databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Ply");
                    final int finalAmountCalc = amountCalc;
                    final int finalToBePaidCalc = toBePaidCalc;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                for (DataSnapshot item : dataSnapshot.getChildren()) {

                                    PlyDetailsStorage details = item.getValue(PlyDetailsStorage.class);
                                    if(details.getName().equals(nameToSearch)){
                                        details.setBillCount(Integer.toString(list.size()));
                                        details.setAmount(Integer.toString(finalAmountCalc));
                                        details.setToBePaid(Integer.toString(finalToBePaidCalc));

                                        databaseReference.child(item.getKey()).setValue(details);
                                    }


                                }
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }



            }
        });


        return view;
    }

    private void setCard() {

        if(name.getText().length()>0&&bill.getText().length()>0&&setDate.getText().length()>0&&amount.getText().length()>0&&paid.getText().length()>0) {

            final BillingDetails details = new BillingDetails();
            details.setName(name.getText().toString().trim());
            details.setBillNo(bill.getText().toString().trim());
            details.setDate(setDate.getText().toString().trim());
            details.setAmount(amount.getText().toString().trim());
            details.setAmountPaid(paid.getText().toString().trim());
            details.setToBePaid(Integer.toString(Integer.parseInt(amount.getText().toString().trim()) - Integer.parseInt(paid.getText().toString().trim())));

            details.save();

            billingDetailsStorage.setName(details.getName());
            billingDetailsStorage.setBillNo(details.getBillNo());
            billingDetailsStorage.setDate(details.getDate());
            billingDetailsStorage.setAmount(details.getAmount());
            billingDetailsStorage.setAmountPaid(details.getAmountPaid());
            billingDetailsStorage.setToBePaid(details.getToBePaid());


            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference user = databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bills");
                    user.push().setValue(billingDetailsStorage);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            list.add(details);



            name.setEnabled(false);
            bill.setText("");
            setDate.setText("");
            amount.setText("");
            paid.setText("");

            bill.requestFocus();


            addList();
        }



    }

    private void addList() {

        if(list.size()!=0) {
            AddAdapter addAdapter = new AddAdapter(getActivity(), R.layout.add_card, list);

            addAdapter.notifyDataSetChanged();

            listView.setAdapter(addAdapter);
        }
    }

}
