package com.example.user.mybrotherhood.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.PagerAdapter;
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
 * Created by user on 3/21/2018.
 */

public class BrotherhoodTabs extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String user;
    // Only other activities in the package can use this String
    static final String FOUND_USERS_LIST = "FOUND_USERS_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brotherhood_tabs);

        handleIntent(getIntent());

        // Change Status bar background
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.teal_700));

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Get user which is signed in
        user = mAuth.getCurrentUser().getDisplayName();

        // Firebase Database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Brotherhood");


        TabLayout tabLayout = (TabLayout) findViewById(R.id.ingrouptab);
        tabLayout.addTab(tabLayout.newTab().setText("Payment"));
        tabLayout.addTab(tabLayout.newTab().setText("Debt"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            // Get the search value , also convert to lower case for easier search
            final String query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            // Continue the search with the query in the DB
            if (!query.isEmpty()) {
                // Get Users from the Brotherhood
                myRef = database.getReference("Users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> listUpdated = new ArrayList<>();
                        // Iterate through all the Users and add to a list
                        for (DataSnapshot dataTest : dataSnapshot.getChildren()) {
                                String userName = dataTest.getKey();
                            System.out.println("**********" + userName);

                            // Compare - true when it contain the sequel
                                if (!userName.isEmpty() && userName.toLowerCase().contains(query)) {
                                    listUpdated.add(userName);
                                    System.out.println("**********" + userName);
                                }
                        }

                        // If list not empty
                        if(listUpdated.size() > 0){
                            // Start a new activity with the founded user list
                            Intent intent = new Intent(getBaseContext(), FoundUserActivity.class);
                            // Send the list to the next activity
                            intent.putExtra(FOUND_USERS_LIST,listUpdated.toArray(new String[listUpdated.size()]));
                            startActivity(intent);
                        }

                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                });
            }
        }
    }

    // -- Start Menu Options --
    // Which type of layout to use in the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    // Which options to add in the menu and their action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_member:
                System.out.println("Baah");
                startActivity(new Intent(this, ShowUsersActivity.class));
        }
        return false;
    }

    // -- End Menu Options
}
