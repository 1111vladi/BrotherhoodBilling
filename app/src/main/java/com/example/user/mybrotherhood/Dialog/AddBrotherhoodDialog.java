package com.example.user.mybrotherhood.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.mybrotherhood.R;

/**
 * Created by Nico on 24/03/2018.
 */

public class AddBrotherhoodDialog extends DialogFragment {

    public interface AddBrotherhoodDialogListener {
        void positiveClicked(String result);
        void negativeClicked(String result);
    }

    private AddBrotherhoodDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddBrotherhoodDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement AddBrotherhoodDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set up the view of which the dialog will use
        View viewInfalted  = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_brotherhood, null);
        // Set up the input
        final EditText name = (EditText) viewInfalted.findViewById(R.id.name);

        builder.setView(viewInfalted)
                .setPositiveButton("Yep", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    String result = name.getText().toString();
                    mListener.positiveClicked(result);
                    }
                })
                .setNegativeButton("Nop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = name.getText().toString();
                        mListener.negativeClicked(result);

                    }
                });
        return builder.create();
    }
}
