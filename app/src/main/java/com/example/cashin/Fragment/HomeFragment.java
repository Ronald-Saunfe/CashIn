package com.example.cashin.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cashin.Activity.Entrance_Page;
import com.example.cashin.Activity.InvestEarn;
import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.Activity.Withdraw;
import com.example.cashin.Activity.profilePage;
import com.example.cashin.Activity.purchaseInvestment;

import com.example.cashin.R;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView UserName;
    ImageView prof;
    //menu button
    private Button btnInvestmentEarn, investmentPurchase, btnMainProfile, withdrawcash;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        Button signout=(Button) view.findViewById(R.id.logout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnInvestmentEarn = view.findViewById(R.id.investment_earn);
        //start activity invest and earn
        btnInvestmentEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InvestEarn.class);
                startActivity(intent);
            }
        });

        investmentPurchase = view.findViewById(R.id.investmentPurchase);
        //start activity invest and earn
        investmentPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), purchaseInvestment.class);
                startActivity(intent);
            }
        });

        btnMainProfile = view.findViewById(R.id.btnMainProfile);
        btnMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),profilePage.class));
                //v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_up));
            }
        });

        withdrawcash = view.findViewById(R.id.withdrawcash);
        //start activity withdraw cash
        withdrawcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Withdraw.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void showDialog() {
        new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setIcon(R.drawable.signout)
                .setCancelable(false)
                .setTitle("Log out!")
                .setMessage("Are you sure you want to logout?\n")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SignOut();
                        startActivity(new Intent(getContext(), Entrance_Page.class));
                    }
                })
                .create().show();

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
}
