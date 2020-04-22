package com.example.cashin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.BuildConfig;
import com.example.cashin.GlobalVariable;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Splash_Screen extends AppCompatActivity implements OkHTTPhandler.onresponseLinstener {

    //views to be animated
    private ImageView imgLogo;
    private TextView subtitleLogo;
    private TextView titleLogo;
    private ProgressBar progBr;

    //animation object for animating views
    private Animation animfadein;
    private Animation animslideup;
    private Animation animSlidedown;

    ///my splash screen>>..................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        OkHTTPhandler.setOnResponseListener(this);

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

        //start app
        OkHTTPhandler.makepayload(null,
                getString(R.string.updateUrl),
                0,
                progBr,
                this,
                null);
    }
    @Override
    public void onResponse(JSONObject response, int id) {
        final Intent i=new Intent(this, Entrance_Page.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        if (id==0){
            try{
                String name = response.getString("versionCode");
                JSONObject jsonObj1 = new JSONObject(name);
                String name2 = jsonObj1.getString("VersionCode");
                float versionCode=Float.parseFloat(name2);

                //get the versionCode
                //check if the app is upto date
                float AppversionCode = BuildConfig.VERSION_CODE;
                if (versionCode != AppversionCode)
                {
                    //start the update activity
                    Intent intt = new  Intent(getApplicationContext(),UpdatePage.class);
                    intt.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intt);
                }
                else
                {
                    startActivity(i);
                    finish();
                }
            }
            catch (JSONException err){
            }
        }

    }

}
