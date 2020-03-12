package com.example.cashin.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cashin.R;

import java.util.Objects;


public class ResetPassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button reset;
    EditText myemail;
    ProgressBar progressBar;
    String email;
    TextView error;

    public ResetPassFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reset_pass, container, false);
        myemail=(EditText) view.findViewById(R.id.reset_email);
        reset=(Button) view.findViewById(R.id.Update_new_pass);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBarUpdate);
        error=(TextView) view.findViewById(R.id.Error_Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = myemail.getText().toString();

                if (TextUtils.isEmpty(email)){
                    myemail.setError(getString(R.string.prompt_email));
                    return;
                }
                if (!email.contains("@gmail.com")) {
                    myemail.setError(getString(R.string.email_should_have));
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    private void showDialog() {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle("Unable to connect!")
                .setMessage(getString(R.string.unable_to_connect))
                .setCancelable(false)
                .setNegativeButton(android.R.string.yes, null)
                .create().show();
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
}
