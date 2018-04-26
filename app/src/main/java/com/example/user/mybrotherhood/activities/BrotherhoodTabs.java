package com.example.user.mybrotherhood.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.adapters.PagerAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 3/21/2018.
 */

public class BrotherhoodTabs extends AppCompatActivity {

    private SharedPreferences pref;
    private final static String PREF_NAME = "members_name", memNames = "names";
    private Set<String> memberNamesSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brotherhood_tabs);

        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Test in adding names
        memberNamesSet.add("Vlad");
        memberNamesSet.add("Trynx");
        memberNamesSet.add("Tommi");
        memberNamesSet.add("Koichy");
        pref.edit().putStringSet(memNames, memberNamesSet).apply();

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
            String query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            // Continue the search with the query in the DB
            if (!query.isEmpty()) {
                // Search in DB
                pref.getStringSet(memNames, memberNamesSet);
                System.out.println("Names: " + memberNamesSet.toString()); // TEST
//                if (memberNamesSet.contains(query)) {
                    for (String memberName : memberNamesSet) {
                        if (memberName.equalsIgnoreCase(query)) {
                            System.out.println("Found");
                            Toast.makeText(this,"Found member: " + memberName,Toast.LENGTH_SHORT).show();
                        }
                    }
//                }
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
                startActivity(new Intent(this, AddMemberActivity.class));
        }
        return false;
    }

    // -- End Menu Options
}
