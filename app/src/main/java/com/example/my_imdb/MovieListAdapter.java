package com.example.my_imdb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{

    LayoutInflater inflater;
    List<Movie> movies;

    public MovieListAdapter(Context ctx, List<Movie> movies){
        this.inflater = LayoutInflater.from(ctx);
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("dbug", "inflate cardview");
        View view = inflater.inflate(R.layout.movie_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Bind
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.imdbRating.setText(movies.get(position).getImDbRating());
        Picasso.get().load(movies.get(position).getImage()).fit().centerCrop().into(holder.movieCoverImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle, imdbRating;
        ImageView movieCoverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.movieTitle);
            imdbRating = itemView.findViewById(R.id.Ratings);
            movieCoverImage = itemView.findViewById(R.id.coverImage);

        }
    }
}
