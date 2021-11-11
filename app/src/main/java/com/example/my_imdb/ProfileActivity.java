package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button signoutbtn = findViewById(R.id.signOutButton);
        signoutbtn.setOnClickListener(this);

//Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signOutButton:
                SignOut();
            case R.id.bottomNav1:
                startActivity( new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
        }
    }

    public void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
