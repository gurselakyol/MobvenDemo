package grsl.com.mobvenmoviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import grsl.com.mobvenmoviedb.Interfaces.ApiInterface;
import grsl.com.mobvenmoviedb.Models.MovieDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "c7f6f897fc0b1e635bbeea31a5b5fbb6";
    private static String POSTER_URL="https://image.tmdb.org/t/p/w185";
    private String MOVIE_ID;

    private ImageView moviePoster;
    private TextView movieTitle, movieInfo, movieGenre, movieRating;
    private ImageButton addFavorites, removeFavorites;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        db = new DatabaseHelper(this);

        moviePoster = (ImageView) findViewById(R.id.movie_details_poster);
        movieTitle = (TextView) findViewById(R.id.movie_details_movie_title);
        movieInfo = (TextView) findViewById(R.id.movie_details_movie_info);
        movieGenre = (TextView) findViewById(R.id.movie_details_movie_genre);
        movieRating = (TextView) findViewById(R.id.movie_details_rating);
        addFavorites = (ImageButton) findViewById(R.id.movie_details_add_favorites);
        removeFavorites = (ImageButton) findViewById(R.id.movie_details_remove_favorites);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        toolbar = (Toolbar) findViewById(R.id.movie_details_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        MOVIE_ID = String.valueOf(intent.getIntExtra("movieId",0));

        checkFavs(MOVIE_ID);

        addFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(MOVIE_ID);
            }
        });

        removeFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(MOVIE_ID);
            }
        });

        getMovieDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getMovieDetails(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MovieDetails> call =  apiInterface.getMovieDetails(MOVIE_ID,API_KEY);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails results = response.body();

                Glide.with(getApplicationContext())
                        .load(POSTER_URL+results.getPoster_path())
                        .into(moviePoster);

                movieTitle.setText(results.getTitle());
                movieInfo.setText(results.getOverview());
                movieRating.setText(String.valueOf(results.getVote_average()));
                movieGenre.setText(results.getGenres().get(0).getName());
                progressBar.setVisibility(View.GONE);
                toolbar.setTitle(results.getTitle());
                toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void checkFavs(final String movieId){
        if(db.getData(movieId)){
            removeFavorites.setVisibility(View.VISIBLE);
            addFavorites.setVisibility(View.GONE);
        }else{
            addFavorites.setVisibility(View.VISIBLE);
            removeFavorites.setVisibility(View.GONE);
        }
    }

    public  void addData(final String movieId) {
        boolean isInserted = db.insertData(movieId);
        if(isInserted == true) {
            Toast.makeText(MovieDetailsActivity.this, "Favorites Added", Toast.LENGTH_SHORT).show();
            addFavorites.setVisibility(View.GONE);
            removeFavorites.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(MovieDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(final String movieId) {
        Integer deletedRows = db.deleteData(movieId);
        if(deletedRows > 0) {
            Toast.makeText(MovieDetailsActivity.this, "Favorite Removed", Toast.LENGTH_SHORT).show();
            addFavorites.setVisibility(View.VISIBLE);
            removeFavorites.setVisibility(View.GONE);
        }else {
            Toast.makeText(MovieDetailsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
