package com.example.user.mybrotherhood.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 26-Apr-18.
 */

public class ShowDetailCategoryDialog extends DialogFragment {

    private TextView showList;
    private FirebaseAuth mAuth;
    private String user;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private StringBuilder showDetail;
    private String [] categoryDetailArr;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Get user which is signed in
        user = mAuth.getCurrentUser().getDisplayName();
        // Initialize Firebase DB
        database = FirebaseDatabase.getInstance();

        categoryDetailArr = getArguments().getStringArray("KEY"); // [0] = Payment/Debt , [1] = Name
        showDetail = new StringBuilder();
        getDetailList();

        showList = (TextView) getActivity().findViewById(R.id.tv_list);
        showList.setText(showDetail.toString()); // Get data from DB
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set up the view of which the dialog will use
        View viewInfalted = LayoutInflater.from(getContext()).inflate(R.layout.dialog_show_detail_category, null);


        builder.setView(viewInfalted)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    // Positive is clicked and transfer the name which was written
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

    // Get the category from the DB which is respective to the user
    private void getDetailList(){

        // Get Category from DB
        myRef = database.getReference("Brotherhood");
        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child(categoryDetailArr[0]).child(categoryDetailArr[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listUpdated = new ArrayList<>();

                for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                    if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()) {
                        addDetail(dataTest.getValue().toString());

                    }
                }
            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });

    }

    private void addDetail(String detail){
        showDetail.append(detail).append("\n");
    }
}
