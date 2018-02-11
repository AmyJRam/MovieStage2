package www.movieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import www.movieapp.R;
import www.movieapp.module.MovieReviewDB;

/**
 * Created by Amy
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieViewHolder> {
    List<MovieReviewDB> moviePostersList;
    Context context;

    public MovieReviewAdapter(Context context, List<MovieReviewDB> moviePostersList) {
        this.context = context;
        this.moviePostersList= moviePostersList;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final MovieReviewDB moviePostersDB=moviePostersList.get(position);
        final String author = moviePostersDB.getAuthor();
        final String content = moviePostersDB.getContent();
        holder.textViewContent.setText(content);
        holder.textViewAuthor.setText(author);

    }

    @Override
    public int getItemCount() {
        return moviePostersList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor,textViewContent;

        public MovieViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (TextView)itemView.findViewById(R.id.author_tv);
            textViewContent = (TextView)itemView.findViewById(R.id.content_tv);

        }
    }
}
