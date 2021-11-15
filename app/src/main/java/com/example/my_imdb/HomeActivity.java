package com.example.my_imdb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieListAdapter.RecyclerViewClickListener, View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout PosterSlider;
    ArrayList<String> listUrl;
    ArrayList<String> listName;
    ArrayList<String> listId;

    RecyclerView recyclerViewTrendingList;
    List<Movie> trending;
    MovieListAdapter trendingAdapter;

    RecyclerView recyclerViewMovieList;
    List<Movie> movies;
    MovieListAdapter movieAdapter;

    RecyclerView recyclerViewTvSeriesList;
    List<Movie> tvSeries;
    MovieListAdapter tvSeriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PosterSlider = findViewById(R.id.slider);
        extractPoster();

        recyclerViewTrendingList = findViewById(R.id.trendingList);
        trending = new ArrayList<>();
        extractTrending();
        recyclerViewTrendingList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingAdapter = new MovieListAdapter(getApplicationContext(), trending, this);
        recyclerViewTrendingList.setAdapter(trendingAdapter);

        recyclerViewMovieList = findViewById(R.id.movieList);
        movies = new ArrayList<>();
        extractMovies();
        recyclerViewMovieList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        movieAdapter = new MovieListAdapter(getApplicationContext(), movies, this);
        recyclerViewMovieList.setAdapter(movieAdapter);

        recyclerViewTvSeriesList = findViewById(R.id.tvSeriesList);
        tvSeries = new ArrayList<>();
        extractTvSeries();
        recyclerViewTvSeriesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        tvSeriesAdapter = new MovieListAdapter(getApplicationContext(), tvSeries, this);
        recyclerViewTvSeriesList.setAdapter(tvSeriesAdapter);

        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout searchBtn = findViewById(R.id.bottomNav3);
        searchBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.bottomNav3:
                i = new Intent(this, SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity(new Intent(this, ProfileActivity.class));
            case R.id.bottomNav5:
                i = new Intent(this, ProfileActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0, 0);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                break;
//                startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    public void onRecyclerClick(String val) {
//        Log.d("dbug", "onRecyclerClick: "+val);
        Intent i = new Intent(this, FullMovieActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(0, 0);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        i.putExtra("key_id", val);
        i.putExtra("from", "HomeActivity.class");
        startActivity(i);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        PosterSlider.stopAutoCycle();
        super.onStop();
    }

    private void extractPoster() {

        listUrl = new ArrayList<>();
        listName = new ArrayList<>();
        listId = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/now_playing?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jarray = response.getJSONArray("results");
//                    Log.d("dbug", "Jarray: " + Jarray);

                    for (int i = 0; i < 4; i++) {
                        try {
                            JSONObject movieObject = Jarray.getJSONObject(i);
                            listUrl.add("https://www.themoviedb.org/t/p/w1920_and_h800_multi_faces" + movieObject.getString("backdrop_path").toString());
                            listName.add(movieObject.getString("title").toString() + " (" + movieObject.getString("release_date").toString().substring(0, 4) + ")");
                            listId.add(movieObject.getString("id").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("dbug", "movie: " + e);
                        }
                    }
                    setPosterImg();

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

    private void setPosterImg() {
//        Log.d("dbug", "poster" + listUrl);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        //.diskCacheStrategy(DiskCacheStrategy.NONE)
        //.placeholder(R.drawable.placeholder)
        //.error(R.drawable.placeholder);

        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView  instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this)
                    .description(listName.get(i));

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra_name", listName.get(i));
            sliderView.getBundle().putString("extra_id", listId.get(i));
//            Log.d("dbug","poster:"+sliderView);
            PosterSlider.addSlider(sliderView);
        }
        // set Slider Transition Animation
        // PosterSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        PosterSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        PosterSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        PosterSlider.setCustomAnimation(new DescriptionAnimation());
        PosterSlider.setDuration(4000);
        PosterSlider.addOnPageChangeListener(this);
    }

    private void extractTrending() {
//        Log.d("dbug", "here: ");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/trending/all/day?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jarray = response.getJSONArray("results");
//                    Log.d("dbug", "Jarray: " + Jarray);


                    for (int i = 0; i < Jarray.length(); i++) {

                        try {
                            JSONObject movieObject = Jarray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setMovieId(movieObject.getString("id").toString());
                            movie.setTitle(movieObject.getString("title").toString() + " (" + movieObject.getString("release_date").toString().substring(0, 4) + ")");
                            movie.setImDbRating(movieObject.getString("vote_average").toString());
                            movie.setImage("https://image.tmdb.org/t/p/w185" + movieObject.getString("poster_path").toString());
                            trending.add(movie);
//                            Log.d("dbug", "movie: " + movie);

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("dbug", "movie: " + e);
                        }
//                        recyclerViewTrendingList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                        trendingAdapter = new MovieListAdapter(getApplicationContext(), trending, globalThis);
//                        recyclerViewTrendingList.setAdapter(trendingAdapter);
                        trendingAdapter.notifyDataSetChanged();
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

    private void extractMovies() {
//        Log.d("dbug", "here: ");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/top_rated?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jarray = response.getJSONArray("results");
//                    Log.d("dbug", "Jarray: " + Jarray);


                    for (int i = 4; i < Jarray.length(); i++) {

                        try {
                            JSONObject movieObject = Jarray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setMovieId(movieObject.getString("id").toString());
                            movie.setTitle(movieObject.getString("title").toString() + " (" + movieObject.getString("release_date").toString().substring(0, 4) + ")");
                            movie.setImDbRating(movieObject.getString("vote_average").toString());
                            movie.setImage("https://image.tmdb.org/t/p/w185" + movieObject.getString("poster_path").toString());
                            movies.add(movie);

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("dbug", "movie: " + e);
                        }
//                        recyclerViewMovieList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                        movieAdapter = new MovieListAdapter(getApplicationContext(), movies, this);
//                        recyclerViewMovieList.setAdapter(movieAdapter);
                        movieAdapter.notifyDataSetChanged();

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

    private void extractTvSeries() {
//        Log.d("dbug", "here: ");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/tv/popular?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Jarray = response.getJSONArray("results");
//                    Log.d("dbug", "Jarray: " + Jarray);


                    for (int i = 0; i < Jarray.length(); i++) {

                        try {
                            JSONObject movieObject = Jarray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setMovieId("tv" + movieObject.getString("id").toString());
                            movie.setTitle(movieObject.getString("name").toString() + " (" + movieObject.getString("first_air_date").toString().substring(0, 4) + ")");
                            movie.setImDbRating(movieObject.getString("vote_average").toString());
                            movie.setImage("https://image.tmdb.org/t/p/w185" + movieObject.getString("poster_path").toString());
                            tvSeries.add(movie);

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("dbug", "movie: " + e);
                        }
//                        recyclerViewTvSeriesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                        tvSeriesAdapter = new MovieListAdapter(getApplicationContext(), tvSeries, globalThis);
//                        recyclerViewTvSeriesList.setAdapter(tvSeriesAdapter);
                        tvSeriesAdapter.notifyDataSetChanged();
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this, slider.getBundle().getString("extra_id") + "", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, FullMovieActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(0, 0);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        i.putExtra("key_id", slider.getBundle().getString("extra_id"));
        startActivity(i);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
