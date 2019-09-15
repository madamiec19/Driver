package com.example.driver10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.driver10.R;

public class MiddlepointDialog extends AppCompatDialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private MiddlepointDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_middlepoint_dialog, null);

        builder.setView(view)
                .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(radioGroup.getCheckedRadioButtonId()!= -1){
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = view.findViewById(selectedId);
                            String middlepoint = radioButton.getText().toString();
                            listener.applyMiddlepoint(middlepoint);
                        }

                    }
                });
        radioGroup = view.findViewById(R.id.rgMiddlepoint);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (MiddlepointDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement MiddlepointDialogListener");
        }
    }

    public interface MiddlepointDialogListener{
        void applyMiddlepoint(String middlepoint);
    }
}
