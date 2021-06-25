package com.example.flixterapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixterapp.R;
import com.example.flixterapp.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    Context context;
    List<Movie> movies;
    OnClickListener clickListener;

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener clickListener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = clickListener;
    }

    //Usually involves inflating a Layout from XML and returning the holder
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movies, parent, false);
        return new ViewHolder(movieView);
    }

    //Involves populating  data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@androidx.annotation.NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            tvOverview.setMovementMethod(new ScrollingMovementMethod());
            String imgUrl;
            String placeHolderLocation;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imgUrl = movie.getBackdropPath();
                Glide.with(context).load(imgUrl).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
            } else {
                imgUrl = movie.getPosterPath();
                Glide.with(context).load(imgUrl).transform(new RoundedCornersTransformation(1, 1)).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }
            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
