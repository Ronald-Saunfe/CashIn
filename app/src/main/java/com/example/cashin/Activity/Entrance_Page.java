package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import com.example.cashin.Fragment.EntranceFragment;
import com.example.cashin.Fragment.HomeFragment;
import com.example.cashin.Fragment.LoginFragment;
import com.example.cashin.Fragment.ResetPassFragment;
import com.example.cashin.Fragment.SignUpFragment;
import com.example.cashin.R;

public class Entrance_Page extends AppCompatActivity
        implements LoginFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener,
        ResetPassFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener{

    Class fragmentclass;
    public static Fragment fragment;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance__page);


        ///Adding or Inflating myfragment to the layout............>>>
        fragmentclass= EntranceFragment.class;
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
