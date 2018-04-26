package com.example.user.mybrotherhood;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 3/21/2018.
 */



public class Brotherhood {
    // IS
    public String brotherhoodName;

    private List<Brotherhood> brotherhoodList = new ArrayList<>();


    // HAS
    public Brotherhood(String brotherhoodName){
        this.brotherhoodName = brotherhoodName;
    }





}
