package com.example.driver10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.driver10.R;

public class ExtrasDialog extends AppCompatDialogFragment {
    private EditText etExtrasValue;
    private CheckBox cbSTC, cbTC, cbCl, cbGps;
    private ExtrasDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layour_extras_dialog, null);

        builder.setView(view)
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String extras = null;
                        int extrasValue;

                        if(cbSTC.isChecked()) extras += cbSTC.getText().toString() + " ";
                        if(cbTC.isChecked()) extras += " " + cbTC.getText().toString() + " ";
                        if(cbCl.isChecked()) extras += " " + cbCl.getText().toString() + " ";
                        if(cbGps.isChecked()) extras += " " + cbGps.getText().toString() + " ";

                        extrasValue = Integer.parseInt(etExtrasValue.getText().toString());

                        listener.applyExtras(extras, extrasValue);
                    }
                });
        cbSTC = view.findViewById(R.id.cbSTC);
        cbTC = view.findViewById(R.id.cbTC);
        cbCl = view.findViewById(R.id.cbCl);
        cbGps = view.findViewById(R.id.cbGps);
        etExtrasValue = view.findViewById(R.id.etExtrasValue);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExtrasDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExtrasDialogListener");
        }
    }

    public interface ExtrasDialogListener{
        void applyExtras(String extras, int extrasValue);
    }
}

