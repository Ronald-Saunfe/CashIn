package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cashin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Splash_Screen extends AppCompatActivity{

    //views to be animated
    private ImageView imgLogo;
    private TextView subtitleLogo;
    private TextView titleLogo;
    private ProgressBar progBr;

    //animation object for animating views
    private Animation animfadein;
    private Animation animslideup;
    private Animation animSlidedown;
    private Context activity;

    ///my splash screen>>..................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        //animate the logo
        //reference of views to be animated
        imgLogo = findViewById(R.id.logoIntro);
        titleLogo = findViewById(R.id.titleIntro);
        subtitleLogo = findViewById(R.id.subtitleIntro);
        progBr = findViewById(R.id.progressBarIntro);

        //declare animations
        animfadein = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade_in);
        animslideup = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.slide_up);
        animSlidedown =  AnimationUtils.loadAnimation( getApplicationContext(),R.anim.slide);
        animslideup.setDuration(800);
        animfadein.setDuration(800);
        animSlidedown.setDuration(800);
        //start animations
        //set the logo to 1.0 (alpha)
        imgLogo.startAnimation(animfadein);
        //translate the title and subtitle
        titleLogo.startAnimation(animslideup);
        subtitleLogo.startAnimation(animslideup);

        //show loading progressbar
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progBr.setVisibility(View.VISIBLE);
            }
        }, 1000);


        final Intent i=new Intent(this, Entrance_Page.class);

        Thread timer=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }

            }
        };
        timer.start();


    }
    //check netconnection
}
