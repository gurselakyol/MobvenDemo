package grsl.com.mobvenmoviedb;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;
import java.util.Objects;

import grsl.com.mobvenmoviedb.Adapters.ListMoviesAdapter;
import grsl.com.mobvenmoviedb.Interfaces.ApiInterface;
import grsl.com.mobvenmoviedb.Interfaces.OnItemClickTransition;
import grsl.com.mobvenmoviedb.Models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    public static String BASE_URL = "https://api.themoviedb.org";
    public static String API_KEY = "c7f6f897fc0b1e635bbeea31a5b5fbb6";

    private EditText searchText;
    private RecyclerView searchRecycler;
    private ImageButton backArrow;

    private List<Movie.ResultsBean> listOfMovies;
    ListMoviesAdapter adapter;
    private String textQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backArrow = (ImageButton) findViewById(R.id.search_back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchRecycler = (RecyclerView) findViewById(R.id.search_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchRecycler.setLayoutManager(linearLayoutManager);

        searchText = (EditText)findViewById(R.id.search_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    getMovies(s.toString().trim(),1);
                    textQuery = s.toString().trim();
                }

            }
        });

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getMovies(textQuery,page + 1);
            }
        };

        searchRecycler.addOnScrollListener(scrollListener);
    }

    private void getMovies(String query, final int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<Movie> call =  apiInterface.makeSearch(API_KEY,query,page);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if(page==1){
                    listOfMovies = response.body().getResults();
                    adapter = new ListMoviesAdapter(listOfMovies, SearchActivity.this, new OnItemClickTransition() {
                        @Override
                        public void onItemClick(int index, View view) {
                            Intent intent = new Intent(SearchActivity.this, MovieDetailsActivity.class);
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(
                                            SearchActivity.this, view,
                                            Objects.requireNonNull(ViewCompat.getTransitionName(view))
                                    );
                            intent.putExtra("movieId",index);
                            startActivity(intent, optionsCompat.toBundle());
                        }
                    });
                    searchRecycler.setAdapter(adapter);
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
