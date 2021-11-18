package com.example.my_imdb;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity implements RankListAdapter.RecyclerViewClickListener, View.OnClickListener {

    BarChart barChart;

    RecyclerView recyclerViewUserRankList;
    List<UserRank> userRankList;
    RankListAdapter rankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        //Bottom Navigator
        LinearLayout homeBtn = findViewById(R.id.bottomNav1);
        homeBtn.setOnClickListener(this);
        LinearLayout searchBtn = findViewById(R.id.bottomNav3);
        searchBtn.setOnClickListener(this);
        LinearLayout watchlistBtn = findViewById(R.id.bottomNav4);
        watchlistBtn.setOnClickListener(this);
        LinearLayout profileBtn = findViewById(R.id.bottomNav5);
        profileBtn.setOnClickListener(this);

        barChart = findViewById(R.id.barChart);
        barChart.setNoDataText("");


        recyclerViewUserRankList = findViewById(R.id.userRankRecyclerView);


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
//        Intent i = new Intent(this, FullMovieActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        overridePendingTransition(0, 0);
//        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//        i.putExtra("key_id", val);
//        i.putExtra("from", "SearchActivity.class");
//        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();

        userRankList = new ArrayList<>();

        Log.d("dbug", "start");
        readFirestoreData(new FirebaseCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCallback(List<UserRank> userIdList) {
                Log.d("dbug", "allUid " + userIdList.get(5).getUserName() + userIdList.get(5).getRankMovieInt());

                Comparator<UserRank> compareById =
                        (UserRank o1, UserRank o2) -> o1.getRankMovieInt().compareTo(o2.getRankMovieInt());

                Collections.sort(userIdList, compareById.reversed());

                if (userIdList.size() >= 3) {
                    setBarChart(parseInt(userIdList.get(1).getRankMovieInt()), userIdList.get(1).getUserName(), parseInt(userIdList.get(0).getRankMovieInt()), userIdList.get(0).getUserName(), parseInt(userIdList.get(2).getRankMovieInt()), userIdList.get(2).getUserName());
                }
//                rankListAdapter.notifyDataSetChanged();
//                for (int i = 0; i < userIdList.size(); i++) {
//                    Log.d("dbug", "allUid " + userIdList.get(i).getUserRank());
//                }
                Log.d("dbug", "allUid " + userRankList.size());
                setUserList();
//                rankListAdapter.notifyDataSetChanged();
//                Log.d("dbug", "allUid "+ userIdList.UserRank );
            }
        });

    }

    public void setUserList(){
        recyclerViewUserRankList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rankListAdapter = new RankListAdapter(getApplicationContext(), userRankList, this);
        recyclerViewUserRankList.setAdapter(rankListAdapter);
    }

    private void readFirestoreData(FirebaseCallback firebaseCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("dbug", document.getData().toString());

                                UserRank userRank = new UserRank(document.get("name").toString(), document.get("rank_int").toString());
                                userRankList.add(userRank);

                            }
                            firebaseCallback.onCallback(userRankList);

                        } else {
                            Log.d("dbug", "Error getting allUserId: ", task.getException());
                        }
                    }
                });
    }

    private interface FirebaseCallback {
        void onCallback(List<UserRank> userIdList);
    }

    private void setBarChart(int d1, String s1, int d2, String s2, int d3, String s3) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(d1, 0));
        barEntries.add(new BarEntry(d2, 1));
        barEntries.add(new BarEntry(d3, 2));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setBarSpacePercent(50f);
        barDataSet.setColors(
                new int[]{Color.parseColor("#C0C0C0"), Color.parseColor("#f0ca00"), Color.parseColor("#de8126")}
        );
        barDataSet.setValueTextColor(Color.parseColor("#ffffff"));
        barDataSet.setValueTextSize(14);

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add(s1);
        theDates.add(s2);
        theDates.add(s3);

        BarData barData = new BarData(theDates, barDataSet);
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        barData.setValueFormatter(vf);

        barChart.setData(barData);
        barChart.animateY(1500);
        barChart.setDescription("");
        barChart.setDrawValueAboveBar(true);

        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getXAxis().setAxisLineWidth(1.1f);
        barChart.getXAxis().setAxisLineColor(Color.parseColor("#ffffff"));
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLimitLinesBehindData(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
//        barChart.getXAxis().setDrawLabels(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setTextSize(14);
        barChart.getXAxis().setDrawLimitLinesBehindData(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
        barChart.getAxisRight().setTextColor(Color.parseColor("#ffffff"));
        barChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        barChart.getLegend().setTextColor(Color.parseColor("#ffffff"));
    }

}


