package www.movieapp.MovieDatabase;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Amy
 */

public class MovieContract {

    public static final String AUTHORITY = "www.movieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String TABLE_NAME = "favMovies";

        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "relase_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";


        public static final String[] MOVIE_COLUMNS = {
                COLUMN_TITLE,
                COLUMN_POSTER_PATH,
                COLUMN_SYNOPSIS,
                COLUMN_RATING,
                COLUMN_RELEASE_DATE,
                COLUMN_MOVIE_ID

        };





    }

}
