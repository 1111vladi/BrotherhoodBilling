package com.example.user.mybrotherhood.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.fragments.AddPaymnet;

/**
 * Activity where the user use to add payment (item_brotherhood) - Name can be changed to better compatibility
 */

public class AddPaymentActivity extends AppCompatActivity{


    private String selectedCategoryName;
    private TextView amount;

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
    }


    // Button Add Payment
    public void addPayment(View view) {
        System.out.println("Name: " + selectedCategoryName + ", amount: " + amount.getText().toString()); // TEST
        // TODO - add the categoryName + amount to the DB


    }

    public void setSelectedCategoryName(String categoryName){
        selectedCategoryName = categoryName;
    }
}
