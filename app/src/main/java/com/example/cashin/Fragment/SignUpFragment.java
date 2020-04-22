package com.example.cashin.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.Activity.termsCondition;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpFragment extends Fragment implements OkHTTPhandler.onresponseLinstener{

    private EditText myusername, myemail, mypassword, myconfirmpass;
    private TextInputLayout RRUsername, RRemail, RRpassword, RR_Confirm_password;
    private Button RegisterAC;
    private ProgressBar pbSignUp;
    private ImageButton btnBackSignup;
    public String email, password, cpassword, username;
    private int RegBtnInitWidth;
    private CheckBox checkBox_tmc;
    private TextView txtChkSugnUp;
    private NestedScrollView nstSignup;
    private int Rertycount = 0;
    private String token;

    public static boolean onGoingLoading =false;

    HashMap<String,String> payload = new HashMap<String,String>();

    private List<String> GPSCorrdinates = new ArrayList<String>();


    public String postUrl = "https://us-central1-cashin-270220.cloudfunctions.net/signUp";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater
                .from(getContext())
                .inflateTransition(android.R.transition.move).setDuration(300));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegBtnInitWidth = RegisterAC.getWidth();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        OkHTTPhandler.setOnResponseListener(this);

        myusername=(EditText) view.findViewById(R.id.RUsername);
        myemail=(EditText) view.findViewById(R.id.Remail);
        mypassword=(EditText) view.findViewById(R.id.Rpassword);
        myconfirmpass=(EditText) view.findViewById(R.id.R_Confirm_password);
        nstSignup = (NestedScrollView) view.findViewById(R.id.nstSignup);

        //reference for textinput layouts
        RRUsername=view.findViewById(R.id.RRUsername);
        RRemail= view.findViewById(R.id.RRemail);
        RRpassword= view.findViewById(R.id.RRpassword);
        RR_Confirm_password = view.findViewById(R.id.RR_Confirm_password);

        pbSignUp = (ProgressBar) view.findViewById(R.id.pbSignUp);
        checkBox_tmc = (CheckBox) view.findViewById(R.id.checkBox_tmc);

        //set terms and condition part of textview clickable
        txtChkSugnUp = (TextView) view.findViewById(R.id.txtChkSugnUp);
        String myString = "I agree with the Terms and Conditions";
        int i1 = myString.indexOf("Terms");
        int i2 = myString.indexOf("Conditions");
        txtChkSugnUp.setMovementMethod(LinkMovementMethod.getInstance());
        txtChkSugnUp.setText(myString, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable)txtChkSugnUp.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(getContext(), termsCondition.class));
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RegisterAC=(Button) view.findViewById(R.id.btnRegister);
        //back toolbar button
        btnBackSignup = view.findViewById(R.id.btnBackSignup);
        //back toolbar button go to entrance fragment
        btnBackSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        RegisterAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
        }});

        return view;
    }

    private void AnimateRegBtn(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RegisterAC.setText("");
                RegisterAC.setEnabled(false);
                btnBackSignup.setEnabled(false);
                onGoingLoading = true;
                //animate the register account button to display loading progress
                ValueAnimator valueAnimator = ValueAnimator.ofInt(RegBtnInitWidth, 150);
                valueAnimator.setInterpolator(new AnticipateOvershootInterpolator()); // increase the speed first and then decrease
                valueAnimator.setDuration(1000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = RegisterAC.getLayoutParams();
                        layoutParams.width = val;
                        RegisterAC.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        pbSignUp.setVisibility(View.VISIBLE);
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
                pbSignUp.setVisibility(View.INVISIBLE);
                //animate the register account button to display loading progress
                ValueAnimator valueAnimator = ValueAnimator.ofInt(150, RegBtnInitWidth);
                valueAnimator.setInterpolator(new AnticipateOvershootInterpolator()); // increase the speed first and then decrease
                valueAnimator.setDuration(1000);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int val = (Integer) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = RegisterAC.getLayoutParams();
                        layoutParams.width = val;
                        RegisterAC.setLayoutParams(layoutParams);
                    }
                });

                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        RegisterAC.setText("REGISTER ACCOUNT");
                        RegisterAC.setEnabled(true);
                        btnBackSignup.setEnabled(true);
                        onGoingLoading=false;
                    }
                });
                valueAnimator.start();
            }});

    }

    private void SignUp(){

        email = myemail.getText().toString();
        password = mypassword.getText().toString();
        username = myusername.getText().toString();
        cpassword = myconfirmpass.getText().toString();

        RRUsername.setError(null);
        RRemail.setError(null);
        RRpassword.setError(null);
        RR_Confirm_password.setError(null);

        //check if the inputs contain values
        if (TextUtils.isEmpty(email)) {
            RRemail.setError("Enter email address");
        } else if (TextUtils.isEmpty(username)) {
            RRUsername.setError("Enter username");
        } else if (TextUtils.isEmpty(password)) {
            RRpassword.setError("Enter password");
        } else if (TextUtils.isEmpty(cpassword)) {
            RR_Confirm_password.setError("Enter confirm password");
        } else if (!checkBox_tmc.isChecked()){
            new AlertDialog.Builder(getContext())
                    .setMessage("Agree with the terms and conditions before proceeding")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkBox_tmc.setChecked(true);
                        }
                    })

                    .setNegativeButton("DISAGREE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkBox_tmc.setChecked(false);
                        }
                    })
                    .show();
        }
        else {
            AnimateRegBtn();
            payload.put("email", email);
            payload.put("Username", username);
            payload.put("Password", password);
            payload.put("confirmPassword", cpassword);

            OkHTTPhandler.makepayload(payload,
                    postUrl,
                    1,
                    pbSignUp,
                    null,
                    this);
        }
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
        payload.clear();
        payload.put("email", GlobalVariable.GlobEmail);
        payload.put("token", token);

        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/UpdateFCMToken",
                1,
                pbSignUp,
                null,
                this);
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0) {
            try {
                String code = response.getString("code");

                final String Cod = code;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Cod.equals("400")) {
                            if (onGoingLoading == true) {
                                AnimateRegBtnRev();
                            }
                            nstSignup.smoothScrollTo((int) RRpassword.getX(), (int) RRpassword.getY());
                            RRpassword.setError("Password does not match");
                        } else if (Cod.equals("555")) {
                            if (onGoingLoading == true) {
                                AnimateRegBtnRev();
                            }
                            nstSignup.smoothScrollTo((int) RRemail.getX(), (int) RRemail.getY());
                            RRemail.setError("Email already exists");
                        } else if (Cod.equals("200")) {
                            GlobalVariable.GlobUsername = myusername.getText().toString();
                            ;
                            GlobalVariable.GlobEmail = myemail.getText().toString();
                            getTokenFCM();
                        }
                    }
                });
            } catch (JSONException e) {

            }
        }
        else if(id==1) {
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
