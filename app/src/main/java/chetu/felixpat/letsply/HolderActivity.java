package chetu.felixpat.letsply;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import chetu.felixpat.letsply.constants.Constants;
import chetu.felixpat.letsply.fragments.DetailsAddFragment;
import chetu.felixpat.letsply.fragments.ResetPasswordFragment;
import chetu.felixpat.letsply.fragments.SignUpFragment;

public class HolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        final Bundle bundle = getIntent().getExtras();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (bundle.getString(Constants.FRAGMENT_NAME).equals(Constants.FRAGMENT_SIGNUP)) {
            SignUpFragment signUpFragment = new SignUpFragment();
            signUpFragment.setArguments(bundle);
            ft.replace(R.id.activity_holder, signUpFragment);
        }
        else if (bundle.getString(Constants.FRAGMENT_NAME).equals(Constants.FRAGMENT_ADD_DETAILS)) {
            DetailsAddFragment detailsAddFragment = new DetailsAddFragment();
            detailsAddFragment.setArguments(bundle);
            ft.replace(R.id.activity_holder, detailsAddFragment);
        }
        else if (bundle.getString(Constants.FRAGMENT_NAME).equals(Constants.FRAGMENT_RESET_PASSWORD)){
            ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
            resetPasswordFragment.setArguments(bundle);
            ft.replace(R.id.activity_holder, resetPasswordFragment);
        }
        else if (bundle.getString(Constants.FRAGMENT_NAME).equals(Constants.FRAGMENT_VIEW)){
            DetailsAddFragment detailsAddFragment = new DetailsAddFragment();
            detailsAddFragment.setArguments(bundle);
            ft.replace(R.id.activity_holder, detailsAddFragment);
        }

        ft.commit();
    }
}
