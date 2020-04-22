package com.example.cashin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cashin.Fragment.EntranceFragment;
import com.example.cashin.Fragment.LoginFragment;
import com.example.cashin.Fragment.SignUpFragment;
import com.example.cashin.R;


public class Entrance_Page extends AppCompatActivity implements EntranceFragment.onNavigateClick {

    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance__page);

        EntranceFragment.setonNavigateClick(this);

        //add the entrance fragment
        Fragment fragment = new EntranceFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fmlEmtrance, fragment);
        transaction.commit();
    }

    @Override
    public void onLogin() {
        Fragment fragment = new LoginFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fmlEmtrance, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onRegistration() {
        Fragment fragment = new SignUpFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fmlEmtrance, fragment).addToBackStack(null).commit();
    }
}
