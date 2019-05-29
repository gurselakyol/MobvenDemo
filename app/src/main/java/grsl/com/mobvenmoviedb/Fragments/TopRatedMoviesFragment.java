package grsl.com.mobvenmoviedb.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import grsl.com.mobvenmoviedb.Adapters.ListMoviesAdapter;
import grsl.com.mobvenmoviedb.EndlessRecyclerViewScrollListener;
import grsl.com.mobvenmoviedb.Interfaces.ApiInterface;
import grsl.com.mobvenmoviedb.Interfaces.OnItemClickTransition;
import grsl.com.mobvenmoviedb.Models.Movie;
import grsl.com.mobvenmoviedb.MovieDetailsActivity;
import grsl.com.mobvenmoviedb.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopRatedMoviesFragment extends Fragment {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "c7f6f897fc0b1e635bbeea31a5b5fbb6";
    public static String CATEGORY = "top_rated";
    public static String REGION = "US";

    private List<Movie.ResultsBean> listOfMovies;
    private RecyclerView movieRecycle;
    ListMoviesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_rated_movies_fragment, container, false);

        movieRecycle = view.findViewById(R.id.top_rated_movies_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        movieRecycle.setLayoutManager(layoutManager);
        movieRecycle.setItemAnimator(new DefaultItemAnimator());

        getMovies(1);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMovies(page + 1);
            }
        };

        movieRecycle.addOnScrollListener(scrollListener);

        return view;
    }

    private void getMovies(final int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Movie> call =  apiInterface.getMovies(CATEGORY,API_KEY,REGION,page);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if(page==1){
                    listOfMovies = response.body().getResults();
                    adapter = new ListMoviesAdapter(listOfMovies, getContext(), new OnItemClickTransition() {
                        @Override
                        public void onItemClick(int index, View view) {
                            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(
                                            getActivity(), view,
                                            Objects.requireNonNull(ViewCompat.getTransitionName(view))
                                    );
                            intent.putExtra("movieId",index);
                            getContext().startActivity(intent, optionsCompat.toBundle());
                        }
                    });
                    movieRecycle.setAdapter(adapter);
                }else {
                    List<Movie.ResultsBean> movies = response.body().getResults();
                    for(Movie.ResultsBean movie : movies){
                        listOfMovies.add(movie);
                        adapter.notifyItemInserted(listOfMovies.size()-1);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
