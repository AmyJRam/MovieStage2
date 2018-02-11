package www.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import www.movieapp.Constant.Constant;
import www.movieapp.MovieDetailedView;
import www.movieapp.R;
import www.movieapp.module.MovieDB;

/**
 * Created by amy on 26/10/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    List<MovieDB> movieDBList;
    Context context;

    public MovieListAdapter(Context context, List<MovieDB> movieDBList) {
        this.context = context;
        this.movieDBList = movieDBList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_adapter, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        MovieDB movieDB = movieDBList.get(position);
        final String moviePoster = movieDB.getMoviePosters();
        final String movieTitle = movieDB.getMovieTitle();
        final String movieSynopsis = movieDB.getMovieDescription();
        final String movieReleaseDate = movieDB.getMovieReleaseDate();
        final String movieRating = movieDB.getMovieRating();
        final String movieId = movieDB.getMovieId();
        Picasso.with(context).load(Constant.POSTER_PATH + moviePoster).into(holder.imageViewMoviePoster);
        holder.imageViewMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMovieDetailedView=new Intent();
                intentMovieDetailedView.putExtra(Constant.MOVIE_ID,movieId);
                intentMovieDetailedView.putExtra(Constant.MOVIE_TITLE,movieTitle);
                intentMovieDetailedView.putExtra(Constant.MOVIE_IMAGE_POSTER,moviePoster);
                intentMovieDetailedView.putExtra(Constant.MOVIE_SYNOPSIS,movieSynopsis);
                intentMovieDetailedView.putExtra(Constant.MOVIE_RATING,movieRating);
                intentMovieDetailedView.putExtra(Constant.MOVIE_ReleaseDate,movieReleaseDate);
                intentMovieDetailedView.setClass(context, MovieDetailedView.class);
                context.startActivity(intentMovieDetailedView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieDBList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageViewMoviePoster = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);
        }
    }
}
