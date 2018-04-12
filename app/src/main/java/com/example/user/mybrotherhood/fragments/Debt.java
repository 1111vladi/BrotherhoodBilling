package com.example.user.mybrotherhood.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Todo - Change fragment v4 to newer (v7)

public class Debt extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_debt, container, false);
        FloatingActionButton fab=rootView.findViewById(R.id.fabDebt);
        // get the listview
        expListView = rootView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(rootView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return rootView;

    }



    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("tommy");
        listDataHeader.add("nicolas");
        listDataHeader.add("vlad");

        // Adding child data
        List<String> tommy = new ArrayList<String>();
        tommy.add("The Shawshank Redemption");
        tommy.add("The Godfather");
        tommy.add("The Godfather: Part II");
        tommy.add("Pulp Fiction");
        tommy.add("The Good, the Bad and the Ugly");
        tommy.add("The Dark Knight");
        tommy.add("12 Angry Men");

        List<String> nicolas = new ArrayList<String>();
        nicolas.add("The Conjuring");
        nicolas.add("Despicable Me 2");
        nicolas.add("Turbo");
        nicolas.add("Grown Ups 2");
        nicolas.add("Red 2");
        nicolas.add("The Wolverine");

        List<String> vlad = new ArrayList<String>();
        vlad.add("2 Guns");
        vlad.add("The Smurfs 2");
        vlad.add("The Spectacular Now");
        vlad.add("The Canyons");
        vlad.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), tommy); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nicolas);
        listDataChild.put(listDataHeader.get(2), vlad);
    }
}