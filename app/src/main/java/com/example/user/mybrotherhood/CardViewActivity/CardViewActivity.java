package com.example.user.mybrotherhood.CardViewActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;

public class CardViewActivity extends Activity {

    TextView brotherhoodName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_brotherhood_layout);

        brotherhoodName = (TextView)findViewById(R.id.brotherhoodName);




    }
}
