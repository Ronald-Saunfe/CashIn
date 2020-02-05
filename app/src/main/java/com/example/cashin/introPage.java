package com.example.cashin;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */

public class introPage extends Fragment {
    private ImageView img;
    private TextView introName;
    private TextView introDesc;
    private ProgressBar prog;
    private Animation animation;
    private Animation animation1;
    private Animation animation2;
    private Animation animProg;
    private Context activity;

    public introPage() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_page, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        animation1 = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        animation2 = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        animProg = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        prog = getView().findViewById(R.id.introProgB);
        img= getView().findViewById(R.id.imgLogoIntro);
        introName= getView().findViewById(R.id.introAppName);
        introDesc= getView().findViewById(R.id.descLogoIntro);

        img.startAnimation(animation);
        animation1.setDuration(900);
        introName.startAnimation(animation1);
        //set on end animation listener
        animation2.setDuration(1000);
        introDesc.setAnimation(animation2);
        introDesc.startAnimation(animation2);
        animProg.setStartOffset(1000);
        prog.startAnimation(animProg);
        unshowIntro();
    }
    private void unshowIntro(){
        Animation animation3 = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        Animation animation4 = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        Animation animation5 = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);

        img.clearAnimation();
        introName.clearAnimation();
        introDesc.clearAnimation();

        img.startAnimation(animation3);
        animation3.setDuration(900);
        introName.startAnimation(animation4);
        animation4.setDuration(1000);
        introDesc.startAnimation(animation5);

        ((onFragmentComplete) activity).fragComplete("introPage");

    }



    public interface onFragmentComplete{
        public void fragComplete(String frag);
    }
}
