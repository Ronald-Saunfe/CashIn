package com.example.cashin.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class investEarnFrag_1_1 extends Fragment {
    //backbutton on toolbar
    private ImageButton btnBackInvestEarn1_1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //backbutton on toolbar
        btnBackInvestEarn1_1 = view.findViewById(R.id.btnBackInvestEarn1_1);

        //return to main menu invest and earn
        btnBackInvestEarn1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                investEarnFrag_1 frag = new investEarnFrag_1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invest_earn_frag_1_1, container, false);
    }
}
