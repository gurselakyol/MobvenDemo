package grsl.com.mobvenmoviedb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import grsl.com.mobvenmoviedb.Interfaces.OnItemClickTransition;
import grsl.com.mobvenmoviedb.Models.Movie;
import grsl.com.mobvenmoviedb.R;

public class ListMoviesAdapter extends RecyclerView.Adapter<ListMoviesAdapter.MyViewHolder> {

    private static String URL = "https://image.tmdb.org/t/p/w185";
    private List<Movie.ResultsBean> movieList;
    private Context context;
    private OnItemClickTransition movieClick;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;
        TextView movieName, movieInfo;
        MaterialCardView movieListCard;

        MyViewHolder(View view){
            super(view);
            moviePoster = view.findViewById(R.id.list_item_movie_poster);
            movieName = view.findViewById(R.id.list_item_movie_name);
            movieInfo = view.findViewById(R.id.list_item_movie_info);
            movieListCard = view.findViewById(R.id.list_item_movie_card);
        }
    }

    public ListMoviesAdapter(List<Movie.ResultsBean> movieList, Context context, OnItemClickTransition movieClick){
        this.movieList = movieList;
        this.context = context;
        this.movieClick = movieClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position){
        final Movie.ResultsBean movies = movieList.get(position);

        Glide.with(context).load(URL+movies.getPoster_path()).into(holder.moviePoster);
        holder.movieName.setText(movies.getTitle());
        holder.movieInfo.setText(movies.getOverview());
        holder.movieListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieClick.onItemClick(movies.getId(), holder.movieListCard);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
