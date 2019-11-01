package chetu.felixpat.letsply;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import chetu.felixpat.letsply.constants.Constants;
import chetu.felixpat.letsply.model.BillingDetails;
import chetu.felixpat.letsply.model.BillingDetailsStorage;
import chetu.felixpat.letsply.model.PlyDetails;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    EditText userName,password;
    String checkUserName, checkEmailId, checkPassword;
    ProgressDialog progressDialog;
    private boolean exit = false;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userNames");

        Button login = findViewById(R.id.activity_main_login);
        TextView signUp = findViewById(R.id.activity_main_signup);
        TextView resetPassword = findViewById(R.id.activity_main_resetPasswored);
        userName = findViewById(R.id.activity_main_userName);
        password = findViewById(R.id.activity_main_password);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),HolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME,Constants.FRAGMENT_SIGNUP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),HolderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FRAGMENT_NAME,Constants.FRAGMENT_RESET_PASSWORD);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserName = userName.getText().toString().trim();
                checkPassword = password.getText().toString().trim();
                signIn();

            }
        });



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mfirebaseAuth) {
                FirebaseUser user = mfirebaseAuth.getCurrentUser();
                if (user != null ) {

                    Intent intent = new Intent(MainActivity.this,PlyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FRAGMENT_NAME,Constants.ACTIVITY_LOG_IN);
                    intent.putExtras(bundle);

                    startActivity(intent);

                }
                else{

                }
            }
        };

    }

    private void syncDetails() {





    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public void signIn()
    {
        if (!validateUserEnteredDetails()) {
            return;
        }
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(checkUserName).exists())
                {
                    checkEmailId = (String) dataSnapshot.child(checkUserName).child("EmailId").getValue();
                    firebaseAuth.signInWithEmailAndPassword(checkEmailId, checkPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (!task.isSuccessful())
                            {
                                Toast.makeText(getBaseContext(),"Incorrect Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(getBaseContext(), "User does't exists!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean validateUserEnteredDetails() {
        boolean valid = true;
        if ((checkUserName.isEmpty() || checkUserName.length() < 3) && valid == true) {
            userName.setError("at least 3 characters");
            valid = false;
        } else {
            userName.setError(null);
        }

        if ((checkPassword.isEmpty()) && valid == true ) {
            password.setError("password cannot be empty");
            valid = false;
        } else {
            password.setError(null);
        }


        return valid;
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

}
