package com.example.cashin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cashin.Fragment.HomeFragment;
import com.example.cashin.R;


import de.hdodenhof.circleimageview.CircleImageView;

public class Navigation_Main extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView username,email;
    private ImageView  profile_pic;

    Class fragmentclass;
    public static Fragment fragment;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__main);


        fragmentclass=HomeFragment.class;
        try {
            fragment=(Fragment) fragmentclass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().setTransition(R.anim.slide_up).replace(R.id.root_layout_nav,fragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
            case R.id.About_Cashin:
                Toast toast= Toast.makeText(getApplicationContext(),"About Cash in",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                showAbout();
                return true;
            case R.id.settings:
                Toast.makeText(Navigation_Main.this,"coming soon",Toast.LENGTH_LONG).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() {
        new AlertDialog.Builder(this)
                .setTitle("About Cash in")
                .setMessage("Cash in helps you to earn more.\n\n\n Version 1.0.1")
                .setCancelable(false)
                .setNegativeButton(android.R.string.yes, null)
                .create().show();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentCommunicationListener {
        void onNameChange(String name);

        void onEmailChange(String email);

    }
}
