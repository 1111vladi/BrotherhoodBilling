package com.example.user.mybrotherhood.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.mybrotherhood.Category;
import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.activities.AddPaymentActivity;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter.CategoryOnClickListener;
import com.example.user.mybrotherhood.dialog.AddCategoryDialog;
import com.example.user.mybrotherhood.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a test class which is use to test RecyclerView in Fragments
 * all the tutorials are in those websites :
 * https://developer.android.com/samples/RecyclerView/src/com.example.android.recyclerview/RecyclerViewFragment.html
 * https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment
 */

public class AddPaymnet extends Fragment implements AddCategoryDialog.AddCategoryDialogListener, CategoryOnClickListener {


    private RecyclerView mRecyclerView;
    private CategoryRVAdapter mAdapter;
    private List<Category> categories;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private Map<DatabaseReference, ValueEventListener> firebaseListenersMap = new HashMap<>();
    private Set<String> categoriesNames = new HashSet<>();
    private RecyclerView.AdapterDataObserver adapteObserver;
    private String strItemRemoved;
    public SharedPreferences prefs;
    public final String PREF_NAME_CATEGORY = "category_name", categoryName = "names";

    private FirebaseAuth mAuth;
    private String user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rv_category_layout, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(llm);

        prefs = this.getActivity().getSharedPreferences(PREF_NAME_CATEGORY, Context.MODE_PRIVATE);

        // Add FloatingActionButton -> Show a dialog to add Category
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.category_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        // Initialize Firebase DB
        database = FirebaseDatabase.getInstance();

        // Need to initialize the Category List -> Could lead to NullPointerException
        categories = new ArrayList<>();

        initDataset();
        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Get user which is signed in
        user = mAuth.getCurrentUser().getDisplayName();

        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Important to unregister any observer when the fragment life cycle is done
        mAdapter.unregisterAdapterDataObserver(adapteObserver);
        // Iterate through the firebaseListenerMap and remove each listener from the respective firebase reference
        for(Map.Entry<DatabaseReference , ValueEventListener > entry : firebaseListenersMap.entrySet()){
            DatabaseReference ref = entry.getKey();
            ValueEventListener listener = entry.getValue();
            ref.removeEventListener(listener);
        }
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        // Get Category from DB
        myRef = database.getReference("Brotherhood");
        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Build the listener , which get the category list
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> listUpdated = new ArrayList<>();

                        for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                            if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()) {
                                listUpdated.add(dataTest.getValue().toString());

                            }
                        }
                        // Add the names to the Category RecycleView
                        addCategoryList(listUpdated);
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
        if(mAdapter == null){

            mAdapter = new CategoryRVAdapter(categories, this);
            // Set adapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);

            // AdapterObserver - Remove the item from the DB once it's removed from the list
            adapteObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    strItemRemoved = categories.get(positionStart).categoryName;
                    removeItemDB(strItemRemoved);
                }
            };
            mAdapter.registerAdapterDataObserver(adapteObserver);

            // To use Animation for remove and move items
            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);

            // Click Listener to know which item was selected
            mRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Wee" + mRecyclerView.getChildAdapterPosition(view));
                }
            });
        } else {
            // Set adapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
        }

    }
    // Add a single category
    public void addCategoryList(String name) {
        categories.add(new Category(name));
    }

    // Add a list of category
    public void addCategoryList(List<String> names) {
        categories.clear();
        for(String name : names){
            categories.add(new Category(name));
        }
    }

    private void removeItemDB(final String itemName) {
        // Delete the Category if this user is the Admin
        myRef = database.getReference("Brotherhood");
        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // The user is the Admin
                if(dataSnapshot.getValue() != null){
                    boolean isAdmin = false;
                    try {
                        JSONObject adminJson = new JSONObject(dataSnapshot.getValue().toString());
                        isAdmin = adminJson.getBoolean(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // User is the Admin -> Delete the Brotherhood
                    if(isAdmin) {
                        // Delete the Category with a null in the value
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put(itemName, null);
                        myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Category").updateChildren(categoryMap);
                    } else { // Not Admin -> Remove from Brotherhood
                        initDataset();
                        Toast.makeText(getContext(),"Unauthorizied to delete categories",Toast.LENGTH_SHORT).show();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Show dialog
    private void showDialog() {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.setTargetFragment(this, 0);
        addCategoryDialog.show(getFragmentManager(), "addCategory");
    }

    // Once the user add a Category , save it
    @Override
    public void positiveClicked(final String result) {
        if (result != null && !result.isEmpty()) {

            myRef = database.getReference("Brotherhood");
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String categoryName = result;
                    List<String> listUpdated = new ArrayList<>();
                    // Iterate through the list that is saved in the DB and save it in a temporal category list
                    for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                        if (dataTest.getValue() != null && !dataTest.getValue().toString().isEmpty()) {
                            listUpdated.add(dataTest.getValue().toString());
                        }
                    }
                    // Update the category list with the new category
                    listUpdated.add(categoryName);
                    // Update DB Category List
                    myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Category").setValue(listUpdated);
                }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }
            } ;
            myRef.child(FirebaseBrotherhood.getBrotherhoodName()).child("Category").addListenerForSingleValueEvent(valueEventListener);
        }
    }

    @Override
    public void negativeClicked(String result) {
        Toast.makeText(getContext(), "Canceled" + result, Toast.LENGTH_SHORT).show();

    }
    // -- Dialog End --

    // Once the item in RecyclerView is clicked , it will pass those parameters
    @Override
    public void passCategoryName(View view, int position) {
        // Get the name of which the position is at
        String categoryName = categories.get(position).categoryName;
        // Set the name which was selected in the Activity which holds this fragment
        ((AddPaymentActivity) getActivity()).setSelectedCategoryName(categoryName);
    }
}

