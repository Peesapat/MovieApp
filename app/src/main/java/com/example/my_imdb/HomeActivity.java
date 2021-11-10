package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button signoutbtn = findViewById(R.id.signOutButton);
        signoutbtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.signOutButton: SignOut();
        }
    }

    public void SignOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
