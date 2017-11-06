package www.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import www.movieapp.Constant.Constant;

/**
 * Created by amy on 11/5/2017.
 */

public class MovieDetailedView extends AppCompatActivity {
    TextView textViewMovieTitle,textViewMovieRating,textViewMovieSynopsis,textViewMovieReleaseDate;
    ImageView imageViewMoviePoster;
    Context context;
    String movieImagePath,movieTitle,movieSynopsis,movieRating,movieReleaseDate;;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
        readMovieData();
        loadMovieData();
    }
    public void initObjects() {
        setContentView(R.layout.activity_movie_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        context = this;
        textViewMovieRating=(TextView)findViewById(R.id.textView_movie_rating);
        textViewMovieReleaseDate=(TextView)findViewById(R.id.textView_movie_release_date);
        textViewMovieTitle=(TextView)findViewById(R.id.textView_movie_title);
        textViewMovieSynopsis=(TextView)findViewById(R.id.textView_movie_synopsis);
        imageViewMoviePoster=(ImageView) findViewById(R.id.imageView_movie_image_poster);
    }
    public void readMovieData()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            movieTitle=bundle.getString(Constant.MOVIE_TITLE);
            movieRating=bundle.getString(Constant.MOVIE_RATING);
            movieImagePath=bundle.getString(Constant.MOVIE_IMAGE_POSTER);
            movieSynopsis=bundle.getString(Constant.MOVIE_SYNOPSIS);
            movieReleaseDate=bundle.getString(Constant.MOVIE_ReleaseDate);
        }
        else
        {


        }
    }
    public void loadMovieData() {
        textViewMovieRating.setText(movieRating+"/10");
        textViewMovieTitle.setText(movieTitle);
        textViewMovieReleaseDate.setText(movieReleaseDate);
        textViewMovieSynopsis.setText(movieSynopsis);
        Picasso.with(context).load(Constant.POSTER_PATH + movieImagePath).into(imageViewMoviePoster);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}


