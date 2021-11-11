package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView recyclerView;
    List<Movie> movies;
    MovieListAdapter movieAdapter;
    private static String JSON_URL = "https://imdb-api.com/en/API/Top250Movies/k_k0ep63j0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.movieList);
        movies = new ArrayList<>();
        extractMovies();

        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomNav5:
                startActivity( new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
        }
    }



    private void extractMovies() {
        Log.d("dbug", "here: ");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jarray = response.getJSONArray("items");
//                    Log.d("dbug", "Jarray: " + Jarray);


                    for (int i = 0; i < Jarray.length(); i++) {

                        try {
                            JSONObject movieObject = Jarray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setTitle(movieObject.getString("fullTitle").toString());
                            movie.setImDbRating(movieObject.getString("imDbRating").toString());
                            movie.setImage(movieObject.getString("image").toString());
                            movies.add(movie);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        movieAdapter = new MovieListAdapter(getApplicationContext(), movies);
                        recyclerView.setAdapter(movieAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dbug", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

}
