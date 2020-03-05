package com.example.cashin.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.R;


public class purchaseInvestmentFrag_1 extends Fragment {

    private ImageButton btnBackPurchInvst;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBackPurchInvst =view.findViewById(R.id.btnBackPurchInvst);
        //navigate to homepage
        btnBackPurchInvst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call the activity
                //the current activity wlll not be saved in the back stack
                Intent i = new Intent(getContext(), Navigation_Main.class);
                i.setFlags(i.getFlags()|i.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_investment_frag_1, container, false);
    }

}
