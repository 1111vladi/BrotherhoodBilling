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
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.activities.AddPaymentActivity;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter;
import com.example.user.mybrotherhood.adapters.CategoryRVAdapter.CategoryOnClickListener;
import com.example.user.mybrotherhood.dialog.AddCategoryDialog;
import com.example.user.mybrotherhood.itemtouchhelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Set<String> categoriesNames = new HashSet<>();
    private RecyclerView.AdapterDataObserver adapteObserver;
    private String strItemRemoved;
    public SharedPreferences prefs;
    public final String PREF_NAME_CATEGORY = "category_name", categoryName = "names";


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

        initDataset();
        initializeAdapter();

        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Important to unregister any observer when the fragment life cycle is done
        mAdapter.unregisterAdapterDataObserver(adapteObserver);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    // TODO - Get data from the DB , Maybe do a class to do all the database transitions
    private void initDataset() {
        categories = new ArrayList<>();
        // Default Data
         /*categories.add(new Category("Water"));
         categories.add(new Category("Electricity"));
         categories.add(new Category("Rent"));
         categories.add(new Category("Vlad's Shit"));*/

        // The data in the recyclerview
        Set<String> names = prefs.getStringSet(categoryName, null);
        if (names != null) {
            // Add the names to the haseset
            categoriesNames.addAll(names);
            // Iterate through each name and add to the category list
            for (String name : categoriesNames) {
                addCategoryList(name);
            }
        }

    }

    private void initializeAdapter() {
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

    }

    public void addCategoryList(String name) {
        categories.add(new Category(name));
    }

    private void removeItemDB(String itemName) {
        // First remove the item from the recyclerview
        categoriesNames.remove(itemName);
        // Secondly overwrite the new list(StringSet) in the share preference
        prefs.edit().putStringSet(categoryName, categoriesNames).apply();
    }

    // Show dialog
    private void showDialog() {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.setTargetFragment(this, 0);
        addCategoryDialog.show(getFragmentManager(), "addCategory");
    }

    // Once the user add a Category , save it
    @Override
    public void positiveClicked(String result) {
        // TODO - Change to DB
        if (result != null && !result.isEmpty()) {
            // Add the new name to the set
            categoriesNames.add(result);
            // Add to the Shared Preference (DB)
            prefs.edit().putStringSet(categoryName, categoriesNames).apply();
            addCategoryList(result);
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

