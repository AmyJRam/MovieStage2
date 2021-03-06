package www.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import www.movieapp.R;
import www.movieapp.module.MovieTrailerDB;

/**
 * Created by Amy
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieViewHolder> {
    List<MovieTrailerDB> movieTrailerList;
    Context context;

    public MovieTrailerAdapter(Context context, List<MovieTrailerDB> movieTrailerList) {
        this.context = context;
        this.movieTrailerList = movieTrailerList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_list, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final MovieTrailerDB movieTrailerDB = movieTrailerList.get(position);
        final String movieTrailerKey = movieTrailerDB.getKey();
        final String movieTrailerName = movieTrailerDB.getTrailerName();

        holder.movieTrailerName.setText(movieTrailerName);
        holder.movieTrailerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movieTrailerKey));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieTrailerList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTrailerName;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTrailerName = (TextView)itemView.findViewById(R.id.trailer_name);
        }
    }
}
