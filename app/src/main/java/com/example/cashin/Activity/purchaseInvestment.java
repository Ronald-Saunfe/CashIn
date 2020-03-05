package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cashin.Fragment.investEarnFrag_1;
import com.example.cashin.Fragment.purchaseInvestmentFrag_1;
import com.example.cashin.R;

public class purchaseInvestment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_investment);
        
        //inflate the fragment
        //Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.rootPurchaseInvestment, new purchaseInvestmentFrag_1());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
}
