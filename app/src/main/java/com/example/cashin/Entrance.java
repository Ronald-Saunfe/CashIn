package com.example.cashin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cashin.ui.login.LoginActivity;


public class Entrance extends Activity {
    Button login,register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_entrance);


    }
    public void openForms(View view){
        if (view.getId()==R.id.btnLogin){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else if (view.getId()==R.id.btnRegEntr){
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }

    }
}
