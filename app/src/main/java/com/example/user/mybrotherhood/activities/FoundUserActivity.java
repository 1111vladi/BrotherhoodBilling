package com.example.user.mybrotherhood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
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
import java.util.Set;

import static com.example.user.mybrotherhood.activities.BrotherhoodTabs.FOUND_USERS_LIST;

/**
 * Created by Nico on 26/04/2018.
 */

public class FoundUserActivity extends AppCompatActivity implements FoundUserRVAdapter.FoundUserOnClickListener {

    private String userSelected = "";

    private FirebaseDatabase database;
    private DatabaseReference myRefBrotherhood;
    private DatabaseReference myRefUsers;
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
        myRefBrotherhood = database.getReference("Brotherhood");
        myRefUsers = database.getReference("Users");

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
        // Add to Brotherhood Users list
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put(userSelected,true);
        myRefBrotherhood.child(FirebaseBrotherhood.getBrotherhoodName()).child("Users").updateChildren(newUserMap); // Can't use parameter set -> So use a List instead

        // Add Debt & Payment
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(userSelected,"");
        myRefBrotherhood.child(FirebaseBrotherhood.getBrotherhoodName()).child("Debt").updateChildren(userMap);
        myRefBrotherhood.child(FirebaseBrotherhood.getBrotherhoodName()).child("Payment").updateChildren(userMap);

        // Add to User Brotherhood list
        Map<String, Object> newBrotherhoodMap = new HashMap<>();
        newBrotherhoodMap.put(FirebaseBrotherhood.getBrotherhoodName(),true);
        myRefUsers.child(userSelected).child("Brotherhood").child("BrotherhoodName").updateChildren(newBrotherhoodMap);  // Can't use parameter set -> So use a List instead

        finish();
    }


}
