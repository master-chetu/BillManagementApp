package chetu.felixpat.letsply.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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



public class SignUpFragment extends Fragment {

    View view;
    EditText userName, emailId, password, confirmPassword;
    String checkUserName, checkEmailId, checkPassword, checkConfirmPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button signUp;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUp = view.findViewById(R.id.fragment_sign_up_signUp);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("userNames");

        userName = view.findViewById(R.id.fragment_sign_up_userName);
        emailId = view.findViewById(R.id.fragment_sign_up_email);
        password = view.findViewById(R.id.fragment_sign_up_password);
        confirmPassword = view.findViewById(R.id.fragment_sign_up_confirm_password);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserName = userName.getText().toString().trim();
                checkEmailId = emailId.getText().toString().trim();
                checkPassword = password.getText().toString().trim();
                checkConfirmPassword = confirmPassword.getText().toString().trim();
                signUp();
            }
        });
        return view;
    }

    public void signUp() {
        if (!validateUserEnteredDetails()) {
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(checkUserName).exists()){
                    Toast.makeText(getContext(), "username already exists!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(checkEmailId, checkPassword)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Email already used.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        DatabaseReference currentUser = databaseReference.child(checkUserName);
                                        currentUser.child("EmailId").setValue(checkEmailId);
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                }
                            });
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

        if ((checkEmailId.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(checkEmailId).matches()) && valid == true) {
            emailId.setError("enter a valid email address");
            valid = false;
        } else {
            emailId.setError(null);
        }

        if ((checkPassword.isEmpty() || checkPassword.length() < 8) && valid == true ) {
            password.setError("more than 8 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if ((checkConfirmPassword.isEmpty() || !checkPassword.equals(checkConfirmPassword)) && valid == true ) {
            confirmPassword.setError("password mismatch");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        return valid;
    }

}
