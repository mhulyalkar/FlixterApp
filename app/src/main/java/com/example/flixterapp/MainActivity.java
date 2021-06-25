package com.example.flixterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixterapp.adapters.MovieAdapter;
import com.example.flixterapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "mainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();
        MovieAdapter.OnClickListener onClickListener = new MovieAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "View Item pressed, " + position);
                //create detailsactivity
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                //pass the data
                i.putExtra("title" , movies.get(position).getTitle());
                i.putExtra("overview" , movies.get(position).getOverview());
                i.putExtra("rating" , movies.get(position).getRating());
                i.putExtra("id", movies.get(position).getId());
                i.putExtra("posterPath", movies.get(position).getPosterPath());
                //display detailadapter
                startActivity(i);
            }
        };
        //Create an Adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies, onClickListener);
        //Set up the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        //Server request
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Result, " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies, " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });


    }
}