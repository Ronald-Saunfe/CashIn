package com.example.cashin.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.cashin.R;

import java.util.Objects;


public class EntranceFragment extends Fragment {

    private LoginFragment.OnFragmentInteractionListener mListener;

    private ConstraintLayout layout;
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

        Button login = (Button) inflate.findViewById(R.id.btnLogin);
        Button register = (Button) inflate.findViewById(R.id.btnRegEntr);
        layout=(ConstraintLayout) inflate.findViewById(R.id.entrance_frag);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout.removeAllViews();
                LoginFragment newFragment=new LoginFragment();
                FragmentTransaction ft= Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null).replace(R.id.frame_layout,newFragment).commit();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout.removeAllViews();
                SignUpFragment myf=new SignUpFragment();
                FragmentTransaction ft= Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null).replace(R.id.frame_layout,myf).commit();

            }
        });



        return inflate;
    }

}
