package chetu.felixpat.letsply.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import chetu.felixpat.letsply.MainActivity;
import chetu.felixpat.letsply.R;

public class ResetPasswordFragment extends Fragment {
    View view;
    EditText emailId;
    String checkEmailId;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button resetPassword;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        resetPassword = view.findViewById(R.id.fragment_reset_password_resetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userNames");

        emailId = view.findViewById(R.id.fragment_reset_password_emailId);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailId = emailId.getText().toString().trim();
                sendMailToResetPassword();
            }
        });
        return view;
    }

    public void sendMailToResetPassword() {
        if (!validateUserEnteredDetails()) {
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(checkEmailId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(),"Reset link is sent to your mail", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
                else {
                    Toast.makeText(getContext(), "EmailId doesnot exists",Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    public boolean validateUserEnteredDetails() {
        boolean valid = true;

        if ((checkEmailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(checkEmailId).matches()) && valid == true) {
            emailId.setError("enter a valid email address");
            valid = false;
        } else {
            emailId.setError(null);
        }

        return valid;
    }

}


