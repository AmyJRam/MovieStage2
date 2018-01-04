package www.movieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;

import www.movieapp.Constant.Constant;
import www.movieapp.adapter.MovieCastData;
import www.movieapp.adapter.MovieCrewData;
import www.movieapp.adapter.MovieReviewData;
import www.movieapp.adapter.MovieTrailerData;
import www.movieapp.module.MovieCastCrewDB;
import www.movieapp.module.MovieDBJsonParse;
import www.movieapp.module.MovieDetailsDB;
import www.movieapp.module.MovieReviewDB;
import www.movieapp.module.MovieTrailerDBs;
import www.movieapp.module.Movies;
import www.movieapp.utilities.NetworkUtils;
import www.movieapp.utilities.DataBase;

/**
 * Created by Amy
 */

public class MovieDetailedView extends AppCompatActivity {

    public static String movieId = null;
    public static String movieDetailsUrl = null;
    public static String movieReviewsUrl=null;
    public static String movieTrailerUrl = null;
    public static String movieCastAndCrewsUrl = null;
    public static String apiKey = "?api_key="+ Constant.API_KEY;
    List<MovieDetailsDB> movieDetailsDBs;
    List<MovieReviewDB> movieReviewsDBs;
    List<MovieCastCrewDB> movieCasstDBs;
    List<MovieCastCrewDB> movieCrewDBs;
    List<MovieTrailerDBs> movieTrailerDBs;
    Bundle bundle;
    boolean FavId;
    MovieReviewData movieReviewData;
    MovieCastData movieCastData;
    MovieCrewData movieCrewData;
    MovieTrailerData movieTrailerData;
    Movies movie;
    DataBase db;
    public Context context;
    RecyclerView movieReview, movieCast, movieCrew, movieTrailer;
    TextView movieTitleText, movieReleaseDateText, movieBudgetText, movieRevenueText, movieReleaseStatusText;
    TextView movieVoteAverageText, movieDescriptionText, movieTagLineText, movieVoteCountUsers;
    RatingBar movieRatingBar,movieSingleStarRatingBar;
    ImageView movieImage, favoriteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_screen);
        Bundle intent = getIntent().getExtras();
        bundle = getIntent().getExtras();
        movieId = intent.getString("movieId");
        initialisationOfId();
        checkMovie(movieId);
        movieDetailsUrl = "http://api.themoviedb.org/3/movie/" + movieId + apiKey;
        movieCastAndCrewsUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/credits" + apiKey;
        movieReviewsUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews" + apiKey;
        movieTrailerUrl = "http://api.themoviedb.org/3/movie/" + movieId + "/videos" + apiKey;
        loadMovieDetailsData(movieDetailsUrl);
        laodMoviePostersData(movieReviewsUrl);
        loadMovieCastData(movieCastAndCrewsUrl);
        loadMovieTrailerData(movieTrailerUrl);


        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                movie = new Movies();
                movie.setID(movieDetailsDBs.get(0).getMovieTagId());
               // Toast.makeText(context, "POster="+movieDetailsDBs.get(0).getMovieImage(), Toast.LENGTH_SHORT).show();
                movie.setPosterPath(movieDetailsDBs.get(0).getMovieImage());

                if (FavId) {

                    boolean check = db.deleteNonFavWatchMovie(movie.getID());
                    if(check)
                    {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                        favoriteImage.setImageResource(R.drawable.favorite_disable_normal);

                    }
                    else
                    {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(context, "ID="+db.addMovie(movie), Toast.LENGTH_SHORT).show();
                }
                checkMovie(movieId);
            }
        });

    }

    private void checkMovie(String id) {

        Boolean check = db.checkMovie(id);
        if (check) { //checks if movie does not existing in database
            favoriteImage.setImageResource(R.drawable.favorite_enable_normal);
            FavId=true;

        }  else {
            favoriteImage.setImageResource(R.drawable.favorite_disable_normal);
            FavId=false;
            }



    }

    public void loadMovieTrailerData(String movieTrailerUrl) {
        URL url = NetworkUtils.buildUrl(movieTrailerUrl);
        new RequestMovieTrailerdata().execute(url);
    }

    public void loadMovieCastData(String movieCastCrewUrl) {
        URL url = NetworkUtils.buildUrl(movieCastCrewUrl);
        new RequestMovieCastCrewdata().execute(url);
    }
    public void laodMoviePostersData(String movieReviewsUrl){
        URL url =NetworkUtils.buildUrl(movieReviewsUrl);
        new RequestMoviePostersdata().execute(url);
    }

    public void loadMovieDetailsData(String movieDetailsUrl) {
        URL url = NetworkUtils.buildUrl(movieDetailsUrl);
        new RequestMovieDetailsdata().execute(url);
    }

    public void initialisationOfId() {
        context = this;
        movieTitleText = (TextView) findViewById(R.id.title);
        movieTagLineText = (TextView) findViewById(R.id.tag_line);
        movieReleaseDateText = (TextView) findViewById(R.id.release_date);
        movieBudgetText = (TextView) findViewById(R.id.budget);
        movieRevenueText = (TextView) findViewById(R.id.revenue);
        movieReleaseStatusText = (TextView) findViewById(R.id.status);
        movieVoteAverageText = (TextView) findViewById(R.id.vote_average);
        movieDescriptionText = (TextView) findViewById(R.id.description);
        movieVoteCountUsers = (TextView) findViewById(R.id.vote_count_users);
        movieRatingBar = (RatingBar) findViewById(R.id.ratingBar2);
        movieImage = (ImageView) findViewById(R.id.movieImage);
        movieSingleStarRatingBar = (RatingBar) findViewById(R.id.movie_single_star_rating_bar);
        movieReview=(RecyclerView)findViewById(R.id.rv_reviews);
        db= new DataBase(MovieDetailedView.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        movieReview.setLayoutManager(layoutManager);

        movieTrailer = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        movieTrailer.setLayoutManager(linearLayoutManagerTrailer);

        movieCast = (RecyclerView) findViewById(R.id.cast_list);
        LinearLayoutManager linearLayoutManagerCast = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        movieCast.setLayoutManager(linearLayoutManagerCast);

        movieCrew = (RecyclerView) findViewById(R.id.crew_list);
        LinearLayoutManager linearLayoutManagerCrew = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        movieCrew.setLayoutManager(linearLayoutManagerCrew);

        favoriteImage = (ImageView) findViewById(R.id.favourite_image_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadMovieAdapter(String movieResponseData) {
        Log.i("Hi see the Json String ", movieResponseData);
        movieDetailsDBs = MovieDBJsonParse.parseMovieDetailsStringToJson(movieResponseData);
        Log.i("ArrayList Size", "" + movieDetailsDBs.size());
        setDataIntoLayoutFields(movieDetailsDBs);
    }

    private void loadMoviePostersAdapter(String movieResponsePosterData){

        movieReviewsDBs= MovieDBJsonParse.parseMoviePostersStringToJson(movieResponsePosterData);
        setPosterIntoLayoutFields(movieReviewsDBs);

    }

    private void loadMovieCastCrewAdapter(String movieCastCrewsResponseData) {

        movieCasstDBs = MovieDBJsonParse.parseMovieCastStringToJson(movieCastCrewsResponseData);
        movieCrewDBs = MovieDBJsonParse.parseMovieCrewStringToJson(movieCastCrewsResponseData);
        setCastIntoLayoutFields(movieCasstDBs);
        setCrewIntoLayoutFields(movieCrewDBs);

        //setCrewerIntoLayoutFields(movieCrewDBs);

    }

    private void loadMovieTrailerAdapter(String movieResponsePosterData) {
        movieTrailerDBs = MovieDBJsonParse.parseMovieTrailerStringToJson(movieResponsePosterData);
        setTrailerIntoLayoutFields(movieTrailerDBs);
    }

    public void setTrailerIntoLayoutFields(List<MovieTrailerDBs> movieTrailerList) {
        movieTrailerData = new MovieTrailerData(context, movieTrailerList);
        movieTrailer.setAdapter(movieTrailerData);
    }

    public void setCrewIntoLayoutFields(List<MovieCastCrewDB> movieCrewList) {
        movieCrewData = new MovieCrewData(context, movieCrewList);
        movieCrew.setAdapter(movieCrewData);
    }

    public void setCastIntoLayoutFields(List<MovieCastCrewDB> movieCastList) {
        movieCastData = new MovieCastData(context, movieCastList);
        movieCast.setAdapter(movieCastData);
    }

    public void setPosterIntoLayoutFields(List<MovieReviewDB> movieReviewsDBs){

        movieReviewData = new MovieReviewData(context, movieReviewsDBs);
        movieReview.setAdapter(movieReviewData);

    }
    //    Stting Data into the Text fields From the movieDetailsDBs List
    public void setDataIntoLayoutFields(List<MovieDetailsDB> movieDetailsDBs) {
        //Formatting the Numbers into Readable Form
        int movieBudget = Integer.parseInt(movieDetailsDBs.get(0).getMovieBudget().toString());
        int movieRevenue = Integer.parseInt(movieDetailsDBs.get(0).getMovieRevenue().toString());
        int VoteCountUsers = Integer.parseInt(movieDetailsDBs.get(0).getMovieVoteCountUsers().toString());
        //float movieVoteAvg = Float.parseFloat(movieDetailsDBs.get(0).getMovieVoteAverage().toString());

        Picasso.with(context).load(Constant.POSTER_PATH + movieDetailsDBs.get(0).getMovieImage().toString()).into(movieImage);
        movieTitleText.setText(movieDetailsDBs.get(0).getMovieTitle().toString());
        movieTagLineText.setText(movieDetailsDBs.get(0).getMovieTagLine().toString());
        movieReleaseDateText.setText(movieDetailsDBs.get(0).getMovieRealeaseDate().toString());
        movieBudgetText.setText("Budget: " + NumberFormat.getIntegerInstance().format(movieBudget));
        movieRevenueText.setText("Revenue: " + NumberFormat.getIntegerInstance().format(movieRevenue));
        movieReleaseStatusText.setText("Staus: " + movieDetailsDBs.get(0).getMovieStatus().toString());
        movieVoteAverageText.setText(movieDetailsDBs.get(0).getMovieVoteAverage().toString() + "/10");
        movieDescriptionText.setText(movieDetailsDBs.get(0).getMovieDescription().toString());
        movieVoteCountUsers.setText(NumberFormat.getIntegerInstance().format(VoteCountUsers) + " users");
        //float d = movieVoteAvg * 10;
        movieRatingBar.setRating(Float.parseFloat(movieDetailsDBs.get(0).getMovieVoteAverage())/2);
        movieSingleStarRatingBar.setRating(Float.parseFloat(movieDetailsDBs.get(0).getMovieVoteAverage())/10);

        //movieRatingBar.setStepSize(d);
    }

    class RequestMovieCastCrewdata extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String movieCastCrewsResponseData = null;
            URL url = urls[0];
            try {
                movieCastCrewsResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                movieCastCrewsResponseData = e.getMessage();
            }


            return movieCastCrewsResponseData;
        }

        protected void onPostExecute(String movieCastCrewsResponseData) {
            super.onPostExecute(movieCastCrewsResponseData);
            Log.d("Data", movieCastCrewsResponseData);
            if (movieCastCrewsResponseData != null) {
                loadMovieCastCrewAdapter(movieCastCrewsResponseData);
            }
        }

    }
    class  RequestMoviePostersdata extends  AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            String movieReviewsResponseData=null;
            URL url =urls[0];
            try {
                movieReviewsResponseData=NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                movieReviewsResponseData=e.getMessage();
            }


            return movieReviewsResponseData;
        }

        protected void onPostExecute(String movieResponsePosterData) {
            super.onPostExecute(movieResponsePosterData);
            Log.d("Data", movieResponsePosterData);
            if (movieResponsePosterData!= null) {
                loadMoviePostersAdapter(movieResponsePosterData);
            }
        }
    }

    class RequestMovieDetailsdata extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            String movieDetailsResponseData = null;
            URL url = urls[0];
            try {
                movieDetailsResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ErrorMessage", e.getMessage());
            }
            return movieDetailsResponseData;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String movieResponseData) {
            super.onPostExecute(movieResponseData);
            Log.d("Data", movieResponseData);
            if (movieResponseData != null) {
                loadMovieAdapter(movieResponseData);
            }
        }
    }

    class RequestMovieTrailerdata extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            String movieTrailerResponseData = null;
            URL url = urls[0];
            try {
                movieTrailerResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ErrorMessage", e.getMessage());
            }
            return movieTrailerResponseData;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String movieResponseData) {
            super.onPostExecute(movieResponseData);
            Log.d("Data", movieResponseData);
            if (movieResponseData != null) {
                loadMovieTrailerAdapter(movieResponseData);
            }
        }
    }
}
