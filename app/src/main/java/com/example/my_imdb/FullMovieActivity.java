package com.example.my_imdb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullMovieActivity extends AppCompatActivity implements View.OnClickListener {

    String key_id;
    String from_class;
    String type;

    CardView watchlistCardView;
    ImageButton watchlistButton;
    CardView rankCardView;
    ImageButton rankButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_movie);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key_id = extras.getString("key_id");
            from_class = extras.getString("from");
//            Log.d("dbug", "key: " + key_id);
            if (key_id.substring(0, 2).equals("tv")) {
                type = "tv";
                key_id = key_id.substring(2, extras.getString("key_id").length());
            } else {
                type = "movie";
            }

            ImageButton backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(this);

            fetchDataActors();
            fetchData();
            fetchWatchlistAndRank();

            watchlistCardView = findViewById(R.id.watchlistCardView);
            watchlistButton = findViewById(R.id.watchlistButton);
            watchlistButton.setOnClickListener(this);

            rankCardView = findViewById(R.id.rankCardView);
            rankButton = findViewById(R.id.rankButton);
            rankButton.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.backButton:
                if (from_class.equals("HomeActivity.class")) {
                    i = new Intent(this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    break;
                } else if (from_class.equals("SearchActivity.class")) {
                    i = new Intent(this, SearchActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    break;
                } else if (from_class.equals("WatchlistActivity.class")) {
                    i = new Intent(this, WatchlistActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    break;
                } else{
                    i = new Intent(this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(0, 0);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    break;
                }
            case R.id.watchlistButton:
                addToWatchlist();
                break;
            case R.id.rankButton:
                addRank();
                break;
        }
    }

    public void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/" + type + "/" + key_id + "?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    JSONArray Jarray = response.getJSONArray("results");
//                    Log.d("dbug", "Jarray: " + response.getString("backdrop_path"));

                    ImageView backdropImageView = findViewById(R.id.backdropImageView);
                    Picasso.get().load("https://www.themoviedb.org/t/p/w1920_and_h800_multi_faces" + response.getString("backdrop_path")).fit().centerCrop().into(backdropImageView);

                    ImageView posterImageView = findViewById(R.id.posterImageView);
                    try {
                        if (response.getString("poster_path").toString().equals("null")) {
                            Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                        } else {
                            Picasso.get().load("https://image.tmdb.org/t/p/w185" + response.getString("poster_path")).fit().centerCrop().into(posterImageView);
                        }
                    } catch (JSONException e) {
                        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSNUolEReqL0k9etUdLqyqr_yhlYYleykCwdmIEs1DteHxkZiWZ6xUSVGewMAFAf9JhYg&usqp=CAU");
                    }

                    TextView fullMovieTitle = findViewById(R.id.fullMovieTitle);
                    try {
                        if (type.equals("tv")) {
                            fullMovieTitle.setText(response.getString("name"));
                            if (response.getString("name").length() <= 17) {
                                fullMovieTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            }
                        } else {
                            fullMovieTitle.setText(response.getString("title"));
                            if (response.getString("title").length() <= 17) {
                                fullMovieTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            }
                        }
                    } catch (JSONException e) {
                        fullMovieTitle.setText("-");
                    }

                    TextView releaseDate = findViewById(R.id.releaseDate);
                    try {
                        if (type.equals("tv")) {
                            releaseDate.setText(response.getString("first_air_date"));
                        } else {
                            releaseDate.setText(response.getString("release_date"));
                        }
                    } catch (JSONException e) {
                        releaseDate.setText("-");
                    }

                    TextView duration = findViewById(R.id.duration);
                    try {
                        if (type.equals("tv")) {
                            duration.setText(response.getJSONArray("episode_run_time").get(0).toString() + " min");
                        } else {
                            duration.setText(response.getString("runtime") + " min");
                        }
                    } catch (JSONException e) {
                        duration.setText("-");
                    }

                    TextView genre = findViewById(R.id.genre);
                    try {
                        if (response.getJSONArray("genres").length() == 0) {
                            genre.setText("-");
                        } else {
                            try {
                                genre.setText(response.getJSONArray("genres").getJSONObject(0).getString("name") + ", " + response.getJSONArray("genres").getJSONObject(1).getString("name"));
                            } catch (JSONException e) {
                                genre.setText(response.getJSONArray("genres").getJSONObject(0).getString("name"));
                            }
                        }
                    } catch (JSONException e) {
                        genre.setText("-");
                    }

                    TextView ratings = findViewById(R.id.ratings);
                    try {
                        ratings.setText(response.getString("vote_average"));
                    } catch (JSONException e) {
                        ratings.setText("-");
                    }

                    TextView overviewText = findViewById(R.id.overviewText);
                    try {
                        overviewText.setText(response.getString("overview"));
                    } catch (JSONException e) {
                        overviewText.setText("-");
                    }

                    TextView budget = findViewById(R.id.budget);
                    try {
                        if (response.getString("budget").equals("0")) {
                            budget.setText("—");
                        } else {
                            NumberFormat nfBudget = NumberFormat.getCurrencyInstance();
                            String budgetFormat = nfBudget.format(new BigDecimal(response.getString("budget")));
                            //                    Log.d("dbug","budget :"+ budgetFormat.substring(0, budgetFormat.length() - 3));
                            budget.setText(budgetFormat.substring(0, budgetFormat.length() - 3));
                        }
                    } catch (JSONException e) {
                        budget.setText("—");
                    }

                    TextView revenue = findViewById(R.id.revenue);
                    try {
                        if (response.getString("revenue").equals("0")) {
                            revenue.setText("—");
                        } else {
                            NumberFormat nfRevenue = NumberFormat.getCurrencyInstance();
                            String revenueFormat = nfRevenue.format(new BigDecimal(response.getString("revenue")));
                            //                    Log.d("dbug","budget :"+ revenueFormat.substring(0, budgetFormat.length() - 3));
                            revenue.setText(revenueFormat.substring(0, revenueFormat.length() - 3));
                        }
                    } catch (JSONException e) {
                        revenue.setText("—");
                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                    Log.d("dbug", "e data: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dbug", "onErrorResponse1: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void fetchDataActors() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.themoviedb.org/3/" + type + "/" + key_id + "/credits?api_key=9e1c6b9dc63ee73be745dbc21b241e65", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<String> actorArray = new ArrayList<>();
                try {
                    JSONArray Jarray = response.getJSONArray("cast");
//                    Log.d("dbug", "Jarray: " + Jarray.getJSONObject(0));

                    TextView actorsText = findViewById(R.id.actorsText);
                    if (Jarray.length() == 0) {
                        actorsText.setText("-");
                    } else {
                        int arrLen = 5;
                        if (Jarray.length() <= 5) {
                            arrLen = Jarray.length();
                        }
                        for (int i = 0; i < arrLen; i++) {
                            try {
                                JSONObject actorArrayObject = Jarray.getJSONObject(i);
//                            Log.d("dbug", "actor " + actorArrayObject.getString("name") + " (" + actorArrayObject.getString("character") + ")");
                                if (actorArrayObject.getString("character").equals("")) {
                                    actorArray.add(actorArrayObject.getString("name"));
                                } else {
                                    actorArray.add(actorArrayObject.getString("name") + " (" + actorArrayObject.getString("character") + ")");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("dbug", "actorArray ex: " + e);
                            }
                        }
//                    setPosterImg();
                        String combinedActorList = android.text.TextUtils.join(", ", actorArray);
                        actorsText.setText(combinedActorList);
                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                    Log.d("dbug", "e data actors: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dbug", "onErrorResponse2: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    Boolean watched = false;
    Boolean ranked = false;

    public void fetchWatchlistAndRank() {

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
                                if (document.getId().equals(key_id)) {
                                    watchlistCardView.setCardBackgroundColor(Color.parseColor("#636161"));
                                    watched = true;
                                }
//                                Log.d("dbug", document.getId());
                            }
//                            Log.d("dbug", "" + watched);
                        } else {
                            Log.d("dbug", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("rank")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(key_id)) {
                                    rankCardView.setCardBackgroundColor(Color.parseColor("#636161"));
                                    ranked = true;
                                }
                                Log.d("dbug", document.getId());
                            }
                            Log.d("dbug", "" + ranked);
                        } else {
                            Log.d("dbug", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addToWatchlist() {
        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(200); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        watchlistButton.startAnimation(animation);

//        Log.d("dbug", "watchlist "+ type + key_id);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (watched) {
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("watchlist")
                    .document(key_id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            watched = false;
                            watchlistCardView.setCardBackgroundColor(Color.parseColor("#FFFF4D40"));
                            Toast.makeText(FullMovieActivity.this, "Removed from watchlist ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("dbug", "Error adding watchlist", e);
                        }
                    });
        } else {
            Map<String, Object> watchlistId = new HashMap<>();
            watchlistId.put("watchlist_key", key_id);
            watchlistId.put("type", type);
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("watchlist")
                    .document(key_id)
                    .set(watchlistId)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
//                                                Log.d("dbug", "DocumentSnapshot added with ID: " + documentReference.getId());
                            watched = true;
                            watchlistCardView.setCardBackgroundColor(Color.parseColor("#636161"));
                            Toast.makeText(FullMovieActivity.this, "successfully added to watchlist ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbug", "Error adding watchlist", e);
                        }
                    });
        }

    }

    public void addRank() {
        Animation animation = new AlphaAnimation((float) 0.5, 0); // Change alpha from fully visible to invisible
        animation.setDuration(200); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        rankButton.startAnimation(animation);

//        Log.d("dbug", "watchlist "+ type + key_id);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (ranked) {
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("rank")
                    .document(key_id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ranked = false;
                            rankCardView.setCardBackgroundColor(Color.parseColor("#F1B400"));
                            Toast.makeText(FullMovieActivity.this, "Removed from rank ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("dbug", "Error adding rank1", e);
                        }
                    });
        } else {
            Map<String, Object> watchlistId = new HashMap<>();
            watchlistId.put("watchlist_key", key_id);
            watchlistId.put("type", type);
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("rank")
                    .document(key_id)
                    .set(watchlistId)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
//                                                Log.d("dbug", "DocumentSnapshot added with ID: " + documentReference.getId());
                            ranked = true;
                            rankCardView.setCardBackgroundColor(Color.parseColor("#636161"));
                            Toast.makeText(FullMovieActivity.this, "successfully added to rank2 ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbug", "Error adding rank2", e);
                        }
                    });
        }
    }

}
