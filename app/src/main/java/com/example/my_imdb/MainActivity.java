package com.example.my_imdb;

import static android.util.Log.d;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        }

        Button signinbtn1 = findViewById(R.id.signinbtn1);
        Button cna = findViewById(R.id.cnabtn);

        signinbtn1.setOnClickListener(this);
        cna.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(0, 0);
        } else {
            d("dbug", "user not logged in");
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.signinbtn1:
                i = new Intent(this, SignIn.class);
                startActivity(i);
                break;
            case R.id.cnabtn:
                i = new Intent(this, SignUp.class);
                startActivity(i);
                break;
        }
    }
}
