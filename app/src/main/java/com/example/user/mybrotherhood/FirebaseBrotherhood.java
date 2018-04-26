package com.example.user.mybrotherhood;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nico on 26/04/2018.
 */

public class FirebaseBrotherhood {

    private static String brotherhoodName;

    public static String getBrotherhoodName() {
        return brotherhoodName;
    }

    public static void setBrotherhoodName(String name) {
        brotherhoodName = name;
    }
}
