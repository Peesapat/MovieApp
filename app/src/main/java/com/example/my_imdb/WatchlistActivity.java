package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WatchlistActivity extends AppCompatActivity implements SearchListAdapter.RecyclerViewClickListener, View.OnClickListener {

    RecyclerView recyclerViewWatchlist;
    List<Movie> watchlistList;
    SearchListAdapter watchlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout rankBtn = findViewById(R.id.bottomNav2);
        rankBtn.setOnClickListener(this);
        LinearLayout searchBtn = findViewById(R.id.bottomNav3);
        searchBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);
//        Log.d("dbug", "fetchWatchlistFirestore");
        fetchWatchlistFirestore();

        recyclerViewWatchlist = findViewById(R.id.watchlistRecyclerView);
        watchlistList = new ArrayList<>();
        recyclerViewWatchlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        watchlistAdapter = new SearchListAdapter(getApplicationContext(), watchlistList, this);
        recyclerViewWatchlist.setAdapter(watchlistAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.bottomNav1:
                i = new Intent(this, MainActivity.class);
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
            case R.id.bottomNav5:
                i = new Intent(this, ProfileActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity( new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onRecyclerClick(String val) {
//        Log.d("dbug", "onRecyclerClick: " + val);
        Intent i = new Intent(this, FullMovieActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(0, 0);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        i.putExtra("key_id", val);
        i.putExtra("from", "WatchlistActivity.class");
        startActivity(i);
    }

    Boolean theresMovieOrTV = false;

    private void fetchWatchlistFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("watchlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("dbug", "" + document.getData().get("type").toString() + " " + document.getData().get("movie_id").toString());
                                fetchSearchAPI(document.getData().get("type").toString(), document.getData().get("movie_id").toString());
                            }

                        } else {
                            Log.d("dbug", "Error getting watchlist: ", task.getException());
                        }
                    }
                });
    }

    private void fetchSearchAPI(String type, String key) {

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/" + type + "/" + key + "?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Movie movie = new Movie();

                    if (type.equals("movie")) {
                        movie.setMovieId(key);
                    } else if (type.equals("tv")) {
                        movie.setMovieId("tv" + key);
                    }

                    if (type.equals("movie")) {
                            movie.setTitle(response.getString("title").toString());
                    } else if (type.equals("tv")) {
                        movie.setTitle(response.getString("name").toString());
                    } else {
                        movie.setTitle("-");
                    }

                    if (type.equals("movie")) {
                        movie.setYear(response.getString("release_date").toString());
                    } else if (type.equals("tv")) {
                        movie.setYear(response.getString("first_air_date").toString());
                    } else {
                        movie.setYear("-");
                    }

                    movie.setType(type);

                    try {
                        movie.setImDbRating(response.getString("vote_average").toString());
                    } catch (JSONException e) {
                        movie.setImDbRating("-");
                    }

                    try {
                        if (response.getString("poster_path").toString().equals("null")) {
                            movie.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                        } else {
                            movie.setImage("https://image.tmdb.org/t/p/w185" + response.getString("poster_path").toString());
                        }
                    } catch (JSONException e) {
                        movie.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                    }

//                    Log.d("dbug", "" +movie.getTitle());
                    watchlistList.add(movie);
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                    Log.d("dbug", "e data actors: " + e);
                }

                watchlistAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dbug", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void exeToast() {
        Toast.makeText(this, "There are no movies that matched your query.", Toast.LENGTH_SHORT).show();
    }
}
