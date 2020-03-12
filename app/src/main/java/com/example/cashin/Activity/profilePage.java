package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cashin.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class profilePage extends AppCompatActivity {

    private ImageButton btnBackProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        btnBackProfile = findViewById(R.id.btnBackProfile);
        //navigate to homepage
        btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call the activity
                //the current activity wlll not be saved in the back stack
                //startActivity(new Intent(getApplicationContext(),Navigation_Main.class));

                Intent i = new Intent(getApplicationContext(), Navigation_Main.class);
                i.setFlags(i.getFlags()|i.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        TextView username=(TextView) findViewById(R.id.txtProfUsername);
        TextView useremail=(TextView) findViewById(R.id.txtProfEmail);
        CircleImageView profile_pic=(CircleImageView) findViewById(R.id.profilePic);
    }
}
