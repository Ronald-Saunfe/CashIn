package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.cashin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Splash_Screen extends AppCompatActivity{

    TextView textView;
    CircleImageView circleImageView;

    ///my splash screen>>..................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);



        textView=(TextView) findViewById(R.id.TV);
        circleImageView=(CircleImageView) findViewById(R.id.IV);

        Animation animation=AnimationUtils.loadAnimation(this,R.anim.mytransation);

        final Intent i=new Intent(this, Entrance_Page.class);

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
    //check netconnection
}
