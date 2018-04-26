package com.example.user.mybrotherhood.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.user.mybrotherhood.R;

/**
 * Created by Nico on 24/03/2018.
 */

public class AddBrotherhoodDialog extends DialogFragment {

    // The interface to transfer the data
    private AddBrotherhoodDialogListener mListener;

    // Interface use to transfer data between the Dialog and Activity
    public interface AddBrotherhoodDialogListener {
        void positiveClicked(String result);
        void negativeClicked(String result);
    }

    // Attach the context to the Activity where it was created on
    // *MUST* implement AddBrotherhoodDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (AddBrotherhoodDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement AddCategoryDialogListener");
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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // Positive is clicked and transfer the name which was written
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = name.getText().toString();
                        mListener.positiveClicked(result);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // Negative is clicked and transfer the name which was written
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = name.getText().toString();
                        mListener.negativeClicked(result);
                    }
                });
        return builder.create();
    }
}
