package com.example.user.mybrotherhood.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.fragments.AddPaymnet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity where the user use to add payment (item_brotherhood) - Name can be changed to better compatibility
 */

public class AddPaymentActivity extends AppCompatActivity{


    private String selectedCategoryName;
    private TextView amount;

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        // Get fragment manager
        FragmentManager fm = getFragmentManager();

        // Add fragment from the class AddPayment
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.list_container_payment, new AddPaymnet());
        ft.commit();

        amount = (TextView) findViewById(R.id.et_amount);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Get user which is signed in
        user = mAuth.getCurrentUser().getDisplayName();

        // Firebase Database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Brotherhood");
    }


    // Button Add Payment
    public void addPayment(View view) {
        System.out.println("Name: " + selectedCategoryName + ", amount: " + amount.getText().toString()); // TEST
        // TODO - add the categoryName + amount to the DB


        myRef = database.getReference("Brotherhood");
        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Sum
                int allUsersCount = (int) dataSnapshot.getChildrenCount();
                // Divide in the number of Users
                int eachUserCount = Integer.parseInt(amount.getText().toString()) / allUsersCount;

                List<String> userList = new ArrayList<>();

                for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                    if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty() && !dataTest.getValue().toString().equals(user)) {
                        userList.add(dataTest.getValue().toString());

                    }
                }

                // In each user add in debt the category with the divided sum
                for(String name : userList){
                    Map<String, Object> mapInsert = new HashMap<>();
                    mapInsert.put(name,eachUserCount);
                    // Add into each user Debt the amount
                    myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Debt").child(name).updateChildren(mapInsert);
                    // Add this user Paymnet the amount
                    myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Paymnet").child(user).updateChildren(mapInsert);

                }

            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });



    }

    public void setSelectedCategoryName(String categoryName){
        selectedCategoryName = categoryName;
    }
}
