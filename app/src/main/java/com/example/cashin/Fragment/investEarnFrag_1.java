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

import com.example.cashin.Activity.InvestEarn;
import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.R;



public class investEarnFrag_1 extends Fragment {
    //navigation btn for next and skip
    private Button btnNextIEf;
    private Button btnSkipIEf;
    //back button in the toolbar
    private ImageButton btnBackInvestEarn1;

    public investEarnFrag_1() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //navigation btn for next and skip
        btnNextIEf = view.findViewById(R.id.btnNextIEf);
        btnSkipIEf = view.findViewById(R.id.btnSkipIEf);
        btnBackInvestEarn1 = view.findViewById(R.id.btnBackInvestEarn1);

        //onclick for next button
        //call fragment invest and earn2
        btnNextIEf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invetEarnFrag_2 frag = new invetEarnFrag_2();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //onclick for skip button
        btnSkipIEf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                investEarnFrag_1_1 frag = new investEarnFrag_1_1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rootLayoutInvestEarn, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //return to main menu activiy
        btnBackInvestEarn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Navigation_Main.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invest_earn_frag_1, container, false);
    }

}
