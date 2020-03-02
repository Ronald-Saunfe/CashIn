package com.example.cashin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthCredential;
import com.google.firebase.auth.GithubAuthProvider;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {



    public static final int GOOGLE_SIGN_IN_CODE = 0;


    EditText myusername, myemail, mypassword, myconfirmpass;
    Button Login,forgottenpass,NoAc;
    TextView error;
    SignInButton signInButton;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;


    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myemail = (EditText) view.findViewById(R.id.LEmail);
        myemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        myemail = (EditText) view.findViewById(R.id.LEmail);
        mypassword = (EditText) view.findViewById(R.id.Lpassword);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarlogin);
        Login = (Button) view.findViewById(R.id.signin);
        NoAc = (Button) view.findViewById(R.id.donthaveac);
        forgottenpass=(Button) view.findViewById(R.id.forgottenpass);
        signInButton=(SignInButton) view.findViewById(R.id.SignIn);
        error=(TextView) view.findViewById(R.id.Error);


        auth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken("991450820914-fnn0812r0snf02mdohnkmtgp8m0o5v6e.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), googleSignInOptions);

        GoogleSignInAccount googleSignInAccount=GoogleSignIn.getLastSignedInAccount(getActivity());
        if (googleSignInAccount !=null){
            Toast.makeText(getActivity(),"Welcome to CashIn Softs",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),Home_Page.class));
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent sign= googleSignInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });


        NoAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment newfrag=new SignUpFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });

        forgottenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassFragment newfrag=new ResetPassFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email, password;

                email = myemail.getText().toString();
                password = mypassword.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    myemail.setError(getString(R.string.prompt_email));
                    mypassword.setError(getString(R.string.prompt_password));
                    return;
                }

                if (password.length() <6){
                    Toast.makeText(getActivity(), "Password too short, enter minimum (6) characters!",
                            Toast.LENGTH_SHORT).show();

                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Activity) Objects.requireNonNull(getContext()), new OnCompleteListener<AuthResult>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    error.setText("Unable to connect to sever.Please check you internet connection!");

                                } else {

                                    Intent i=new Intent(getActivity(),Home_Page.class);
                                    startActivity(i);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setText("Your password or email is incorrect.");
                    }
                });
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAccount=signInAccountTask.getResult(ApiException.class);
                AuthCredential authCredential= GithubAuthProvider.getCredential(signInAccount.getIdToken());

                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),"Your Account is connected to CashIn Welcome!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),Home_Page.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }
    public interface MyInterface{

        public void setResult(String s);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    private void updateUI() {
    }
}