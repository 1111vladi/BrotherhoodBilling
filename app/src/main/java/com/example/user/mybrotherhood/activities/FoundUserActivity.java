package com.example.user.mybrotherhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.FoundUserRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.mybrotherhood.activities.BrotherhoodTabs.FOUND_USERS_LIST;

/**
 * Created by Nico on 26/04/2018.
 */

public class FoundUserActivity extends AppCompatActivity implements FoundUserRVAdapter.FoundUserOnClickListener {

    private String userSelected = "";

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static final String TAG_DB = "tag_db";
    private List<String> foundUsersList;
    private RecyclerView rv;
    private FoundUserRVAdapter adapter;
    private FirebaseAuth mAuth;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_user);
        // Get the previous array of found users
        String[] foundUsersArr = getIntent().getStringArrayExtra(FOUND_USERS_LIST);
        // Add the array in a list
        foundUsersList = new ArrayList<>();
        foundUsersList.addAll(Arrays.asList(foundUsersArr));

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeAdapter();

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getDisplayName();

        // Firebase Database
        database = FirebaseDatabase.getInstance();

    }


    // The user which was selected to add
    @Override
    public void passCategoryName(View view, int position) {
        userSelected = foundUsersList.get(position);

    }

    private void initializeAdapter() {
        // Lazy Loading on the adapter , only update the recyclerview adapter if it already exist
        adapter = new FoundUserRVAdapter(foundUsersList, this);
        rv.setAdapter(adapter);
    }

    // Button Add User Action
    public void addUser(View view) {

        // Delete the Brother if this user is the Admin
        myRef = database.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> listUpdated = new ArrayList<>();
                // Iterate through the data and add to a list
                for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                    if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()) {
                        listUpdated.add(dataTest.getKey());

                    }
                }
                // Add the new user to the list
                listUpdated.add(userSelected);
                for (String name : listUpdated) {
                    // Only add if the user doesn't exist in the list
                    if (name.equals(user) && name.isEmpty()) {
                        listUpdated.remove(name);
                    }
                }
                myRef = database.getReference("Brotherhood");
                myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Users").setValue(listUpdated);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Done adding the user -> Back to Brotherhood Tabs
        finish();
    }


}
