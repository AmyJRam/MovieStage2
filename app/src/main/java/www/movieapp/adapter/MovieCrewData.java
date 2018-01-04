package www.movieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import www.movieapp.Constant.Constant;
import www.movieapp.R;
import www.movieapp.module.MovieCastCrewDB;

/**
 * Created by Amy
 */

public class MovieCrewData extends RecyclerView.Adapter<MovieCrewData.MovieViewHolder> {
    List<MovieCastCrewDB> movieCrewList;
    Context context;

    public MovieCrewData(Context context, List<MovieCastCrewDB> movieCrewList) {
        this.context = context;
        this.movieCrewList = movieCrewList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_crew_list, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final MovieCastCrewDB movieCrewDB = movieCrewList.get(position);
        final String movieCrewPoster = movieCrewDB.getCrewFilePath();
        final String movieCrewFullName = movieCrewDB.getCrewFullName();
        final String movieCrewCharacterName = movieCrewDB.getCrewjob();

        holder.crewFullName.setText(movieCrewFullName);
        holder.crewCharacterName.setText(movieCrewCharacterName);
        Picasso.with(context).load(Constant.POSTER_PATH + movieCrewPoster).into(holder.imageViewMovieCrewPoster);

    }

    @Override
    public int getItemCount() {
        return movieCrewList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMovieCrewPoster;
        TextView crewFullName, crewCharacterName;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageViewMovieCrewPoster = itemView.findViewById(R.id.crew_image);
            crewFullName = itemView.findViewById(R.id.crew_full_name);
            crewCharacterName = itemView.findViewById(R.id.crew_character_name);

        }
    }
}
