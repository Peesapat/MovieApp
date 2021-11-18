package com.example.my_imdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {

    private RankListAdapter.RecyclerViewClickListener sRecyclerViewClickListener;

    LayoutInflater inflater;
    List<UserRank> userRankList;

    public RankListAdapter(Context context, List<UserRank> userRank,
                           RankListAdapter.RecyclerViewClickListener recyclerViewClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.userRankList = userRank;
        this.sRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public RankListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        Log.d("dbug", "inflate cardview");
        View view = inflater.inflate(R.layout.fragment_rank_names, parent, false);
        return new RankListAdapter.ViewHolder(view, sRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RankListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Bind
//        Log.d("dbug", "" + position + userRankList.get(position).getRankMovieInt());
        holder.rankUserName.setText(userRankList.get(position).getUserName());// userRankList.get(position).getUserName())
        holder.rankId.setText(userRankList.get(position).getRankMovieInt());
        if (position == 0) {
            Picasso.get().load("https://cdn-icons-png.flaticon.com/512/2583/2583344.png").fit().centerCrop().into(holder.rankImage);
        } else if (position == 1) {
            Picasso.get().load("https://cdn-icons-png.flaticon.com/512/2583/2583319.png").fit().centerCrop().into(holder.rankImage);
        } else if (position == 2) {
            Picasso.get().load("https://cdn-icons-png.flaticon.com/512/2583/2583434.png").fit().centerCrop().into(holder.rankImage);

        }


    }


    @Override
    public int getItemCount() {
        return userRankList.size();
    }


    public interface RecyclerViewClickListener {
        void onRecyclerClick(String val);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rankUserName, rankId;
        ImageView rankImage;

        RankListAdapter.RecyclerViewClickListener recyclerViewClickListener;

        public ViewHolder(@NonNull View itemView, RankListAdapter.RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            this.recyclerViewClickListener = recyclerViewClickListener;

            rankUserName = itemView.findViewById(R.id.rankUserName);
            rankId = itemView.findViewById(R.id.rankId);
            rankImage = itemView.findViewById(R.id.rankImage);

//            rankImage.setOnClickListener(this);
//            rankUserName.setOnClickListener(this);
//            rankId.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

//            recyclerViewClickListener.onRecyclerClick(userRank.get(getAdapterPosition()).getMovieId());


        }
    }
}
