package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchListAdapter.RecyclerViewClickListener, View.OnClickListener {

    EditText searchInput;
    ImageView searchTrigger;

    RecyclerView recyclerViewSearchList;
    List<Movie> searchList;
    SearchListAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);

        searchTrigger = findViewById(R.id.searchTrigger);
        searchTrigger.setOnClickListener(this);
        recyclerViewSearchList = findViewById(R.id.searchRecyclerView);
        searchList = new ArrayList<>();
        recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchListAdapter(getApplicationContext(), searchList, this);
        recyclerViewSearchList.setAdapter(searchAdapter);

        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout rankBtn = findViewById(R.id.bottomNav2);
        rankBtn.setOnClickListener(this);
        LinearLayout watchlistBtn = findViewById(R.id.bottomNav4);
        watchlistBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);
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
            case R.id.bottomNav4:
                i = new Intent(this, WatchlistActivity.class);
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
            case R.id.searchTrigger:
                searchList.removeAll(searchList);
                fetchSearch();
                break;
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
        i.putExtra("from", "SearchActivity.class");
        startActivity(i);
    }

    Boolean theresMovieOrTV = false;
    private void fetchSearch() {
//        Log.d("dbug", "input " + input);
        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(200); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        searchTrigger.startAnimation(animation);

        String input = searchInput.getText().toString().trim();
        if (searchInput.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Search input is empty", Toast.LENGTH_SHORT).show();
        } else {

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/search/multi?api_key=9e1c6b9dc63ee73be745dbc21b241e65&query=" + input, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray Jarray = response.getJSONArray("results");
//                        Log.d("dbug", "Jarray: " + Jarray);
                        if (Jarray.length() == 0) {
                            searchList.removeAll(searchList);
                            searchAdapter.notifyDataSetChanged();
                            exeToast();
                        } else {

                            for (int i = 0; i < Jarray.length(); i++) {

                                try {
                                    JSONObject movieObject = Jarray.getJSONObject(i);
                                    Movie movie = new Movie();

                                    if (movieObject.getString("media_type").equals("movie")) {
                                        movie.setMovieId(movieObject.getString("id").toString());
                                    } else if (movieObject.getString("media_type").equals("tv")) {
                                        movie.setMovieId("tv" + movieObject.getString("id").toString());
                                    }

                                    if (movieObject.getString("media_type").equals("movie")) {
                                        if (movieObject.getString("title").toString().equals("")) {
                                            movie.setTitle(movieObject.getString("original_name").toString());
                                        } else {
                                            movie.setTitle(movieObject.getString("title").toString());
                                        }
                                    } else if (movieObject.getString("media_type").equals("tv")) {
                                        movie.setTitle(movieObject.getString("name").toString());
                                    } else {
                                        movie.setTitle("-");
                                    }

                                    if (movieObject.getString("media_type").equals("movie")) {
                                        movie.setYear(movieObject.getString("release_date").toString());
                                    } else if (movieObject.getString("media_type").equals("tv")) {
                                        movie.setYear(movieObject.getString("first_air_date").toString());
                                    } else {
                                        movie.setYear("-");
                                    }

                                    movie.setType(movieObject.getString("media_type").toString());

                                    try {
                                        movie.setImDbRating(movieObject.getString("vote_average").toString());
                                    } catch (JSONException e) {
                                        movie.setImDbRating("-");
                                    }

                                    try {
                                        if (movieObject.getString("poster_path").toString().equals("null")) {
                                            movie.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                                        } else {
                                            movie.setImage("https://image.tmdb.org/t/p/w185" + movieObject.getString("poster_path").toString());
                                        }
                                    } catch (JSONException e) {
                                        movie.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                                    }

                                    if (movieObject.getString("media_type").equals("movie") || movieObject.getString("media_type").equals("tv")) {
                                        searchList.add(movie);
                                        theresMovieOrTV = true;
//                                Log.d("dbug", "movie: " + movie);
                                    } else {
                                        Log.d("dbug", "not movie or tv " + movie);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("dbug", "movie: " + e);
                                }
                            }
                            if (theresMovieOrTV) {
                                searchAdapter.notifyDataSetChanged();
                            }else{
                                searchList.removeAll(searchList);
                                searchAdapter.notifyDataSetChanged();
                                exeToast();
                            }
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

    private void exeToast() {
        Toast.makeText(this, "There are no movies that matched your query.", Toast.LENGTH_SHORT).show();
    }
}
