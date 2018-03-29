package com.example.user.mybrotherhood.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.mybrotherhood.fragments.Debt;
import com.example.user.mybrotherhood.fragments.Payment;

/**
 * Created by tommy on 29/03/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Payment tab1 = new Payment();
                return tab1;
            case 1:
                Debt tab2 = new Debt();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
