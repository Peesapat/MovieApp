package com.example.my_imdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private SearchListAdapter.RecyclerViewClickListener sRecyclerViewClickListener;

    LayoutInflater inflater;
    List<Movie> movies;

    public SearchListAdapter(Context context, List<Movie> movies,
                             SearchListAdapter.RecyclerViewClickListener recyclerViewClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;
        this.sRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        Log.d("dbug", "inflate cardview");
        View view = inflater.inflate(R.layout.fragment_search_movie, parent, false);
        return new SearchListAdapter.ViewHolder(view, sRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Bind
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.releaseDate.setText(movies.get(position).getYear());
        holder.type.setText(movies.get(position).getType());
        holder.imdbRating.setText(movies.get(position).getImDbRating());
        Picasso.get().load(movies.get(position).getImage()).fit().centerCrop().into(holder.movieCoverImage);


    }


    @Override
    public int getItemCount() {
        return movies.size();
    }


    public interface RecyclerViewClickListener {
        void onRecyclerClick(String val);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieTitle, releaseDate, type, imdbRating;
        ImageView movieCoverImage;

        SearchListAdapter.RecyclerViewClickListener recyclerViewClickListener;

        public ViewHolder(@NonNull View itemView, SearchListAdapter.RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            this.recyclerViewClickListener = recyclerViewClickListener;

            movieTitle = itemView.findViewById(R.id.searchMovieTitle);
            releaseDate = itemView.findViewById(R.id.searchReleaseDate);
            type = itemView.findViewById(R.id.searchType);
            imdbRating = itemView.findViewById(R.id.searchRatings);
            movieCoverImage = itemView.findViewById(R.id.searchImage);

            movieCoverImage.setOnClickListener(this);
            releaseDate.setOnClickListener(this);
            type.setOnClickListener(this);
            imdbRating.setOnClickListener(this);
            movieTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

                recyclerViewClickListener.onRecyclerClick(movies.get(getAdapterPosition()).getMovieId());


        }
    }

}
