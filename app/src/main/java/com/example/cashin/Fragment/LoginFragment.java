package com.example.cashin.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.R;
public class LoginFragment extends Fragment {


    EditText myusername, myemail, mypassword, myconfirmpass;
    Button Login,forgottenpass,NoAc;
    TextView error;
    private ProgressBar progressBar;
    private ImageButton btnBackLogin;

    private OnFragmentInteractionListener mListener;

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

        //navigate back
        btnBackLogin = (ImageButton) view.findViewById(R.id.btnBackLogin);
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntranceFragment newfrag= new EntranceFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });
        Login=(Button) view.findViewById(R.id.signin);
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



            }
        });



        return view;
    }

    public interface MyInterface{

        public void setResult(String s);
    }

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