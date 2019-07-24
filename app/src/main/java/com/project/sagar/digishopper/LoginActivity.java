package com.project.sagar.digishopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import fragment.LoginPageFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginPageFragment loginPageFragment=new LoginPageFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginContainer,loginPageFragment,LoginPageFragment.TAG)
                .commit();

    }
}
