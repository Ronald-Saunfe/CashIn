package com.example.cashin.Fragment;


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

import com.example.cashin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class invetEarnFrag_2 extends Fragment {

    //backbutton on toolbar
    private ImageButton btnBackInvestEarn2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //backbutton on toolbar
        btnBackInvestEarn2 = view.findViewById(R.id.btnBackInvestEarn2);

        //return to main menu invest and earn
        btnBackInvestEarn2.setOnClickListener(new View.OnClickListener() {
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
        return inflater.inflate(R.layout.fragment_invet_earn_frag_2, container, false);
    }

}
