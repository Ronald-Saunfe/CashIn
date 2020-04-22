package com.example.cashin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cashin.ChangeUserName;
import com.example.cashin.Fragment.SignUpFragment;
import com.example.cashin.GlobalVariable;
import com.example.cashin.OkHTTPhandler;
import com.example.cashin.R;
import com.example.cashin.ResponseError;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedbottompicker.TedRxBottomPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.cashin.Fragment.SignUpFragment.JSON;

public class profilePage extends AppCompatActivity implements ChangeUserName.ChangeUsernameListener, OkHTTPhandler.onresponseLinstener {

    private ImageButton btnBackProfile;
    private Button btnEditProf;
    private ImageButton btneUsername;
    private ImageButton btneEmail;
    private TextView txtProfUsername;
    private TextView txtProfEmail;
    private ProgressBar pbProf;
    private ImageButton btnChoosePic;
    private ImageView profilePic;
    private String profilePicPath;
    private int Rertycount=0;
    private Uri uri;
    private String newUsername;
    HashMap<String,String> payload = new HashMap<String,String>();


    private static final String TAG = profilePage.class.getSimpleName();

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        OkHTTPhandler.setOnResponseListener(this);
        pbProf = findViewById(R.id.pbProf);
        btnChoosePic = findViewById(R.id.btnChoosePic);

        txtProfUsername = findViewById(R.id.txtProfUsername);
        txtProfEmail = findViewById(R.id.txtProfEmail);


        btneUsername = findViewById(R.id.btneUsername);
        btneEmail = findViewById(R.id.btneEmail);

        profilePic = findViewById(R.id.profilePic);

        btnBackProfile = findViewById(R.id.btnBackProfile);
        //navigate to homepage
        btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Navigation_Main.class);
                startActivity(i);
            }
        });

        TextView username=(TextView) findViewById(R.id.txtProfUsername);
        TextView useremail=(TextView) findViewById(R.id.txtProfEmail);
        CircleImageView profile_pic=(CircleImageView) findViewById(R.id.profilePic);

        username.setText(GlobalVariable.GlobUsername);
        useremail.setText(GlobalVariable.GlobEmail);
        //show edit animation
        btnEditProf = findViewById(R.id.btnEditProf);
        btnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditableBtn();
            }
        });

        //change the username
        btneUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the dialog fragment and show it
                ChangeUserName dialog = new ChangeUserName();
                dialog.show(getSupportFragmentManager(), "ChangeUserNameeDialogFragment");
            }
        });

        //upload an image
        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for camera permission first
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            //show photo picker
                            TedBottomPicker.with(profilePage.this)
                                    .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                                        @Override
                                        public void onImageSelected(Uri uris) {
                                            // here is selected image uri
                                            profilePicPath=uris.getPath();
                                            uri =uris;
                                            System.out.println("pic path=================="+profilePicPath);
                                            getUploadSignedUrl();
                                        }
                                    });
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                        }
                    };

                    TedPermission.with(getApplicationContext())
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .check();
                }
                else {
                    //show photo picker
                    TedBottomPicker.with(profilePage.this)
                            .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                                @Override
                                public void onImageSelected(Uri uris) {
                                    // here is selected image uri
                                    profilePicPath=uris.getPath();
                                    uri =uris;
                                    System.out.println("pic path=================="+profilePicPath);
                                    getUploadSignedUrl();
                                }
                            });
                }

            }
        });
    }

    //get the upload signed url
    private void getUploadSignedUrl(){
        payload.put("email",GlobalVariable.GlobEmail);
        OkHTTPhandler.makepayload(payload,
                "https://us-central1-cashin-270220.cloudfunctions.net/UploadImg",
                0,
                pbProf,
                this,
                null);
    }

    //upload picture to server
    private void uploadProfilePic(String serverURL){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbProf.setVisibility(View.VISIBLE);
            }
        });
        OkHttpClient client = new OkHttpClient();
        File file= new File(uri.getPath());
        Uri files = Uri.fromFile(new File(profilePicPath));
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(files.toString());
        System.out.println("============image/"+fileExt);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image",file.getName(),
                        RequestBody.create(new File(profilePicPath),
                                MediaType.get(String.format("image/*"))
                        ))
                .build();

        Request request = new Request.Builder()
                .url(serverURL)
                .header("content_type","image/*")
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                // Handle the error
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pbProf.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pbProf.setVisibility(View.GONE);
                    }
                });
                if (!response.isSuccessful()) {
                    // Handle the error
                }
                // Upload successful
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pbProf.setVisibility(View.GONE);
                        try {
                            ImageView imageView = findViewById(R.id.profilePic);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            // Log.d(TAG, String.valueOf(bitmap));
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void applytext(final String username) {
        if (username.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Username",Toast.LENGTH_LONG).show();
        }
        else {
            newUsername = username;
            payload.put("email", GlobalVariable.GlobEmail);
            payload.put("Username", newUsername);
            OkHTTPhandler.makepayload(payload,
                    "https://us-central1-cashin-270220.cloudfunctions.net/function-1",
                    1,
                    pbProf,
                    this,
                    null);
        }
    }

    private void ShowEditableBtn(){
        //set fading animation
        ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(btneUsername, "alpha",0f,1f);
        textViewAnimator.setDuration(100);
        textViewAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        textViewAnimator.start();

        //animate sizes
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 24);
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator()); // increase the speed first and then decrease
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btneUsername.getLayoutParams();
                layoutParams.width = val;
                layoutParams.height = val;
                btneUsername.setLayoutParams(layoutParams);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation)
            {
            }
        });
        valueAnimator.start();
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        if (id==0){
            try {
                String code = response.getString("code");

                final String Cod = code;
                if (Cod.equals("200")){
                    String signedUrl = response.getString("response");
                    //upload the image using the url
                    System.out.println("url============================="+signedUrl);
                    uploadProfilePic(signedUrl);
                }
            }
            catch (JSONException s){
                }
            }

        else if(id==1){
            try{
                String code = response.getString("code");

                final String Cod = code;
                if (code=="200"){
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            txtProfUsername.setText(newUsername);
                            pbProf.setVisibility(View.GONE);
                            GlobalVariable.GlobEmail = newUsername;
                            Toast.makeText(getApplicationContext(),"Username updated successfully",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch (JSONException s){

            }
        }

    }
}
