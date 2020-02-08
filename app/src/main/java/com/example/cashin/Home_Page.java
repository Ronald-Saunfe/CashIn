package com.example.cashin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Home_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Home");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.About_Cashin:
                Toast toast= Toast.makeText(getApplicationContext(),"About Cash in",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                showAbout();
                return true;
            case R.id.settings:
                Toast.makeText(Home_Page.this,"coming soon",Toast.LENGTH_LONG).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() {
        new AlertDialog.Builder(this)
                .setTitle("About Cash in")
                .setMessage("Cash in helps you to earn more.\n\n\n Version 1.0.1")
                .setCancelable(false)
                .setNegativeButton(android.R.string.yes, null)
                .create().show();
    }


}
