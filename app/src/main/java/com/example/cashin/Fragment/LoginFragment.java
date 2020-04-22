package com.example.cashin.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.GlobalVariable;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment implements OkHTTPhandler.onresponseLinstener{


    EditText myemail, mypassword;
    Button Login,forgottenpass,NoAc;
    private ProgressBar progressBar;
    private ImageButton btnBackLogin;
    private boolean onGoingLoading=false;
    private TextInputLayout LLEmail,LLpassword;
    private int RegBtnInitWidth;
    private int Rertycount = 0;
    private String token;
    HashMap<String,String> payload = new HashMap<String,String>();


    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater
                .from(getContext())
                .inflateTransition(android.R.transition.move));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegBtnInitWidth = Login.getWidth();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        OkHTTPhandler.setOnResponseListener(this);
        myemail = (EditText) view.findViewById(R.id.LEmail);
        mypassword = (EditText) view.findViewById(R.id.Lpassword);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarlogin);
        Login = (Button) view.findViewById(R.id.signin);
        NoAc = (Button) view.findViewById(R.id.donthaveac);
        forgottenpass=(Button) view.findViewById(R.id.forgottenpass);

        LLpassword = view.findViewById(R.id.LLpassword);
        LLEmail= view.findViewById(R.id.LLEmail);

        myemail = (EditText) view.findViewById(R.id.LEmail);

        //navigate back
        btnBackLogin = (ImageButton) view.findViewById(R.id.btnBackLogin);
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Login=(Button) view.findViewById(R.id.signin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return view;
    }

    private void AnimateRegBtn(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Login.setText("");
                Login.setEnabled(false);
                Login.setEnabled(false);
                onGoingLoading = true;
                //animate the register account button to display loading progress
                ValueAnimator valueAnimator = ValueAnimator.ofInt(RegBtnInitWidth, 150);
                valueAnimator.setInterpolator(new AnticipateOvershootInterpolator()); // increase the speed first and then decrease
                valueAnimator.setDuration(1000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = Login.getLayoutParams();
                        layoutParams.width = val;
                        Login.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void AnimateRegBtnRev(){
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                progressBar.setVisibility(View.INVISIBLE);
                //animate the register account button to display loading progress
                ValueAnimator valueAnimator = ValueAnimator.ofInt(150, RegBtnInitWidth);
                valueAnimator.setInterpolator(new AnticipateOvershootInterpolator()); // increase the speed first and then decrease
                valueAnimator.setDuration(1000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = Login.getLayoutParams();
                        layoutParams.width = val;
                        Login.setLayoutParams(layoutParams);
                    }
                });

                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        Login.setText("SIGN IN");
                        Login.setEnabled(true);
                        Login.setEnabled(true);
                        onGoingLoading=false;
                    }
                });
                valueAnimator.start();
            }});

    }

    private void login(){
        AnimateRegBtn();
        final String email, password;
        email = myemail.getText().toString();
        password = mypassword.getText().toString();

        LLpassword.setError(null);
        LLEmail.setError(null);

        payload.put("email",email);
        payload.put("password",password);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/Login",
                0,
                progressBar,
                null,
                this);
    }

    private void getTokenFCM(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Intent i = new Intent(getContext(), Navigation_Main.class);
                            startActivity(i);
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Rertycount=0;
                        updateTokenFCM();
                    }
                });
    }

    private void updateTokenFCM(){
        final String email, password;
        email = myemail.getText().toString();
        password = mypassword.getText().toString();

        LLpassword.setError(null);
        LLEmail.setError(null);
        payload.clear();
        payload.put("email",GlobalVariable.GlobEmail);
        payload.put("token",token);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/UpdateFCMToken",
                1,
                progressBar,
                null,
                this);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try {
                String code = response.getString("code");

                final String Cod = code;
                final String username = response.getString("Username");
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        if (Cod.equals("100")){
                            if (onGoingLoading==true){AnimateRegBtnRev();}
                            LLpassword.setError("Password does not match");
                        }
                        else if(Cod.equals("101")){
                            if (onGoingLoading==true){AnimateRegBtnRev();}
                            LLEmail.setError("Email already exists");
                        }
                        else if(Cod.equals("200")){
                            GlobalVariable.GlobUsername = username;
                            GlobalVariable.GlobEmail=  myemail.getText().toString();
                            getTokenFCM();

                        }
                    }
                });
            }
            catch (JSONException e){}
        }
        else if(id==1){
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    Intent i = new Intent(getContext(), Navigation_Main.class);
                    startActivity(i);
                }
            });
        }

    }
}