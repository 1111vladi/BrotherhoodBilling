package com.example.user.mybrotherhood.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.FoundUserRVAdapter;
import com.example.user.mybrotherhood.adapters.ShowUsersRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Nico on 22/04/2018.
 */

public class ShowUsersActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static final String TAG_DB = "tag_db";
    private List<String> showUsersList;
    private RecyclerView rv;
    private ShowUsersRVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);
        // Add the array in a list
        showUsersList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        // Firebase Database
        database = FirebaseDatabase.getInstance();

        initDataset();

    }

    private void initDataset() {
        // Get Category from DB
        myRef = database.getReference("Brotherhood");
        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> listUpdated = new ArrayList<>();

                for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                    if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()) {
                        listUpdated.add(dataTest.getKey());

                    }
                }
                // Add the names to the Category RecycleView
                addShowUsersList(listUpdated);
                // Update the RecyclerView
                initializeAdapter();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initializeAdapter() {

        // Lazy Loading on the adapter , only update the recyclerview adapter if it already exist
        if (adapter == null) {

            adapter = new ShowUsersRVAdapter(showUsersList);

            // Set adapter as the adapter for RecyclerView.
            rv.setAdapter(adapter);


        } else {
            // Set adapter as the adapter for RecyclerView.
            rv.setAdapter(adapter);
        }

    }

    // Add a list of category
    public void addShowUsersList(List<String> names) {
        showUsersList.clear();
        showUsersList.addAll(names);
    }


}
