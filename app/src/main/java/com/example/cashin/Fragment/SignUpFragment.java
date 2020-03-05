package com.example.cashin.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.R;
import com.example.cashin.Service.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class SignUpFragment extends Fragment {

    private static final int GOOGLE_SIGN_IN_CODE = 0;
    private static final Integer PICK_IMAGE_REQUEST=1;

    private SignInButton signInButton;
    private OnFragmentInteractionListener mListener;
    private EditText myusername,myemail,mypassword,myconfirmpass;
    private Button RegisterAC;
    private ProgressBar progressBar;
    Uri imagePath;
    Bitmap bitmap;
    Uri uri;
    User user;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    private DatabaseReference reference,databaseReference;
    private FirebaseAuth auth;
    private AuthStateListener mAuthListener;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private ImageButton btnBackSignup;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //back toolbar button
        btnBackSignup = view.findViewById(R.id.btnBackSignup);
        //back toolbar button go to entrance fragment
        btnBackSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntranceFragment newfrag= new EntranceFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntranceFragment newfrag= new EntranceFragment();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout,newfrag).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        myusername=(EditText) view.findViewById(R.id.RUsername);
        myemail=(EditText) view.findViewById(R.id.Remail);
        mypassword=(EditText) view.findViewById(R.id.Rpassword);
        myconfirmpass=(EditText) view.findViewById(R.id.R_Confirm_password);
        //profile_pic=(ImageView) view.findViewById(R.id.profile);
        signInButton = view.findViewById(R.id.SignInGoogle);

        progressBar=(ProgressBar) view.findViewById(R.id.progressBarSignUp);
        RegisterAC=(Button) view.findViewById(R.id.btnRegister);


        auth = FirebaseAuth.getInstance();//my Auth
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Users Information");//my user Ref
        firebaseUser=auth.getCurrentUser();

        user=new User();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading. Please wait...");

        //mAuthListener=(AuthStateListener)(
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        //On profile pic touched
//        profile_pic.setOnClickListener(new View.OnClickListener() {
  //          @Override
    //        public void onClick(View v) {
      //          Intent profileIntent = new Intent();
        //        profileIntent.setType("image/*");
          //      profileIntent.setAction(Intent.ACTION_GET_CONTENT);
            //    startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE_REQUEST);
            //}
        //});

        // Configure Google Sign In
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("991450820914-fnn0812r0snf02mdohnkmtgp8m0o5v6e.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();

        //Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), googleSignInOptions);

        GoogleSignInAccount googleSignInAccount=GoogleSignIn.getLastSignedInAccount(getActivity());
        if (googleSignInAccount !=null){
            Toast.makeText(getActivity(),"Welcome to CashIn Softs",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), Navigation_Main.class));
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent sign= googleSignInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });

        RegisterAC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String email,password,cpassword,username;

                email = myemail.getText().toString();
                password = mypassword.getText().toString();
                username = myusername.getText().toString();
                cpassword = myconfirmpass.getText().toString();



                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) &&TextUtils.isEmpty(username)
                        && TextUtils.isEmpty(cpassword)) {
                    myemail.setError(getString(R.string.prompt_email));
                    mypassword.setError(getString(R.string.prompt_password));
                    myconfirmpass.setError(getString(R.string.prompt_confirm_new_pass));
                    myusername.setError(getString(R.string.prompt_Username));
                    return;
                }
                if (password.length() <6){
                    mypassword.setError(getString(R.string.minimum_password));
                }
                if (!email.contains("@gmail.com")) {
                    myemail.setError(getString(R.string.email_should_have));
                    return;
                }
//                if (profile_pic.getDrawable() == null){
  //                  error.setText("Error! Upload");

    //            }


                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //addUserInDatabse(task.getResult().getUser());
                                Toast.makeText(getActivity(), "Account is Successfully registered",
                                        Toast.LENGTH_SHORT).show();
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        getValues();
                                        reference.child(firebaseUser.getUid()).setValue(user);
                                        Toast.makeText(getContext(),"Data updated",Toast.LENGTH_LONG).show();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                progressBar.setVisibility(View.GONE);

                                if(!task.isSuccessful()) {
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        //Log.d(TAG, "onComplete: exist_email");
                                        Toast.makeText(getActivity(),
                                                "Email already registered,Try to Login",
                                                Toast.LENGTH_LONG).show();

                                    }
                                    catch (FirebaseAuthInvalidCredentialsException invalidDetails)
                                    {
                                        Toast.makeText(getActivity(),
                                                "Your datails is invalid \n" +
                                                        "Please try again!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }else {

                                    Intent i = new Intent(getActivity(), Navigation_Main.class);
                                    startActivity(i);
                                    Toast.makeText(getActivity(),
                                            "Welcome to CashIn Home ",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                final GoogleSignInAccount signInAccount=signInAccountTask.getResult(ApiException.class);
                assert signInAccount != null;
                AuthCredential authCredential= GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);

                auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(getActivity(),"Your Account is connected to CashIn Welcome!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),Navigation_Main.class));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }

        assert data != null;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            uri = data.getData();
           // profile_pic.setImageURI(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                Toast.makeText(getActivity(), "hey you selected image" + bitmap, Toast.LENGTH_SHORT).show();
             //   profile_pic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    //adding user details to database
   private void getValues(){
        user.setEmail(myemail.getText().toString());
        user.setPhone(myconfirmpass.getText().toString());
        user.setProfilePic(mypassword.getText().toString());
        user.setUsername(myusername.getText().toString());
   }
    ///Adding my profile to the storage....
    private void addUserInDatabse(final FirebaseUser firebaseUser){
        //profile_pic.setDrawingCacheEnabled(true);
        //profile_pic.buildDrawingCache();
        //Bitmap bitmap = ((BitmapDrawable) profile_pic.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] data = bytes.toByteArray();

        FirebaseStorage.getInstance().getReference().child("Users").child(firebaseUser.getUid())
                .child("Profile_Pics")
                .putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url=storageReference.getDownloadUrl().toString();
                        User user=new User(firebaseUser.getEmail(),firebaseUser.getUid(),url);
                        FirebaseDatabase.getInstance().getReference().child("User")
                                .child(firebaseUser.getUid()).setValue(user);
                        databaseReference.setValue(user);

                    }
                });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
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
