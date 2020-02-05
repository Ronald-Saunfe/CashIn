package com.example.cashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements introPage.onFragmentComplete,
        Login.onfragComplete, Entrance.ButtonCommand {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        introPage fragment = new introPage();
        fragmentTransaction.add(R.id.rootLayout, fragment);
        fragmentTransaction.commit();
    }

    public void fragComplete(String frag){
        //show fragment entrance
        if (frag.equals("introPage")){
            Entrance fragment = new Entrance();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rootLayout, fragment);
            fragmentTransaction.commit();
        }
        else if(frag.equals("Menu"))
        {
            Menu fragment = new Menu();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rootLayout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void BtnClicked(String btn){
        //show fragment login
        if (btn=="Login"){
            Login fragment = new Login();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rootLayout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }


}
