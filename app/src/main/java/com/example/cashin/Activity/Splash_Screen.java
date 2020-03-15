package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.BuildConfig;
import com.example.cashin.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        Animation anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        Thread timer=new Thread(){
            @Override
            public void run() {
                //check for updates
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(getString(R.string.updateUrl))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();

                        Splash_Screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progBr.setVisibility(View.INVISIBLE);
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Experienced an error while loading data...",
                                        Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(), Splash_Screen.class));
                                    }
                                });
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));

                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(Color.parseColor("#003c8f"));

                                snackbar.show();

                            }
                        });
                    }
                    
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final String myResponse = response.body().string();

                        Splash_Screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //convertion of response to json to fetch value
                                    JSONObject jsonObj = new JSONObject(myResponse);
                                    String name = jsonObj.getString("versionCode");
                                    JSONObject jsonObj1 = new JSONObject(name);
                                    String name2 = jsonObj1.getString("VersionCode");
                                    float versionCode=Float.parseFloat(name2);

                                    //get the versionCode
                                    //check if the app is upto date
                                    float AppversionCode = BuildConfig.VERSION_CODE;
                                    if (versionCode != AppversionCode)
                                    {
                                        //start the update activity
                                        startActivity(new Intent(getApplicationContext(),UpdatePage.class));
                                    }
                                    else
                                    {
                                        startActivity(i);
                                        finish();
                                    }

                                }catch (JSONException err){
                                    Log.d("Error", err.toString());
                                    Toast.makeText(getApplicationContext(),err.toString(),Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }
                });
            }
        };
        timer.start();


    }
    //check netconnection
}
