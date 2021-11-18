package com.example.my_imdb;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRank {

    public String userName;
    public String rankMovieInt;

    public String getUserRank() {
        return userName +" " + rankMovieInt;
    }

    public String getRankMovieInt() {
        return rankMovieInt;
    }

    public void setRankMovieInt(String watchedMovieInt) {
        this.rankMovieInt = watchedMovieInt;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public UserRank() {
    }

    public UserRank(String userName, String watchedMovieInt) {
        this.userName = userName;
        this.rankMovieInt = watchedMovieInt;
    }

}
