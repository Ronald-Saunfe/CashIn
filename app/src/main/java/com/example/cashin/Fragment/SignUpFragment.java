package com.example.cashin.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cashin.Activity.Navigation_Main;
import com.example.cashin.Activity.Splash_Screen;
import com.example.cashin.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.spark.submitbutton.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText myusername, myemail, mypassword, myconfirmpass;
    private Button RegisterAC;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;
    private ImageButton btnBackSignup;
    public String email, password, cpassword, username;
    String latitudeString,longitudeString;
    String[] cordinates;
    String GPS;
    private LocationListener listener;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationManager locationManager;


    public String postUrl = "https://us-central1-cashin-270220.cloudfunctions.net/signUp";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater
                .from(getContext())
                .inflateTransition(android.R.transition.move).setDuration(300));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        myusername=(EditText) view.findViewById(R.id.RUsername);
        myemail=(EditText) view.findViewById(R.id.Remail);
        mypassword=(EditText) view.findViewById(R.id.Rpassword);
        myconfirmpass=(EditText) view.findViewById(R.id.R_Confirm_password);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        RegisterAC=(Button) view.findViewById(R.id.btnRegister);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading. Please wait...");
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
        RegisterAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location !=null){
                            latitudeString = String.valueOf(location.getLatitude());
                            longitudeString = String.valueOf(location.getLongitude());

                            System.out.println("===============================================" +latitudeString+
                                    "========================================="+longitudeString);

                            cordinates= new String[]{latitudeString, longitudeString};
                            Gson gson=new Gson();
                            GPS=gson.toJson(cordinates);
                        }

                    }
                });



                final JSONObject jsonObject=new JSONObject();
                final JSONObject jsonObject1=new JSONObject();

                try {
                    jsonObject.put("email",email);
                    jsonObject.put("Username",username);
                    jsonObject.put("Password",password);
                    jsonObject.put("confirmPassword",cpassword);
                    jsonObject.put("GPSLocation",GPS);

                    jsonObject1.put("message",jsonObject);
                    System.out.println(jsonObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, String.valueOf(jsonObject1));
                final Request request = new Request.Builder()
                        .url(postUrl)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        Snackbar.make(getView().findViewById(R.id.fragment_container),"Experienced an error while loading...",
                                Snackbar.LENGTH_LONG)
                                .setAction("retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                         startActivity(new Intent(getContext(),Splash_Screen.class));
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("TAG",response.body().string());
                        startActivity(new Intent(getContext(),Navigation_Main.class));
                    }
                });
            }
        });


        return view;
    }

    private void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request_permission();
            }
        } else {
            // permission has been granted
            locationManager.requestLocationUpdates("gps", 5000, 0, listener);
        }

    }

    private void request_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
                ACCESS_COARSE_LOCATION)) {

            Snackbar.make(getView().findViewById(R.id.fragment_container), "Location permission is needed because ...",
                    Snackbar.LENGTH_LONG)
                    .setAction("retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                        }
                    })
                    .show();
        } else {
            // permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
