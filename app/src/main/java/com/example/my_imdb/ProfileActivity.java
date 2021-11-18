package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button signoutbtn = findViewById(R.id.signOutButton);
        signoutbtn.setOnClickListener(this);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

// Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.DEFAULT;

// Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
//                    Log.d("dbug", "Cached document data: " + document.getData().get("profile"));

                    ImageView profileImg = findViewById(R.id.profileImg);
                    Picasso.get().load(document.getData().get("profile").toString()).fit().centerCrop().into(profileImg);

                    TextView nameTextView = findViewById(R.id.nameTextView);
                    nameTextView.setText(document.getData().get("name").toString());

                    TextView emailTextView = findViewById(R.id.emailTextView);
                    emailTextView.setText(document.getData().get("email").toString());
                } else {
                    Log.d("dbug", "Cached get failed: ", task.getException());
                }
            }
        });


        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout rankBtn = findViewById(R.id.bottomNav2);
        rankBtn.setOnClickListener(this);
        LinearLayout searchBtn = findViewById(R.id.bottomNav3);
        searchBtn.setOnClickListener(this);
        LinearLayout watchlistBtn = findViewById(R.id.bottomNav4);
        watchlistBtn.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.signOutButton:
                SignOut();
                break;
            case R.id.bottomNav1:
                i = new Intent(this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity( new Intent(this, MainActivity.class));
            case R.id.bottomNav2:
                i = new Intent(this, RankActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity( new Intent(this, MainActivity.class));
            case R.id.bottomNav3:
                i = new Intent(this, SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity( new Intent(this, MainActivity.class));
            case R.id.bottomNav4:
                i = new Intent(this, WatchlistActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//            startActivity( new Intent(this, MainActivity.class));
        }
    }

    public void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
