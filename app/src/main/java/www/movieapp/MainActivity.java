package www.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import www.movieapp.Constant.Constant;
import www.movieapp.adapter.MovieListAdapter;
import www.movieapp.module.MovieDB;
import www.movieapp.module.MovieDBJsonParse;
import www.movieapp.utilities.NetworkUtils;

/**
 * Created by amy on 26/10/17.
 */

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewMovieList;
    Context context;
    MovieListAdapter movieListAdapter;
    List<MovieDB> movieDBList;
    String movieDbUrl;
    String movieSort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
        String movieUrlQuery = movieDbUrl + movieSort;
        loadMovieData(movieUrlQuery);
    }

    @Override
    protected void onStart() {
        super.onStart();
       if(!isOnline())
       {
           Intent intent=new Intent();
           intent.setClass(this,OffLineActivity.class);
           startActivity(intent);
       }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void initObjects() {
        setContentView(R.layout.activity_main);
         movieDbUrl = Constant.END_POINT;
         movieSort = Constant.SORT_BY_POPULAR;
        context = this;
        movieDBList = new ArrayList<>();
        recyclerViewMovieList = (RecyclerView) findViewById(R.id.r_v_movie_db_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerViewMovieList.setLayoutManager(layoutManager);
    }

    public void loadMovieData(String movieDbUrl) {
        URL url = NetworkUtils.buildUrl(movieDbUrl);
        new RequestMovieDbData().execute(url);
    }

    public void loadMovieAdapter(String resultData) {
        movieDBList = MovieDBJsonParse.parseMovieStringToJson(resultData);
        movieListAdapter = new MovieListAdapter(context, movieDBList);
        recyclerViewMovieList.setAdapter(movieListAdapter);
    }

    class RequestMovieDbData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String movieResponseData = null;
            URL url = urls[0];
            try {
                movieResponseData = NetworkUtils.getResponseFromMovieDb(url);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ErrorMessage", e.getMessage());
            }

            return movieResponseData;
        }

        @Override
        protected void onPostExecute(String movieResponseData) {
            super.onPostExecute(movieResponseData);
            Log.d("Data", movieResponseData);
            if (movieResponseData != null) {
                loadMovieAdapter(movieResponseData);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        if (selectedItemId == R.id.menu_sort_by_top_rated) {
            movieSort = Constant.SORT_BY_TOP_RATED;
            String movieUrlQuery = movieDbUrl + movieSort;
            loadMovieData(movieUrlQuery);
        } else if (selectedItemId == R.id.menu_sort_by_popular) {
            movieSort = Constant.SORT_BY_POPULAR;
            String movieUrlQuery = movieDbUrl + movieSort;
            loadMovieData(movieUrlQuery);
        }
        return super.onOptionsItemSelected(item);
    }
}
