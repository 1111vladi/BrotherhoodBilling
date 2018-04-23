package com.example.user.mybrotherhood.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.user.mybrotherhood.expandablerecycleview.BMembersDataFactory.makeMultiCheckMembers;
import com.example.user.mybrotherhood.expandablerecycleview.multicheck.MultiCheckMemberAdapter;
import com.example.user.mybrotherhood.R;

// Todo - Change fragment v4 to newer (v7)

public class Debt extends Fragment {

    RecyclerView ERVDebt;
    private MultiCheckMemberAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,@Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_debt, container, false);
        FloatingActionButton fab=rootView.findViewById(R.id.fabDebt);

        //recycle view stuff below
        ERVDebt=rootView.findViewById(R.id.ERV_debt);
        RecyclerView.LayoutManager RVELManager= new LinearLayoutManager(getActivity());
        adapter = new MultiCheckMemberAdapter(makeMultiCheckMembers());
        ERVDebt.setLayoutManager(RVELManager);
        ERVDebt.setAdapter(adapter);
        return rootView;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }
//todo onRestoreInstanceState() in fragment
}