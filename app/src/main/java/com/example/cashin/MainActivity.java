package com.example.cashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    TextView textView;
    CircleImageView circleImageView;

    ///my splash screen>>..................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView=(TextView) findViewById(R.id.TV);
        circleImageView=(CircleImageView) findViewById(R.id.IV);

        Animation animation=AnimationUtils.loadAnimation(this,R.anim.mytransation);

        final Intent i=new Intent(this, EntranceActivity.class);

        textView.startAnimation(animation);
        circleImageView.startAnimation(animation);

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
}
