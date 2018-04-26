package com.example.user.mybrotherhood.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nico on 22/04/2018.
 */

public class AddMemberActivity extends AppCompatActivity {

    private EditText searchValue;
    private TextView nameDisplay;
    private SharedPreferences pref;
    private final static String PREF_NAME = "members_name", memNames = "names";
    private Set<String> memberNamesSet = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        searchValue = (EditText) findViewById(R.id.search_value);
        nameDisplay = (TextView) findViewById(R.id.tv_member_name);

        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Test in adding names
        memberNamesSet.add("Vlad");
        memberNamesSet.add("Trynx");
        memberNamesSet.add("Tommi");
        memberNamesSet.add("Koichy");
        pref.edit().putStringSet(memNames, memberNamesSet).apply();
    }

    public void searchMember(View view) {
        String nameStr = searchValue.getText().toString();
        if (!nameStr.isEmpty()) {
            // Search in DB
            pref.getStringSet(memNames, memberNamesSet);
            System.out.println("Names: " + memberNamesSet.toString()); // TEST
            if (memberNamesSet.contains(nameStr)) {
                for (String memberName : memberNamesSet) {
                    if (memberName.equalsIgnoreCase(nameStr)) {
                        System.out.println("Found");
                        nameDisplay.setText(nameStr);
                    }
                }
            }
        }
    }
}
