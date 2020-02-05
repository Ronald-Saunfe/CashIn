package com.example.cashin;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {
    private  Context activity;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar mActionBar = getView().findViewById(R.id.toolbLogin);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (onfragComplete) activity).fragComplete("introPage");
            }
        });

        Button btnSignin = getView().findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((onfragComplete)activity).fragComplete("Menu");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = context;
    }

    public interface onfragComplete{
        public void fragComplete(String frag);
    }

}
