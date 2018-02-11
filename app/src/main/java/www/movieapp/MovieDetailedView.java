package www.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.URL;
import java.util.List;

import www.movieapp.Constant.Constant;
import www.movieapp.MovieDatabase.MovieContract;
import www.movieapp.adapter.MovieReviewAdapter;
import www.movieapp.adapter.MovieTrailerAdapter;
import www.movieapp.module.MovieDBJsonParse;
import www.movieapp.module.MovieReviewDB;
import www.movieapp.module.MovieTrailerDB;
import www.movieapp.utilities.NetworkUtils;


/**
 * Created by amy on 11/5/2017.
 */

public class MovieDetailedView extends AppCompatActivity {
    TextView textViewMovieTitle,textViewMovieRating,textViewMovieSynopsis,textViewMovieReleaseDate;
    ImageView imageViewMoviePoster;
    Context context;
    String movieImagePath,movieTitle,movieSynopsis,movieRating,movieReleaseDate,movieID;
    public static String apiKey = "?api_key=" + Constant.API_KEY;
    MovieTrailerAdapter movieTrailerAdapter;
    MovieReviewAdapter movieReviewAdapter;
    List<MovieReviewDB> movieReviewsDBs;
    List<MovieTrailerDB> movieTrailerDBs;
    RecyclerView movieReview, movieTrailer;
    Button buttonFav;
    boolean FavMovie=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
        readMovieData();
        loadMovieData();
        checkFavMovie(movieID);
       String ReviewsUrl =Constant.END_POINT+"movie/" + movieID + "/reviews" + apiKey;
      String  TrailerUrl = Constant.END_POINT+"movie/" + movieID + "/videos" + apiKey;
        loadMovieTrailerData(TrailerUrl);
        laodMovieReviewData(ReviewsUrl);

    }
    public void initObjects() {
        setContentView(R.layout.activity_movie_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        context = this;
        textViewMovieRating=(TextView)findViewById(R.id.text_view_rating);
        textViewMovieReleaseDate=(TextView)findViewById(R.id.text_view_release_date);
        textViewMovieTitle=(TextView)findViewById(R.id.text_view_title);
        textViewMovieSynopsis=(TextView)findViewById(R.id.text_view_synopsis);
        imageViewMoviePoster=(ImageView) findViewById(R.id.iv_poster);
        movieTrailer=(RecyclerView)findViewById(R.id.rv_movie_trailers);
        movieReview=(RecyclerView)findViewById(R.id.rv_reviews);
        buttonFav=(Button)findViewById(R.id.fav_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        movieTrailer.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        movieReview.setLayoutManager(layoutManager1);
    }
    public void readMovieData()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            movieID=bundle.getString(Constant.MOVIE_ID);
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
    public void loadMovieTrailerData(String movieTrailerUrl) {
        URL url = NetworkUtils.buildUrl(movieTrailerUrl);
        new RequestMovieTrailer().execute(url);
    }
    class RequestMovieTrailer extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            String movieTrailerResponseData = null;
            URL url = urls[0];
            try {
                movieTrailerResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ErrorInTrailer", e.getMessage());
            }
            return movieTrailerResponseData;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String movieResponseData) {
            super.onPostExecute(movieResponseData);
            if (movieResponseData != null) {
                loadMovieTrailerAdapter(movieResponseData);
            }
        }
    }
    private void loadMovieTrailerAdapter(String movieResponsePosterData) {
        movieTrailerDBs = MovieDBJsonParse.parseMovieTrailerStringToJson(movieResponsePosterData);
        movieTrailerAdapter = new MovieTrailerAdapter(context, movieTrailerDBs);
        movieTrailer.setAdapter(movieTrailerAdapter);
    }

    public void laodMovieReviewData(String movieReviewsUrl) {
        URL url = NetworkUtils.buildUrl(movieReviewsUrl);
        new RequestMovieReviewdata().execute(url);
    }
    class RequestMovieReviewdata extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String movieReviewsResponseData = null;
            URL url = urls[0];
            try {
                movieReviewsResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                movieReviewsResponseData = e.getMessage();
            }


            return movieReviewsResponseData;
        }

        protected void onPostExecute(String movieResponsePosterData) {
            super.onPostExecute(movieResponsePosterData);
            Log.d("Data", movieResponsePosterData);
            if (movieResponsePosterData != null) {
                loadMovieReviewAdapter(movieResponsePosterData);
            }
        }
        private void loadMovieReviewAdapter(String movieResponsePosterData) {

            movieReviewsDBs = MovieDBJsonParse.parseMovieReviewStringToJson(movieResponsePosterData);
            movieReviewAdapter = new MovieReviewAdapter(context, movieReviewsDBs);
            movieReview.setAdapter(movieReviewAdapter);

        }
    }
    private void checkFavMovie(String id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        final String[] projection = MovieContract.MovieEntry.MOVIE_COLUMNS;
        Boolean check=false;
        Cursor cursor=getContentResolver().query(uri,projection,"movie_id=?",new String[]{movieID},null);
        if(cursor.moveToFirst())
        {
            check=true;
        }
        if (check) {
            buttonFav.setBackground(getResources().getDrawable(R.drawable.ic_wished) );
            FavMovie = true;

        } else {
            buttonFav.setBackground(getResources().getDrawable(R.drawable.ic_wish) );
            FavMovie = false;
        }


    }
    public void AddFavList(View view)
    {
        if (FavMovie) {
            deleteFromFavList(movieID);

        } else {
            insertToFavList();
            // Toast.makeText(context, "ID="+db.addMovie(movie), Toast.LENGTH_SHORT).show();
        }
        checkFavMovie(movieID);
    }

    private void insertToFavList() {


        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieImagePath);
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movieSynopsis);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, movieRating);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        Log.v("Inserting Error", uri.toString());
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }


    }


    private void deleteFromFavList(String movieId) {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        // uri = uri.buildUpon().appendPath(movieId).build();
        int taskDeleted = getContentResolver().delete(uri,  "movie_id=?",
                new String[]{movieId});
        Toast.makeText(getApplicationContext(),"Deleted="+taskDeleted,Toast.LENGTH_LONG).show();
        Log.v("Deleting Error", String.valueOf(taskDeleted));


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}


