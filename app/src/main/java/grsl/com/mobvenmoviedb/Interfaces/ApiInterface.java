package grsl.com.mobvenmoviedb.Interfaces;

import grsl.com.mobvenmoviedb.Models.Movie;
import grsl.com.mobvenmoviedb.Models.MovieDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/3/movie/{category}")
    Call<Movie> getMovies(
            @Path("category")String category,
            @Query("api_key")String apiKey,
            @Query("region")String region,
            @Query("page")int page
    );
    @GET("/3/movie/{movieId}")
    Call<MovieDetails> getMovieDetails(
            @Path("movieId")String movieId,
            @Query("api_key")String apiKey
    );
    @GET("/3/search/movie")
    Call<Movie> makeSearch(
            @Query("api_key")String apiKey,
            @Query("query")String query,
            @Query("page")int page
    );
}
