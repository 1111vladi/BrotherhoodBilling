package com.example.user.mybrotherhood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.mybrotherhood.activities.CreateBrotherhoodActivity;
import com.example.user.mybrotherhood.activities.RegisterActivity;

public class MainLoginActivity extends AppCompatActivity {

    Button loginBtn,registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginBtn = (Button)findViewById(R.id.loginBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);




    }

    // TODO - Check if the user exists
    // If the user input is correct log in
    public void onClickLogin(View view) {
        Intent intent = new Intent(this, CreateBrotherhoodActivity.class);
        startActivity(intent);

    }
    // TODO - Users will be able to register/login using an existing google account
    // Goes to a RegisterActivity
    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
