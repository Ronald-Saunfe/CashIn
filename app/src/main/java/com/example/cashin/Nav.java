package com.example.cashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class Nav extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener,ResetPassFragment.OnFragmentInteractionListener{

    Button login,register;
    Class fragmentclass;
    public static Fragment fragment;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        ///Adding myfragment to the layout............>>>
        fragmentclass=EntranceFragment.class;
        try {
            fragment=(Fragment) fragmentclass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().setTransition(R.anim.fade_in).replace(R.id.frame_layout,fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
