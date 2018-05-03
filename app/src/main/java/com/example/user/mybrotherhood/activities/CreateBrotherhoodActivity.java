package com.example.user.mybrotherhood.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.adapters.BrotherhoodRVAdapter;
import com.example.user.mybrotherhood.Brotherhood;
import com.example.user.mybrotherhood.dialog.AddBrotherhoodDialog;
import com.example.user.mybrotherhood.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.example.user.mybrotherhood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by user on 3/21/2018.
 */

public class CreateBrotherhoodActivity extends AppCompatActivity implements
        AddBrotherhoodDialog.AddBrotherhoodDialogListener,
        View.OnClickListener {


    private FirebaseDatabase database;
    private DatabaseReference myRefBrotherhood, myRefUsers;
    private static final String TAG_DB = "tag_db";
    private List<Brotherhood> brotherhoods;
    private RecyclerView rv;
    private ItemTouchHelper touchHelper;
    private RecyclerView.AdapterDataObserver adapterObserver;
    private BrotherhoodRVAdapter adapter;
    private String strItemRemoved;
    private String user;

    private Map<DatabaseReference , ValueEventListener > firebaseListenersMap = new HashMap<>();

    private FirebaseAuth mAuth;

    // A client for interacting with the Google Sign In API.
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_brotherhood_layout);

        brotherhoods = new ArrayList<>();
        // Initiate FirebaseDatabase
        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Users");
        myRefBrotherhood = database.getReference("Brotherhood");
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser().getDisplayName();
        connectToDB( user);



        // Change Status bar background
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.teal_800));


        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // SignOut from google account
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // Specifies that an ID token for authenticated users is requested.
                // Requesting an ID token requires that the server client ID be specified.
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initializeData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the observer once finish to use on this activity
        // to prevent an ongoing listener
        adapter.unregisterAdapterDataObserver(adapterObserver);

        // Iterate through the firebaseListenerMap and remove each listener from the respective firebase reference
        for(Map.Entry<DatabaseReference , ValueEventListener > entry : firebaseListenersMap.entrySet()){
            DatabaseReference ref = entry.getKey();
            ValueEventListener listener = entry.getValue();
            ref.removeEventListener(listener);
        }
    }

    // Get the data from FirebaseDB and each new brotherhood that is added , will update the list
    private void initializeData() {
        myRefUsers.child(user).child("Brotherhood").child("BrotherhoodName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Build the listener , which get the brotherhood list
                    ValueEventListener listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> listUpdated = new ArrayList<>();

                            for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                                if (dataTest.getKey() != null && !dataTest.getKey().isEmpty()) {
                                    listUpdated.add(dataTest.getKey());

                                }
                            }
                            // Add the names to the Brotherhood RecycleView
                            addBrotherhoodList(listUpdated);
                            // Update the RecyclerView
                            initializeAdapter();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    // Set the listener
                    dataSnapshot.getRef().addValueEventListener(listener);
                    // Save in map
                    firebaseListenersMap.put(dataSnapshot.getRef(),listener );

                }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }
        });

    }

    private void initializeAdapter() {
        // Lazy Loading on the adapter , only update the recyclerview adapter if it already exist
        if(adapter == null){
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
        } else {
            rv.setAdapter(adapter);
        }
    }

    private void connectToDB(final String username) {

        myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getChildren().toString().contains(username)) {

                    Map<String, Object> placeholderMap = new HashMap<>();
                    placeholderMap.put("Placeholder",true);
                    myRefUsers.child(username).updateChildren(placeholderMap);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG_DB, "Failed to read value.", databaseError.toException());
            }
        });
    }


    // -- Dialog Start --
    // Start alert after the FloadButton was presses
    public void addBrotherhood(View view) {
        AddBrotherhoodDialog addBrotherhoodDialog = new AddBrotherhoodDialog();
        addBrotherhoodDialog.show(getFragmentManager(), "addBrother");

    }

    // Create a new Brotherhood
    @Override
    public void positiveClicked(final String result) {
        if (result != null && !result.isEmpty()) {

            ValueEventListener valueSingleEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String brotherhoodName = result;
                    // Save the Brotherhod Name in a map and then update the user Brotherhoods
                    Map<String, Object> brotherhoodMap = new HashMap<>();
                    brotherhoodMap.put(brotherhoodName, true);
                    // Add the Brotherhood to the current user
                    myRefUsers.child(user).child("Brotherhood").child("BrotherhoodName").updateChildren(brotherhoodMap);

                    // Default categories
                    Map<String, Object> categoryMap = new HashMap<>();

                    categoryMap.put("Electricity",true);
                    categoryMap.put("Water",true);
                    categoryMap.put("Rent",true);

                    // Create the Brotherhood in the Brotherhood Group DB
                    // Get a new reference to the Brotherhood DB
                    // Add Default categories
                    myRefBrotherhood.child(brotherhoodName).child("Category").updateChildren(categoryMap);

                    // Add Debt & Payment
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put(user,"");
                    myRefBrotherhood.child(brotherhoodName).child("Debt").updateChildren(userMap);
                    myRefBrotherhood.child(brotherhoodName).child("Payment").updateChildren(userMap);

                    // Add Users
                    Map<String, Object> newUserMap = new HashMap<>();
                    newUserMap.put(user + "   ♚", true);
                    myRefBrotherhood.child(brotherhoodName).child("Users").updateChildren(newUserMap);
                    // Add Admin
                    Map<String, Object> adminMap = new HashMap<>();
                    adminMap.put(user, true);
                    myRefBrotherhood.child(brotherhoodName).child("Admin").setValue(adminMap);
                }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        } ;

        myRefUsers.child(user).child("Brotherhood").child("BrotherhoodName").addListenerForSingleValueEvent(valueSingleEventListener);
        Toast.makeText(this, "Created Brotherhood " + result, Toast.LENGTH_SHORT).show();
    }

}


    @Override
    public void negativeClicked(String result) {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

    }
    // -- Dialog End --

    // Add a list of names
    private void addBrotherhoodList(List<String> names) {
        brotherhoods.clear();
        for (String name : names) {
            brotherhoods.add(new Brotherhood(name));
        }
    }

    // Remove the item_brotherhood from the storage (DB, SharePrefs...)
    private void removeItemDB(final String itemName) {

        // Admin ValueEventListener
        final ValueEventListener adminEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> listUpdated = new ArrayList<>();
                // Iterate through the data and add to a list
                for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                    if (dataTest.getKey() != null && !dataTest.getKey().isEmpty()) {
                        listUpdated.add(dataTest.getKey());

                    }
                }

                Map<String, Object> removeBrotherhoodMap = new HashMap<>();
                removeBrotherhoodMap.put(itemName,null);
                // Remove the brotherhood from each user
                for(String name : listUpdated){
                    myRefUsers.child(name).child("Brotherhood").child("BrotherhoodName").updateChildren(removeBrotherhoodMap);
                }

                // After removed from each user -> Delete the whole Brotherhood
                myRefBrotherhood.child(itemName).setValue(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Delete the Brother if this user is the Admin
        myRefBrotherhood.child(itemName).child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // The user is the Admin
                        if(dataSnapshot.getValue() != null){
                            boolean isAdmin = false;
                            // Iterate through the Admin of Brotherhood , get the name of each admin
                           for(DataSnapshot admin : dataSnapshot.getChildren()){
                               // If the user is one of the Admins -> stops the iteration
                               if(admin.getKey().equals(user)){
                                   isAdmin = true;
                                   break;
                               }
                           }

                            if(isAdmin) {
                                // Delete the Brotherhood
                                myRefBrotherhood.child(itemName).child("Users").addListenerForSingleValueEvent(adminEventListener);
                            } else { // Not Admin -> Remove the user from Brotherhood
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(user,null);
                                // Remove from Debt
                                myRefBrotherhood.child(itemName).child("Debt").updateChildren(userMap);
                                // Remove from Payment
                                myRefBrotherhood.child(itemName).child("Payment").updateChildren(userMap);
                                // Remove from Users
                                myRefBrotherhood.child(itemName).child("Users").updateChildren(userMap);
                                // Remove user from Brotherhood
                                Map<String, Object> brotherhoodMap = new HashMap<>();
                                brotherhoodMap.put(itemName,null);
                                myRefUsers.child(user).child("Brotherhood").child("BrotherhoodName").updateChildren(brotherhoodMap);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    // ----------------------- Sign out START -----------------------
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(this, MainLoginActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sign_out_button) {
            signOut();
            /*1. Dismiss any dialogs the activity was managing.
              2. Close any cursors the activity was managing.
              3. Close any open search dialog*/
            finish();
        }
    }
    // ----------------------- Sign out END -----------------------
}
