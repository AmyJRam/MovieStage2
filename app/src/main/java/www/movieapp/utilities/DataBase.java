package www.movieapp.utilities;

/**
 * Created by Amy
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import www.movieapp.MovieDetailedView;
import www.movieapp.module.MovieDB;
import www.movieapp.module.Movies;

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movie Database";
    private static final String TABLE_MOVIEDETAILS = "Movies";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_POSTER_PATH = "poster_path";

    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
    private static final String COLUMN_VOTE_COUNT = "vote_count";

    private static final String COLUMN_IS_FAVORITE = "favorite";
    private static final String COLUMN_IS_WATCHLIST = "watchlist";
    private static final String DATATYPE_NUMERIC = " INTEGER";
    private static final String DATATYPE_VARCHAR = " TEXT";
    private static final String OPEN_BRACE = "(";
    private static final String COMMA = ",";

    private static final String CREATE_TABLE_MOVIEDETAILS = new StringBuilder()
            .append(CREATE_TABLE).append(TABLE_MOVIEDETAILS).append(OPEN_BRACE)
            .append(COLUMN_ID).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_POSTER_PATH).append(DATATYPE_VARCHAR + COMMA)
            .append("UNIQUE(").append(COLUMN_ID).append(") ON CONFLICT REPLACE)").toString();

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MOVIEDETAILS);
    }

    public long addMovie(Movies movieInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String poster=movieInfo.getPosterPath();
        values.put(COLUMN_ID, movieInfo.getID());
        values.put(COLUMN_POSTER_PATH, poster);
        return  db.insert(TABLE_MOVIEDETAILS, null, values);
       // db.close();
    }

    public Movies getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, new String[]{COLUMN_TITLE, COLUMN_RELEASE_DATE,
                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE, COLUMN_IS_WATCHLIST}, COLUMN_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Movies movieInfo = new Movies();
        try {
            movieInfo.setID(id);
            movieInfo.setPosterPath(cursor.getString(2));
        } catch (Exception e) {
            MovieDetailedView d = new MovieDetailedView();
            Toast.makeText(d.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return movieInfo;
    }

    public boolean checkMovie(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_MOVIEDETAILS, null, COLUMN_ID + "=?", new String[]{id}, null, null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            MovieDetailedView d = new MovieDetailedView();
            Toast.makeText(d.context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }



    public List<MovieDB> getAllData() {
        List<MovieDB> movieDBList = new ArrayList<>();
        //db=dbHelper.getReadableDatabase();
        //String query = "SELECT * FROM " + Constants.TO_DO_LIST + " where " + Constants.KEY_STATUS + " = " + 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_MOVIEDETAILS,null);
        if (cursor.moveToFirst()) {
            do {
                MovieDB movieDB = new MovieDB();
                movieDB.setMovieId(cursor.getString(0).toString());
               String poster=cursor.getString(1);
                movieDB.setMoviePosters(poster);
                movieDBList.add(movieDB);
            } while (cursor.moveToNext());
        }
        System.out.println("size" + movieDBList.size());
        cursor.close();
        return movieDBList;
    }



    public boolean  deleteNonFavWatchMovie(String MoiveId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MOVIEDETAILS, COLUMN_ID + "=" + MoiveId, null) > 0;


        // db.delete(TABLE_MOVIEDETAILS, COLUMN_IS_FAVORITE + "=? AND " + COLUMN_IS_WATCHLIST + "=?", new String[]{String.valueOf(0), String.valueOf(0)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIEDETAILS);
        onCreate(db);
    }
}
