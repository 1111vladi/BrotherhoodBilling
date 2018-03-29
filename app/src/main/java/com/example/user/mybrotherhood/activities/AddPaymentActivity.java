package com.example.user.mybrotherhood.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.mybrotherhood.R;

/**
 * Activity where the user use to add payment (item) - Name can be changed to better compatibility
 */

public class AddPaymentActivity extends AppCompatActivity {

    // Start this activity from Floatbutton at the activity MainBrotherhood at the fragment DebtFragment
    // There will be two fields
    // Amount - The user will input a float amount
    // Category - Open a fragment which will have all the categories and new categories could be insert
    // and saved in the DB , the categories will be shown in a RecyclerView


    @Override
    public void onCreate(Bundle savedInstanceState,PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_spent);

    }


    private void addCategoryFragment(){
         /*  // get fragment manager
        FragmentManager fm = getFragmentManager();

// add
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.list_container, new DetailFragment());
// alternatively add it with a tag
// trx.add(R.id.your_placehodler, new YourFragment(), "detail");
        ft.commit();*/
    }
}
