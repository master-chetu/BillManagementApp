package chetu.felixpat.letsply;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import chetu.felixpat.letsply.adapter.plyAdapter;
import chetu.felixpat.letsply.constants.Constants;
import chetu.felixpat.letsply.model.BillingDetails;
import chetu.felixpat.letsply.model.PlyDetails;

public class PlyActivity extends AppCompatActivity {
    private boolean exit = false;
    private List<PlyDetails> list,list1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ply);





    }



    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity();

        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout)
        {
            List<BillingDetails> billList = BillingDetails.findWithQuery(BillingDetails.class,"SELECT * FROM Billing_Details");
            for(BillingDetails listItem:billList){
                listItem.delete();
            }
            List<PlyDetails> plyList = PlyDetails.findWithQuery(PlyDetails.class,"SELECT * FROM Ply_Details");
            for(PlyDetails listItem1:plyList){
                listItem1.delete();
            }
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(PlyActivity.this,MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RelativeLayout add = findViewById(R.id.ply_activity_add);
        ListView listView = findViewById(R.id.ply_activity_listview);
        plyAdapter adapter;


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),HolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME,Constants.FRAGMENT_ADD_DETAILS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        list1 = PlyDetails.findWithQuery(PlyDetails.class,"SELECT * FROM Ply_Details");

        if(list1.size()==0){
            final DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bills");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {

                            BillingDetails billingDetails = item.getValue(BillingDetails.class);
                            billingDetails.save();
                            Log.e("bill","yess");

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final DatabaseReference databaseReferenceply;
            databaseReferenceply = FirebaseDatabase.getInstance().getReference("Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Ply");
            databaseReferenceply.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {

                            PlyDetails plyDetails = item.getValue(PlyDetails.class);
                            plyDetails.save();
                            list1.add(plyDetails);

                            Log.e("ply","yess");


                        }
                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }




        list = PlyDetails.findWithQuery(PlyDetails.class,"SELECT * FROM Ply_Details");
        if(list.size()==0){
            adapter = new plyAdapter(getBaseContext(),R.layout.list_view_card,list1);
            listView.setAdapter(adapter);
        }
        else {

            adapter = new plyAdapter(getBaseContext(),R.layout.list_view_card,list);
            listView.setAdapter(adapter);
        }




    }
}
