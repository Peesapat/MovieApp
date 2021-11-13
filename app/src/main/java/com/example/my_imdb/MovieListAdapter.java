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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private RecyclerViewClickListener mRecyclerViewClickListener;

    LayoutInflater inflater;
    List<Movie> movies;

    public MovieListAdapter(Context context, List<Movie> movies,
                            RecyclerViewClickListener recyclerViewClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        Log.d("dbug", "inflate cardview");
        View view = inflater.inflate(R.layout.fragment_movie_cardview, parent, false);
        return new ViewHolder(view, mRecyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Bind
        holder.movieTitle.setText(movies.get(position).getTitle());
        if (movies.get(position).getTitle().length() >= 37) {
            holder.movieTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        }
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
        TextView movieTitle, imdbRating;
        ImageView movieCoverImage;

        RecyclerViewClickListener recyclerViewClickListener;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            this.recyclerViewClickListener = recyclerViewClickListener;

            movieTitle = itemView.findViewById(R.id.movieTitle);
            imdbRating = itemView.findViewById(R.id.Ratings);
            movieCoverImage = itemView.findViewById(R.id.coverImage);

            movieCoverImage.setOnClickListener(this);
            movieTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.movieTitle) {
//                Log.d("dbug", "onClick: ");
                recyclerViewClickListener.onRecyclerClick(movies.get(getAdapterPosition()).getMovieId());
            } else if (v.getId() == R.id.coverImage) {
//                Log.d("dbug", "onClick: ");
                recyclerViewClickListener.onRecyclerClick(movies.get(getAdapterPosition()).getMovieId());
            } else {
            }

        }
    }
}
