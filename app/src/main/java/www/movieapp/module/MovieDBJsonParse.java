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


    public static List<MovieDB> parseMovieStringToJson(String movieResultData) {
        List<MovieDB> movieDBList = new ArrayList<>();
        try {
            JSONObject movieResultJsonObject = new JSONObject(movieResultData);
            JSONArray movieResultJsonArray = movieResultJsonObject.getJSONArray("results");
            for (int i = 0; i <= movieResultJsonArray.length(); i++) {
                MovieDB movieDB = new MovieDB();

                movieDB.setMovieTitle(movieResultJsonArray.getJSONObject(i).getString("original_title"));
                movieDB.setMovieRating(movieResultJsonArray.getJSONObject(i).getString("vote_average"));
                movieDB.setMovieDescription(movieResultJsonArray.getJSONObject(i).getString("overview"));
                movieDB.setMovieReleaseDate(movieResultJsonArray.getJSONObject(i).getString("release_date"));
                movieDB.setMoviePosters(movieResultJsonArray.getJSONObject(i).getString("poster_path"));
                movieDB.setMovieId(movieResultJsonArray.getJSONObject(i).getString("id"));

                movieDBList.add(movieDB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieDBList;

    }




    public static List<MovieReviewDB> parseMovieReviewStringToJson(String movieResultData) {
        //Log.i("Movie result Data", movieResultData);
        List<MovieReviewDB> movieReviewDBList = new ArrayList<>();

        try {

            JSONObject movieResultJsonObject = new JSONObject(movieResultData);


            JSONArray reviewsResultsArray = movieResultJsonObject.getJSONArray("results");

            for (int i = 0; i < reviewsResultsArray.length(); i++) {

                JSONObject currentReviewResult = reviewsResultsArray.getJSONObject(i);

                String reviewAuthor = currentReviewResult.getString("author");
                String reviewContent = currentReviewResult.getString("content");
                //System.out.print("Author"+reviewAuthor);
                MovieReviewDB movieReviewDB = new MovieReviewDB();

                movieReviewDB.setAuthor(reviewAuthor);
                movieReviewDB.setContent(reviewContent);
                movieReviewDBList.add(movieReviewDB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieReviewDBList;
    }


    public static List<MovieTrailerDB> parseMovieTrailerStringToJson(String movieResultData) {
        //Log.i("Movie result Data", movieResultData);
        List<MovieTrailerDB> movieTrailerList = new ArrayList<>();

        try {
            JSONObject movieResultJsonObject = new JSONObject(movieResultData);
            JSONArray movieResultJsonArray = movieResultJsonObject.getJSONArray("results");
            movieResultJsonArray.put(movieResultJsonObject);

            //System.out.println("size of the Json is " + movieResultData.length());
            for (int i = 0; i < movieResultJsonArray.length(); i++) {

                MovieTrailerDB movieTrailerDB = new MovieTrailerDB();
                movieTrailerDB.setKey(movieResultJsonArray.getJSONObject(i).get("key").toString());
                movieTrailerDB.setTrailerName(movieResultJsonArray.getJSONObject(i).get("name").toString());
                movieTrailerDB.setTrailerType(movieResultJsonArray.getJSONObject(i).get("type").toString());
                movieTrailerList.add(movieTrailerDB);
               // System.out.println("Hello " + movieTrailerList.get(i).getTrailerName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieTrailerList;
    }

}
