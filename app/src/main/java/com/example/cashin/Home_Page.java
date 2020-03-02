package com.example.cashin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Page extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    Class fragmentclass;
    public static Fragment fragment;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Home");

        //sending data to my fragment
        //HomeFragment fragment2 = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.HomeFragment);
        //fragment2.setArguments(getIntent().getExtras());


        fragmentclass=HomeFragment.class;
        try {
            fragment=(Fragment) fragmentclass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().setTransition(R.anim.mytransation).replace(R.id.Menu_Frame,fragment).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.About_Cashin:
                Toast toast= Toast.makeText(getApplicationContext(),"About Cash in",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                showAbout();
                return true;
            case R.id.settings:
                Toast.makeText(Home_Page.this,"coming soon",Toast.LENGTH_LONG).show();
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
    public void openLink(final View view) {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(Home_Page.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut()
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(view.getContext(),Nav.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home_Page.this,"Sign Out Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentCommunicationListener {
        void onNameChange(String name);

        void onEmailChange(String email);

    }
}
