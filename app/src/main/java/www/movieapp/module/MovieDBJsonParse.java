package www.movieapp.module;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amy on 10/24/2017.
 */

public class MovieDBJsonParse {



    public static  List<MovieDB> parseMovieStringToJson(String movieResultData) {
        List<MovieDB> movieDBList = new ArrayList<>();
        try {
            JSONObject movieResultJsonObject = new JSONObject(movieResultData);

            JSONArray movieResultJsonArray = movieResultJsonObject.getJSONArray("results");
            for (int i = 0; i < movieResultJsonArray.length(); i++) {
                MovieDB movieDB = new MovieDB();
                movieDB.setMovieTitle(movieResultJsonArray.getJSONObject(i).getString("original_title"));
                movieDB.setImagePath(movieResultJsonArray.getJSONObject(i).getString("poster_path"));
                movieDB.setMovieRating(movieResultJsonArray.getJSONObject(i).getString("vote_average"));
                movieDB.setMovieSynopsis(movieResultJsonArray.getJSONObject(i).getString("overview"));
                movieDB.setMovieReleaseDate(movieResultJsonArray.getJSONObject(i).getString("release_date"));
                movieDBList.add(movieDB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDBList;
    }

}
