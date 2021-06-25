package com.example.flixterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixterapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailsActivity extends AppCompatActivity {

    public static final String YT_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView titleItem;
    RatingBar ratingBar;
    TextView descriptionView;
    ImageView ivPoster;
    public static final String TAG = "DetailsActivity";
    String youtubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int idNumber = (int) getIntent().getExtras().get("id");
        titleItem = findViewById(R.id.titleView);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionView = findViewById(R.id.descriptionView);
        ivPoster = findViewById(R.id.ivPoster);
        Glide.with(this).load(getIntent().getStringExtra("posterPath")).transform(new RoundedCornersTransformation(1, 1)).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, MovieTrailerActivity.class);
                i.putExtra("id", youtubeKey);
                startActivity(i);
            }
        });

        double rating = Double.valueOf(getIntent().getStringExtra("rating")) / 2.0;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(YT_URL, idNumber), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject videos = results.getJSONObject(0);
                    youtubeKey = videos.getString("key");
                    Log.i(TAG, "Result, " + results.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });

        titleItem.setText(getIntent().getStringExtra("title"));
        ratingBar.setNumStars(5);
        ratingBar.setRating((float) rating);
        descriptionView.setText(rating + "\n" + "\n" + getIntent().getStringExtra("overview"));
    }
}