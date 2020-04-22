package com.example.cashin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangeUserName extends AppCompatDialogFragment {
    private EditText edtTxt;
    private ChangeUsernameListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.changeusername,null);

        builder.setView(view)
                .setTitle("Change User name")
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeUserName.this.getDialog().cancel();
                    }
                })
                .setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = edtTxt.getText().toString();
                        listener.applytext(username);
                    }
                })
                ;



        edtTxt = view.findViewById(R.id.txtEdtDialog);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChangeUsernameListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must impelement ChangeUsernameListener");
        }
    }

    public interface ChangeUsernameListener{
        void applytext(String username);
    }
}
