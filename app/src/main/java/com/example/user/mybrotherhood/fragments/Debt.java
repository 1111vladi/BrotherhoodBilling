package com.example.user.mybrotherhood.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mybrotherhood.R;


public class Debt extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_debt, container, false);
        FloatingActionButton fab=rootView.findViewById(R.id.fabDebt);
        return rootView;

    }

    public void additem(View view) {


    }


    }