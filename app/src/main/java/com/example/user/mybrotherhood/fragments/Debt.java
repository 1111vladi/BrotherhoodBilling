package com.example.user.mybrotherhood.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.RvDebtAdapter;

// Todo - Change fragment v4 to newer (v7)

public class Debt extends Fragment {

    public Debt(){}
    RecyclerView RVDebt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_debt, container, false);


        //recycle view stuff below
        RVDebt =rootView.findViewById(R.id.RV_debt);
        RecyclerView.LayoutManager RVELManager= new LinearLayoutManager(getActivity());
        RVDebt.setLayoutManager(RVELManager);
        RvDebtAdapter adapter = new RvDebtAdapter(new String[]{"vlad", "nicolas", "tommy"});
        RVDebt.setAdapter(adapter);
        return rootView;

    }



}