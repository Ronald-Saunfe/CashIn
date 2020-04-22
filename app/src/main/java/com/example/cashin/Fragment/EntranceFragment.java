package com.example.cashin.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.cashin.R;


public class EntranceFragment extends Fragment {

    private static onNavigateClick listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_entrance, container, false);

        final Button login = (Button) inflate.findViewById(R.id.btnLogin);
        final Button register = (Button) inflate.findViewById(R.id.btnRegEntr);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRegistration();
            }
        });
        return inflate;
    }

    public interface onNavigateClick{
        void onLogin();
        void onRegistration();
    }

    public static void setonNavigateClick(onNavigateClick listener) {
        EntranceFragment. listener= listener;
    }

}
