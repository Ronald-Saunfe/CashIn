package com.example.cashin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView UserName;
    ImageView prof;
    User userinfo;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorage;
    private FirebaseDatabase database;
    private String currentUserID;
    private DatabaseReference reference;


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        prof=(ImageView) view.findViewById(R.id.profile_home);
        UserName=(TextView) view.findViewById(R.id.UserName);
        Button signout=(Button) view.findViewById(R.id.logout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String info=firebaseUser.getUid();

        /*if (firebaseUser !=null){
            reference=FirebaseDatabase.getInstance().getReference().child("Users Information").child(info);
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("email").getValue(String.class);
                        UserName.setText("Loged As:\t" + username);
                    }else{
                        Toast.makeText(getActivity(),"Data was not found",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }*/


        if (firebaseUser != null) {
            for (UserInfo profile : firebaseUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();
                // UID specific to the provider
                String uid = profile.getUid();
                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                UserName.setText("Signed In as:\t\t" +name);
                Picasso.with(getContext())
                        .load(photoUrl)
                        .into(prof);
            }
        }
        return view;
    }

    //adding user details to database
    private void getValues(){
        userinfo.getEmail();

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
                        SignOut();
                    }
                })
                .create().show();

    }
    private void SignOut(){
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut()
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Succesfully Signed out",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(),Nav.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Sign Out Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
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
