package com.example.user.mybrotherhood.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.user.mybrotherhood.adapters.BrotherhoodRVAdapter;
import com.example.user.mybrotherhood.Brotherhood;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter;
import com.example.user.mybrotherhood.dialog.AddBrotherhoodDialog;
import com.example.user.mybrotherhood.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.example.user.mybrotherhood.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 3/21/2018.
 */

public class CreateBrotherhoodActivity extends AppCompatActivity implements
        AddBrotherhoodDialog.AddBrotherhoodDialogListener,
        View.OnClickListener{


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private static final String TAG_DB = "tag_db";
    private List<Brotherhood> brotherhoods;
    private RecyclerView rv;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "brotherhood_names", broNames = "names";
    private Set<String> brotherhoodNames = new HashSet<>();
    private ItemTouchHelper touchHelper;
    private RecyclerView.AdapterDataObserver adapterObserver;
    private BrotherhoodRVAdapter adapter;
    private String strItemRemoved;
    private String user;

    boolean isValueEnter = false;

    private FirebaseAuth mAuth;

    // A client for interacting with the Google Sign In API.
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_brotherhood_layout);


        // Change Status bar background
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.grey_500));



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

        // Initiate FirebaseDatabase
        database = FirebaseDatabase.getInstance();

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        String userGoogleEmail = mAuth.getCurrentUser().getEmail();
        String[] arr = userGoogleEmail.split("@");
        user = arr[0];

        connectToDB("Users", user);
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

    private void initializeData() {
        // TODO - Get the brotherhoods from the DB, Maybe do a class to do all the database transitions
        myRef.child(user).child("Brotherhood").child("BrotherhoodName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataTest : dataSnapshot.getChildren()){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        brotherhoods = new ArrayList<>();
        // Get a set of all the names from the shared preference
        Set<String> names = prefs.getStringSet(broNames, null);
        if (names != null) {
            // Add the set to the hashset brotherhoodNames
            brotherhoodNames.addAll(names);

            // Add the names to the recycleview
            for (String name : brotherhoodNames) {
                addBrotherhoodList(name);
            }
        }

    }

    private void initializeAdapter() {
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

    private void connectToDB(String users, final String username) {


        myRef = database.getReference(users);


        myRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    List<String> id = new ArrayList<String>();
                    id.add("");

                    myRef.child(username).child("Brotherhood").child("BrotherhoodName").setValue(id);
                    System.out.println("------------- Doesn't Exist ------------" + dataSnapshot.toString());
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

    // Once the user created a new brotherhood , save it
    @Override
    public void positiveClicked(final String result) {
        // TODO - Add the brotherhood in DB
        if (result != null && !result.isEmpty()) {
            // Test
            myRef = database.getReference("Users");
            List<String> addBrotherhood = new ArrayList<>();
            addBrotherhood.add(result);



            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> userUpdates = new HashMap<>();
                    if(!isValueEnter){

                        System.out.println("---------------------" + dataSnapshot.getChildren().toString());
                        for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                            if(dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()){

                                userUpdates.put(dataTest.getKey(), dataTest.getValue());
                                System.out.println(" ----------------------------------- " + dataTest.getKey() + " " + dataTest.getValue());
                            }
                        }

                        userUpdates.put(Integer.toString(userUpdates.size()), result);
                        myRef.child(user).child("Brotherhood").child("BrotherhoodName").updateChildren(userUpdates);
                        changeValueEntry();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            // TODO - Make it work( not overwrite/delete on click )
                myRef.child(user).child("Brotherhood").child("BrotherhoodName").addValueEventListener(valueEventListener);
                changeValueEntry();

//            myRef = database... Brotherhood - BroName Array - Cat Array / Users


            // Add the name to the set
            brotherhoodNames.add(result);
            // Then save in the shared preference
            prefs.edit().putStringSet(broNames, brotherhoodNames).apply();
            // Eventually add to the recycleview
            addBrotherhoodList(result);
            Toast.makeText(this, "Added " + result, Toast.LENGTH_SHORT).show();
        }
    }


    // Util method for PositiveClicked
    private void changeValueEntry() {
        isValueEnter = !isValueEnter;
    }

    @Override
    public void negativeClicked(String result) {
        Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();

    }
    // -- Dialog End --

    private void addBrotherhoodList(String name) {
        brotherhoods.add(new Brotherhood(name));
    }

    // Remove the item_brotherhood from the storage (DB, SharePrefs...)
    // TODO - Remove from DB the item_brotherhood
    private void removeItemDB(String itemName) {
        // First remove the item_brotherhood from the RecyclerView
        brotherhoodNames.remove(itemName);
        // Secondly overwrite the new list(StringSet) in the share preference
        prefs.edit().putStringSet(broNames, brotherhoodNames).apply();
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

    private void updateUI(FirebaseUser user){
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


// How to check Users
// if the user doesn't exist
//  key = VlSad, value = null

