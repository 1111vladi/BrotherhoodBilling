package com.example.user.mybrotherhood.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.mybrotherhood.DetailFragment;
import com.example.user.mybrotherhood.R;

/**
 * Created by user on 2/4/2018.
 */


// TODO - Might not be necessary

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

// get fragment manager
        FragmentManager fm = getFragmentManager();

// add
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.list_container, new DetailFragment());
// alternatively add it with a tag
// trx.add(R.id.your_placehodler, new YourFragment(), "detail");
        ft.commit();

    }
}
