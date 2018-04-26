package com.example.user.mybrotherhood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.LinearLayoutManager;
import com.example.user.mybrotherhood.activities.AddPaymentActivity;
import com.example.user.mybrotherhood.R;

public class Payment extends Fragment {
    RecyclerView ERVPayment;
    FloatingActionButton fabPayment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_payment, container, false);
        ERVPayment=rootView.findViewById(R.id.ERV_payment);
        RecyclerView.LayoutManager RVELManager= new LinearLayoutManager(getActivity());
        ERVPayment.setLayoutManager(RVELManager);
        fabPayment=rootView.findViewById(R.id.fab_payment);
        fabPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPaymentActivity.class));
            }
        });

        // Goes to the detail Button
        String [] detailArr = {"Payment", "Name"};
        // Send data to the dialog
        Bundle bundle = new Bundle();
        bundle.putStringArray("KEY", detailArr); // Get data From Payment / Debt , Name
        setArguments(bundle);

        return rootView;
    }


    }

