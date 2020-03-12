package com.example.cashin.Fragment;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ProgressBar;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.R;
import com.spark.submitbutton.SubmitButton;

public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText myusername,myemail,mypassword,myconfirmpass;
    private Button RegisterAC;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;
    private ImageButton btnBackSignup;

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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        myusername=(EditText) view.findViewById(R.id.RUsername);
        myemail=(EditText) view.findViewById(R.id.Remail);
        mypassword=(EditText) view.findViewById(R.id.Rpassword);
        myconfirmpass=(EditText) view.findViewById(R.id.R_Confirm_password);

        RegisterAC=(Button) view.findViewById(R.id.btnRegister);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading. Please wait...");
        //back toolbar button
        btnBackSignup = view.findViewById(R.id.btnBackSignup);
        //back toolbar button go to entrance fragment
        btnBackSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntranceFragment newfrag= new EntranceFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });
        RegisterAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email,password,cpassword,username;

                email = myemail.getText().toString();
                password = mypassword.getText().toString();
                username = myusername.getText().toString();
                cpassword = myconfirmpass.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) &&TextUtils.isEmpty(username)
                        && TextUtils.isEmpty(cpassword)) {
                    myemail.setError(getString(R.string.prompt_email));
                    mypassword.setError(getString(R.string.prompt_password));
                    myconfirmpass.setError(getString(R.string.prompt_confirm_new_pass));
                    myusername.setError(getString(R.string.prompt_Username));
                    return;
                }
                if (password.length() <6){
                    mypassword.setError(getString(R.string.minimum_password));
                }
                if (!email.contains("@gmail.com")) {
                    myemail.setError(getString(R.string.email_should_have));
                    return;
                }

                //startActivity(new Intent(getContext(),Navigation_Main.class));
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
