package com.example.user.mybrotherhood.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.user.mybrotherhood.adapters.BrotherhoodRVAdapter;
import com.example.user.mybrotherhood.Brotherhood;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter;
import com.example.user.mybrotherhood.dialog.AddBrotherhoodDialog;
import com.example.user.mybrotherhood.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.example.user.mybrotherhood.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 3/21/2018.
 */

public class CreateBrotherhoodActivity extends AppCompatActivity implements AddBrotherhoodDialog.AddBrotherhoodDialogListener{



    private List<Brotherhood> brotherhoods;
    private RecyclerView rv;
    private SharedPreferences prefs;
    private static final String PREF_NAME= "brotherhood_names", broNames = "names" ;
    private Set<String> brotherhoodNames = new HashSet<>();
    private ItemTouchHelper touchHelper;
    private RecyclerView.AdapterDataObserver adapterObserver;
    private BrotherhoodRVAdapter adapter;
    private String strItemRemoved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_brotherhood_layout);

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        initializeData();
        initializeAdapter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the observer once finish to use on this activity
        // to prevent an ongoing listener
        adapter.unregisterAdapterDataObserver(adapterObserver);
    }

    private void initializeData(){
        // TODO - Get the brotherhoods from the DB, Maybe do a class to do all the database transitions
        brotherhoods = new ArrayList<>();
        // Get a set of all the names from the shared preference
        Set<String> names = prefs.getStringSet(broNames ,null);
        if (names != null){
            // Add the set to the hashset brotherhoodNames
            brotherhoodNames.addAll(names);

            // Add the names to the recycleview
            for(String name : brotherhoodNames ){
                addBrotherhoodList(name);
            }
        }

    }

    private void initializeAdapter(){
        adapter = new BrotherhoodRVAdapter(brotherhoods);
        rv.setAdapter(adapter);

        //  TEST - Get the item_brotherhood which was removed from the RecyclerView Brotherhood ->
        //  To remove from the storage (DB, Shared preference..)
        adapterObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                strItemRemoved = brotherhoods.get(positionStart).brotherhoodName;
                removeItemDB(strItemRemoved);
            }
        };

        adapter.registerAdapterDataObserver(adapterObserver);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }

    // -- Dialog Start --
    // Start alert after the FloadButton was presses
    public void addBrotherhood(View view) {
        AddBrotherhoodDialog addBrotherhoodDialog = new AddBrotherhoodDialog();
        addBrotherhoodDialog.show(getFragmentManager(),"addBrother");

    }

    // Once the user created a new brotherhood , save it
    @Override
    public void positiveClicked(String result) {
        // TODO - Add the brotherhood in DB
        if(result != null && !result.isEmpty()){
            // Add the name to the set
            brotherhoodNames.add(result);
            // Then save in the shared preference
            prefs.edit().putStringSet(broNames, brotherhoodNames).apply();
            // Eventually add to the recycleview
            addBrotherhoodList(result);
            Toast.makeText(this,"Added " + result, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void negativeClicked(String result) {
        Toast.makeText(this,"Canceled", Toast.LENGTH_SHORT).show();

    }
    // -- Dialog End --

    private void addBrotherhoodList(String name){
        brotherhoods.add(new Brotherhood(name));
    }

    // Remove the item_brotherhood from the storage (DB, SharePrefs...)
    // TODO - Remove from DB the item_brotherhood
    private void removeItemDB(String itemName){
        // First remove the item_brotherhood from the RecyclerView
        brotherhoodNames.remove(itemName);
        // Secondly overwrite the new list(StringSet) in the share preference
        prefs.edit().putStringSet(broNames,brotherhoodNames).apply();
    }

}
