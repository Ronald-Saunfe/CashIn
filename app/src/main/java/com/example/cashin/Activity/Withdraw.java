package com.example.cashin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.cashin.R;

public class Withdraw extends AppCompatActivity {
    //back button in toolbar
    private ImageButton btnBackWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        btnBackWithdraw = findViewById(R.id.btnBackWithdraw);
        //navigate to homepage
        btnBackWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call the activity
                //the current activity wlll not be saved in the back stack
                Intent i = new Intent(getApplicationContext(), Navigation_Main.class);
                i.setFlags(i.getFlags()|i.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }
}
