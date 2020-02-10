package com.example.cashin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class EntranceFragment extends Fragment {

    private LoginFragment.OnFragmentInteractionListener mListener;

    Button login,register;
    FrameLayout layout;
    TextView reset;

    public EntranceFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("EntranceFragment","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_entrance, container, false);

        login=(Button) inflate.findViewById(R.id.btnLogin);
        register=(Button) inflate.findViewById(R.id.btnRegEntr);
        layout=(FrameLayout) inflate.findViewById(R.id.entrance_frag);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout.removeAllViews();
                LoginFragment newFragment=new LoginFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null).replace(R.id.frame_layout,newFragment).commit();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout.removeAllViews();
                SignUpFragment myf=new SignUpFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null).replace(R.id.frame_layout,myf).commit();

            }
        });



        return inflate;
    }

}
